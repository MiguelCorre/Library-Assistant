package library.assistant.ui.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;
import library.assistant.util.LibraryAssistantUtil;

/**
 * FXML Controller class
 *
 * @author Miguel HPC
 */
public class MainController implements Initializable {

    @FXML
    private HBox book_info;
    @FXML
    private TextField bookIDInput;
    @FXML
    private Text bookName;
    @FXML
    private Text bookAuthor;
    @FXML
    private Text bookStatus;
    @FXML
    private TextField memberIDInput;
    @FXML
    private Text memberName;
    @FXML
    private Text memberMobile;
    @FXML
    private Text memberMail;
    @FXML
    private TextField bookID2;
    private ListView<String> issueDataList;
    @FXML
    private StackPane rootPane;
    @FXML
    private JFXHamburger hamburguer;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private HBox member_info;
    @FXML
    private Text memberNameH;
    @FXML
    private Text memberEmailH;
    @FXML
    private Text memberContactH;
    @FXML
    private Text bookNameH;
    @FXML
    private Text bookAuthorH;
    @FXML
    private Text bookPublisherH;
    @FXML
    private Text issueDateH;
    @FXML
    private Text noDaysH;
    @FXML
    private Text fineHolder;

    DatabaseHandler databaseHandler;
    Boolean isReadyForSubmission = false;
    @FXML
    private AnchorPane rootAnchorPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        JFXDepthManager.setDepth(book_info, 1);
        JFXDepthManager.setDepth(member_info, 1);

        initDrawer();
    }

    @FXML
    private void loadBookInfo(ActionEvent event) {

        clearBookCache();

        String id = bookIDInput.getText();
        String qu = "SELECT * FROM BOOK WHERE id = '" + id + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        Boolean flag = false;

        try {
            while (rs.next()) {
                String bName = rs.getString("title");
                String bAuthor = rs.getString("author");
                Boolean bStatus = rs.getBoolean("isAvail");

                bookName.setText(bName);
                bookAuthor.setText(bAuthor);
                String status = (bStatus) ? "Available" : "Not Available";
                bookStatus.setText(status);
                flag = true;
            }
            if (!flag) {
                bookName.setText("No Such Book");

            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadMemberInfo(ActionEvent event) {

        clearMemberCache();

        String id = memberIDInput.getText();
        String qu = "SELECT * FROM MEMBER WHERE id = '" + id + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        Boolean flag = false;

        try {
            while (rs.next()) {
                String mName = rs.getString("name");
                String mMobile = rs.getString("mobile");
                String mMail = rs.getString("email");

                memberName.setText(mName);
                memberMobile.setText(mMobile);
                memberMail.setText(mMail);
                flag = true;
            }
            if (!flag) {
                memberName.setText("No Such Member Registered");

            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void clearBookCache() {
        bookName.setText("");
        bookAuthor.setText("");
        bookStatus.setText("");
    }

    void clearMemberCache() {
        memberName.setText("");
        memberMobile.setText("");
        memberMail.setText("");
    }

    @FXML
    private void loadIssueOperation(ActionEvent event) {
        String memberID = memberIDInput.getText();
        String bookID = bookIDInput.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Issue Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to issue the book " + bookName.getText() + " to " + memberName.getText() + "?");

        Optional<ButtonType> response = alert.showAndWait();

        if (response.get() == ButtonType.OK) {
            String str = "INSERT INTO ISSUE(memberID, bookID) VALUES (  "
                    + "'" + memberID + "',"
                    + "'" + bookID + "')";

            String str2 = "UPDATE BOOK SET IsAvail = false WHERE ID = '" + bookID + "'";

            if (databaseHandler.execAction(str) && databaseHandler.execAction(str2)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success!!");
                alert1.setHeaderText(null);
                alert1.setContentText("Book issue Complete!");
                alert1.showAndWait();

            } else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Failure.");
                alert2.setHeaderText(null);
                alert2.setContentText("Book issue failed!");
                alert2.showAndWait();
            }
        } else {
            Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
            alert3.setTitle("Cancelled");
            alert3.setHeaderText(null);
            alert3.setContentText("Book issue Cancelled!");
            alert3.showAndWait();
        }
    }

    @FXML
    private void loadBookInfo2(ActionEvent event) {
        ClearEntries();
        
        ObservableList<String> issueData = FXCollections.observableArrayList();
        isReadyForSubmission = false;
        
        try {
            String id = bookID2.getText();
            String myQuery = "SELECT ISSUE.bookID, ISSUE.memberID, ISSUE.issueTime, ISSUE.renew_count,\n"
                    + "MEMBER.name, MEMBER.mobile, MEMBER.email,\n"
                    + "BOOK.title, BOOK.author, BOOK.publisher\n"
                    + "FROM ISSUE\n"
                    + "LEFT JOIN MEMBER\n"
                    + "ON ISSUE.memberID=MEMBER.ID\n"
                    + "LEFT JOIN BOOK\n"
                    + "ON ISSUE.bookID=BOOK.ID\n"
                    + "WHERE ISSUE.bookID='" + id + "'";
            ResultSet rs = databaseHandler.execQuery(myQuery);
            if (rs.next()) {
                memberNameH.setText(rs.getString("name"));
                memberEmailH.setText(rs.getString("email"));
                memberContactH.setText(rs.getString("mobile"));
                
                bookNameH.setText(rs.getString("title"));
                bookAuthorH.setText(rs.getString("author"));
                bookPublisherH.setText(rs.getString("publisher"));
                
                Timestamp mIssueTime = rs.getTimestamp("issueTime");
                Date dateOfIssue = new Date(mIssueTime.getTime());
                issueDateH.setText(dateOfIssue.toString());
                Long timeElapsed = System.currentTimeMillis() - mIssueTime.getTime();
                Long daysElapsed = TimeUnit.DAYS.convert(timeElapsed, TimeUnit.MILLISECONDS);
                noDaysH.setText(daysElapsed.toString());
                
                isReadyForSubmission = true;
            } else {
                BoxBlur boxBlur = new BoxBlur(3, 3, 3);
                
                JFXDialogLayout dialogLayout = new JFXDialogLayout();
                JFXButton button = new JFXButton("Okay");
                JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                    dialog.close();
                });
          
                dialogLayout.setBody(new Label("No such book in Issue Records"));
                dialogLayout.setActions(button);
                dialog.show();
                dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                    rootAnchorPane.setEffect(null);
                });
                rootAnchorPane.setEffect(boxBlur);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void loadSubmissionOP(ActionEvent event) {
        if (!isReadyForSubmission) {
            Alert alert3 = new Alert(Alert.AlertType.ERROR);
            alert3.setTitle("Error.");
            alert3.setHeaderText(null);
            alert3.setContentText("Book not ready for Submission");
            alert3.showAndWait();
            return;
        }
        String id = bookID2.getText();
        String ac1 = "DELETE FROM ISSUE WHERE bookID = '" + id + "'";
        String ac2 = "UPDATE BOOK SET isAvail = TRUE WHERE id = '" + id + "'";

        if (databaseHandler.execAction(ac1) && databaseHandler.execAction(ac2)) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Success!!");
            alert1.setHeaderText(null);
            alert1.setContentText("Book submission Complete!");
            alert1.showAndWait();
        } else {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Failure.");
            alert2.setHeaderText(null);
            alert2.setContentText("Book submission Failed!");
            alert2.showAndWait();
        }
    }

    @FXML
    private void loadRenewOP(ActionEvent event) {
        if (!isReadyForSubmission) {
            Alert alert3 = new Alert(Alert.AlertType.ERROR);
            alert3.setTitle("Error.");
            alert3.setHeaderText(null);
            alert3.setContentText("Book not ready for Renewal");
            alert3.showAndWait();
            return;
        }

        String id = bookID2.getText();
        String ac1 = "UPDATE ISSUE SET issueTime = CURRENT_TIMESTAMP, renew_count = renew_count+1 WHERE bookID = '" + id + "'";
        if (databaseHandler.execAction(ac1)) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Success!!");
            alert1.setHeaderText(null);
            alert1.setContentText("Book Renewal Complete!");
            alert1.showAndWait();
        } else {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Failure.");
            alert2.setHeaderText(null);
            alert2.setContentText("Book Renewal Failed!");
            alert2.showAndWait();
        }
    }

    @FXML
    private void handleMenuClose(ActionEvent event) {
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    @FXML
    private void handleMenuAddBook(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/addbook/FXMLDocument.fxml"), "Add New Book", null);
    }

    @FXML
    private void handleMenuAddMember(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/addmember/member_add.fxml"), "Add New Member", null);
    }

    @FXML
    private void handleMenuViewBook(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/listbook/book_list.fxml"), "Book List", null);
    }

    @FXML
    private void handleMenuViewMember(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/listmember/member_list.fxml"), "Member List", null);
    }

    @FXML
    private void handleMenuFullScreen(ActionEvent event) {
        Stage stage = ((Stage) rootPane.getScene().getWindow());
        stage.setFullScreen(!stage.isFullScreen());

    }

    private void initDrawer() {
        try {
            VBox toolbar = FXMLLoader.load(getClass().getResource("/library/assistant/ui/main/toolbar/toobal.fxml"));
            drawer.setSidePane(toolbar);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburguer);
        task.setRate(-1);
        hamburguer.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            task.setRate(task.getRate() * -1);
            task.play();
            if (drawer.isClosed()) {
                drawer.open();
            } else {
                drawer.close();
            }
        });
    }

    private void ClearEntries() {
        memberNameH.setText("");
        memberEmailH.setText("");
        memberContactH.setText(""); 
        
        bookAuthorH.setText("");
        bookNameH.setText("");
        bookPublisherH.setText("");
        
        issueDateH.setText("");
        noDaysH.setText("");
        fineHolder.setText("");
    }
}

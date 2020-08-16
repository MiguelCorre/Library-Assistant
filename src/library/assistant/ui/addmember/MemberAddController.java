package library.assistant.ui.addmember;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listmember.MemberListController;


public class MemberAddController implements Initializable {
    
    DatabaseHandler handler;

    @FXML
    private TextField name;
    @FXML
    private TextField id;
    @FXML
    private TextField mobile;
    @FXML
    private TextField email;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private AnchorPane rootPane;
    
    private Boolean isInEditMode = false;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = DatabaseHandler.getInstance();
    }    


    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addMember(ActionEvent event) {
        String mName = name.getText();
        String mID = id.getText();
        String mMobile = mobile.getText();
        String mEmail = email.getText();
        
        Boolean flag = mName.isEmpty() || mID.isEmpty() || mMobile.isEmpty() || mEmail.isEmpty();
        if (flag) {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Por favor preencha todos os espaços.");
            alert.showAndWait();
            return;
    }
        if(isInEditMode) {
            handleUpdateMember();
            return;
        }
        
        String st = "INSERT INTO MEMBER VALUES (" +
                "'" + mID + "'," +
                "'" + mName + "'," +
                "'" + mMobile + "'," +
                "'" + mEmail + "'" +
                ")";
        System.out.println(st);
        if (handler.execAction(st)) {
            Alert alert = new Alert (Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Sucesso!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Por favor preencha todos os espaços.");
            alert.showAndWait();
        }
    }
    
    public void inflateUI(MemberListController.Member member) {
        id.setText(member.getId());
        name.setText(member.getName());
        mobile.setText(member.getMobile());
        email.setText(member.getEmail());
        id.setEditable(false);
        isInEditMode = Boolean.TRUE;
    }

    private void handleUpdateMember() {
        MemberListController.Member member = new MemberListController.Member(name.getText(), id.getText(), mobile.getText(), email.getText());
        if (DatabaseHandler.getInstance().updateMember(member)) {
            AlertMaker.showSimpleAlert("Success", "Member Data Updated");
        } else {
            AlertMaker.showErrorMessage("Failed", "Can't update member data");
        }
    }
    
}

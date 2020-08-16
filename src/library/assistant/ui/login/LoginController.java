
package library.assistant.ui.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.settings.Preferences;
import library.assistant.ui.main.MainController;
import library.assistant.util.LibraryAssistantUtil;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;


public class LoginController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    
    Preferences preferences;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preferences = Preferences.getPreferences();       
    }    

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        
        String uname = username.getText();
        String passeword = password.getText();
        
        if (uname.equals(preferences.getUsername()) && passeword.equals(preferences.getPassword())) {
            closeStage();
            loadMain();
        } else {
            username.getStyleClass().add("wrong-credentials");
            password.getStyleClass().add("wrong-credentials");

            TranslateTransition tt = new TranslateTransition();
            TranslateTransition tt2 = new TranslateTransition();
            tt.setNode(username);
            tt2.setNode(password);
            tt.fromXProperty().setValue(0);
            tt2.fromXProperty().setValue(0);
            tt.toXProperty().setValue(50);
            tt2.toXProperty().setValue(50);
            tt.setDuration(new Duration(75));
            tt2.setDuration(new Duration(75));
            tt.setInterpolator(Interpolator.LINEAR);
            tt2.setInterpolator(Interpolator.LINEAR);
            tt.setAutoReverse(true);
            tt2.setAutoReverse(true);
            tt.setCycleCount(4);
            tt2.setCycleCount(4);
            tt.playFromStart(); 
            tt2.playFromStart(); 
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        System.exit(0);
    }

    private void closeStage() {
        ((Stage)username.getScene().getWindow()).close();
    }
    
    void loadMain () {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/library/assistant/ui/main/main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Library Assistant");
            stage.setScene(new Scene(parent));
            LibraryAssistantUtil.setStageIcon(stage);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

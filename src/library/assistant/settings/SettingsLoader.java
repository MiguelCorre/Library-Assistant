/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.settings;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;

/**
 *
 * @author Miguel HPC
 */
public class SettingsLoader extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/library/assistant/settings/settings.fxml"));
        
        Scene scene = new Scene (root);
        
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Settings");
        
        new Thread (() -> {
            DatabaseHandler.getInstance();
        }).start();
        
        Preferences.initConfig();
        
        
    }
    
    public static void main (String[] args) {
        launch(args);
    }
    
}
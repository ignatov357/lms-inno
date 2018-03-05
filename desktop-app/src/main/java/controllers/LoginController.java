package main.java.controllers;

import main.java.api.API_Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    API_Client api = new API_Client();

    @FXML AnchorPane loginContainer;
    @FXML TextField loginForm_userID;
    @FXML PasswordField loginForm_password;
    @FXML Button loginForm_submitButton;
    @FXML Button loginForm_signInUsingCardButton;

    public void handleLoginFormSubmit() {
        System.out.println(this.loginForm_userID.getText() + ":" + this.loginForm_password.getText());
        try {
            this.api.authorize(Integer.parseInt(this.loginForm_userID.getText()), this.loginForm_password.getText());

            Stage stage = (Stage) loginContainer.getScene().getWindow();
            FXMLLoader sceneLoader = new FXMLLoader(getClass().getResource("../../../resources/views/userMain.fxml"));
            stage.setScene(new Scene(sceneLoader.load()));
            ((UserMainController) sceneLoader.getController()).setApi(this.api);
            stage.getScene();
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authorization");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void handleFieldsInput() {
        if(!this.loginForm_userID.getText().equals("") && !this.loginForm_password.getText().equals("")) {
            this.loginForm_submitButton.setDisable(false);
        } else {
            this.loginForm_submitButton.setDisable(true);
        }
    }

    public void changeSceneTo_loginUsingCard() throws IOException {
        Stage stage = (Stage) this.loginContainer.getScene().getWindow();
        Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../../../resources/views/loginUsingCard.fxml")));
        newScene.getStylesheets().add(getClass().getResource("../../../resources/css/style.css").toExternalForm());
        stage.setScene(newScene);
    }
}

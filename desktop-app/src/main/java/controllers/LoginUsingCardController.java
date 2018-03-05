package main.java.controllers;

import com.mashape.unirest.http.exceptions.UnirestException;
import main.java.api.API_Client;
import main.java.NFC_Reader.Acr122Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginUsingCardController implements Initializable {
    main.java.NFC_Reader.Acr122Manager NfcReader;
    API_Client api = new API_Client();

    @FXML AnchorPane loginUsingCardContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            NfcReader = new Acr122Manager();
            NfcReader.initializeCardListener(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void authorizeWithCardUID(String cardUID) throws IOException, UnirestException {
        System.out.println(cardUID);
        try {
            api.authorizeUsingCardUID(cardUID);
            if(NfcReader != null) {
                NfcReader.terminateListener();
            }

            Stage stage = (Stage) loginUsingCardContainer.getScene().getWindow();
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

    public void changeSceneTo_login() throws IOException {
        if(NfcReader != null) {
            NfcReader.terminateListener();
        }
        Stage stage = (Stage) loginUsingCardContainer.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../resources/views/login.fxml"))));
    }
}

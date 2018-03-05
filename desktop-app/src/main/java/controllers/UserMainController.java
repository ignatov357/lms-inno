package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.NFC_Reader.Acr122Manager;
import main.java.api.API_Client;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserMainController {
    @FXML TableView booksTable, journalArticlesTable, avMaterialsTable;
    @FXML TableView checkedOutBooksTable, checkedOutJournalArticlesTable, checkedOutAvMaterialsTable;
    API_Client api;
    Thread syncingThread;
    @FXML AnchorPane userMainContainer;
    @FXML TabPane checkOutDocumentContainer, checkedOutDocumentsContainer;

    public void setApi(API_Client api) {
        this.api = api;
        initDataSyncing();
    }

    public void initDataSyncing() {
        this.syncingThread = new Thread(new Task() {
            @Override
            protected Object call() throws Exception {
                refreshDocumentsTable(api.getDocuments(true));
                this.wait(60000);
                this.call();

                return null;
            }
        });
        this.syncingThread.run();
    }

    public void handleMenuClick(ActionEvent actionEvent) {
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        if(menuItem.getText().equals("Check out document")) {
            checkOutDocumentContainer.setVisible(true);
            checkedOutDocumentsContainer.setVisible(false);
        } else if(menuItem.getText().equals("Checked out documents")) {
            checkOutDocumentContainer.setVisible(false);
            checkedOutDocumentsContainer.setVisible(true);
        }
    }

    public void refreshDocumentsTable(JSONArray documents) {
        System.out.println(1);
        JSONObject document;
        ObservableList<ObservableList<String>> booksTableRows = FXCollections.observableArrayList();
        ObservableList<ObservableList<String>> journalArticlesTableRows = FXCollections.observableArrayList();
        ObservableList<ObservableList<String>> avMaterialsTableRows = FXCollections.observableArrayList();
        for(int i = 0; i < documents.length(); i++) {
            document = documents.getJSONObject(i);
            ObservableList<String> row = FXCollections.observableArrayList();
            row.clear();
            if(document.getInt("type") == 0) {
                row.add(document.getString("id"));
                row.add(document.getString("title"));
                row.add(document.getString("publisher"));
                row.add(document.getString("edition"));
                row.add(document.getString("publicationYear"));
                row.add(document.getString("authors"));
                row.add(document.getString("bestseller"));
                row.add(document.getString("price"));
                row.add(document.getString("inStock"));

                booksTableRows.add(row);
            } else if (document.getInt("type") == 1) {
                row.add(document.getString("id"));
                row.add(document.getString("title"));
                row.add(document.getString("authors"));
                row.add(document.getString("journalTitle"));
                row.add(document.getString("journalIssuePublicationDate"));
                row.add(document.getString("issueEditors"));
                row.add(document.getString("price"));
                row.add(document.getString("inStock"));

                journalArticlesTableRows.add(row);
            } else if(document.getInt("type") == 2) {
                row.add(document.getString("id"));
                row.add(document.getString("title"));
                row.add(document.getString("authors"));
                row.add(document.getString("price"));
                row.add(document.getString("inStock"));

                avMaterialsTableRows.add(row);
            }
        }

        System.out.println(1);

        this.booksTable.setItems(booksTableRows);
        this.journalArticlesTable.setItems(journalArticlesTableRows);
        this.avMaterialsTable.setItems(avMaterialsTableRows);

    }
}

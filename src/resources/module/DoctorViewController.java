package resources.module;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DoctorViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private ComboBox<Integer> startH, startM, startS;
    @FXML
    private ComboBox<Integer> endH, endM, endS;

    @FXML
    void initialize() {
        for (int i = 0; i < 24; i++) {
            startH.getItems().add(i, i);
            endH.getItems().add(i, i);
        }
        for (int i = 0; i < 60; i++) {
            startM.getItems().add(i, i);
            startS.getItems().add(i, i);
            endM.getItems().add(i, i);
            endS.getItems().add(i, i);
        }
    }
}

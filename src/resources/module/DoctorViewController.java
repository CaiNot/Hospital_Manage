package resources.module;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import resources.DataBaseCon;
import resources.Doctor;
import resources.Patient;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private TableView<Patient> patientTable;
    @FXML
    private TableView<Doctor> doctorTable;
    @FXML
    private DatePicker startDate, endDate;

    private DataBaseCon dataBaseCon;
    String table="t_ksys";
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
        dataBaseCon=new DataBaseCon();
        dataBaseCon.connect();
        String sqlBR = "SELECT * FROM " + table + ";";
        try {
            ResultSet res = dataBaseCon.statement.executeQuery(sqlBR);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


}

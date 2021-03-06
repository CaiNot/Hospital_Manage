package resources.controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import resources.module.Data;
import resources.module.DataBaseCon;
import resources.module.Doctor;
import resources.module.Patient;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LoginController {
    @FXML
    private JFXButton loginBtn;
    @FXML
    private JFXCheckBox isDoctor;
    @FXML
    private JFXButton quitBtn;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ResourceBundle resources;
    @FXML
    private PasswordField passwdIn;
    @FXML
    private TextField userIn;
    @FXML
    private URL location;

    private DataBaseCon dataBaseCon;
    private String table = "t_brxx";
    private String sqlBR, user, psd, path, title;
    private String userNum;
    private Patient patient;
    private Doctor doctor;

    public LoginController() {
        patient = new Patient();
        doctor = new Doctor();
    }

    @FXML
    private void handleLoginBtn(ActionEvent event) throws Exception {

        table = "t_brxx";
        userNum = "BRBH";
        user = userIn.getText().trim();
        psd = passwdIn.getText().trim();
        path = "../view/registered.fxml";
        title = "病人挂号";
        boolean isDoctorValue;
        if ((isDoctorValue = isDoctor.isSelected())) {
            table = "t_ksys";
            userNum = "YSBH";
            path = "../view/DoctorView.fxml";
            title = "医生";
        }
        sqlBR = "SELECT * FROM " + table + " WHERE " + userNum + "='" +
                user + "'AND DLKL='" + psd + "';";
        ResultSet res = dataBaseCon.statement.executeQuery(sqlBR);
        String name;
        if (res.next()) {
            if (isDoctorValue) {
                doctor.setData(res);
                Data.doctor = doctor;
                name = doctor.user + "," + doctor.name;
            } else {
                patient.setDate(res);
                Data.patient = patient;
                name = patient.user + "," + patient.name;
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please Check Your Input UserId and PassWord!");
            alert.showAndWait();
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

        Parent root = loader.load();

        Scene sceneRegister = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle(name);
        stage.setScene(sceneRegister);
        stage.show();

        Stage curStage = (Stage) loginBtn.getScene().getWindow();
        dataBaseCon.close();
        curStage.close();
    }

    @FXML
    private void handleQuitBtn(ActionEvent event) {
        dataBaseCon.close();
        Platform.exit();
    }

    @FXML
    void initialize() {
        dataBaseCon = new DataBaseCon();
        if (dataBaseCon.connect()) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
    }
}

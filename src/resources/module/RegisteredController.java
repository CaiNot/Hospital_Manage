package resources.module;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import resources.DataBaseCon;
import resources.Department;
import resources.Doctor;
import resources.Patient;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisteredController {
    @FXML
    private JFXComboBox<String> departmentBox;

    @FXML
    private JFXComboBox<String> doctorNameBox;
    @FXML
    private JFXComboBox<String> isProBox;
    @FXML
    private JFXTextField kindText;
    String sql;
    ResultSet resultSet;
    DataBaseCon dataBaseCon;

    @FXML
    private void handleOKBtn(ActionEvent event) throws Exception {

    }

    @FXML
    private void handleClrBtn(ActionEvent event) {

    }

    @FXML
    private void handleQuitBtn(ActionEvent event) {
        Platform.exit();
    }


    private Doctor doctor;
    String departmentName, doctorName;

    @FXML
    void initialize() {
        dataBaseCon = new DataBaseCon();
        if (dataBaseCon.connect()) {
            System.out.println("DataBase Connected!");
        } else {
            System.out.println("DataBase Connect Error!");
        }
        isProBox.getItems().addAll("普通号", "专家号");
        //language=MySQL
        sql = "SELECT * FROM t_ksys;";
        doctor = new Doctor();
        Department department = new Department();
        try {
            sql = "SELECT * FROM t_ksxx;";
            resultSet = dataBaseCon.statement.executeQuery(sql);
            while (resultSet.next()) {
                department.setData(resultSet);
                departmentBox.getItems().add(department.num + "," + department.name + "," + department.pingyin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in Select");
        }
        isProBox.getSelectionModel().selectFirst();
        departmentBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                doctorNameBox.getItems().clear();
                sql = "SELECT * FROM t_ksys WHERE KSBH='" +
                        departmentBox.getValue().split(",")[0] + "';";
                try {
                    final ResultSet resultSet = dataBaseCon.statement.executeQuery(sql);
                    while (resultSet.next()) {
                        doctor.setData(resultSet);
                        doctorNameBox.getItems().add(doctor.user + "," + doctor.name + "," + doctor.pingyin);
                    }
                    doctorNameBox.getSelectionModel().selectFirst();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        doctorNameBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                departmentName =;
                String[] stringsDoctor = newValue.split(",");
                String[] stringsDepart = departmentBox.getValue().split(",");
                if (stringsDepart.length > 1 && stringsDoctor.length > 1) {
                    doctorName = stringsDoctor[1];
                    departmentName = stringsDepart[1];
                    kindText.setText(departmentName + "," + doctorName + "," + isProBox.getValue());
                }
            }
        });

        // 鼠标悬停时显示
        this.onMouseEnterd(departmentBox);
        this.onMouseEnterd(doctorNameBox);
        this.onMouseEnterd(isProBox);
    }

    private void onMouseEnterd(JFXComboBox<String> comboBox) {
        comboBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                comboBox.show();
            }
        });
    }
}

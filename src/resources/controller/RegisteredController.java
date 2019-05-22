package resources.controller;

import com.jfoenix.controls.JFXComboBox;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import resources.module.*;


import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RegisteredController {
    @FXML
    private JFXComboBox<String> departmentBox;
    @FXML
    private TextField moneyAllText;
    @FXML
    private TextField chargeText;
    @FXML
    private TextField moneyInText;
    @FXML
    private JFXComboBox<String> doctorNameBox;
    @FXML
    private JFXComboBox<String> isProBox;
    @FXML
    private JFXComboBox<String> kindBox;
    @FXML
    private TextField lastText;
    //language=MySQL
    private String sql;
    private ResultSet resultSet;
    private DataBaseCon dataBaseCon;
    private Doctor doctor;
    private String departmentName, doctorName;
    private NumKind numKind = new NumKind();
    String userID, doctorID, departmentID;
    ArrayList<String> departmentList = new ArrayList<>(), doctorList;
    boolean ok = false;
    @FXML
    private TextField numText;

    @FXML
    private void handleOKBtn(ActionEvent event) throws Exception {
        if (ok)
            return;
        BigDecimal GHFY;
        String date;
        int GHRC;
        boolean THBZ;
        String GHBH, HZBH;

        doctorID = doctorNameBox.getValue();
        HZBH = kindBox.getValue();
        departmentID = departmentBox.getValue();

        String[] temp;
        temp = HZBH.split(",");
        if (temp.length < 3) {
            this.warning();
            return;
        }
        HZBH = temp[0];
        temp = doctorID.split(",");
        if (temp.length < 3) {
            this.warning();
            return;
        }
        if (chargeText.getText().isEmpty()) {
            this.warning();
            return;
        }
        doctorID = temp[0];
        temp = departmentID.split(",");
        if (temp.length < 3) {
            this.warning();
            return;
        }
        sql = "SELECT count(HZBH) FROM t_ghxx WHERE BRBH='" + Data.patient.user + "' AND HZBH='" + HZBH + "';";
        resultSet = dataBaseCon.statement.executeQuery(sql);
        if (resultSet.next()) {
            GHRC = resultSet.getInt(1) + 1;
        } else {
            this.warning();
            return;
        }


        THBZ = true;
        GHFY = new BigDecimal(moneyAllText.getText());
        departmentID = temp[0];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        date = simpleDateFormat.format(new Date());


        sql = "UPDATE t_brxx SET YCJE ='" + chargeText.getText() +
                "' WHERE BRBH ='" + Data.patient.user + "';";
        PreparedStatement psql = dataBaseCon.con.prepareStatement(sql);
        psql.executeUpdate();

        sql = "UPDATE t_hzxx SET GHRS=GHRS+1 WHERE HZBH='" + HZBH + "';";
        psql = dataBaseCon.con.prepareStatement(sql);
        psql.executeUpdate();

        sql = "SELECT max(GHBH) FROM t_ghxx;";
        resultSet = dataBaseCon.statement.executeQuery(sql);
        if (resultSet.next()) {
            GHBH = String.format("%06d",Integer.parseInt(resultSet.getString(1)) + 1);
            psql = dataBaseCon.con.prepareStatement("INSERT INTO T_GHXX(GHBH, HZBH, YSBH, BRBH, GHRC, THBZ, GHFY, RQSJ)" + "VALUES (?,?,?,?,?,?,?,?)");

            psql.setString(1, GHBH);
            psql.setString(2, HZBH);
            psql.setString(3, doctorID);
            psql.setString(4, Data.patient.user);
            psql.setInt(5, GHRC);
            psql.setBoolean(6, THBZ);
            psql.setBigDecimal(7, GHFY);
            psql.setString(8, date);

            psql.executeUpdate();
            psql.close();
            System.out.println("Inert OK!");

            numText.setText(GHBH);
            ok = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("OK");
            alert.setContentText("你的挂号号码为"+GHBH+"\n");
            alert.showAndWait();
        }

    }

    @FXML
    private void handleClrBtn(ActionEvent event) {

    }

    @FXML
    private void handleQuitBtn(ActionEvent event) {
        dataBaseCon.close();
        Platform.exit();
    }

    BigDecimal allMoney;

    private void warning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText("Wrong!");
        alert.showAndWait();
        return;
    }

    @FXML
    void initialize() {
        dataBaseCon = new DataBaseCon();
        if (dataBaseCon.connect()) {
            System.out.println("DataBase Connected!");
        } else {
            System.out.println("DataBase Connect Error!");
        }
        doctor = new Doctor();
        Department department = new Department();
        allMoney = new BigDecimal(0);
        try {
            allMoney = Data.patient.money;
            if (!moneyInText.getText().isEmpty())
                chargeText.setText(String.valueOf(allMoney.doubleValue() - Double.parseDouble(moneyInText.getText())));
            sql = "SELECT * FROM t_ksxx;";
            resultSet = dataBaseCon.statement.executeQuery(sql);
            while (resultSet.next()) {
                department.setData(resultSet);
                departmentBox.getItems().add(department.num + "," + department.name + "," + department.pingyin);
            }
            resultSet.close();
            for (String s : departmentBox.getItems()) {
                departmentList.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in Select");
        }
        isProBox.setEditable(false);
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
        AutoCompleteComboBoxListener<String> autoCom = new AutoCompleteComboBoxListener<>(departmentBox);

        doctorNameBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                String[] stringsDoctor = newValue.split(",");
                String[] stringsDepart = departmentBox.getValue().split(",");
                if (stringsDepart.length > 2 && stringsDoctor.length > 2) {
                    doctorName = stringsDoctor[1];
                    departmentName = stringsDepart[1];
                    sql = "SELECT SFZJ FROM t_ksys WHERE YSBH='" + stringsDoctor[0] + "';";
                    try {
                        resultSet = dataBaseCon.statement.executeQuery(sql);
                        if (resultSet.next()) {
                            boolean isProDoctor = resultSet.getBoolean("SFZJ");
                            isProBox.getItems().clear();
                            if (isProDoctor) {
                                isProBox.getItems().addAll("普通号", "专家号");
                            } else {
                                isProBox.getItems().add("普通号");
                            }
                            isProBox.getSelectionModel().selectFirst();
                        }
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        });

        isProBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null)
                    return;
                String[] stringsDoctor = doctorNameBox.getValue().split(",");
                String[] stringsDepart = departmentBox.getValue().split(",");
                if (stringsDepart.length > 2 && stringsDoctor.length > 2) {
                    departmentName = stringsDepart[1];
                    try {
                        sql = "SELECT * FROM t_hzxx WHERE KSBH='" +
                                stringsDepart[0] + "' and SFZJ='" +
                                (newValue.compareTo("普通号") != 0 ? "1" : "0") + "';";
                        System.out.println(sql);
                        resultSet = dataBaseCon.con.createStatement().executeQuery(sql);
                        kindBox.getItems().clear();
                        while (resultSet.next()) {
                            numKind.setData(resultSet);
                            kindBox.getItems().add(numKind.id + "," + numKind.name
                                    + "," + numKind.pingyin);
                        }
                        resultSet.close();
                        kindBox.getSelectionModel().selectFirst();

                    } catch (SQLException e) {
                        e.printStackTrace();
                        kindBox.getSelectionModel().selectFirst();
                    }
                }
            }
        });
        kindBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String[] data = newValue.split(",");
                if (data.length > 2) {
                    String id = data[0];
                    sql = "SELECT * FROM t_hzxx WHERE HZBH='" + id + "';";
                    try {
                        resultSet = dataBaseCon.statement.executeQuery(sql);
                        if (resultSet.next()) {
                            numKind.setData(resultSet);
                        }
                        moneyAllText.setText(numKind.money.toString());
                        resultSet.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                        return;
                    }
                } else {
                    return;
                }
            }
        });
        moneyAllText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                BigDecimal money = new BigDecimal(newValue);
                if (Data.patient.money.compareTo(money) > 0) {
                    moneyInText.setDisable(true);
                } else {
                    moneyInText.setDisable(false);
                }
                lastText.setText(Data.patient.money.toString());
                chargeText.setText(Data.patient.money.subtract(money).toString());
            }
        });
        moneyInText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                double moneyIn = 0, moneyAll;
                try {
                    moneyIn = Double.parseDouble(newValue);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                moneyAll = Double.parseDouble(moneyAllText.getText());
                chargeText.setText(String.valueOf(moneyIn + allMoney.doubleValue() - moneyAll));
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

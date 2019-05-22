package resources.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import resources.module.Data;
import resources.module.DataBaseCon;
import resources.module.DoctorTableData;
import resources.module.PatientTableData;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private TableView<PatientTableData> patientTable;
    @FXML
    private TableView<DoctorTableData> doctorTable;
    @FXML
    private DatePicker startDate, endDate;

    private DataBaseCon dataBaseCon;
    private final ObservableList<PatientTableData> patientData = FXCollections.observableArrayList();
    private final ObservableList<DoctorTableData> doctorData = FXCollections.observableArrayList();

    @FXML
    void handleLogOut(ActionEvent event) {
        dataBaseCon.close();
        Stage stage = (Stage) doctorTable.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleRefresh(ActionEvent event) {
        this.refreshDoctorTale();
    }

    private void refreshPatientTable() {
        String sqlBR = "SELECT * FROM  t_ghxx WHERE YSBH='" + Data.doctor.user + "';";
        patientData.clear();
        try {
            ResultSet res = dataBaseCon.statement.executeQuery(sqlBR), resBR;
            String GHBH, BRMC, BRBH, GHRQ, HZLX, HZBH;
            boolean THBZ;
            while (res.next()) {
                THBZ = res.getBoolean("THBZ");

                if (!THBZ)
                    continue;
                HZBH = res.getString("HZBH");
                GHBH = res.getString("GHBH");
                BRBH = res.getString("BRBH");
                GHRQ = res.getString("RQSJ");

                sqlBR = "SELECT BRMC FROM t_brxx WHERE BRBH='" + BRBH + "';";
                resBR = dataBaseCon.con.createStatement().executeQuery(sqlBR);
                if (resBR.next()) {
                    BRMC = resBR.getString(1);
                } else return;
                sqlBR = "SELECT SFZJ FROM t_hzxx WHERE HZBH='" + HZBH + "';";
                resBR = dataBaseCon.con.createStatement().executeQuery(sqlBR);
                if (resBR.next()) {
                    if (resBR.getBoolean(1)) {
                        HZLX = "专家号";
                    } else {
                        HZLX = "普通号";
                    }
                } else return;

                PatientTableData patientTableData = new PatientTableData(GHBH, BRMC, GHRQ, HZLX);
                patientData.add(patientTableData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void refreshDoctorTale() {
        LocalDate dateStart = startDate.getValue(), dateEnd = endDate.getValue();
        doctorData.clear();
        if (dateStart.isAfter(dateEnd)) {
            System.out.println("Time Error");
            return;
        }

        String stH, stM, stS, eH, eM, eS;
        stH = startH.getValue().toString();
        stM = startM.getValue().toString();
        stS = startS.getValue().toString();
        eH = endH.getValue().toString();
        eM = endM.getValue().toString();
        eS = endS.getValue().toString();
        String startTime, endTime;
        startTime = stH + ":" + stM + ":" + stS;
        endTime = eH + ":" + eM + ":" + eS;
        if (dateStart.compareTo(dateEnd) == 0 && startTime.compareTo(endTime) > 0) {
            System.out.println("Time Error");
            return;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        startTime = dateStart.format(df) + " " + startTime;
        endTime = dateEnd.format(df) + " " + endTime;

        //language=MySQL
        String sql = "SELECT HZBH,YSBH, COUNT(*) FROM t_ghxx WHERE RQSJ>'" +
                startTime + "' and RQSJ <'" + endTime + "'group by HZBH,YSBH;";
        try {
            ResultSet resultSet = dataBaseCon.statement.executeQuery(sql), resT;
            String HZBH, YSBH, KSMC, YSMC, HZLX;
            int GHRC, SRHJ;
            boolean isPro;
            while (resultSet.next()) {
                HZBH = resultSet.getString("HZBH");
                YSBH = resultSet.getString("YSBH");
                GHRC = resultSet.getInt(3);
                //language=MySQL
                String sqlT = "SELECT KSMC FROM t_ksxx where KSBH IN (" +
                        "SELECT KSBH FROM t_hzxx WHERE HZBH='" + HZBH + "');";
                resT = dataBaseCon.con.createStatement().executeQuery(sqlT);
                if (resT.next()) {
                    KSMC = resT.getString(1);
                } else return;
                resT.close();

                sqlT = "SELECT YSMC FROM t_ksys where YSBH ='" + YSBH + "';";
                resT = dataBaseCon.con.createStatement().executeQuery(sqlT);
                if (resT.next()) {
                    YSMC = resT.getString(1);
                } else return;
                resT.close();
                sqlT = "SELECT SFZJ FROM t_hzxx WHERE HZBH='" + HZBH + "';";
                resT = dataBaseCon.con.createStatement().executeQuery(sqlT);
                if (resT.next()) {
                    isPro = resT.getBoolean(1);
                } else return;
                resT.close();
                if (isPro) {
                    SRHJ = GHRC * 100;
                    HZLX = "专家号";
                } else {
                    SRHJ = GHRC * 2;
                    HZLX = "普通号";
                }

                DoctorTableData doctorTableData = new DoctorTableData(KSMC, YSBH, YSMC, HZLX, Integer.toString(GHRC), Integer.toString(SRHJ));
                doctorData.add(doctorTableData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

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
        startDate.setValue(LocalDate.now().minusDays(1));
        endDate.setValue(LocalDate.now());
        startH.getSelectionModel().select(0);
        endH.getSelectionModel().select(23);
        startM.getSelectionModel().select(0);
        endM.getSelectionModel().select(59);
        startS.getSelectionModel().select(0);
        endS.getSelectionModel().select(59);
        dataBaseCon = new DataBaseCon();
        dataBaseCon.connect();
//        patientTable.getColumns().get("").setCellValueFactory(new PropertyValueFactory<PatientTableData,String>("GHBH"));
        TableColumn<PatientTableData, String> column1 = new TableColumn<>("挂号编号");
        column1.setCellValueFactory(cellData -> cellData.getValue().GHBHProperty());
        TableColumn<PatientTableData, String> column2 = new TableColumn<>("病人名称");
        column2.setCellValueFactory(cellData -> cellData.getValue().BRMCProperty());
        TableColumn<PatientTableData, String> column3 = new TableColumn<>("挂号日期");
        column3.setCellValueFactory(cellData -> cellData.getValue().GHRQProperty());
        TableColumn<PatientTableData, String> column4 = new TableColumn<>("号种类型");
        column4.setCellValueFactory(cellData -> cellData.getValue().HZLXProperty());
        patientTable.getColumns().addAll(column1, column2, column3, column4);

        patientTable.setItems(patientData);
        this.refreshPatientTable();

        TableColumn<DoctorTableData, String> columnDoctor1 = new TableColumn<>("科室名称");
        TableColumn<DoctorTableData, String> columnDoctor2 = new TableColumn<>("医生编号");
        TableColumn<DoctorTableData, String> columnDoctor3 = new TableColumn<>("医生名称");
        TableColumn<DoctorTableData, String> columnDoctor4 = new TableColumn<>("号种类别");
        TableColumn<DoctorTableData, String> columnDoctor5 = new TableColumn<>("挂号人次");
        TableColumn<DoctorTableData, String> columnDoctor6 = new TableColumn<>("收入合计");
        columnDoctor1.setCellValueFactory(cellData -> cellData.getValue().KSMCProperty());
        columnDoctor2.setCellValueFactory(cellData -> cellData.getValue().YSBHProperty());
        columnDoctor3.setCellValueFactory(cellData -> cellData.getValue().YSMCProperty());
        columnDoctor4.setCellValueFactory(cellData -> cellData.getValue().HZLXProperty());
        columnDoctor5.setCellValueFactory(cellData -> cellData.getValue().GHRCProperty());
        columnDoctor6.setCellValueFactory(cellData -> cellData.getValue().SRHJProperty());
        doctorTable.getColumns().addAll(columnDoctor1, columnDoctor2, columnDoctor3, columnDoctor4, columnDoctor5, columnDoctor6);

        doctorTable.setItems(doctorData);
        this.refreshDoctorTale();

//        startH.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
//            @Override
//            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
//                refreshDoctorTale();
//            }
//        });
    }
}

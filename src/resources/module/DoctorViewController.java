package resources.module;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import resources.*;

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
    private TableView<PatientTableData> patientTable;
    @FXML
    private TableView<String> doctorTable;
    @FXML
    private DatePicker startDate, endDate;

    private DataBaseCon dataBaseCon;
    String table = "t_ksys";
    private final ObservableList<PatientTableData> data = FXCollections.observableArrayList();

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
        dataBaseCon = new DataBaseCon();
        dataBaseCon.connect();
//        patientTable.getColumns().get("").setCellValueFactory(new PropertyValueFactory<PatientTableData,String>("GHBH"));
        TableColumn<PatientTableData, String> column1 = new TableColumn("挂号编号");
        column1.setCellValueFactory(cellData -> cellData.getValue().GHBHProperty());
        TableColumn<PatientTableData, String> column2 = new TableColumn("病人名称");
        column2.setCellValueFactory(cellData -> cellData.getValue().BRMCProperty());
        TableColumn<PatientTableData, String> column3 = new TableColumn("挂号日期");
        column3.setCellValueFactory(cellData -> cellData.getValue().GHRQProperty());
        TableColumn<PatientTableData, String> column4 = new TableColumn("号种类型");
        column4.setCellValueFactory(cellData -> cellData.getValue().HZLXProperty());

        patientTable.setItems(data);
        patientTable.getColumns().addAll(column1, column2, column3, column4);

        patientTable.setItems(data);
        String sqlBR = "SELECT * FROM  t_ghxx WHERE YSBH='" + Data.doctor.user + "';";
        try {
            ResultSet res = dataBaseCon.statement.executeQuery(sqlBR), resBR;
            String GHBH, BRMC, BRBH, GHRQ, HZLX, HZBH;
            boolean THBZ;
            while (res.next()) {
                THBZ = res.getBoolean("THBZ");
                System.out.println("aaaa");

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
                System.out.println("wula");
                sqlBR = "SELECT SFZJ FROM t_hzxx WHERE HZBH='" + HZBH + "';";
                resBR = dataBaseCon.con.createStatement().executeQuery(sqlBR);
                if (resBR.next()) {
                    if (resBR.getBoolean(1)) {
                        HZLX = "专家号";
                    } else {
                        HZLX = "普通号";
                    }
                } else return;
                System.out.println("oooo");

                PatientTableData patientTableData = new PatientTableData(GHBH, BRMC, GHRQ, HZLX);
                data.add(patientTableData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
}

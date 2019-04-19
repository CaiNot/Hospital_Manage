package resources.module;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import resources.DataBaseCon;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    @FXML
    private void handleLoginBtn(ActionEvent event) throws Exception {
        table = "t_brxx";
        userNum = "BRBH";
        user = userIn.getText().trim();
        psd = passwdIn.getText().trim();
        path = "../view/registered.fxml";
        title = "病人挂号";

        if (isDoctor.isSelected()) {
            table = "t_ksys";
            path = "../view/DoctorView.fxml";
            title = "医生";
        }
        sqlBR = "SELECT * FROM " + table + " WHERE " + userNum + "='" +
                user + "'AND DLKL='" + psd + "';";
        ResultSet res = dataBaseCon.statement.executeQuery(sqlBR);
        String name = "";
        Double money = 0.0;
        Date date;
        if (res.next()) {
            name = res.getString("BRMC");
            money = res.getBigDecimal("YCJE").doubleValue();
            date = res.getDate("DLRQ");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Wrong!");
            alert.showAndWait();
        }
        System.out.println(name);
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene sceneRegister = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(sceneRegister);
        stage.show();

        Stage curStage = (Stage) loginBtn.getScene().getWindow();
        curStage.close();
    }

    @FXML
    private void handleQuitBtn(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void initialize() {
        String Url = "jdbc:mysql://localhost:3306/hospital";//参数参考MySql连接数据库常用参数及代码示例
        String name = "cai";//数据库用户名
        String psd = "cai";//数据库密码

        dataBaseCon = new DataBaseCon();
        if (dataBaseCon.connect(Url, name, psd)) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
    }
}

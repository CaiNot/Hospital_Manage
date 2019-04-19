package resources;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Doctor {
    public String user;
    public String department;
    public String name;
    public String pingyin;
    public String psd;
    public boolean isPro;
    public Date datetime;

    public void setData(ResultSet resultSet) throws SQLException {
        this.user=resultSet.getString("YSBH");
        this.psd=resultSet.getString("DLKL");
        this.department = resultSet.getString("KSBH");
        this.name = resultSet.getString("YSMC");
        this.pingyin = resultSet.getString("PYZS");
        this.isPro = resultSet.getBoolean("SFZJ");
        this.datetime = resultSet.getDate("DLRQ");
    }
}

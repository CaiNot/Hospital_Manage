package resources;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Department {
    public String num;
    public String name;
    public String pingyin;

    public void setData(ResultSet resultSet) throws SQLException {
        this.num = resultSet.getString("KSBH");
        this.name = resultSet.getString("KSMC");
        this.pingyin = resultSet.getString("PYZS");
    }
}

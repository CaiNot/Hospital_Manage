package resources;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NumKind {
    public String id, name, pingyin, departmentId;
    public boolean isPro;
    public int peopleNum;
    public BigDecimal money;

    public void setData(ResultSet resultSet) throws SQLException {
        id = resultSet.getString("HZBH");
        name = resultSet.getString("HZMC");
        pingyin = resultSet.getString("PYZS");
        departmentId = resultSet.getString("KSBH");
        isPro = resultSet.getBoolean("SFZJ");
        peopleNum = resultSet.getInt("GHRS");
        money = resultSet.getBigDecimal("GHFY");
    }
}

package resources.module;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Patient {
    public String user;
    public String name;
    public String psd;
    public BigDecimal money;
    public Date datetime;

    public void setDate(ResultSet resultSet) throws SQLException {
        this.name = resultSet.getString("BRMC");
        this.user = resultSet.getString("BRBH");
        this.psd = resultSet.getString("DLKL");
        this.money = resultSet.getBigDecimal("YCJE");
        this.datetime = resultSet.getDate("DLRQ");
    }
}

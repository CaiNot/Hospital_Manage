package resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseCon {
    public Connection con;
    public Statement statement;

    public boolean connect(String Url, String name, String psd) {
        String jdbcName = "com.mysql.jdbc.Driver";//连接MySql数据库
        try {
            Class.forName(jdbcName);//向DriverManager注册自己
            con = DriverManager.getConnection(Url, name, psd);//与数据库建立连接
            statement = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}

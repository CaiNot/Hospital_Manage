package resources.module;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseCon {
    public Connection con = null;
    public Statement statement = null;
    String Url = "jdbc:mysql://localhost:3306/hospital";//参数参考MySql连接数据库常用参数及代码示例
    String name = "cai";//数据库用户名
    String psd = "cai";//数据库密码

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

    public boolean connect() {
        return this.connect(Url, name, psd);
    }

    public void close() {
        try {
            if (this.statement != null)
                this.statement.close();
            if (this.con != null)
                this.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

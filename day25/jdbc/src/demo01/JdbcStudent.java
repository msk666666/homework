package demo01;

import java.sql.*;

public class JdbcStudent {
    static final String URL="jdbc:mysql://localhost:3306/test";
    static final String NAME="root";
    static final String PASSWORD="westos";


    public static void main(String[] args) {
        try(Connection conn=DriverManager.getConnection(URL,NAME,PASSWORD);
                )
        {

            int i1 = insertPre(conn, "小李子", "1976-12-09", "男");
            System.out.println(i1);
            int i2 = insertData(conn, "王祖贤", "1964-8-9", "女");
            System.out.println(i2);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static int insertPre(Connection conn,String sname,String birthday,String sex) throws SQLException {
        try (
                PreparedStatement prep = conn.prepareStatement("insert into student(sname,birthday,sex) values(?,?,?)");
                ){
            //给占位？赋值
            prep.setString(1,sname);
            prep.setString(2,birthday);
            prep.setString(3,sex);
            //执行
            return prep.executeUpdate();

        }
    }

    private static int insertData(Connection conn,String sname,String birthday,String sex) throws SQLException {
        try (
                Statement stmt = conn.createStatement();
        ){
            String sqlI="insert into student(sname,birthday,sex) values ('"+sname+"','"+birthday+"','"+sex+"')";
            return stmt.executeUpdate(sqlI);
        }

    }
}

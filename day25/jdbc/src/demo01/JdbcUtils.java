package demo01;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JdbcUtils {
    static final String URL="jdbc:mysql://localhost:3306/test";
    static final String NAME="root";
    static final String PASSWORD="westos";

    public Connection getConnection(){
        try {
            System.out.println("数据库已连接");
            return DriverManager.getConnection(URL,NAME,PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //查询自增列的主键值
    public void test(){
        try (Connection conn = this.getConnection();){
            String sql="insert into student(sid,sname,birthday,sex) values(null,?,?,?)";
            //Statement.RETURN_GENERATED_KEYS  表示一个选项，表示要返回自增主键值
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,"aaa");
            ps.setString(2,"1990-9-06");
            ps.setString(3,"男");
            int i = ps.executeUpdate();
            System.out.println("影响行数："+i);
            //查询刚刚插入的主键
            ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            System.out.println("刚刚新增的主键值："+generatedKeys.getInt(1));

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //查询所有学生
    public ArrayList<Student> findStudent() throws SQLException {
        Connection conn = this.getConnection();
        ArrayList<Student> listStudent = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement("select * from student");
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                listStudent.add(new Student(
                        resultSet.getInt(1),
                        resultSet.getNString(2),
                        resultSet.getDate(3),
                        resultSet.getNString(4)
                ));
            }
            return listStudent;

    }
    public List<Object> findBySid(int sid) throws SQLException {
        Connection conn = this.getConnection();
        PreparedStatement pst = conn.prepareStatement("select * from student where sid=?");
        pst.setInt(1,sid);
        ResultSet resultSet = pst.executeQuery();
        Student st;
        while (resultSet.next()) {
            st=new Student(
                    resultSet.getInt(1),
                    resultSet.getNString(2),
                    resultSet.getDate(3),
                    resultSet.getNString(4)
            );
            return (List<Object>) st;
        }
        return null;
    }

    public  ResultSet findByName(String name) throws SQLException {
        Connection conn = this.getConnection();
        PreparedStatement pst = conn.prepareStatement("select * from student where sname=?");
        pst.setString(1,name);
        ResultSet resultSet = pst.executeQuery();
        return resultSet;

    }
    public ResultSet findNumOfDep() throws SQLException {
        Connection conn = this.getConnection();
        PreparedStatement pst = conn.prepareStatement("select deptno,count(deptno) as num from emp group by deptno");
        ResultSet resultSet = pst.executeQuery();
        return resultSet;
    }

    public Boolean sqlAtt(String username,String password) {
        try (Connection conn = this.getConnection();) {
            Statement stmt = conn.createStatement();
            String sql = "select * from user where username='" + username + "' and password='" + password + "'";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws SQLException {
        JdbcUtils js = new JdbcUtils();
//        ArrayList<Student> student = js.findStudent();
//        System.out.println(student);

//        ResultSet st = js.findByName("王祖贤");
//        while (st.next()){
//            System.out.println(st.getInt(1));
//        }
//        Student bySid = js.findBySid(1001);
//        System.out.println(bySid);
//        ResultSet numOfDep = js.findNumOfDep();
//        while (numOfDep.next()){
//            System.out.println(numOfDep.getInt(1)+","+ numOfDep.getInt(2));
//        }
        System.out.println(js.sqlAtt("李青","789'  or '1=1"));
        //select * from user where username='李青' and password=''  or '1=1 '
        //true

    }
}

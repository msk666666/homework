package demo02;

import jdk.nashorn.internal.objects.annotations.Where;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class HandleMysql {

    //插入数据
    //需要传入sql语句和要添加的值
    public void insert(String sql, Object... values){
        try (Connection conn = JdbcuUtils.getConnection();)
        {
            PreparedStatement ps = conn.prepareStatement(sql);
            //给占位？赋值
            int i=1;
            for (Object value : values) {
                ps.setObject(i,value);
                i++;
            }
            int i1 = ps.executeUpdate();
            System.out.println("已插入"+i1+"条内容");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    //删除数据
    public void deleteOrUpdate(String sql){

        try(Connection conn = JdbcuUtils.getConnection();){
            PreparedStatement ps = conn.prepareStatement(sql);
            int i = ps.executeUpdate();
            System.out.println("已影响"+i+"行内容");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //查询数据
    public <E> List<E> findByCondition(String sql,RowsMapper rowsMapper,Object... values){
        try (Connection conn = JdbcuUtils.getConnection();)
        {
            PreparedStatement ps = conn.prepareStatement(sql);
            int i=1;
            for (Object value : values) {
                ps.setObject(i,value);
                i++;
            }
            ResultSet rs = ps.executeQuery();
            ArrayList<E> array = new ArrayList<>();
            //查询结果会有不同，所以抽象成接口
            while (rs.next()){
                array.add((E) rowsMapper.mapper(rs));
            }

            return array;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static void main(String[] args) {
        HandleMysql hm = new HandleMysql();
        hm.insert("insert into student1(sid,sname,birthday,sex) values(null,?,?,?)","lucy","1999-03-06","男");

        String sql="select * from student1 where sname like ?";
        List<Object> studentList = hm.findByCondition(sql, new RowsMapper() {
            @Override
            public Object mapper(ResultSet rs) throws SQLException {
                return new Student(
                        rs.getInt(1),
                        rs.getNString(2),
                        rs.getDate(3),
                        rs.getNString(4)
                );
            }
        },"张%");
        studentList.forEach(s->{
            System.out.println(s);
        });

        hm.deleteOrUpdate("update student set sname='carry' where sid=1001");
    }
}

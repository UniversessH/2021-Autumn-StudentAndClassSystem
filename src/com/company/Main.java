package com.company;

import java.sql.*;

public class Main {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; //注册驱动
    static final String DB_URL = "jdbc:mysql://localhost:3306/managesystem?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"; //连接数据库

    static final String USER = "root";
    static final String PASS ="hjc030121";

    public static void main(String[] args) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        try{
            //注册JDBC驱动
            Class.forName(JDBC_DRIVER);

            //连接数据库
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //查询课表内容
            System.out.println("实例化Statement对象...");
            stmt = conn.createStatement();
            String sql; //定义查询的SQL语句
            sql = "SELECT id, className, classNum FROM classdata";
            ResultSet rs = stmt.executeQuery(sql);

            //展开结果集数据库
            while(rs.next()){
                //通过字段检索
                int id = rs.getInt("id");
                String className = rs.getString("className");
                int classNum = rs.getInt("classNum");
                System.out.print("课程编号：" + id);
                System.out.print(",   课程名称：" + className);
                System.out.println(",   课程数量：" + classNum);
            }


            //完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
        e.printStackTrace();
        }finally {
            //关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch (SQLException se2){
                //do nothing here;
            }
            try{
                if(conn!=null) conn.close();
            }catch (SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Say goodbye");
    }
}

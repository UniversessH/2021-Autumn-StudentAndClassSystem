package com.company;

import java.sql.*;
import java.util.Scanner;

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

            String sql; //定义查询课程表的SQL语句
            sql = "SELECT id, className, classNum FROM classdata";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("欢迎使用学生选课系统，请输入课程编号以进行选课，输入0以结束选课 (严禁输入课程编号以外的数字)");

            //展开结果集数据库
            while(rs.next()){
                //通过字段检索
                int id = rs.getInt("id");
                String className = rs.getString("className");
                int classNum = rs.getInt("classNum");
                System.out.print("课程编号：" + id);
                System.out.print(",\t\t课程名称：" + className);
                System.out.println(",\t\t课程余量：" + classNum);
            }

            //选课
            int[] classTag = new int[6];
            int temp = 0;
            int index = 0;
            Scanner scan = new Scanner(System.in);
            while (true){
                if(classTag[5] != 0)
                {
                    System.out.println("请至少选择三门课程，最多不超过五门课程，接下来请重新选课");
                    classTag = new int[6]; //将选课内容清0
                    index = 0;
                    continue;
                }
                temp = scan.nextInt();
                classTag[index] = temp;
                if(temp == 0){
                    if(index<3) {
                        System.out.println("请至少选择三门课程，最多不超过五门课程，接下来请重新选课");
                        classTag = new int[6]; //将选课内容清0
                        index = 0;
                        continue;
                    }else if(CompareInt(classTag,index)){
                        System.out.println("请不要输入相同的课程代号！接下来请重新选课");
                        classTag = new int[6]; //将选课内容清0
                        index = 0;
                        continue;
                    }else {
                        break;
                    }
                }
                index++;
            }

            //展示选课结果并修改数据库
            String classes = "";
            for(int i=0; i<index-1; i++){
                classes = classes + classTag[i] + ',';
            }
            classes = classes + classTag[index-1];

            String sql1; //定义查询所选课程的SQL语句
            String sql2; //定义修改课程数量的SQL语句
            sql1 = "SELECT className,classScore FROM classdata WHERE id IN (" + classes + ")";
            sql2 = "UPDATE classdata SET classNum=classNum-1 WHERE id IN (" + classes + ")";

            PreparedStatement ps1 = conn.prepareStatement(sql1);
            PreparedStatement ps1_ = conn.prepareStatement(sql1);
            PreparedStatement ps2 = conn.prepareStatement(sql2);

            ResultSet rs1 = ps1.executeQuery(sql1);
            ResultSet rs2 = ps1_.executeQuery(sql1);

            int n = ps2.executeUpdate(); //返回更新的行数
            System.out.println("你所选取的课程为:");
            while(rs1.next()){
                String className = rs1.getString("className");
                System.out.println(className);
            }

            //为学生生成成绩并根据是否挂科输出挂科通知单
            System.out.println("正在生成成绩单...");
            while (rs2.next()){
                System.out.println();
                String className = rs2.getString("className");
                int classScore = rs2.getInt("classScore");
                int score = 1 + (int)(Math.random() * (100));
                if(score >=60){
                    System.out.println("恭喜你！ 你的《" + className + "》课程以" + score + "分的成绩通过！"  );
                    System.out.println("该门课程所获学分为" + classScore + "分");
                }else {
                    System.out.println("很遗憾，你的《" + className + "》课程分数为" + score + "分");
                    System.out.println("该门课程所获学分为0分");
                    System.out.println("请在开学后与辅导员取得联系，得知补考地点以及获得准考证");
                }
            }

            //完成后关闭
            rs.close();
            rs1.close();
            rs2.close();
            ps.close();
            ps1.close();
            ps1_.close();
            ps2.close();
            conn.close();
            scan.close();
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
        System.out.println();
        System.out.println("感谢使用学生选课系统，欢迎下次使用！");
    }

    public static boolean CompareInt(int[] classTag,int index){
        for(int i=0; i<index; i++){
            for(int j=i+1; j<index; j++){
                if(classTag[i] == classTag[j]) return true; //当数组中存在两个数字相等，返回true
            }
        }
        return false;
    }
}

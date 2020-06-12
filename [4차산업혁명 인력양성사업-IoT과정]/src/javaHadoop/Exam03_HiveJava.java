package javaHadoop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Exam03_HiveJava {

	public static void main(String[] args) {
        Connection conn = null;
        ResultSet rs = null;
       
        try{           
            String driver = "org.apache.hive.jdbc.HiveDriver";
            Class.forName(driver);
           
            String url = "jdbc:hive2://192.168.64.128 :10000/hiveDB";
            //기본 포트가 10000 번이고, 기본데이터베이스 이름이 defualt 임.
            String id = "hive";
            String pw = "hive";
           
            conn = DriverManager.getConnection(url, id, pw);
           
            String sql = "SELECT * FROM dept";
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
           
            while(rs.next()){
                String col1 = rs.getString(1);
                String col2 = rs.getString(2);
                String col3 = rs.getString(3);
                       
                System.out.println( col1 + "/" + col2 + "/" + col3 );
            }
           
            rs.close();
            conn.close();
           
        }catch(Exception ex){
            System.err.println("main error :");
            ex.printStackTrace();
        }finally {
            try{
                if( rs != null ){
                    rs.close();               
                }
            }catch(Exception ex){
                rs = null;
            }
           
            try{
                if( conn != null ){
                    conn.close();               
                }
            }catch(Exception ex){
                conn = null;
            }
        }
    }
}

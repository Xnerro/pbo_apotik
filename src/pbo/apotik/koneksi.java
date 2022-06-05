/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo.apotik;
import java.sql.*;
/**
 *
 * @author Bagus Sr
 */


public class koneksi {
       public static Connection conn;
       
    public Connection getConn() throws SQLException{
        if(conn == null){
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/apotik", "root", "");
           return conn;
        }
           return conn;
    }

    public Statement createStatement( String sql) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}


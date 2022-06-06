/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pbo.apotik;
import Form.Login;
import pbo.apotik.koneksi;
import java.sql.*;

/**
 *
 * @author Bagus Sr
 */
public class PBOApotik {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {    
        Login lg = new Login();
        Connection conn = new koneksi().getConn();
        Statement query = conn.createStatement();
        String sql = "SELECT * FROM users";
        ResultSet result;
        result = query.executeQuery(sql);
        if(result.first()){
            lg.setVisible(true);
        } else {
            String add = "INSERT INTO users (username, password) VALUES ('admin', 'admin')";
            query.execute(add);
            lg.setVisible(true);
        }
    }
    
}

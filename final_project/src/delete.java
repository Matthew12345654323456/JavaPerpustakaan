import java.sql.PreparedStatement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.*;

public class delete {
    public static void main(String[] args) {
        try{
            Connection database = koneksi.connect();
            String sql = "DELETE FROM users WHERE Username = ?";
            PreparedStatement pstmt = database.prepareStatement(sql);

            pstmt.setString(1, "Matthew");

            int affectedRows = pstmt.executeUpdate();
            if(affectedRows >0){
                System.out.println("Data berhasil dihapus dari tabel users.");

            }
            else{
                System.out.println("Tidak ada data yang dihapus");

            }
            database.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}

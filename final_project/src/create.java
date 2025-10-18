// java -cp ./mysql-connector-j-9.3.0.jar:. create
// C:\Users\ASUS\Documents\Java Programming\final\create.java

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class create {
    public static void main(String[] args) {
        String sql = "INSERT INTO users (Name_user, Gender, Email, Phone, Address, Date_of_birth, Username, Passwords) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection database = koneksi.connect();
             PreparedStatement pstmt = database.prepareStatement(sql)) {

            if (database != null) {
                pstmt.setString(1, "Jono");          // Name_user
                pstmt.setString(2, "laki-laki");     // Gender
                pstmt.setString(3, "jono@gmail.com");// Email
                pstmt.setString(4, "78674556");      // Phone
                pstmt.setString(5, "ubud");          // Address
                pstmt.setString(6, "1997-05-05");    // Date_of_birth
                pstmt.setString(7, "jono");          // Username
                pstmt.setString(8, "jono");          // Passwords

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Data berhasil dimasukkan ke tabel users.");
                } else {
                    System.out.println("Data gagal dimasukkan.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat memasukkan data: " + e.getMessage());
        }
    }
}
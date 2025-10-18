import java.sql.*;
public class update {
    public static void main(String[] args){
        try{
            Connection conn = koneksi.connect();
            PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET Name_user = ?, Email = ? WHERE ID_user = ?");

            pstmt.setString(1, "John");
            pstmt.setString(2, "john@gmail.com");
            pstmt.setInt(3,3);

            int affectedRows = pstmt.executeUpdate();
            System.out.println("Jumlah baris yang terpengaruh: "+ affectedRows);
            conn.close();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}

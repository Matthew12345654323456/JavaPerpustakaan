
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class selupddel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Update assignment");
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Menempatkan frame di tengah layar

        JButton view = new JButton("view");
        JButton create = new JButton("create");
        JButton delete = new JButton("delete");
        JButton update = new JButton("update");
        view.setBorderPainted(true); // Mengembalikan garis di sekitar tulisan pada button
        update.setBorderPainted(true);
        delete.setBorderPainted(true); // Mengembalikan garis di sekitar tulisan pada button
        create.setBorderPainted(true);

        // Membuat data untuk tabel
        String[] columnNames = {"ID user", "Nama", "Email"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);

        // Membuat tabel dengan model tersebut
        JTable table = new JTable(model);

        // Menambahkan tabel ke dalam JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 30);

        // Komponen di kolom pertama
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(create, gbc);

        // komponen kolom ketiga
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(view, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(update, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(delete, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 100;
        panel.add(scrollPane, gbc);
        gbc.ipady = 0;

        frame.add(panel);
        frame.setVisible(true);

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                // Membuat form untuk update data
                JTextField newNameField = new JTextField();
                JTextField newEmailField = new JTextField();
                JTextField newGenderField = new JTextField();
                JTextField newPhoneField = new JTextField();
                JTextField newAddressField = new JTextField();
                JTextField newDateField = new JTextField();
                JTextField newUsernamedField = new JTextField();
                JTextField newPasswordField = new JTextField();
                Object[] message = {
                        "Nama:", newNameField,
                        "Gender:", newGenderField,
                        "Email:", newEmailField,
                        "Phone:", newPhoneField,
                        "Address:", newAddressField,
                        "Date:", newDateField,
                        "Username:",newUsernamedField,
                        "Password:", newPasswordField
                };

                int option = JOptionPane.showConfirmDialog(frame, message, "Create Data", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String newName = newNameField.getText();
                    String newGender = newGenderField.getText();
                    String newEmail = newEmailField.getText();
                    String newPhone = newPhoneField.getText();
                    String newAddress = newAddressField.getText();
                    String newDate = newDateField.getText();
                    String newUsername = newUsernamedField.getText();
                    String newPassword = newPasswordField.getText();


                    // Update data di database
                    String sql = "INSERT INTO users (Name_user, Gender, Email, Phone, Address, Date_of_birth, Username, Passwords) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                    try (Connection database = koneksi.connect();
                         PreparedStatement pstmt = database.prepareStatement(sql)) {

                        if (database != null) {
                            pstmt.setString(1, newName);          // Name_user
                            pstmt.setString(2, newGender);     // Gender
                            pstmt.setString(3, newEmail);// Email
                            pstmt.setString(4, newPhone);      // Phone
                            pstmt.setString(5, newAddress);          // Address
                            pstmt.setString(6, newDate);    // Date_of_birth
                            pstmt.setString(7, newUsername);          // Username
                            pstmt.setString(8, newPassword);          // Passwords

                            int rowsInserted = pstmt.executeUpdate();




                            if (rowsInserted > 0) {
                                System.out.println("Data berhasil dimasukkan ke tabel users.");
                            } else {
                                System.out.println("Data gagal dimasukkan.");
                            }



                        }
                    } catch (SQLException s) {
                        System.out.println("Terjadi kesalahan saat memasukkan data: " + s.getMessage());
                    }
                }

            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int option = JOptionPane.showConfirmDialog(frame, "Apakah ingin menhapus data?", "Delete Data", JOptionPane.OK_CANCEL_OPTION);
                    if(option == JOptionPane.OK_OPTION){

                        int id_user = (int) model.getValueAt(selectedRow, 0);



                        try{
                            Connection database = koneksi.connect();
                            String sql = "DELETE FROM users WHERE ID_user = ?";
                            PreparedStatement pstmt = database.prepareStatement(sql);

                            pstmt.setInt(1, id_user);

                            int affectedRows = pstmt.executeUpdate();
                            if(affectedRows >0){
                                System.out.println("Data berhasil dihapus dari tabel users.");

                            }
                            else{
                                System.out.println("Tidak ada data yang dihapus");

                            }
                            database.close();


                            try {
                                Connection conn = koneksi.connect();
                                Statement stmt = conn.createStatement();
                                sql = "SELECT ID_user, Name_user, Email FROM users";
                                ResultSet rs = stmt.executeQuery(sql);

                                // Membersihkan tabel sebelum menampilkan data baru
                                model.setRowCount(0);

                                while (rs.next()) {
                                    id_user = rs.getInt("ID_user");
                                    String name_user = rs.getString("Name_user");
                                    String email_user = rs.getString("Email");

                                    // Menambahkan baris ke dalam model tabel
                                    model.addRow(new Object[]{id_user, name_user, email_user});
                                }
                                conn.close();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }


                            JOptionPane.showConfirmDialog(frame, "Data Berhasil dihapus!", "Delete Data", JOptionPane.CLOSED_OPTION);

                        }
                        catch (SQLException f){
                            f.printStackTrace();
                        }
                    }}}
        });

        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = koneksi.connect();
                    Statement stmt = conn.createStatement();
                    String sql = "SELECT ID_user, Name_user, Email FROM users";
                    ResultSet rs = stmt.executeQuery(sql);

                    // Membersihkan tabel sebelum menampilkan data baru
                    model.setRowCount(0);

                    while (rs.next()) {
                        int id_user = rs.getInt("ID_user");
                        String name_user = rs.getString("Name_user");
                        String email_user = rs.getString("Email");

                        // Menambahkan baris ke dalam model tabel
                        model.addRow(new Object[]{id_user, name_user, email_user});
                    }
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implementasi untuk update data
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int id_user = (int) model.getValueAt(selectedRow, 0);
                    String name_user = (String) model.getValueAt(selectedRow, 1);
                    String email_user = (String) model.getValueAt(selectedRow, 2);

                    // Membuat form untuk update data
                    JTextField newNameField = new JTextField(name_user);
                    JTextField newEmailField = new JTextField(email_user);
                    Object[] message = {
                            "Nama:", newNameField,
                            "Email:", newEmailField
                    };

                    int option = JOptionPane.showConfirmDialog(frame, message, "Update Data", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String newName = newNameField.getText();
                        String newEmail = newEmailField.getText();

                        // Update data di database
                        try {
                            Connection conn = koneksi.connect();
                            String sql = "UPDATE users SET Name_user = ?, Email = ? WHERE ID_user = ?";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            pstmt.setString(1, newName);
                            pstmt.setString(2, newEmail);
                            pstmt.setInt(3, id_user);
                            pstmt.executeUpdate();
                            conn.close();

                            // Update data di tabel
                            model.setValueAt(newName, selectedRow, 1);
                            model.setValueAt(newEmail, selectedRow, 2);

                            JOptionPane.showMessageDialog(frame, "Data berhasil diupdate.");

                            try {
                                conn = koneksi.connect();
                                Statement stmt = conn.createStatement();
                                 sql = "SELECT ID_user, Name_user, Email FROM users";
                                ResultSet rs = stmt.executeQuery(sql);

                                // Membersihkan tabel sebelum menampilkan data baru
                                model.setRowCount(0);

                                while (rs.next()) {
                                     id_user = rs.getInt("ID_user");
                                     name_user = rs.getString("Name_user");
                                     email_user = rs.getString("Email");

                                    // Menambahkan baris ke dalam model tabel
                                    model.addRow(new Object[]{id_user, name_user, email_user});
                                }
                                conn.close();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }



                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(frame, "Error saat update data: " + ex.getMessage());
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Pilih baris yang ingin diupdate!");
                }
            }
        });
    }
}


import java.awt.*;
import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class assignmentcreateconnection {
    public static void main(String[] args){
        JFrame frame = new JFrame("Data Diri");
        frame.setSize(400, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel nama = new JLabel("Nama : ");
        JLabel email = new JLabel("Email : ");
        JLabel phone = new JLabel("Phone : ");
        JLabel gender = new JLabel("Gender : ");
        JLabel address = new JLabel("Address : ");
        JLabel date = new JLabel("Date of birth : ");
        JLabel username = new JLabel("Username : ");
        JLabel passwords = new JLabel("Password : ");

        JTextField text_nama = new JTextField(20);
        JTextField text_gender = new JTextField(20);
        JTextField text_email = new JTextField(20);
        JTextField text_phone = new JTextField(20);
        JTextField text_address = new JTextField(20);
        JTextField text_date = new JTextField(20);
        JTextField text_username = new JTextField(20);
        JTextField text_passwords = new JTextField(20);

        JButton submit = new JButton("Submit");


        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(nama, gbc);


        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(text_nama, gbc);



        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(gender, gbc);


        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(text_gender, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(email, gbc);


        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(text_email, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(phone, gbc);


        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(text_phone, gbc);


        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(address, gbc);


        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(text_address, gbc);


        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(date, gbc);


        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(text_date, gbc);


        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(username, gbc);


        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(text_username, gbc);


        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwords, gbc);


        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(text_passwords, gbc);


        gbc.gridx=0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(submit, gbc);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String sql = "INSERT INTO users (Name_user, Gender, Email, Phone, Address, Date_of_birth, Username, Passwords) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                try (Connection database = koneksi.connect();
                     PreparedStatement pstmt = database.prepareStatement(sql)) {

                    if (database != null) {
                        pstmt.setString(1, text_nama.getText());          // Name_user
                        pstmt.setString(2, text_gender.getText());     // Gender
                        pstmt.setString(3, text_email.getText());// Email
                        pstmt.setString(4, text_phone.getText());      // Phone
                        pstmt.setString(5, text_address.getText());          // Address
                        pstmt.setString(6, text_date.getText());    // Date_of_birth
                        pstmt.setString(7, text_username.getText());          // Username
                        pstmt.setString(8, text_passwords.getText());          // Passwords
                        String password = text_passwords.getText();
                        int rowsInserted = pstmt.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Data berhasil dimasukkan ke tabel users.");
                        } else {
                            System.out.println("Data gagal dimasukkan.");
                        }
                    }
                } catch (SQLException m) {
                    System.out.println("Terjadi kesalahan saat memasukkan data: " + m.getMessage());
                }



            }
        });

        frame.add(panel);
        frame.setVisible(true);



    }
}

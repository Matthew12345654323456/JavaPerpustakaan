
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.*;

public class selupd {
    public static void main(String[] args){



        JFrame frame = new JFrame("Update Assignment");
        frame.setSize(600,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        JButton view = new JButton("View");
        JButton update = new JButton("Update");
        view.setBorderPainted(true); // Mengembalikan garis di sekitar tulisan pada button
        update.setBorderPainted(true);
        String[] columnNames = {"ID user", "Nama", "Email"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);













        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 140, 10, 140);

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(view, gbc);

        gbc.gridx=1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(update, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 100;
        gbc.ipadx=0;
        panel.add(scrollPane, gbc);


        frame.add(panel);
        frame.setVisible(true);

        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                model.setRowCount(0);

                try {
                    Connection conn = koneksi.connect();
                    Statement stmt = conn.createStatement();
                    String sql = "SELECT * FROM users";
                    ResultSet rs = stmt.executeQuery(sql);

                    while(rs.next()){
                        int id_user = rs.getInt("ID_user");
                        String name_user = rs.getString("Name_user");
                        String email_user = rs.getString("Email");

                        System.out.println("ID user: " + id_user + ", Name user: " + name_user + ", Email : " + email_user);


                        model.addRow(new Object[]{id_user, name_user, email_user});
                    }
                    conn.close();
                } catch(SQLException m){
                    m.printStackTrace();
                }



                JTable table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(table);


                frame.add(panel);
                frame.setVisible(true);




                for(int k = 0; k<model.getRowCount(); k++){
                    Integer id =  (Integer) model.getValueAt(k, 0);
                    String name = (String) model.getValueAt(k, 1);
                    String email = (String) model.getValueAt(k, 2);
                    System.out.println("ID: " + id +  " name: "+ name + " email: " + email);
                }




                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.ipady = 100;
                gbc.ipadx=0;
                panel.add(scrollPane, gbc);
            }
        });

        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow =  table.getSelectedRow();
                if(selectedRow >= 0){
                    int id_user = (int) model.getValueAt(selectedRow, 0);
                    String nama_user = (String) model.getValueAt(selectedRow,1);
                    String email_user = (String) model.getValueAt(selectedRow,2);

                    JTextField newNameField = new JTextField(nama_user);
                    JTextField newEmailField = new JTextField(email_user);
                    Object[] message = {
                            "Nama:", newNameField,
                            "Email:", newEmailField
                    };
                    int option = JOptionPane.showConfirmDialog(frame, message, "Update Data", JOptionPane.OK_CANCEL_OPTION);
                    if(option == JOptionPane.OK_OPTION){
                        String newName = newNameField.getText();
                        String newEmail = newEmailField.getText();

                        try{
                            Connection conn = koneksi.connect();
                            PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET Name_user = ?, Email = ? WHERE ID_user = ?");
                            pstmt.setString(1, newName);
                            pstmt.setString(2, newEmail);
                            pstmt.setInt(3, id_user);
                            pstmt.executeUpdate();
                            conn.close();

                            model.setValueAt(newName, selectedRow, 1);
                            model.setValueAt(newEmail, selectedRow, 2);
                            JOptionPane.showMessageDialog(frame, "Data berhasil diupdate.");
                        } catch(SQLException k){
                            k.printStackTrace();
                        }
                    }

                }
            }
        });

    }
}
//use Object[
//500,300

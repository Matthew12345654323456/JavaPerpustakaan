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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class selectupdate {
    public static void main(String[] args){



        JFrame frame = new JFrame("Update Assignment");
        frame.setSize(600,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton view = new JButton("View");
        JButton update = new JButton("Update");
        String[] columnNames = {"ID user", "Nama", "Email"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
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
        } catch(SQLException e){
            e.printStackTrace();
        }



        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);








        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 140, 10, 140);


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

        for(int k = 0; k<model.getRowCount(); k++){
            Integer id =  (Integer) model.getValueAt(k, 0);
            String name = (String) model.getValueAt(k, 1);
            String email = (String) model.getValueAt(k, 2);
            System.out.println("ID: " + id +  " name: "+ name + " email: " + email);
        }
    }
}
//use Object[
     //500,300

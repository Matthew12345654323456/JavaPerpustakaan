import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("Login Admin");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        panel.add(new JLabel("Username:"));
        tfUsername = new JTextField();
        panel.add(tfUsername);
        panel.add(new JLabel("Password:"));
        tfPassword = new JPasswordField();
        panel.add(tfPassword);

        btnLogin = new JButton("Login");
        panel.add(new JLabel());
        panel.add(btnLogin);

        add(panel);

        btnLogin.addActionListener(e -> {
            String user = tfUsername.getText();
            String pass = new String(tfPassword.getPassword());
            if (cekLogin(user, pass)) {
                new MainFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username/password salah!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private boolean cekLogin(String username, String password) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM admin WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AnggotaPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnTambah, btnHapus, btnRefresh;
    private JTextField tfCari;

    public AnggotaPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Nama", "Alamat", "No Telp"}, 0);
        table = new JTable(model);

        JPanel atas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tfCari = new JTextField(20);
        btnRefresh = new JButton("Refresh");
        btnTambah = new JButton("Tambah Anggota");
        btnHapus = new JButton("Hapus Anggota");
        atas.add(new JLabel("Cari Nama:"));
        atas.add(tfCari);
        atas.add(btnRefresh);
        atas.add(btnTambah);
        atas.add(btnHapus);

        add(atas, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadAnggota("");

        btnRefresh.addActionListener(e -> loadAnggota(tfCari.getText()));
        btnTambah.addActionListener(e -> tambahAnggota());
        btnHapus.addActionListener(e -> hapusAnggota());
    }

    private void loadAnggota(String keyword) {
        model.setRowCount(0);
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM anggota WHERE nama LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_anggota"),
                        rs.getString("nama"),
                        rs.getString("alamat"),
                        rs.getString("no_telp")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void tambahAnggota() {
        JTextField nama = new JTextField();
        JTextField alamat = new JTextField();
        JTextField noTelp = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Nama:")); panel.add(nama);
        panel.add(new JLabel("Alamat:")); panel.add(alamat);
        panel.add(new JLabel("No Telp:")); panel.add(noTelp);

        int result = JOptionPane.showConfirmDialog(null, panel, "Tambah Anggota", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = Database.getConnection()) {
                String sql = "INSERT INTO anggota (nama, alamat, no_telp) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nama.getText());
                ps.setString(2, alamat.getText());
                ps.setString(3, noTelp.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Anggota berhasil ditambah!");
                loadAnggota("");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal menambah anggota!");
            }
        }
    }

    private void hapusAnggota() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih anggota yang ingin dihapus!");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Hapus anggota ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = Database.getConnection()) {
                String sql = "DELETE FROM anggota WHERE id_anggota=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Anggota dihapus!");
                loadAnggota("");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
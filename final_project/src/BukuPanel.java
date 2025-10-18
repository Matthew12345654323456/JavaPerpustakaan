import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BukuPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnTambah, btnHapus, btnRefresh;
    private JTextField tfCari;

    public BukuPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Judul", "Pengarang", "Tahun", "Genre", "Stok"}, 0);
        table = new JTable(model);

        JPanel atas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tfCari = new JTextField(20);
        btnRefresh = new JButton("Refresh");
        btnTambah = new JButton("Tambah Buku");
        btnHapus = new JButton("Hapus Buku");
        atas.add(new JLabel("Cari Judul:"));
        atas.add(tfCari);
        atas.add(btnRefresh);
        atas.add(btnTambah);
        atas.add(btnHapus);

        add(atas, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadBuku("");

        btnRefresh.addActionListener(e -> loadBuku(tfCari.getText()));
        btnTambah.addActionListener(e -> tambahBuku());
        btnHapus.addActionListener(e -> hapusBuku());
    }

    private void loadBuku(String keyword) {
        model.setRowCount(0);
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM buku WHERE judul LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_buku"),
                        rs.getString("judul"),
                        rs.getString("pengarang"),
                        rs.getInt("tahun_terbit"),
                        rs.getString("genre"),
                        rs.getInt("stok")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void tambahBuku() {
        JTextField judul = new JTextField();
        JTextField pengarang = new JTextField();
        JTextField tahun = new JTextField();
        JTextField genre = new JTextField();
        JTextField stok = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Judul:")); panel.add(judul);
        panel.add(new JLabel("Pengarang:")); panel.add(pengarang);
        panel.add(new JLabel("Tahun:")); panel.add(tahun);
        panel.add(new JLabel("Genre:")); panel.add(genre);
        panel.add(new JLabel("Stok:")); panel.add(stok);

        int result = JOptionPane.showConfirmDialog(null, panel, "Tambah Buku", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = Database.getConnection()) {
                String sql = "INSERT INTO buku (judul, pengarang, tahun_terbit, genre, stok) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, judul.getText());
                ps.setString(2, pengarang.getText());
                ps.setInt(3, Integer.parseInt(tahun.getText()));
                ps.setString(4, genre.getText());
                ps.setInt(5, Integer.parseInt(stok.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Buku berhasil ditambah!");
                loadBuku("");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal menambah buku!");
            }
        }
    }

    private void hapusBuku() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku yang ingin dihapus!");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Hapus buku ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = Database.getConnection()) {
                String sql = "DELETE FROM buku WHERE id_buku=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Buku dihapus!");
                loadBuku("");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
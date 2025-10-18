import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PeminjamanPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnPinjam, btnKembali, btnRefresh;

    public PeminjamanPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Anggota", "Buku", "Tgl Pinjam", "Tgl Kembali", "Status"}, 0);
        table = new JTable(model);

        JPanel atas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRefresh = new JButton("Refresh");
        btnPinjam = new JButton("Tambah Peminjaman");
        btnKembali = new JButton("Pengembalian");
        atas.add(btnRefresh);
        atas.add(btnPinjam);
        atas.add(btnKembali);

        add(atas, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadPeminjaman();

        btnRefresh.addActionListener(e -> loadPeminjaman());
        btnPinjam.addActionListener(e -> tambahPeminjaman());
        btnKembali.addActionListener(e -> pengembalian());
    }

    private void loadPeminjaman() {
        model.setRowCount(0);
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT p.id_peminjaman, a.nama, b.judul, p.tanggal_pinjam, p.tanggal_kembali, p.status " +
                    "FROM peminjaman p " +
                    "JOIN anggota a ON p.id_anggota=a.id_anggota " +
                    "JOIN buku b ON p.id_buku=b.id_buku " +
                    "ORDER BY p.tanggal_pinjam DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getDate(5),
                        rs.getString(6)
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void tambahPeminjaman() {
        try (Connection conn = Database.getConnection()) {
            // Pilih anggota
            DefaultComboBoxModel<Integer> anggotaModel = new DefaultComboBoxModel<>();
            DefaultComboBoxModel<String> anggotaNameModel = new DefaultComboBoxModel<>();
            Statement stAnggota = conn.createStatement();
            ResultSet rsAnggota = stAnggota.executeQuery("SELECT id_anggota, nama FROM anggota");
            while (rsAnggota.next()) {
                anggotaModel.addElement(rsAnggota.getInt("id_anggota"));
                anggotaNameModel.addElement(rsAnggota.getString("nama"));
            }

            JComboBox<Integer> cbIdAnggota = new JComboBox<>(anggotaModel);
            JComboBox<String> cbNamaAnggota = new JComboBox<>(anggotaNameModel);

            // Pilih buku (yang stok > 0)
            DefaultComboBoxModel<Integer> bukuModel = new DefaultComboBoxModel<>();
            DefaultComboBoxModel<String> bukuNameModel = new DefaultComboBoxModel<>();
            Statement stBuku = conn.createStatement();
            ResultSet rsBuku = stBuku.executeQuery("SELECT id_buku, judul FROM buku WHERE stok > 0");
            while (rsBuku.next()) {
                bukuModel.addElement(rsBuku.getInt("id_buku"));
                bukuNameModel.addElement(rsBuku.getString("judul"));
            }
            if (bukuModel.getSize() == 0) {
                JOptionPane.showMessageDialog(this, "Tidak ada buku tersedia untuk dipinjam.");
                return;
            }
            JComboBox<Integer> cbIdBuku = new JComboBox<>(bukuModel);
            JComboBox<String> cbNamaBuku = new JComboBox<>(bukuNameModel);

            JPanel panel = new JPanel(new GridLayout(0, 2));
            panel.add(new JLabel("Anggota:")); panel.add(cbNamaAnggota);
            panel.add(new JLabel("Buku:")); panel.add(cbNamaBuku);

            int result = JOptionPane.showConfirmDialog(null, panel, "Tambah Peminjaman", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                int idAnggota = cbIdAnggota.getItemAt(cbNamaAnggota.getSelectedIndex());
                int idBuku = cbIdBuku.getItemAt(cbNamaBuku.getSelectedIndex());
                String tanggalPinjam = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                String sql = "INSERT INTO peminjaman (id_buku, id_anggota, tanggal_pinjam, status) VALUES (?, ?, ?, 'Dipinjam')";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, idBuku);
                ps.setInt(2, idAnggota);
                ps.setString(3, tanggalPinjam);
                ps.executeUpdate();

                // Kurangi stok buku
                PreparedStatement ps2 = conn.prepareStatement("UPDATE buku SET stok = stok - 1 WHERE id_buku = ?");
                ps2.setInt(1, idBuku);
                ps2.executeUpdate();

                JOptionPane.showMessageDialog(this, "Peminjaman berhasil ditambah!");
                loadPeminjaman();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void pengembalian() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi peminjaman yang ingin dikembalikan!");
            return;
        }
        String status = (String) model.getValueAt(row, 5);
        if ("Dikembalikan".equals(status)) {
            JOptionPane.showMessageDialog(this, "Buku sudah dikembalikan!");
            return;
        }
        int idPeminjaman = (int) model.getValueAt(row, 0);
        String tanggalKembali = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        try (Connection conn = Database.getConnection()) {
            // Update status dan tanggal kembali
            String sql = "UPDATE peminjaman SET tanggal_kembali=?, status='Dikembalikan' WHERE id_peminjaman=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tanggalKembali);
            ps.setInt(2, idPeminjaman);
            ps.executeUpdate();

            // Kembalikan stok buku
            String sqlBuku = "UPDATE buku SET stok = stok + 1 WHERE id_buku = (SELECT id_buku FROM peminjaman WHERE id_peminjaman=?)";
            PreparedStatement ps2 = conn.prepareStatement(sqlBuku);
            ps2.setInt(1, idPeminjaman);
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Pengembalian berhasil diproses!");
            loadPeminjaman();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
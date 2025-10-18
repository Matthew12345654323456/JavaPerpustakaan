import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;

    public MainFrame() {
        setTitle("Sistem Manajemen Perpustakaan Digital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        // Membuat Menu Bar
        JMenuBar menuBar = new JMenuBar();

        // Menu Buku
        JMenu menuBuku = new JMenu("Buku");
        JMenuItem itemDataBuku = new JMenuItem("Data Buku");
        itemDataBuku.addActionListener(e -> setPanel(new BukuPanel()));
        menuBuku.add(itemDataBuku);

        // Menu Anggota
        JMenu menuAnggota = new JMenu("Anggota");
        JMenuItem itemDataAnggota = new JMenuItem("Data Anggota");
        itemDataAnggota.addActionListener(e -> setPanel(new AnggotaPanel()));
        menuAnggota.add(itemDataAnggota);

        // Menu Peminjaman
        JMenu menuPeminjaman = new JMenu("Peminjaman");
        JMenuItem itemDataPeminjaman = new JMenuItem("Data Peminjaman");
        itemDataPeminjaman.addActionListener(e -> setPanel(new PeminjamanPanel()));
        menuPeminjaman.add(itemDataPeminjaman);

        // Menu Logout
        JMenu menuLogout = new JMenu("Logout");
        JMenuItem itemLogout = new JMenuItem("Keluar");
        itemLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        menuLogout.add(itemLogout);

        // Menambah semua menu ke menu bar
        menuBar.add(menuBuku);
        menuBar.add(menuAnggota);
        menuBar.add(menuPeminjaman);
        menuBar.add(menuLogout);

        setJMenuBar(menuBar);

        // Panel utama untuk menampung panel konten (CardLayout bisa juga dipakai)
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel);

        // Panel default (misal: panel buku)
        setPanel(new BukuPanel());
    }

    // Method untuk ganti panel di tengah frame
    private void setPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
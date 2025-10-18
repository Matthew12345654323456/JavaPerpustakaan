import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class test {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Simulasi POS Penjualan");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(750, 450);
            frame.setLocationRelativeTo(null);

            // === Layout Setup ===
            frame.setLayout(new BorderLayout());

            // === Top Panel ===
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(new JLabel("📋 Simulasi Penjualan (Dummy POS Layout)"));
            frame.add(topPanel, BorderLayout.NORTH);

            // === Left Panel: Product Selection ===
            JPanel leftPanel = new JPanel(new GridLayout(0, 1, 5, 5));
            leftPanel.setBorder(BorderFactory.createTitledBorder("Tambah Produk"));

            JComboBox<String> cbProduk = new JComboBox<>(new String[]{"Apple", "Banana", "Orange"});
            JTextField tfJumlah = new JTextField("1");
            JButton btnAdd = new JButton("Tambah ke Keranjang");

            leftPanel.add(new JLabel("Pilih Produk:"));
            leftPanel.add(cbProduk);
            leftPanel.add(new JLabel("Jumlah:"));
            leftPanel.add(tfJumlah);
            leftPanel.add(btnAdd);
            frame.add(leftPanel, BorderLayout.WEST);

            // === Center Panel: Cart Table ===
            DefaultTableModel cartModel = new DefaultTableModel(new Object[]{"Produk", "Jumlah", "Harga", "Total"}, 0);
            JTable tableCart = new JTable(cartModel);
            JScrollPane scrollPane = new JScrollPane(tableCart);
            frame.add(scrollPane, BorderLayout.CENTER);

            // === Bottom Panel: Total & Buttons ===
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JLabel lblTotal = new JLabel("Total: Rp 0");
            JButton btnClear = new JButton("Clear");
            JButton btnBayar = new JButton("Bayar");

            bottomPanel.add(lblTotal);
            bottomPanel.add(btnBayar);
            bottomPanel.add(btnClear);
            frame.add(bottomPanel, BorderLayout.SOUTH);

            // === Action Logic ===
            btnAdd.addActionListener(e -> {
                try {
                    String produk = (String) cbProduk.getSelectedItem();
                    int jumlah = Integer.parseInt(tfJumlah.getText());
                    double harga = getHargaDummy(produk);
                    double total = jumlah * harga;

                    cartModel.addRow(new Object[]{produk, jumlah, harga, total});
                    updateTotal(cartModel, lblTotal);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Jumlah harus angka!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnClear.addActionListener(e -> {
                cartModel.setRowCount(0);
                updateTotal(cartModel, lblTotal);
            });

            btnBayar.addActionListener(e -> {
                if (cartModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(frame, "Keranjang kosong!", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Transaksi selesai! Terima kasih.");
                    cartModel.setRowCount(0);
                    updateTotal(cartModel, lblTotal);
                }
            });

            // === Show Window ===
            frame.setVisible(true);
        });
    }

    // === Dummy harga produk ===
    private static double getHargaDummy(String produk) {
        switch (produk) {
            case "Apple": return 4000;
            case "Banana": return 5000;
            case "Orange": return 6000;
            default: return 0;
        }
    }

    // === Update total label ===
    private static void updateTotal(DefaultTableModel model, JLabel lblTotal) {
        double total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            total += (double) model.getValueAt(i, 3);
        }
        lblTotal.setText("Total: Rp " + total);
    }
}

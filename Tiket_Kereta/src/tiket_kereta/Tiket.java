/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiket_kereta;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class Tiket extends JFrame {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/kereta_api";
    static final String USER = "root";
    static final String PASS = "";

    private String Jk[] = {"laki-laki", "Perempuan"};
    private String Stasiun[] = {"GMR", "LPN", "MN", "SGU"};
    Connection koneksi;
    Statement statement;
    JLabel judul = new JLabel("Tiket Kereta");
    JLabel lNama = new JLabel("Nama :");
    JTextField tfNama = new JTextField();
    JLabel lJk = new JLabel("Jenis Kelamin :");

    JLabel lStasiun = new JLabel("Stasiun Tujuan :");

    JLabel lKereta = new JLabel("Kereta :");
    JComboBox cmbJk = new JComboBox(Jk);
    JComboBox cmbStasiun = new JComboBox(Stasiun);

    JButton btnBatalPanel = new JButton("Batal");
    JButton btnCreatePanel = new JButton("Tambah");
    JButton btnExitPanel = new JButton("Keluar");

    JTable tabel;
    DefaultTableModel tabelModel;
    JScrollPane scrollPane;
    Object namaKolom[] = {"Nama", "Jenis Kelamin", "Stasiun", "Kereta"};

    public Tiket() {
        try {
            Class.forName(JDBC_DRIVER);
            koneksi = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Koneksi Berhasil");
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.out.println("Koneksi Gagal");
        }
        tabelModel = new DefaultTableModel(namaKolom, 0);
        tabel = new JTable(tabelModel);
        scrollPane = new JScrollPane(tabel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        setSize(850, 580);
        setLocation(225, 75);

        add(judul);
        judul.setBounds(350, 10, 200, 20);
        judul.setFont(new Font("", Font.CENTER_BASELINE, 15));
        add(lNama);
        lNama.setBounds(110, 300, 90, 20);
        add(tfNama);
        tfNama.setBounds(220, 300, 120, 20);
        add(lJk);
        lJk.setBounds(110, 330, 90, 20);
        add(cmbJk);
        cmbJk.setBounds(220, 330, 120, 20);
        add(lStasiun);
        lStasiun.setBounds(110, 360, 90, 20);
        add(cmbStasiun);
        cmbStasiun.setBounds(220, 360, 120, 20);
        

        String data[] = new String[dataKereta()];

        data = Kereta();
        JComboBox cmbKereta = new JComboBox(data);

        add(lKereta);
        lKereta.setBounds(110, 390, 90, 20);
        add(cmbKereta);
        cmbKereta.setBounds(220, 390, 120, 20);

        add(btnCreatePanel);
        btnCreatePanel.setBounds(400, 310, 100, 20);
        add(btnBatalPanel);
        btnBatalPanel.setBounds(400, 340, 100, 20);
        
        add(btnExitPanel);
        btnExitPanel.setBounds(400, 370, 100, 20);

        add(scrollPane);
        scrollPane.setBounds(110, 50, 600, 200);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        if (this.getBanyakData() != 0) {
            String dataTiket[][] = this.readTiket();
            tabel.setModel((new JTable(dataTiket, namaKolom)).getModel());
        } else {
            JOptionPane.showMessageDialog(null, "Data Tidak Ada");
        }
        btnCreatePanel.addActionListener((ActionEvent e) -> {
            if (tfNama.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Field tidak boleh kosong");
            } else {
                String Nama = tfNama.getText();//encaptulation
                String Jk = cmbJk.getSelectedItem().toString();
                String Stasiun = cmbStasiun.getSelectedItem().toString();
                String Kereta = cmbKereta.getSelectedItem().toString();

                this.insertTiket(Nama, Jk, Stasiun, Kereta);
                String dataTiket[][] = readTiket();
                tabel.setModel(new JTable(dataTiket, namaKolom).getModel());
            }
        });

        btnBatalPanel.addActionListener((ActionEvent e) -> {
            tfNama.setText("");

        });
        btnExitPanel.addActionListener((ActionEvent e) -> {//registrasi listener untuk exit (event ActionListener untuk button, enter di text,milih menu)
          Menu g = new Menu();//modifier object menu
           dispose();//keluar
        });

    }

    int getBanyakData() {
        int jmlData = 0;

        try {//kalo bener
            statement = koneksi.createStatement();
            String query = "SELECT * from `tiket";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                jmlData++;
            }
            return jmlData;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("SQL error");
            return 0;
        }
    }

    String[][] readTiket() {

        try {
            int jmlData = 0;
            String data[][] = new String[getBanyakData()][8];
            String query = "Select * from `tiket`";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                data[jmlData][0] = resultSet.getString("nama");
                data[jmlData][1] = resultSet.getString("jk");
                data[jmlData][2] = resultSet.getString("stasiun");
                data[jmlData][3] = resultSet.getString("kereta");

                jmlData++;
            }
            return data;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("SQL error");
            return null;
        }
    }

    public void insertTiket(String Nama, String Jk, String Stasiun, String Kereta) {

        try {

            String query = "SET FOREIGN_KEY_CHECKS=0;";
            statement.execute(query);
            query = "INSERT INTO `tiket`(`nama`,`jk`,`stasiun`,`kereta`) VALUES ('" + Nama + "','" + Jk + "','" + Stasiun + "','" + Kereta + "')";
            statement.executeUpdate(query);
            query = "SET FOREIGN_KEY_CHECKS=1;";
            statement.execute(query);

            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (Exception sql) {
            System.out.println(sql.getMessage());
            JOptionPane.showMessageDialog(null, sql.getMessage());
        }
    }

    int dataKereta() {
        int jmlData = 0;

        try {
            statement = koneksi.createStatement();
            String query = "SELECT * from `kereta";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                jmlData++;
            }
            return jmlData;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("SQL error");
            return 0;
        }
    }

    String[] Kereta() {

        try {
            int jmlData = 0;
            String data[] = new String[dataKereta()];
            String query = "Select * from `kereta`";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                data[jmlData] = resultSet.getString("nama_kereta");

                jmlData++;
            }
            return data;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("SQL error");
            return null;
        }
    }

}

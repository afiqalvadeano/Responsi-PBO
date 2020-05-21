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
public class Kereta extends JFrame {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/kereta_api";
    static final String USER = "root";
    static final String PASS = "";
    Connection koneksi;
    Statement statement;
    JLabel judul = new JLabel("Daftar Kereta"); 
    JLabel lID= new JLabel("ID Kereta :");
    JTextField tfID = new JTextField();
    JLabel lNamaK = new JLabel("Nama Kereta :");
    JTextField tfNamaK = new JTextField();
    JLabel lKelas = new JLabel("Kelas :");
    JTextField tfKelas = new JTextField();
    
    JButton btnBatalPanel = new JButton("Batal");
    JButton btnCreatePanel = new JButton("Tambah");
    JButton btnDeletePanel = new JButton("Hapus");
    JButton btnEditPanel = new JButton("Edit");
    JButton btnExitPanel = new JButton("Keluar");
    JTable tabel;
    DefaultTableModel tabelModel;
    JScrollPane scrollPane;
    Object namaKolom[] = {"ID Kereta","Nama Kereta","Kelas Kereta"};
    
    public Kereta(){
        try{
            Class.forName(JDBC_DRIVER);
            koneksi = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("Koneksi Berhasil");
        }catch(ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.out.println("Koneksi Gagal");
        }  
        tabelModel = new DefaultTableModel (namaKolom,0);
        tabel = new JTable(tabelModel);
        scrollPane = new JScrollPane(tabel);
    
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        setSize(850,580);
        setLocation(225,75);
        
        add(judul);
        judul.setBounds(350, 10, 200, 20);
        judul.setFont(new Font("",Font.CENTER_BASELINE, 15));
        add(lID);
        lID.setBounds(60,260,90,20);
        add(tfID);
        tfID.setBounds(170,260,120,20);
        add(lNamaK);
        lNamaK.setBounds(60,290,90,20);
        add(tfNamaK);
        tfNamaK.setBounds(170,290,120,20);
        add(lKelas);
        lKelas.setBounds(60,320,90,20);
        add(tfKelas);
        tfKelas.setBounds(170,320,120,20);
        
        add(btnCreatePanel);
        btnCreatePanel.setBounds(700,70,100,20);
        add(btnEditPanel);
        btnEditPanel.setBounds(700,100,100,20);
        add(btnDeletePanel);
        btnDeletePanel.setBounds(700,130,100,20);
        add(btnBatalPanel);
        btnBatalPanel.setBounds(700,160,100,20);
        add(scrollPane);
        add(btnExitPanel);
        btnExitPanel.setBounds(700, 190, 100, 20);
        scrollPane.setBounds(60,50,600,200);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

if (this.getBanyakData() != 0) {
            String dataKereta[][] = this.readKereta();
            tabel.setModel((new JTable(dataKereta, namaKolom)).getModel());
        } else {
            JOptionPane.showMessageDialog(null, "Data Tidak Ada");
        }
btnCreatePanel.addActionListener((ActionEvent e) -> {
            if (tfID.getText().equals("") ) {
                JOptionPane.showMessageDialog(null, "Field tidak boleh kosong");
            } else {
                String ID = tfID.getText();
                String NamaK = tfNamaK.getText();
                String Kelas = tfKelas.getText();
                this.insertKereta(ID, NamaK, Kelas);
                String dataKereta[][] = readKereta();
                tabel.setModel(new JTable(dataKereta,namaKolom).getModel());
            }
        });

btnEditPanel.addActionListener((ActionEvent e) -> {
            if (tfID.getText().equals("") ) {
                JOptionPane.showMessageDialog(null, "Field tidak boleh kosong");
            } else {
                String ID = tfID.getText();//encaptulation
                String NamaK = tfNamaK.getText();
                String Kelas = tfKelas.getText();
                this.updateKereta(ID, NamaK, Kelas);
                String dataKereta[][] = readKereta();
                tabel.setModel(new JTable(dataKereta,namaKolom).getModel());
            }
        }); 
tabel.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e){ 
               int baris = tabel.getSelectedRow();
               int kolom = tabel.getSelectedColumn(); 
               String dataterpilih = tabel.getValueAt(baris, 0).toString();
               try {
        String query = "SELECT * FROM `kereta` WHERE `id_kereta` = '" + dataterpilih + "'";
        statement = koneksi.createStatement();
        ResultSet resultSet = statement.executeQuery(query); 
        while (resultSet.next()) { 
            tfID.setText(resultSet.getString("id_kereta"));
            tfNamaK.setText(resultSet.getString("nama_kereta"));
            tfKelas.setText(resultSet.getString("kelas_kereta"));
                        
        }
        
    } catch (SQLException sql) {
        System.out.println(sql.getMessage());
    }  
      
btnDeletePanel.addActionListener((ActionEvent f) -> {
                  deleteKereta(dataterpilih);
                  String dataKereta[][] = readKereta();
                tabel.setModel(new JTable(dataKereta,namaKolom).getModel());
                }); 
           }
        });
       
btnBatalPanel.addActionListener((ActionEvent e) -> {
          tfID.setText("");
          tfNamaK.setText("");
          tfKelas.setText("");
        });
btnExitPanel.addActionListener((ActionEvent e) -> {
          Menu g = new Menu();
           dispose();
        });



}
    int getBanyakData() {
        int jmlData = 0;
        
        try{
            statement = koneksi.createStatement();
            String query = "SELECT * from `Kereta";
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                jmlData++;
            }
            return jmlData;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            System.out.println("SQL error");
            return 0;
        }
    }

String[][] readKereta() {
    
        try{
            int jmlData = 0;
            String data[][]=new String[getBanyakData()][8];
            String query = "Select * from `kereta`";
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                data[jmlData][0] = resultSet.getString("id_kereta");
                data[jmlData][1] = resultSet.getString("nama_kereta");
                data[jmlData][2] = resultSet.getString("kelas_kereta");
                
                jmlData++;
            }
            return data;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            System.out.println("SQL error");
            return null;
        }
    }

public void insertKereta(String ID, String NamaK, String Kelas) {
    
        try{
            String query = "INSERT INTO `kereta`(`id_kereta`,`nama_kereta`,`kelas_kereta`) VALUES ('"+ID+"','"+NamaK+"','"+Kelas+"')";        statement = (Statement) koneksi.createStatement();
        statement.executeUpdate(query);
        JOptionPane.showMessageDialog(null,"Data berhasil ditambahkan");
        }catch(Exception sql){
            System.out.println(sql.getMessage());
            JOptionPane.showMessageDialog(null, sql.getMessage());
        }   
    }
public void updateKereta(String ID, String NamaK, String Kelas) {
    
        try{
            String query = "UPDATE kereta SET nama_kereta = '"+NamaK+"', kelas_kereta = '"+Kelas+"' where `id_kereta` = '"+ID+"'";        statement = (Statement) koneksi.createStatement();
        statement.executeUpdate(query);
        JOptionPane.showMessageDialog(null,"Data berhasil ditambahkan");
        }catch(Exception sql){
            System.out.println(sql.getMessage());
            JOptionPane.showMessageDialog(null, sql.getMessage());
        }   
    }

void deleteKereta(String ID) {
    
        try{
            String query = "DELETE FROM `kereta` WHERE `id_kereta` = '"+ID+"'";
            statement = koneksi.createStatement();
            statement.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "berhasil dihapus" );
        }catch(SQLException sql){
            System.out.println(sql.getMessage());
        }
    }
}


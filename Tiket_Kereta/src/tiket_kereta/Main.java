/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiket_kereta;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Menu g = new Menu();
    }
    
}
class Menu extends JFrame {
    JLabel menu = new JLabel("KERETA API");
    JButton tombolTiket = new JButton("DAFTAR TIKET");
    JButton tombolKereta = new JButton("DAFTAR KERETA");
    
    public Menu() {//method
    
        setTitle("KERETA API");
        setDefaultCloseOperation(3);
        setSize(350,250);
        setLocation(500,275);
        setLayout(null);
        
        add(menu);
        
        add(tombolTiket);
        add(tombolKereta);
        
        menu.setBounds(130,10,120,20);
        menu.setFont(new Font("",Font.CENTER_BASELINE, 15));
        
        tombolTiket.setBounds(100,70,150,40);
        tombolKereta.setBounds(100,120,150,40);
        
        tombolTiket.addActionListener((ActionEvent e) -> {
            Tiket b = new Tiket();
            dispose();
        });
        tombolKereta.addActionListener((ActionEvent e) -> {
            Kereta c = new Kereta();
            dispose();
        });
        
        setVisible(true);
    }
    
}
package gui;

import bussinessLayer.TextFilesConfigurator;

import javax.swing.*;
import java.util.*;


public class HomePage  extends javax.swing.JFrame {
    private final TextFilesConfigurator textFilesConfigurator = new TextFilesConfigurator();

    public HomePage() {
        initComponents();
        TextFilesConfigurator.configureTextFiles();
    }

    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabelBackround = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(600, 430));
        getContentPane().setLayout(null);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jScrollPane1.setForeground(new java.awt.Color(255, 204, 204));
        jScrollPane1.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jTextArea1.setBackground(new java.awt.Color(232, 237, 255));
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(310, 40, 260, 330);

        jLabel1.setFont(new java.awt.Font("Bookman Old Style", 2, 30)); // NOI18N
        jLabel1.setText("Search");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(30, 40, 110, 40);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField1);
        jTextField1.setBounds(30, 110, 220, 30);

        jScrollPane2.setViewportView(jList1);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(30, 170, 220, 120);

        jLabelBackround.setIcon(new javax.swing.ImageIcon(getClass().getResource("/searchBackroundImage.jpg")));
        getContentPane().add(jLabelBackround);
        jLabelBackround.setBounds(0, 0, 600, 400);

        pack();
    }// </editor-fold>

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        performSearch();
        updateJListFromCache();
    }

    private void performSearch() {
        String query = jTextField1.getText().trim();
        if (query.isEmpty()) {
            jTextArea1.setText("");
            return;
        }


        textFilesConfigurator.setQuery(query);

        String results = textFilesConfigurator.getQuery();
        jTextArea1.setText(results);
    }

    public void updateJListFromCache() {
        Map<String, String> searchCache = TextFilesConfigurator.getSearchCache();
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String query : searchCache.keySet()) {
            listModel.addElement(query);
        }

        jList1.setModel(listModel);
    }

    // Variables declaration
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelBackround;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration
}

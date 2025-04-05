package z_assignment_2;

import java.io.IOException;

public class HomePage2 extends javax.swing.JFrame {
    private final Manager manager = new Manager();

    public HomePage2() {
        initComponents();
        startWorkers();
    }

    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabelBackround = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(600, 430));
        getContentPane().setLayout(null);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jScrollPane1.setForeground(new java.awt.Color(255, 204, 204));
        jScrollPane1.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane1.setAutoscrolls(false);
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

        jLabelBackround.setIcon(new javax.swing.ImageIcon(getClass().getResource("/searchBackroundImage.jpg"))); // NOI18N
        getContentPane().add(jLabelBackround);
        jLabelBackround.setBounds(0, 0, 600, 400);

        pack();
    }// </editor-fold>

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        performSearch();
    }

    private void performSearch() {
        String query = jTextField1.getText().trim();
        if (query.isEmpty()) {
            jTextArea1.setText("");
            return;
        }

        manager.setQuery(query);
        String results = manager.getQuery();
        jTextArea1.setText(results);
    }

    public void startWorkers() {
        new Thread(() -> {
            try {
                //new Worker("C:\\Users\\claud\\OneDrive\\Documente\\Facultate\\An 3 Sem2\\SD\\Data\\folder1", 8001).startServer();
                new Worker("C:\\Users\\claud\\OneDrive\\Documente\\Facultate\\An 2", 8001).startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                new Worker("C:\\Users\\claud\\OneDrive\\Documente\\Facultate\\An 3 Sem1", 8002).startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                new Worker("C:\\Users\\claud\\OneDrive\\Documente\\Facultate\\An 3 Sem2", 8003).startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    // Variables declaration
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelBackround;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration
}

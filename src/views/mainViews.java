/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;
import pbo.apotik.koneksi;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import Form.Login;
/**
 *
 * @author Bagus Sr
 */
public class mainViews extends javax.swing.JFrame {

    ResultSet result = null;
    PreparedStatement pst = null;
    
//    Connection conn = null;

    /**
     *
     */
    /**
     * Creates new form mainViews
     */
    public mainViews() {
        initComponents();
        harga.setVisible(false);
        stok_.setVisible(false);
        labelStok.setVisible(false);
        ListSelectionModel cell = tableEdit.getSelectionModel();
        cell.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        validasiTambah.setVisible(false);
        updateCombo();
        getTableObat();
        getTableOrder();
        
        cell.addListSelectionListener((ListSelectionEvent e) -> {
            String selectedData = null;
            
            int[] selectedRow = tableEdit.getSelectedRows();
            int[] selectedColumns = tableEdit.getSelectedColumns();
            
            for (int i = 0; i < selectedRow.length; i++) {
                for (int j = 0; j < selectedColumns.length; j++) {
                    selectedData = (String) tableEdit.getValueAt(selectedRow[i], selectedColumns[j]);
                }
            }
            getObatId(selectedData);
        });
        
        DocumentListener dl;
        dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTotal();
            }
            protected void updateTotal(){
                if(!qtyObat.getText().equals("")){
                    int total = (Integer.parseInt(qtyObat.getText())*(Integer.parseInt(harga.getText())));
                    total_.setText(Integer.toString(total));
                }
            }
        };
        qtyObat.getDocument().addDocumentListener(dl);
    }

// Dropwdon Database 
    
    private void getObatId(String x){
        String sql = String.format("SELECT * FROM obat WHERE id = '%s'", x);
        try {
            Connection koneksi = conn.getConn();
            Statement query = koneksi.createStatement();
            result = query.executeQuery(sql);
            while(result.next()){
                idObatEdit.setText(result.getString("id"));
                namaObatEdit.setText(result.getString("nama"));
                merkObatEdit.setText(result.getString("merk"));
                hargaObatEdit.setText(result.getString("harga"));
                stokObatEdit.setText(result.getString("stok"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(mainViews.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void getTableObat(){
            String sql = String.format("SELECT * FROM obat");
            int counter= 1;
        try {
            DefaultTableModel table = new DefaultTableModel();
            table.addColumn("No");
            table.addColumn("Id");
            table.addColumn("Nama obat");
            table.addColumn("Merk");
            table.addColumn("Harga");
            table.addColumn("Stok");
            Connection koneksi = conn.getConn();
            Statement query = koneksi.createStatement();
            result = query.executeQuery(sql);
            while(result.next()){
                table.addRow(new Object[]{
                counter++, result.getString("id"),result.getString("nama"),result.getString("merk"), result.getString("harga"), result.getString("stok")           
                });
                }
            tableEdit.setModel(table);
            tableBarang.setModel(table);
        } catch (SQLException ex) {
            Logger.getLogger(mainViews.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getTableOrder(){
        String sql = String.format("SELECT * FROM orders");
            int counter= 1;
        try {
            DefaultTableModel table = new DefaultTableModel();
            table.addColumn("No");
            table.addColumn("Id");
            table.addColumn("tanggal");
            table.addColumn("IdObat");
            table.addColumn("Qty");
            table.addColumn("Total");
            Connection koneksi = conn.getConn();
            Statement query = koneksi.createStatement();
            result = query.executeQuery(sql);
            while(result.next()){
                table.addRow(new Object[]{
                counter++, result.getString("id"),result.getString("tanggal"),result.getString("id_obat"), result.getString("qty"), result.getString("total")           
                });
                }
            tableOrder.setModel(table);
        } catch (SQLException ex) {
            Logger.getLogger(mainViews.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateCombo(){
        String sql = String.format("SELECT * FROM obat");
        combo.removeAllItems();
        combo.addItem("Buka untuk Memilih");
        try {
            Connection koneksi = conn.getConn();
            Statement query = koneksi.createStatement();
            result = query.executeQuery(sql);
            while(result.next()){
                combo.addItem(result.getString("nama"));
        }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void emptyTambah(){
        idObat.setText("");
        namaObat.setText("");
        merkObat.setText("");
        hargaObat.setText("");
    }
    
    private void emptyOrder(){
        id_obat.setText("");
        nama_obat.setText("");
        merk_obat.setText("");
        qtyObat.setText("");
        labelStok.setVisible(false);
    }
    
   private void updateStok(String a, int x) throws SQLException{
       String sql = String.format("UPDATE obat SET stok = '%s' WHERE id = '%s'", x, a);
       Connection koneksi = conn.getConn();
       Statement query = koneksi.createStatement();
       query.executeUpdate(sql);
  
   }
   
   private boolean checkStok(String a, int x) throws SQLException{
       String sql = String.format("SELECT stok FROM obat WHERE id = '%s'", a);
       Connection koneksi = conn.getConn();
       Statement query = koneksi.createStatement();
       result = query.executeQuery(sql);
       while(result.next()){
           if(result.getString("stok").equals("0")){
                return false;
           } else {
                if((Integer.parseInt(result.getString("stok"))) - x > 0 ){
                    return true;
                }
                else{
                    return false;
                }
            }
       }
       return false;
   }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableBarang = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableOrder = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        idObat = new javax.swing.JTextField();
        namaObat = new javax.swing.JTextField();
        merkObat = new javax.swing.JTextField();
        hargaObat = new javax.swing.JTextField();
        tambahObat = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        validasiTambah = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableEdit = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        idObatEdit = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        namaObatEdit = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        merkObatEdit = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        hargaObatEdit = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        stokObatEdit = new javax.swing.JTextField();
        update = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        nama_obat = new javax.swing.JTextField();
        id_obat = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        merk_obat = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        qtyObat = new javax.swing.JTextField();
        tambahOrder = new javax.swing.JButton();
        combo = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        total_ = new javax.swing.JLabel();
        harga = new javax.swing.JTextField();
        stok_ = new javax.swing.JTextField();
        labelStok = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Apotik Sejahtera");
        setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTabbedPane1FocusGained(evt);
            }
        });
        jTabbedPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabbedPane1KeyPressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 200, 243));

        tableBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Id", "Nama Obat", "Merk", "Harga", "Stok"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableBarang);

        tableOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Id", "Tanggal", "IdObat", "Qty", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableOrder);

        jLabel1.setText("Tabel Barang");

        jLabel2.setText("Tabel Penjualan");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(188, 188, 188)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(146, 146, 146))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(39, 39, 39))
        );

        jTabbedPane1.addTab("Data", jPanel1);

        jPanel2.setBackground(new java.awt.Color(0, 200, 243));

        idObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idObatActionPerformed(evt);
            }
        });
        idObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                idObatKeyTyped(evt);
            }
        });

        namaObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaObatActionPerformed(evt);
            }
        });

        tambahObat.setBackground(new java.awt.Color(102, 255, 255));
        tambahObat.setText("Tambah");
        tambahObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahObatActionPerformed(evt);
            }
        });

        jLabel3.setText("Id Obat");

        jLabel4.setText("Nama Obat");

        jLabel5.setText("Merk Obat");

        jLabel6.setText("Harga Obat");

        validasiTambah.setBackground(new java.awt.Color(255, 204, 204));
        validasiTambah.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        validasiTambah.setForeground(new java.awt.Color(0, 204, 0));
        validasiTambah.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        validasiTambah.setText("Data Berhasil Ditambah");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hargaObat, javax.swing.GroupLayout.PREFERRED_SIZE, 626, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(merkObat)
                            .addComponent(namaObat, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tambahObat, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idObat, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(306, 306, 306)
                        .addComponent(validasiTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(102, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {hargaObat, idObat, merkObat, namaObat});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(validasiTambah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(idObat, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(namaObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(merkObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hargaObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tambahObat, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(164, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {hargaObat, idObat, merkObat, namaObat});

        jTabbedPane1.addTab("Tambah Obat", jPanel2);

        jPanel3.setBackground(new java.awt.Color(0, 200, 243));

        tableEdit.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Id ", "Nama Obat", "Merk", "Harga", "Stok"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tableEdit);

        jLabel7.setText("Data Obat");

        jLabel8.setText("Id");

        idObatEdit.setEditable(false);
        idObatEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idObatEditActionPerformed(evt);
            }
        });

        jLabel9.setText("Nama Obat");

        namaObatEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaObatEditActionPerformed(evt);
            }
        });

        jLabel10.setText("Merk Obat");

        merkObatEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                merkObatEditActionPerformed(evt);
            }
        });

        jLabel11.setText("Harga Obat");

        jLabel12.setText("Stok");

        stokObatEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stokObatEditActionPerformed(evt);
            }
        });

        update.setBackground(new java.awt.Color(153, 255, 153));
        update.setText("Update");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });

        delete.setBackground(new java.awt.Color(255, 102, 102));
        delete.setText("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idObatEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(namaObatEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(merkObatEdit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                            .addComponent(hargaObatEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stokObatEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(update)
                                .addGap(18, 18, 18)
                                .addComponent(delete))))))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {hargaObatEdit, idObatEdit, merkObatEdit, namaObatEdit, stokObatEdit});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idObatEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(namaObatEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(merkObatEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hargaObatEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stokObatEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(update)
                            .addComponent(delete))))
                .addGap(28, 28, 28))
        );

        jTabbedPane1.addTab("Edit dan Delete", jPanel3);

        jPanel4.setBackground(new java.awt.Color(0, 200, 243));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel13.setText("Order");

        jLabel14.setText("Id Barang");

        jLabel15.setText("Nama Obat");

        nama_obat.setEditable(false);

        id_obat.setEditable(false);

        jLabel16.setText("Merk");

        merk_obat.setEditable(false);

        jLabel17.setText("Qty");

        qtyObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qtyObatActionPerformed(evt);
            }
        });

        tambahOrder.setText("Order");
        tambahOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahOrderActionPerformed(evt);
            }
        });

        combo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboItemStateChanged(evt);
            }
        });
        combo.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                comboPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboActionPerformed(evt);
            }
        });

        jLabel18.setText("Total :");

        total_.setText("0");

        harga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hargaActionPerformed(evt);
            }
        });

        stok_.setText("0");

        labelStok.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelStok.setForeground(new java.awt.Color(255, 51, 51));
        labelStok.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelStok.setText("Stok Habis");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(411, 411, 411)
                        .addComponent(jLabel13)
                        .addGap(0, 78, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(219, 219, 219)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(qtyObat, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(merk_obat, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nama_obat)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(id_obat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(tambahOrder)
                                .addGap(182, 182, 182)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(total_, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(harga, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stok_, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(169, 169, 169))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(287, 287, 287)
                .addComponent(labelStok, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel13)
                .addGap(14, 14, 14)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(id_obat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nama_obat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(merk_obat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(stok_, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(qtyObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tambahOrder)
                    .addComponent(jLabel18)
                    .addComponent(total_))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelStok, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(128, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Penjualan", jPanel4);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabbedPane1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1KeyPressed

    private void jTabbedPane1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabbedPane1FocusGained
        // TODO add your handling code here:
        this.setVisible(false);
        new Login().setVisible(true);
    }//GEN-LAST:event_jTabbedPane1FocusGained

    private void hargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hargaActionPerformed

    private void comboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboActionPerformed

//Get Data Dari Dropdown (Belum Berhasil)

    private void comboPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_comboPopupMenuWillBecomeInvisible

    }//GEN-LAST:event_comboPopupMenuWillBecomeInvisible

    private void comboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboItemStateChanged
        try {
            // TODO add your handling code here:
            if(combo.getItemCount() != 0){
                Connection koneksi = conn.getConn();
                Statement query = koneksi.createStatement();
                String item = combo.getSelectedItem().toString();
                String sql = String.format("SELECT * FROM obat where nama = '%s'", item);
                result = query.executeQuery(sql);
                while(result.next()){
                    id_obat.setText(result.getString("id"));
                    nama_obat.setText(result.getString("nama"));
                    merk_obat.setText(result.getString("merk"));
                    harga.setText(result.getString("harga"));
                    stok_.setText(result.getString("stok"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(mainViews.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_comboItemStateChanged

    private void tambahOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahOrderActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            Connection koneksi = conn.getConn();
            String  id, qty, total, stok;
            int a;
            Statement query = koneksi.createStatement();
            id = id_obat.getText();
            qty = qtyObat.getText();
            total = total_.getText();
            stok = stok_.getText();
            a = (Integer.parseInt(stok)) - (Integer.parseInt(qty));
            if(checkStok(id, Integer.parseInt(qty)) ){
                updateStok(id, a);
                String sql = String.format("INSERT INTO orders (tanggal, id_obat, qty, total) values (now(),'%s', '%s', '%s')", id, qty, total);
                query.execute(sql);
                getTableOrder();
                getTableObat();
                combo.removeAllItems();
                combo.addItem("Buka untuk Memilih");
                updateCombo();
                emptyOrder();
            }
            else{
                labelStok.setVisible(true);
            }

        } catch (SQLException ex) {
            Logger.getLogger(mainViews.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tambahOrderActionPerformed

    private void qtyObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qtyObatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_qtyObatActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        // TODO add your handling code here:
        String id;
        id = idObatEdit.getText();
        String sql = String.format("DELETE FROM obat WHERE id = '%s'", id);
        try {
            // TODO add your handling code here:
            Connection koneksi = conn.getConn();
            Statement query = koneksi.createStatement();
            query.executeUpdate(sql);
            getTableObat();
            updateCombo();
        } catch (SQLException ex) {
            Logger.getLogger(mainViews.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_deleteActionPerformed

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        String id, nama, merk, harga, stok;
        id = idObatEdit.getText();
        nama = namaObatEdit.getText();
        merk = merkObatEdit.getText();
        harga = hargaObatEdit.getText();
        stok = stokObatEdit.getText();
        Integer.parseInt(stok);
        Integer.parseInt(harga);
        String sql = String.format("UPDATE obat SET nama = '%s', merk = '%s', harga = '%s', stok = '%s' WHERE id = '%s'",nama, merk, harga, stok, id);
        try {
            // TODO add your handling code here:
            Connection koneksi = conn.getConn();
            Statement query = koneksi.createStatement();
            query.executeUpdate(sql);
            getTableObat();
            combo.removeAllItems();
            combo.addItem("Buka untuk Memilih");
            combo.removeAllItems();
            updateCombo();
        } catch (SQLException ex) {
            Logger.getLogger(mainViews.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_updateActionPerformed

    private void stokObatEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stokObatEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stokObatEditActionPerformed

    private void merkObatEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_merkObatEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_merkObatEditActionPerformed

    private void namaObatEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaObatEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaObatEditActionPerformed

    private void idObatEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idObatEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idObatEditActionPerformed

    private void tambahObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahObatActionPerformed
        try {
            // TODO add your handling code here:
            Connection koneksi = conn.getConn();
            String nama, merk, id, harga;
            Statement query = koneksi.createStatement();
            id = idObat.getText();
            nama = namaObat.getText();
            merk = merkObat.getText();
            harga = hargaObat.getText();
            Integer.parseInt(harga);
            String sql = String.format("INSERT INTO obat values ('%s', '%s', '%s', '%s', 0)", id, nama, merk, harga);
            query.executeUpdate(sql);
            validasiTambah.setVisible(true);
            getTableObat();
            updateCombo();
            emptyTambah();
        } catch (SQLException ex) {
            Logger.getLogger(mainViews.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tambahObatActionPerformed

    private void namaObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaObatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaObatActionPerformed

    private void idObatKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idObatKeyTyped
        // TODO add your handling code here:
        validasiTambah.setVisible(false);
    }//GEN-LAST:event_idObatKeyTyped

    private void idObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idObatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idObatActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainViews.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainViews.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainViews.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainViews.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainViews().setVisible(true);                
            }
        });
    }
    koneksi conn = new koneksi();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> combo;
    private javax.swing.JButton delete;
    private javax.swing.JTextField harga;
    private javax.swing.JTextField hargaObat;
    private javax.swing.JTextField hargaObatEdit;
    private javax.swing.JTextField idObat;
    private javax.swing.JTextField idObatEdit;
    private javax.swing.JTextField id_obat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelStok;
    private javax.swing.JTextField merkObat;
    private javax.swing.JTextField merkObatEdit;
    private javax.swing.JTextField merk_obat;
    private javax.swing.JTextField namaObat;
    private javax.swing.JTextField namaObatEdit;
    private javax.swing.JTextField nama_obat;
    private javax.swing.JTextField qtyObat;
    private javax.swing.JTextField stokObatEdit;
    private javax.swing.JTextField stok_;
    private javax.swing.JTable tableBarang;
    private javax.swing.JTable tableEdit;
    private javax.swing.JTable tableOrder;
    private javax.swing.JButton tambahObat;
    private javax.swing.JButton tambahOrder;
    private javax.swing.JLabel total_;
    private javax.swing.JButton update;
    private javax.swing.JLabel validasiTambah;
    // End of variables declaration//GEN-END:variables
}

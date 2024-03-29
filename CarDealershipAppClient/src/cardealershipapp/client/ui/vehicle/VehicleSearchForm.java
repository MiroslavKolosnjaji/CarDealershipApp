package cardealershipapp.client.ui.vehicle;

import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.client.ui.vehicle.controller.VehicleSearchController;

import javax.swing.*;

/**
 * @author Miroslav Kološnjaji
 */
public class VehicleSearchForm extends javax.swing.JDialog {

    private StringBuilder stringBuilder;
    private VehicleSearchController vehicleSearchController;
    private boolean filtered;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddToPurchaseOrder;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox comboBodyType;
    private javax.swing.JComboBox comboBrand;
    private javax.swing.JComboBox comboCity;
    private javax.swing.JComboBox comboFuelType;
    private javax.swing.JComboBox comboModel;
    private javax.swing.JComboBox<String> comboSortList;
    private javax.swing.JComboBox comboYearFrom;
    private javax.swing.JComboBox comboYearTo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnlFilter;
    private javax.swing.JPanel pnlInfo;
    private javax.swing.JPanel pnlOptions;
    private javax.swing.JPanel pnlVehicleSearch;
    private javax.swing.JTable tblVehicles;
    private javax.swing.JTextArea txtAreaBusinessUnitInfo;

    public VehicleSearchForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        stringBuilder = new StringBuilder();
        initComponents();
        initController();
        loadPurchaseOrders();
        fillTable();
        populateCombo();
    }

    private void initController() {
        this.vehicleSearchController = new VehicleSearchController(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlVehicleSearch = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVehicles = new javax.swing.JTable();
        pnlFilter = new javax.swing.JPanel();
        comboBrand = new javax.swing.JComboBox();
        comboModel = new javax.swing.JComboBox();
        comboBodyType = new javax.swing.JComboBox();
        comboFuelType = new javax.swing.JComboBox();
        comboYearFrom = new javax.swing.JComboBox();
        comboYearTo = new javax.swing.JComboBox();
        comboCity = new javax.swing.JComboBox();
        btnSearch = new javax.swing.JButton();
        pnlInfo = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaBusinessUnitInfo = new javax.swing.JTextArea();
        pnlOptions = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnAddToPurchaseOrder = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        comboSortList = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pregled svih vozila");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        pnlVehicleSearch.setBorder(javax.swing.BorderFactory.createTitledBorder("Pregled vozila"));

        tblVehicles.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10"
                }
        ));
        tblVehicles.setRowHeight(50);
        tblVehicles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVehiclesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblVehicles);

        pnlFilter.setBorder(javax.swing.BorderFactory.createTitledBorder("Fiter pretrage"));

        comboBrand.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Marka"}));
        comboBrand.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBrandItemStateChanged(evt);
            }
        });

        comboModel.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Model"}));

        comboBodyType.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Karoserija"}));

        comboFuelType.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Gorivo"}));

        comboYearFrom.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Godiste od"}));

        comboYearTo.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Godiste do"}));

        comboCity.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Grad"}));

        btnSearch.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSearch.setText("PRETRAGA\n");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFilterLayout = new javax.swing.GroupLayout(pnlFilter);
        pnlFilter.setLayout(pnlFilterLayout);
        pnlFilterLayout.setHorizontalGroup(
                pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFilterLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(comboBrand, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(pnlFilterLayout.createSequentialGroup()
                                                .addComponent(comboYearFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                                .addComponent(comboYearTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlFilterLayout.createSequentialGroup()
                                                .addComponent(comboModel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                                                        .addComponent(comboBodyType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(pnlFilterLayout.createSequentialGroup()
                                                .addComponent(comboFuelType, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(comboCity, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(20, 20, 20))
        );
        pnlFilterLayout.setVerticalGroup(
                pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlFilterLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(comboBodyType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(comboYearFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboYearTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboFuelType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(btnSearch)
                                .addContainerGap(28, Short.MAX_VALUE))
        );

        pnlInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Informacije o salonu"));

        txtAreaBusinessUnitInfo.setEditable(false);
        txtAreaBusinessUnitInfo.setColumns(20);
        txtAreaBusinessUnitInfo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtAreaBusinessUnitInfo.setRows(5);
        jScrollPane2.setViewportView(txtAreaBusinessUnitInfo);

        javax.swing.GroupLayout pnlInfoLayout = new javax.swing.GroupLayout(pnlInfo);
        pnlInfo.setLayout(pnlInfoLayout);
        pnlInfoLayout.setHorizontalGroup(
                pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInfoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                                .addContainerGap())
        );
        pnlInfoLayout.setVerticalGroup(
                pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlInfoLayout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pnlOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("Opcije"));

        btnDelete.setText("Obrisi");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnAdd.setText("Dodaj");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setText(" Izmeni");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnAddToPurchaseOrder.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAddToPurchaseOrder.setText("DODAJ U PORUDZBINU");
        btnAddToPurchaseOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToPurchaseOrderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlOptionsLayout = new javax.swing.GroupLayout(pnlOptions);
        pnlOptions.setLayout(pnlOptionsLayout);
        pnlOptionsLayout.setHorizontalGroup(
                pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlOptionsLayout.createSequentialGroup()
                                .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(pnlOptionsLayout.createSequentialGroup()
                                                .addGap(238, 238, 238)
                                                .addComponent(btnAddToPurchaseOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pnlOptionsLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))
        );
        pnlOptionsLayout.setVerticalGroup(
                pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlOptionsLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnEdit)
                                        .addComponent(btnAdd)
                                        .addComponent(btnDelete))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                                .addComponent(btnAddToPurchaseOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Sortiraj po"));

        comboSortList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Osnovno", "ceni opadajuce", "ceni rastuce", "godini proizvodnje opadajuce", "godini proizvodnje rastuce"}));
        comboSortList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboSortListItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(19, Short.MAX_VALUE)
                                .addComponent(comboSortList, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(comboSortList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlVehicleSearchLayout = new javax.swing.GroupLayout(pnlVehicleSearch);
        pnlVehicleSearch.setLayout(pnlVehicleSearchLayout);
        pnlVehicleSearchLayout.setHorizontalGroup(
                pnlVehicleSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlVehicleSearchLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlVehicleSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(pnlVehicleSearchLayout.createSequentialGroup()
                                                .addGroup(pnlVehicleSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(pnlOptions, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(pnlFilter, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(pnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVehicleSearchLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        pnlVehicleSearchLayout.setVerticalGroup(
                pnlVehicleSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlVehicleSearchLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(pnlVehicleSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlVehicleSearchLayout.createSequentialGroup()
                                                .addComponent(pnlFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(pnlOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(pnlInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pnlVehicleSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pnlVehicleSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 895, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboBrandItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBrandItemStateChanged
        vehicleSearchController.updateComboModel();
    }//GEN-LAST:event_comboBrandItemStateChanged

    private void tblVehiclesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVehiclesMouseClicked
        infoArea();

    }//GEN-LAST:event_tblVehiclesMouseClicked

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        vehicleSearchController.search();
        filtered = true;
//        comboSortList.setSelectedIndex(0);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        vehicleSearchController.add(this);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        vehicleSearchController.edit();
    }//GEN-LAST:event_btnEditActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        loadPurchaseOrders();
        vehicleSearchController.filterTable();
        vehicleSearchController.search();
        infoArea();
        optionArea();
    }//GEN-LAST:event_formWindowActivated

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        vehicleSearchController.delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void comboSortListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboSortListItemStateChanged
        vehicleSearchController.sortMainList();
    }//GEN-LAST:event_comboSortListItemStateChanged

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void btnAddToPurchaseOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToPurchaseOrderActionPerformed
        vehicleSearchController.addToPurchaseOrder();
    }//GEN-LAST:event_btnAddToPurchaseOrderActionPerformed
    // End of variables declaration//GEN-END:variables

    private void infoArea() {
        vehicleSearchController.infoAdrea();
    }

    private void optionArea() {
        if (ApplicationSession.getInstance().getLoggedUser() != null) {
            pnlOptions.setVisible(true);
        } else {
            pnlOptions.setVisible(false);
        }
    }

    private void fillTable() {
        vehicleSearchController.fillTable();
    }

    private void populateCombo() {
        vehicleSearchController.fillCombo();
    }

    private void loadPurchaseOrders() {
        vehicleSearchController.loadPurchaseOrders();
    }

    public JTable getTblVehicles() {
        return tblVehicles;
    }

    public JComboBox getComboBrand() {
        return comboBrand;
    }

    public JComboBox getComboModel() {
        return comboModel;
    }

    public JComboBox getComboBodyType() {
        return comboBodyType;
    }

    public JComboBox getComboYearFrom() {
        return comboYearFrom;
    }

    public JComboBox getComboYearTo() {
        return comboYearTo;
    }

    public JComboBox getComboFuelType() {
        return comboFuelType;
    }

    public JComboBox getComboCity() {
        return comboCity;
    }

    public JComboBox getComboSortList() {
        return comboSortList;
    }

    public JTextArea getTxtAreaBusinessUnitInfo() {
        return txtAreaBusinessUnitInfo;
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean value) {
        this.filtered = value;
    }


}

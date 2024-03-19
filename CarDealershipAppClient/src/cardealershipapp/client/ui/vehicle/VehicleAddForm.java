package cardealershipapp.client.ui.vehicle;

import cardealershipapp.client.ui.vehicle.controller.VehicleAddController;

import javax.swing.*;


/**
 * @author Miroslav Kološnjaji
 */
public class VehicleAddForm extends javax.swing.JDialog {

    /**
     * Creates new form VehicleAddForm
     */

    private VehicleAddController vehicleAddController;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<Object> comboBodyType;
    private javax.swing.JComboBox<Object> comboBrand;
    private javax.swing.JComboBox<Object> comboBusinessUnit;
    private javax.swing.JComboBox<Object> comboCurrency;
    private javax.swing.JComboBox<Object> comboFuelType;
    private javax.swing.JComboBox<Object> comboModel;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblEngineCapacity;
    private javax.swing.JPanel pnlAdditionalInfo;
    private javax.swing.JTextField txtEngineDisplacement;
    private javax.swing.JTextField txtEnginePower;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtViN;
    private javax.swing.JTextField txtYear;
    public VehicleAddForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initController();
        populateCombo();
    }

    private void initController() {
        this.vehicleAddController = new VehicleAddController(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pnlAdditionalInfo = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        comboBodyType = new javax.swing.JComboBox<>();
        lblEngineCapacity = new javax.swing.JLabel();
        txtEngineDisplacement = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtEnginePower = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtViN = new javax.swing.JTextField();
        comboFuelType = new javax.swing.JComboBox<>();
        comboBusinessUnit = new javax.swing.JComboBox<>();
        txtPrice = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        comboCurrency = new javax.swing.JComboBox<>();
        txtYear = new javax.swing.JTextField();
        comboModel = new javax.swing.JComboBox<>();
        comboBrand = new javax.swing.JComboBox<>();
        btnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Dodaj vozilo"));

        pnlAdditionalInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Podaci"));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Godina proizvodnje:");

        comboBodyType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Karoserija"}));

        lblEngineCapacity.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEngineCapacity.setText("Zapremina motora:");
        lblEngineCapacity.setToolTipText("");

        txtEngineDisplacement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEngineDisplacementActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Snaga motora (kW):");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Broj sasije (VIN):");

        comboFuelType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Vrsta goriva"}));
        comboFuelType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboFuelTypeItemStateChanged(evt);
            }
        });

        comboBusinessUnit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Lokacija"}));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Cena:");

        comboCurrency.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Valuta"}));

        comboModel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Model"}));

        comboBrand.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Marka"}));
        comboBrand.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBrandItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout pnlAdditionalInfoLayout = new javax.swing.GroupLayout(pnlAdditionalInfo);
        pnlAdditionalInfo.setLayout(pnlAdditionalInfoLayout);
        pnlAdditionalInfoLayout.setHorizontalGroup(
                pnlAdditionalInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlAdditionalInfoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlAdditionalInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlAdditionalInfoLayout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addGap(38, 38, 38)
                                                .addComponent(txtViN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comboFuelType, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pnlAdditionalInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(pnlAdditionalInfoLayout.createSequentialGroup()
                                                        .addGroup(pnlAdditionalInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel6)
                                                                .addComponent(lblEngineCapacity))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(pnlAdditionalInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                .addComponent(txtEngineDisplacement, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                                                .addComponent(txtEnginePower))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(pnlAdditionalInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(pnlAdditionalInfoLayout.createSequentialGroup()
                                                                        .addComponent(jLabel7)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(txtYear))
                                                                .addComponent(comboBusinessUnit, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAdditionalInfoLayout.createSequentialGroup()
                                                        .addComponent(jLabel9)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(comboCurrency, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(172, 172, 172))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAdditionalInfoLayout.createSequentialGroup()
                                                        .addComponent(comboBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(40, 40, 40)
                                                        .addComponent(comboModel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                                                        .addComponent(comboBodyType, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(11, Short.MAX_VALUE))
        );
        pnlAdditionalInfoLayout.setVerticalGroup(
                pnlAdditionalInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlAdditionalInfoLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(pnlAdditionalInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(comboBodyType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlAdditionalInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtViN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8)
                                        .addComponent(comboFuelType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlAdditionalInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblEngineCapacity)
                                        .addComponent(txtEngineDisplacement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7)
                                        .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlAdditionalInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(txtEnginePower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBusinessUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlAdditionalInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9)
                                        .addComponent(comboCurrency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(67, Short.MAX_VALUE))
        );

        btnSave.setText("Sacuvaj");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pnlAdditionalInfo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSave, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pnlAdditionalInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSave)
                                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // End of variables declaration//GEN-END:variables
    private void txtEngineDisplacementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEngineDisplacementActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEngineDisplacementActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        vehicleAddController.save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void comboBrandItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBrandItemStateChanged
        vehicleAddController.updateComboModel();
    }//GEN-LAST:event_comboBrandItemStateChanged

    private void comboFuelTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboFuelTypeItemStateChanged
        vehicleAddController.comboFuelTypeState();
    }//GEN-LAST:event_comboFuelTypeItemStateChanged

    private void populateCombo() {
        vehicleAddController.populateCombo();
    }

    public JTextField getTxtVin() {
        return txtViN;
    }

    public JTextField getTxtEngineDisplacement() {
        return txtEngineDisplacement;
    }

    public JTextField getTxtEnginePower() {
        return txtEnginePower;
    }

    public JTextField getTxtYear() {
        return txtYear;
    }

    public JTextField getTxtPrice() {
        return txtPrice;
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

    public JComboBox getComboCurrency() {
        return comboCurrency;
    }

    public JComboBox getComboBusinessUnit() {
        return comboBusinessUnit;
    }

    public JComboBox getComboFuelType() {
        return comboFuelType;
    }

    public JLabel getLblEngineCapacity() {
        return lblEngineCapacity;
    }


}

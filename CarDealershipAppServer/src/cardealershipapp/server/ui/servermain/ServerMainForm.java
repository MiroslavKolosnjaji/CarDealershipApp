package cardealershipapp.server.ui.servermain;

import cardealershipapp.server.communication.Server;
import cardealershipapp.server.database.DataBase;
import cardealershipapp.server.session.ServerSession;
import cardealershipapp.server.thread.ProcessClientRequests;
import cardealershipapp.server.ui.component.table.model.ActiveClientTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class ServerMainForm extends javax.swing.JFrame {

    private Server server;
    private final List<String> stats;
    private List<ProcessClientRequests> connectedClients;
    private Thread tableThread;

    public ServerMainForm() {
        initComponents();
        stats = new ArrayList<>();
        connectedClients = new ArrayList<>();
        fillTable();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlServer = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        btnStart = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        areaStatus = new javax.swing.JTextArea();
        pnlClient = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblConnectedUsers = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Port:");

        txtPort.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtPort.setForeground(new java.awt.Color(153, 0, 153));
        txtPort.setText("9000");

        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        btnStop.setText("Stop");
        btnStop.setEnabled(false);
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Server status"));

        areaStatus.setEditable(false);
        areaStatus.setColumns(20);
        areaStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        areaStatus.setForeground(new java.awt.Color(153, 0, 153));
        areaStatus.setRows(5);
        jScrollPane2.setViewportView(areaStatus);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        pnlClient.setBorder(javax.swing.BorderFactory.createTitledBorder("Connections"));

        tblConnectedUsers.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tblConnectedUsers.setForeground(new java.awt.Color(153, 0, 153));
        jScrollPane1.setViewportView(tblConnectedUsers);

        javax.swing.GroupLayout pnlClientLayout = new javax.swing.GroupLayout(pnlClient);
        pnlClient.setLayout(pnlClientLayout);
        pnlClientLayout.setHorizontalGroup(
            pnlClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlClientLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlClientLayout.setVerticalGroup(
            pnlClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlClientLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlServerLayout = new javax.swing.GroupLayout(pnlServer);
        pnlServer.setLayout(pnlServerLayout);
        pnlServerLayout.setHorizontalGroup(
            pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlServerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlClient, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlServerLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtPort))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlServerLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnStart)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnStop)))
                .addContainerGap())
        );
        pnlServerLayout.setVerticalGroup(
            pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlServerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnStart)
                    .addComponent(btnStop))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        int port = Integer.parseInt(txtPort.getText().trim());
        server = new Server(port);
        server.start();

        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        try {
            Thread.sleep(50);
            stats.clear();
            stats.add("Server is running!");
            status();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerMainForm.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnStartActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        server.interrupt();
        server.stopServer();
        stats.clear();
        stats.add("Server closed!");
        connectedClients.clear();
        btnStop.setEnabled(false);
        btnStart.setEnabled(true);
        status();
        fillTable();
    }//GEN-LAST:event_btnStopActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {

            if (tableThread != null && !tableThread.isInterrupted()) {
                tableThread.interrupt();
            }

            DataBase.getInstance().disconnectFromDb();

        } catch (Exception ex) {
            Logger.getLogger(ServerMainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaStatus;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnStop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnlClient;
    private javax.swing.JPanel pnlServer;
    private javax.swing.JTable tblConnectedUsers;
    private javax.swing.JTextField txtPort;
    // End of variables declaration//GEN-END:variables

    private void status() {
        if (server != null) {
            for (String stat : stats) {
                areaStatus.setText(areaStatus.getText() + stat + "\n");
            }
        }
    }

    private void fillTable() {
        connectedClients = ServerSession.getInstance().getConnectedClients();
        AbstractTableModel myTableModel = new ActiveClientTableModel(connectedClients);
        tblConnectedUsers.setModel(myTableModel);

        tableThread = new Thread(() -> {

            while (true) {
                myTableModel.fireTableDataChanged();
                
                try {
                    Thread.sleep(125);
                } catch (InterruptedException ex) {
                    break;
                }
            }

        });

        tableThread.start();
    }

}

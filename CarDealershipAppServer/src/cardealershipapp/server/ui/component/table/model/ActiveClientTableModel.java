package cardealershipapp.server.ui.component.table.model;

import cardealershipapp.server.thread.ProcessClientRequests;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class ActiveClientTableModel extends AbstractTableModel{
    

    private final List<ProcessClientRequests> connectedClients;
    private final String[] columns = {"Active user", "Time"};
    
    public ActiveClientTableModel(List<ProcessClientRequests> connectedClients) {
        this.connectedClients = connectedClients;
    }


    @Override
    public int getRowCount() {
        if(connectedClients == null){
            return 0;
        }
        
        return connectedClients.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
       ProcessClientRequests client = connectedClients.get(rowIndex);
       
       switch(columnIndex){  
           case 0 -> { return client.getConnectedUser().getUser().getFirstName() + " " + client.getConnectedUser().getUser().getLastName(); } 
           case 1 -> { return client.getLoginDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss")); }  
       }  
       return "N/A";
    }

    @Override
    public String getColumnName(int column) {
        switch(column){  
           case 0 -> { return columns[0]; } 
           case 1 -> { return columns[1]; }  
       }  
       return "N/A";
    }

}

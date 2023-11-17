package cardealershipapp.server.main;

import cardealershipapp.server.ui.servermain.ServerMainForm;
import javax.swing.JFrame;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class Main {
    
    
    public static void main(String[] args) {
        
        JFrame server = new ServerMainForm();
        server.setLocationRelativeTo(null);
        server.setVisible(true);
    }
}

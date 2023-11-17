package cardealershipapp.client.ui.component.table;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class MyTableCustomComponents {

    public static void setTblHeader(JTable table) {
        JTableHeader tblHeader = table.getTableHeader();
        tblHeader.setBackground(new Color(0, 153, 102));
        tblHeader.setForeground(Color.WHITE);
        tblHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    public static void centerCellText(JTable table) {
        DefaultTableCellRenderer dcr = new DefaultTableCellRenderer();
        dcr.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, dcr);
    }

}

package cardealershipapp.client.ui.component.table.model;

import cardealershipapp.common.domain.User;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class UserTableModel extends AbstractTableModel {

    private final List<User> users;

    public UserTableModel(List<User> users) {
        this.users = users;
    }

    @Override
    public int getRowCount() {
        if (users == null) {
            return 0;
        }
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        User user = users.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return user.getId();
            case 1:
                return user.getFirstName();
            case 2:
                return user.getLastName();
            case 3:
                return dtf.format(user.getDateOfBirth());
            case 4:
                return user.getGender();
            case 5:
                return user.getResidence().getName();
            default:
                break;
        }
        return "N/A";
    }

    @Override
    public String getColumnName(int column) {

        switch (column) {
            case 0:
                return "ID";
            case 1:
                return "Ime";
            case 2:
                return "Prezime";
            case 3:
                return "Datum rodjenja";
            case 4:
                return "Pol";
            case 5:
                return "Prebivaliste";
        }
        return "N/A";
    }

}

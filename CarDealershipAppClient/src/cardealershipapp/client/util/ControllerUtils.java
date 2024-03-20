package cardealershipapp.client.util;

import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ControllerUtils {

    public static int exitOption(JFrame frame) {
        return option("Aplikacija će biti prekinuta! Da li želite da nastavite dalje?", "Izlaz", frame);
    }

    public static int logoutOption(JFrame frame) {
        return option("Da li ste sigurni da želite da se izlogujete?", "Odjavljivanje", frame);
    }

    private static int option(String message, String title, JFrame frame) {
        String[] options = {"Da", "Ne", "Odustani"};
        return JOptionPane.showOptionDialog(frame, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, EXIT_ON_CLOSE);
    }

    public static int optionPane(String message, JDialog dialog) {
        String[] options = {"Da", "Ne"};
        return JOptionPane.showOptionDialog(dialog, message, "Pažnja!",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, EXIT_ON_CLOSE);
    }

    public static void showMessageDialog(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showMessageDialog(JDialog dialog, String message) {
        JOptionPane.showMessageDialog(dialog, message, "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
    }


}

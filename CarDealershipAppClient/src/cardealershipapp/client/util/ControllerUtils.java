package cardealershipapp.client.util;

import javax.swing.*;

import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ControllerUtils {

    public static int exitOption(JFrame frame) {
        return option("Aplikacija će biti prekinuta! Da li želite da nastavite dalje?", "Izlaz", frame);
    }

    public static int logoutOption(JFrame frame) {
        return option("Da li ste sigurni da želite da se izlogujete?", "Odjavljivanje", frame);
    }

    public static int confirmOption(String message, Container container) {
        return option(message, "Upozorenje!", container);
    }

    public static int optionPane(String message, Container container) {
        String[] options = {"Da", "Ne"};
        return JOptionPane.showOptionDialog(container, message, "Pažnja!",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, EXIT_ON_CLOSE);
    }

    public static void showMessageDialog(Container container, String message) {
        JOptionPane.showMessageDialog(container, message, "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
    }

    private static int option(String message, String title, Container container) {
        String[] options = {"Da", "Ne", "Odustani"};
        int answer = JOptionPane.showOptionDialog(container, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
        return answer;
    }


}

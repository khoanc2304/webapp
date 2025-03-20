package utils.debug;

import javax.swing.*;

public class ErrorDialog {
    public static void showError(String errorMessage,String fileName) {
        JOptionPane.showMessageDialog(null, errorMessage, "ERROR:" + fileName, JOptionPane.ERROR_MESSAGE);
    }
}
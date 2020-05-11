package gui;

import log.ExceptionLogger;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//          UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//          UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            ExceptionLogger.writeException(e.getStackTrace(), "Something got wrong");
        }
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.pack();
            frame.setVisible(true);
        });
    }
}
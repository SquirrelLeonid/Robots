package gui;

import service.management.Scheduler;
import log.ExceptionLogger;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class RobotsProgram {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            ExceptionLogger.writeException(e.getStackTrace(), "Something got wrong");
        }
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            Scheduler.runAllTasks();
            frame.pack();
            frame.setVisible(true);
        });
    }
}
package gui;

import log.LogEntry;
import log.LogWindowSource;
import log.LogChangeListener;
import java.awt.TextArea;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class LogWindow extends StorableJInternalFrame implements LogChangeListener {
    private TextArea m_logContent;
    private LogWindowSource m_logSource;
    private final PropertiesKeeperSingleton m_keeper;

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;

    LogWindow(LogWindowSource logSource, PropertiesKeeperSingleton keeper) {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logContent = new TextArea("");
        m_logSource.registerListener(this);
        m_logContent.setSize(200, 500);
        initWindowState();
        updateLogContent();
        m_keeper = keeper;
        m_keeper.register(this.title, this);
    }

    private void initWindowState() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setSize(WIDTH, HEIGHT);
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public void dispose() {
        m_logSource.unregisterListener(this);
        m_keeper.unregister(this.title);
        super.dispose();
    }
}
package gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public class GameWindow extends StorableJInternalFrame {
    private final GameVisualizer m_visualizer;
    private final PropertiesKeeperSingleton m_keeper;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    public GameWindow(PropertiesKeeperSingleton keeper) {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setSize(WIDTH, HEIGHT);
        m_keeper = keeper;
        keeper.register(this.title, this);
    }

    @Override
    public void dispose() {
        m_keeper.unregister(this.title);
        super.dispose();
    }
}
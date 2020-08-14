package gui;

import model.Robot;
import service.localization.Localizer;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

public class GameWindow extends StorableJInternalFrame implements PropertyChangeListener
{
    private final Robot m_robot;
    private final GameVisualizer m_visualizer;
    private final PropertiesKeeperSingleton m_keeper;

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    GameWindow(PropertiesKeeperSingleton keeper, Robot robot) {
        super("Игровое поле", true, true, true, true);
        setName("gameWindow_Title");
        m_robot = robot;
        m_keeper = keeper;
        m_visualizer = new GameVisualizer(m_robot);
        initEvents();
        initWindowState();
        keeper.register(this.title, this);
        Localizer.addPropertyChangeListener(this);
    }

    private void initWindowState() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setSize(WIDTH, HEIGHT);
    }

    private void initEvents() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_robot.setTargetPosition(e.getPoint());
                repaint();
            }
        });
    }

    @Override
    public void dispose() {
        m_keeper.unregister(this.title);
        Localizer.removePropertyChangeListener(this);
        super.dispose();
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent)
    {
        ResourceBundle bundle = (ResourceBundle)propertyChangeEvent.getNewValue();
        this.setLocale(bundle.getLocale());
        this.setTitle(bundle.getString(this.getName()));
    }
}
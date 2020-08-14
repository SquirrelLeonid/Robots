package gui;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import model.Robot;
import service.localization.Localizer;

import javax.swing.*;

public class RobotWindow extends StorableJInternalFrame implements PropertyChangeListener
{
    private static final int m_defaultWidth = 400;
    private static final int m_defaultHeight = 400;
    private final Robot m_robot;
    private final PropertiesKeeperSingleton m_keeper;

    private RobotMonitorVisualizer m_visualizer;

    RobotWindow(PropertiesKeeperSingleton keeper, Robot robot) {
        super("Окно робота", true, true, false, true);
        setName("robotWindow_Title");
        m_robot = robot;
        m_visualizer = new RobotMonitorVisualizer(m_robot);
        m_robot.addPropertyChangeListener(m_visualizer);;
        m_keeper = keeper;
        m_keeper.register(this.title, this);
        Localizer.addPropertyChangeListener(this);
        initWindowState();
    }

    private void initWindowState() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setSize(m_defaultWidth, m_defaultHeight);
    }

    @Override
    public void dispose() {
        m_keeper.unregister(this.title);
        m_robot.removePropertyChangeListener(m_visualizer);
        Localizer.removePropertyChangeListener(this);
        this.dispose();
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent)
    {
        ResourceBundle bundle = (ResourceBundle)propertyChangeEvent.getNewValue();
        this.setLocale(bundle.getLocale());
        this.setTitle(bundle.getString(this.getName()));
    }
}
package gui;

import Model.Robot;

import javax.swing.*;
import java.awt.*;


public class RobotWindow extends StorableJInternalFrame {

    private static final int m_defaultWidth = 400;
    private static final int m_defaultHeight = 400;

    private RobotMonitorVisualizer m_visualizer;

    private final Robot m_robot;
    private final PropertiesKeeperSingleton m_keeper;

    RobotWindow(PropertiesKeeperSingleton keeper, Robot robot) {
        super("Окно робота", true, true, false, true);
        m_robot = robot;
        m_visualizer = new RobotMonitorVisualizer(m_robot);
        m_robot.addPropertyChangeListener(m_visualizer);
        initWindowState();
        m_keeper = keeper;
        m_keeper.register(this.title, this);
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
        this.dispose();
    }
}
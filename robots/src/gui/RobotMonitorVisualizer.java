package gui;

import model.Robot;
import javax.swing.*;
import java.text.DecimalFormat;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

class RobotMonitorVisualizer extends JPanel implements PropertyChangeListener {
    private Robot m_robot;
    //Unit circle's variables
    private static final int m_circleRadius = 200;
    private static final int m_halfRadius = m_circleRadius / 2;
    //Coordinate axis's variables
    private static final int m_ledgeValue = 20;

    RobotMonitorVisualizer(Robot robot) {
        m_robot = robot;
        setDoubleBuffered(true);
    }

    private void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int circleCenterX = this.getWidth() / 2;
        int circleCenterY = (this.getHeight() / 2) + 50;

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 16));

        updateRobotInfo(g2d);

        drawUnitCircle(g2d, circleCenterX, circleCenterY);
        drawCoordinateAxis(g2d, circleCenterX, circleCenterY);
        drawBaseAngleValues(g2d, circleCenterX, circleCenterY);

        drawCurrentAngleValue(g2d, circleCenterX, circleCenterY);
    }

    //region drawing circle
    private static void drawUnitCircle(Graphics2D g2d, int circleCenterX, int circleCenterY) {
        g2d.drawOval(circleCenterX - m_circleRadius / 2, circleCenterY - m_circleRadius / 2, m_circleRadius, m_circleRadius);
    }

    private static void drawCoordinateAxis(Graphics2D g2d, int circleCenterX, int circleCenterY) {
        g2d.drawLine(circleCenterX - m_halfRadius - m_ledgeValue, circleCenterY, circleCenterX + m_halfRadius + m_ledgeValue, circleCenterY);
        g2d.drawLine(circleCenterX, circleCenterY - m_halfRadius - m_ledgeValue, circleCenterX, circleCenterY + m_halfRadius + m_ledgeValue);
    }

    private static void drawBaseAngleValues(Graphics2D g2d, int circleCenterX, int circleCenterY) {
        g2d.drawString("0", circleCenterX + m_halfRadius - m_ledgeValue / 2, circleCenterY);
    }

    private void drawCurrentAngleValue(Graphics2D g2d, int circleCenterX, int circleCenterY) {
        AffineTransform t = AffineTransform.getRotateInstance(m_robot.getRobotDirectionRadians(), circleCenterX, circleCenterY);
        g2d.setTransform(t);
        g2d.setColor(Color.RED);

        int endX = circleCenterX + m_halfRadius + m_ledgeValue;
        g2d.drawLine(circleCenterX, circleCenterY, endX, circleCenterY);
        g2d.drawLine(endX, circleCenterY, endX - 10, circleCenterY - 5);
        g2d.drawLine(endX, circleCenterY, endX - 10, circleCenterY + 5);

    }
    //endregion

    private void updateRobotInfo(Graphics2D g2d) {
        DecimalFormat df = new DecimalFormat("#.##");
        g2d.drawString("Robot Ox = " + df.format(m_robot.getRobotX()), 5, 20);
        g2d.drawString("Robot Oy = " + df.format(m_robot.getRobotY()), 5, 40);
        g2d.drawString("Robot angle =  " + df.format(-m_robot.getRobotDirectionDegree()), 5, 60);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        onRedrawEvent();
    }
}
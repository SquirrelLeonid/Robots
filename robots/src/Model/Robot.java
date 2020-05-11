package Model;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Timer;
import java.util.TimerTask;

public class Robot {

    private Timer m_timer;

    private double m_robotX = 100;
    private double m_robotY = 100;
    private double m_robotDirection = 0;

    private static final double m_maxVelocity = 0.1;
    private static final double m_maxAngularVelocity = 0.005;
    private static final double m_duration = 10;

    private double m_targetX = 100;
    private double m_targetY = 100;

    private PropertyChangeSupport m_support;

    public Robot() {
        m_timer = new Timer();
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 10);
        m_support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        m_support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        m_support.removePropertyChangeListener(pcl);
    }

    //region getters and setters
    public double getTargetX() {
        return m_targetX;
    }

    public double getTargetY() {
        return m_targetY;
    }

    public double getRobotX() {
        return m_robotX;
    }

    public double getRobotY() {
        return m_robotY;
    }

    public double getRobotDirectionRadians() {
        return m_robotDirection;
    }

    public double getRobotDirectionDegree() {
        return m_robotDirection * 180 / Math.PI;
    }

    public void setTargetPosition(Point mousePos) {
        m_targetX = mousePos.x;
        m_targetY = mousePos.y;
    }
    //endregion

    //region Applied methods
    private double distance() {
        double diffX = m_targetX - m_robotX;
        double diffY = m_targetY - m_robotY;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private double angleTo() {
        double diffX = m_targetX - m_robotX;
        double diffY = m_targetY - m_robotY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private double asNormalizedRadians(double angle) {
        while (angle < -2 * Math.PI) {
            if (angle + 2 * Math.PI > 0)
                break;
            angle += 2 * Math.PI;
        }

        while (angle >= 2 * Math.PI) {
            if (angle - 2 * Math.PI < 0)
                break;
            angle -= 2 * Math.PI;
        }

        return angle;
    }

    private double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }
    //endregion

    private void onModelUpdateEvent() {
        double distance = distance();
        if (distance < 0.5) {
            return;
        }

        double angularVelocity = 0;
        double angleToTarget = angleTo();

        if (angleToTarget > m_robotDirection) {
            angularVelocity = m_maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection) {
            angularVelocity = -m_maxAngularVelocity;
        }

        moveRobot(angularVelocity, angleToTarget);
    }

    private void moveRobot(double angularVelocity, double angleToTarget) {
        angularVelocity = applyLimits(angularVelocity, -m_maxAngularVelocity, m_maxAngularVelocity);
        if (Math.abs(angleToTarget - m_robotDirection) < 0.1) {
            m_robotX = m_robotX + Math.cos(angleToTarget) * m_duration * m_maxVelocity;
            m_robotY = m_robotY + Math.sin(angleToTarget) * m_duration * m_maxVelocity;
        }
        else {
            m_robotDirection = asNormalizedRadians(m_robotDirection + angularVelocity * m_duration);
        }
        m_support.firePropertyChange("", false,true);
    }
}
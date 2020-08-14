package model;

import log.ExceptionLogger;
import service.AppliedSolver;
import service.management.Taskable;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.TimerTask;

public class Robot extends Taskable {
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
        addTask(this,"onModelUpdateEvent", new TimerTask() {
            @Override
            public void run() {
                //TODO: продумать выход из цикла? флаг?
                while (true) {
                    onModelUpdateEvent();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        String message = String.format("Interrupt error in %s thread", Thread.currentThread().getName());
                        ExceptionLogger.writeException(ex.getStackTrace(), message);
                    }
                }
            }
        }, 0);
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

    private void onModelUpdateEvent() {
        double distance = AppliedSolver.distance(m_robotX, m_robotY, m_targetX, m_targetY);
        if (distance < 0.5) {
            return;
        }

        double angularVelocity = 0;
        double angleToTarget = AppliedSolver.angleTo(m_robotX, m_robotY, m_targetX, m_targetY);

        if (angleToTarget > m_robotDirection) {
            angularVelocity = m_maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection) {
            angularVelocity = -m_maxAngularVelocity;
        }

        moveRobot(angularVelocity, angleToTarget);
    }

    private void moveRobot(double angularVelocity, double angleToTarget) {
        angularVelocity = AppliedSolver.applyLimits(angularVelocity, -m_maxAngularVelocity, m_maxAngularVelocity);
        if (Math.abs(angleToTarget - m_robotDirection) < 0.1) {
            m_robotX = m_robotX + Math.cos(angleToTarget) * m_duration * m_maxVelocity;
            m_robotY = m_robotY + Math.sin(angleToTarget) * m_duration * m_maxVelocity;
        }
        else {
            m_robotDirection = AppliedSolver.asNormalizedRadians(m_robotDirection + angularVelocity * m_duration);
        }
        m_support.firePropertyChange("", false,true);
    }
}
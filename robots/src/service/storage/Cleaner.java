package service.storage;

import service.Taskable;

import java.util.TimerTask;

public class Cleaner extends Taskable implements Runnable {
    private static final int m_minDelay = 5000;
    private static final int m_maxDelay = 60000;

    private int m_removeDelay = m_maxDelay / 2;
    private boolean m_isEnable = true;

    Cleaner() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
            }
        };
        addTask(this, "CleanTask", task);
    }

    public void setEnable() {
        m_isEnable = true;
    }

    public void setDisable() {
        m_isEnable = false;
    }

    @Override
    public void run() {

    }
}
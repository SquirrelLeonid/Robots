package log;

import service.FastContainer;
import java.util.ArrayList;

public class LogWindowSource {
    private final ArrayList<LogChangeListener> m_listeners;

    private int m_iQueueLength;
    private FastContainer<LogEntry> m_messages;
    private volatile LogChangeListener[] m_activeListeners;

    LogWindowSource(int iQueueLength) //default size = 100
    {
        m_iQueueLength = iQueueLength;
        m_listeners = new ArrayList<>();
        m_messages = new FastContainer<>(iQueueLength);
    }

    public void registerListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }

    public void unregisterListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }

    void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.addItem(entry);
        LogChangeListener[] activeListeners = m_activeListeners;
        if (activeListeners == null) {
            synchronized (m_listeners) {
                if (m_activeListeners == null) {
                    activeListeners = m_listeners.toArray(new LogChangeListener[0]);
                    m_activeListeners = activeListeners;
                }
            }
        }

        for (LogChangeListener listener : activeListeners) {
            listener.onLogChanged();
        }
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        int indexTo = startFrom + count;
        return m_messages.getSegment(startFrom, indexTo);
    }

    public Iterable<LogEntry> all() {
        return m_messages;
    }
}
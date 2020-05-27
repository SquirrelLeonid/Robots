package service.storage;

import log.ExceptionLogger;
import service.Taskable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TimerTask;

public class CustomContainer extends Taskable {

    private static final int m_minDelay = 5000;
    private static final int m_maxDelay = 60000;

    private int m_removeDelay = m_maxDelay / 2;
    private boolean m_isEnable = true;

    private final ArrayList<ContainerNode> m_storage;
    private ContainerNode m_head;
    private ContainerNode m_tail;
    private int m_limit = 50;

    public CustomContainer() {
        m_storage = new ArrayList<>();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

            }
        };
        addTask(this, "cleanTask", task);
    }

    public void add (String value) {
        //TODO: удаление при достижении m_limit
        synchronized (m_storage) {
            ContainerNode node = new ContainerNode(value, m_tail);
            m_tail.setNext(node);
            m_tail = node;
        }

    }

    public ContainerNode[] getSegment(int startIndex, int endIndex) {
        synchronized (m_storage) {
            if (!areIndexesCorrect(startIndex, endIndex))
                return null;
            ContainerNode[] segment = new ContainerNode[endIndex - startIndex];
            for (int i = 0; i < segment.length; i++)
                segment[i] = m_storage.get(i + startIndex);
            return segment;
        }
    }

    private boolean areIndexesCorrect (int startIndex, int endIndex) {
        if (startIndex < 0 || startIndex > m_storage.size() - 1)
            return false;
        if (endIndex < 0 || endIndex > m_storage.size() - 1)
            return false;
        if (endIndex < startIndex)
            return false;
        return true;
    }
}

package service;

import java.util.*;

public class FastContainer<E> implements Iterable<E>
{
    private int m_maxSize;
    private int m_nextIndex = 0;
    private int m_oldestIndex = 0;
    private boolean m_isDataFull= false;
    private ArrayList<E> m_data;

    public FastContainer(int size)
    {
        m_data = new ArrayList<E>(size);
        m_maxSize = size;
    }

    @Override
    public Iterator<E> iterator()
    {
        synchronized (m_data)
        {
            return new FastContainerIterator(m_data, m_oldestIndex);
        }
    }

    private class FastContainerIterator implements Iterator<E>
    {
        private int m_current;
        private int m_count = 0;
        private final ArrayList<E> m_elements;

        FastContainerIterator (ArrayList<E> data, int oldestIndex)
        {
            m_elements = new ArrayList<>();
            Collections.copy(m_elements, data);
            m_current = oldestIndex;
        }

        @Override
        public boolean hasNext()
        {
            return m_count < m_elements.size();
        }

        @Override
        public E next()
        {
            if (!hasNext())
                throw new NoSuchElementException();
            m_count++;
            E item = m_elements.get(m_current);
            m_current = (m_current + 1) % m_elements.size();
            return item;
        }
    }

    public E getItem(int index)
    {
        int actualIndex = getOffset(index);
        return m_data.get(actualIndex);
    }

    public ArrayList<E> getSegment(int startIndex, int endIndex)
    {
        int actualEndIndex = getOffset(endIndex);
        int actualStartIndex = getOffset(startIndex);
        ArrayList<E> segment = new ArrayList<>();
        int current = actualStartIndex;
        while (current != actualEndIndex)
        {
            segment.add(m_data.get(current++));
            if (current == m_maxSize)
                current = 0;

        }
        return segment;
    }

    public void addItem(E item)
    {
        //TODO: можно ли упростить блок synchronized?
        synchronized (m_data)
        {
            if (m_data.size() == m_maxSize)
                m_data.set(m_nextIndex, item);
            else
                m_data.add(item);

            if (m_isDataFull && m_nextIndex == m_oldestIndex)
                m_oldestIndex++;

            m_nextIndex++;
            m_nextIndex %= m_maxSize;
            m_oldestIndex %= m_maxSize;
            if (!m_isDataFull && m_nextIndex == 0)
                m_isDataFull = true;
        }
    }

    private int getOffset(int index)
    {
        return (m_oldestIndex + index) % m_maxSize;
    }
}

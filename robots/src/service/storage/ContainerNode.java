package service.storage;

public class ContainerNode {
    private String m_value;
    private ContainerNode m_next;
    private ContainerNode m_previous;

    public ContainerNode(String value, ContainerNode previous) {
        m_value = value;
        m_previous = previous;
    }

    //TODO: обобщенный тип и возможно возвращать стоит НОВОЕ значение, а не старое
    //p.s возвращаемые ссылочные типы могут быть изменены внутри
    public String getValue(){
        return m_value;
    }

    public ContainerNode getNext() {
        return m_next;
    }

    public void setNext(ContainerNode next) {
        m_next = next;
    }

    public ContainerNode getPrevious() {
        return m_previous;
    }

    public void setPrevious(ContainerNode previous) {
        m_previous = previous;
    }
}
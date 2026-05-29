package co.edu.udistrital.model.structures;

/**
 * Cada uno de los nodos que conforman la lista enlazada
 *
 * @author Jimmy86gb
 * @param <T> lo que sea que vayamos a meter dentro del nodo
 */
public class Node<T> {

    private T data;
    private Node<T> next;

    /**
     * Crea un nodo nuevo suelto
     *
     * @param data la informacion a guardar
     */
    public Node(T data) {
        this(data, null);
    }

    /**
     * Crea un nodo que ya sabe quien es su siguiente
     *
     * @param data la informacion a guardar
     * @param next el nodo que va pegado despues
     */
    public Node(T data, Node<T> next) {
        this.data = data;
        this.next = next;
    }

    /**
     * Devuelve el dato que hay adentro del nodo
     *
     * @return la informacion del nodo
     */
    public T getData() {
        return data;
    }

    /**
     * Reemplaza lo que hay dentro del nodo
     *
     * @param data el nuevo dato
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Devuelve el nodo que sigue
     *
     * @return el nodo de enfrente
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * Un atajo para evitar doble llamado f1().f2() si queremos el dato del
     * siguiente
     *
     * @return la informacion del nodo que sigue
     */
    public T getNextData() {
        Node<T> next = getNext();
        return next.getData();
    }

    /**
     * Pega un nodo nuevo despues de este
     *
     * @param next el nodo a conectar
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }

    /**
     * Atajo para evitar llamado en cascada si queremos setear el dato siguiente
     *
     * @param data la informacion a asignar a siguiente
     */
    public void setNextData(T data) {
        Node<T> next = getNext();
        next.setData(data);
    }

    /**
     * Pasa el nodo a texto para verlo mas facil
     *
     * @return el texto del dato
     */
    @Override
    public String toString() {
        return data.toString();
    }
}


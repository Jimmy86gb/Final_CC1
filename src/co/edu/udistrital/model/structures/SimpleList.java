package co.edu.udistrital.model.structures;

/**
 * Lista simplemente enlazada
 *
 * @author Jimmy86gb
 * @param <T> el tipo de dato que queremos guardar en la lista
 */
public class SimpleList<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Arranca la lista vacia sin nada adentro
     */
    public SimpleList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Mete un dato nuevo al final de la cola
     *
     * @param data lo que vamos a guardar
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }

    /**
     * Devuelve el primer nodo de todos / cabeza
     *
     * @return el nodo cabeza
     */
    public Node<T> getHead() {
        return head;
    }

    /**
     * Devuelve el nodo que se encuentre al lado de la cabeza
     * 
     * @return el nodo despues de cabeza
     */
    public Node<T> getNextNodeToHead() {
        Node<T> head = getHead();
        return head.getNext();
    }

    /**
     * Cambia a la fuerza quien es el primero
     *
     * @param head el nuevo nodo inicial
     */
    public void setHead(Node<T> head) {
        this.head = head;
    }

    /**
     * Devuelve el ultimo nodo de la fila
     *
     * @return el nodo cola
     */
    public Node<T> getTail() {
        return tail;
    }

    /**
     * Cambia a la fuerza el nodo final
     *
     * @param tail el nuevo nodo final
     */
    public void setTail(Node<T> tail) {
        this.tail = tail;
    }

    /**
     * Devuelve el tamaño de la lista
     *
     * @return el tamano de la lista
     */
    public int getSize() {
        return size;
    }

    /**
     * Cambia el tamaño de la lista
     *
     * @param size el nuevo tamano
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Revisa si la lista esta vacia
     *
     * @return verdadero si esta vacia, falso si ya tiene algo
     */
    public boolean isEmpty() {
        if (getSize() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Busca y devuelve el nodo que esta en una posicion especifica
     *
     * @param index la posicion numerica (empezando desde 0)
     * @return el nodo en esa posicion, o null si el indice no existe
     */
    public Node<T> getNodeAt(int index) {
        // Validacion de seguridad para evitar errores
        if (index < 0 || index >= size) {
            return null;
        }

        Node<T> current = head;
        int count = 0;
        while (current != null && count < index) {
            current = current.getNext();
            count++;
        }
        return current;
    }

}

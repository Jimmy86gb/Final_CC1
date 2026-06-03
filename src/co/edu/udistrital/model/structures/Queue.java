package co.edu.udistrital.model.structures;

import java.io.Serializable;

/**
 * Implementación genérica de una estructura de datos tipo cola (Queue)
 * basada en nodos enlazados. Sigue el principio FIFO (First In, First Out),
 * donde el primer elemento en entrar es el primero en salir.
 * 
 * @param <T> Tipo de dato almacenado en la cola.
 * 
 * @author ChrZ
 */
public class Queue<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Node<T> head;
	private Node<T> tail;
	private int size;

	/**
	 * Constructor que crea una cola vacía.
	 */
	public Queue() {
		this.head = null;
		this.tail = null;
		this.size = 0;
	}

	/**
	 * Inserta un nuevo elemento al final de la cola.
	 * 
	 * @param data El elemento a insertar.
	 */
	public void enqueue(T data) {
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
	 * Elimina y retorna el elemento ubicado al frente de la cola.
	 * 
	 * @return El elemento removido o null si la cola está vacía.
	 */
	public T dequeue() {
		if (isEmpty()) {
			return null;
		} else {
			T dataToReturn = head.getData();
			head = head.getNext();
			size--;
			if (head == null) {
				tail = null;
			}
			return dataToReturn;
		}
	}

	/**
	 * Obtiene el nodo ubicado al frente de la cola.
	 * 
	 * @return El nodo cabeza de la cola.
	 */
	public Node<T> getHead() {
		return this.head;
	}

	/**
	 * Obtiene el elemento ubicado al frente de la cola sin eliminarlo.
	 * 
	 * @return El primer elemento de la cola.
	 */
	public T peek() {
		return this.head.getData();
	}

	/**
	 * Obtiene la cantidad de elementos almacenados en la cola.
	 * 
	 * @return El tamaño actual de la cola.
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * Verifica si la cola se encuentra vacía.
	 * 
	 * @return true si la cola no contiene elementos, false en caso contrario.
	 */
	public boolean isEmpty() {
		return head == null;
	}
}
package co.edu.udistrital.model.structures;

import java.io.Serializable;

/**
 * Implementación genérica de una estructura de datos tipo pila (Stack)
 * basada en nodos enlazados. Sigue el principio LIFO (Last In, First Out),
 * donde el último elemento en entrar es el primero en salir.
 *
 * @param <T> Tipo de dato almacenado en la pila.
 * 
 * @author Jimmy86gb
 */
public class Stack<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Node<T> top;
	private int size;

	/**
	 * Constructor que crea una pila vacía.
	 */
	public Stack() {
		this.top = null;
		this.size = 0;
	}

	/**
	 * Inserta un nuevo elemento en la parte superior de la pila.
	 *
	 * @param data El elemento a insertar.
	 */
	public void push(T data) {
		Node<T> newNode = new Node<>(data);
		newNode.setNext(top);
		top = newNode;
		size++;
	}

	/**
	 * Elimina y retorna el elemento ubicado en la parte superior de la pila.
	 *
	 * @return El elemento removido o null si la pila está vacía.
	 */
	public T pop() {
		if (isEmpty()) {
			return null;
		} else {
			T dataToReturn = top.getData();
			top = top.getNext();
			size--;
			return dataToReturn;
		}
	}

	/**
	 * Verifica si la pila se encuentra vacía.
	 *
	 * @return true si la pila no contiene elementos, false en caso contrario.
	 */
	public boolean isEmpty() {
		return top == null;
	}

	/**
	 * Obtiene el nodo ubicado en la parte superior de la pila.
	 *
	 * @return El nodo superior de la pila.
	 */
	public Node<T> getTop() {
		return this.top;
	}

	/**
	 * Obtiene la cantidad de elementos almacenados en la pila.
	 *
	 * @return El tamaño actual de la pila.
	 */
	public int getSize() {
		return this.size;
	}
}
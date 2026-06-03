package co.edu.udistrital.model.structures;

import java.io.Serializable;

/**
 * Lista simplemente enlazada.
 *
 * @author Jimmy86gb
 * @param <T> el tipo de dato que queremos guardar en la lista.
 */
public class SimpleList<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Node<T> head;
	private int size;

	/**
	 * Constructor que crea una lista vacía.
	 */
	public SimpleList() {
		head = null;
		size = 0;
	}

	// ---------------------- Operaciones Básicas ----------------------

	/**
	 * Agrega un elemento al final de la lista.
	 *
	 * @param data El elemento a agregar.
	 */
	public void add(T data) {
		Node<T> newNode = new Node<>(data);
		if (head == null) {
			head = newNode;
		} else {
			Node<T> actual = head;
			while (actual.getNext() != null) {
				actual = actual.getNext();
			}
			actual.setNext(newNode);
		}
		size++;
	}

	/**
	 * Elimina la primera ocurrencia del elemento indicado.
	 *
	 * @param data El elemento a eliminar.
	 * @return true si el elemento fue eliminado, false en caso contrario.
	 */
	public boolean delete(T data) {
		if (head == null) {
			return false;
		}

		// Caso especial: delete la head
		if (head.getData().equals(data)) {
			head = head.getNext();
			size--;
			return true;
		}

		// Buscar en el resto de la lista
		Node<T> actual = head;
		while (actual.getNext() != null && !actual.getNext().getData().equals(data)) {
			actual = actual.getNext();
		}

		if (actual.getNext() != null) {
			actual.setNext(actual.getNext().getNext());
			size--;
			return true;
		}
		return false;
	}

	/**
	 * Actualiza un elemento existente de la lista.
	 *
	 * @param actualData Elemento actual que se desea reemplazar.
	 * @param newData Nuevo elemento que reemplazará al actual.
	 * @return true si la actualización fue exitosa, false si el elemento no existe.
	 */
	public boolean update(T actualData, T newData) {
		if (head == null) {
			return false;
		}

		if (head.getData().equals(actualData)) {

			head.setData(newData);
			return true;

		} else {

			Node<T> actual = head;

			while (actual.getNext() != null && !actual.getData().equals(actualData)) {
				actual = actual.getNext();
			}

			if (actual.getNext() == null && !actual.getData().equals(actualData)) {
				return false;
			} else {
				actual.setData(newData);
				return true;
			}
		}
	}

	// ---------------------- Búsqueda ----------------------

	/**
	 * Verifica si un elemento se encuentra almacenado en la lista.
	 *
	 * @param data El elemento a buscar.
	 * @return true si el elemento existe en la lista, false en caso contrario.
	 */
	public boolean contains(T data) {
		Node<T> actual = head;
		while (actual != null) {
			if (actual.getData().equals(data)) {
				return true;
			}
			actual = actual.getNext();
		}
		return false;
	}

	// ---------------------- Utilidades ----------------------

	/**
	 * Verifica si la lista está vacía.
	 *
	 * @return true si la lista no contiene elementos, false en caso contrario.
	 */
	public boolean isEmpty() {
		return head == null;
	}

	/**
	 * Obtiene la cantidad de elementos almacenados en la lista.
	 *
	 * @return El tamaño actual de la lista.
	 */
	public int getSize() {
		return size;
	}

	// ---------------------- Iterador Básico ----------------------

	/**
	 * Crea un iterador para recorrer los elementos de la lista.
	 *
	 * @return Un iterador posicionado al inicio de la lista.
	 */
	public Iterator<T> iterador() {
		return new Iterator<>(head);
	}

	/**
	 * Iterador básico para recorrer los elementos de una lista simplemente enlazada.
	 *
	 * @param <T> Tipo de dato almacenado en la lista.
	 */
	public static class Iterator<T> {
		private Node<T> actual;

		/**
		 * Crea un iterador a partir del nodo inicial de la lista.
		 *
		 * @param head Nodo inicial de la lista.
		 */
		public Iterator(Node<T> head) {
			this.actual = head;
		}

		/**
		 * Verifica si existen más elementos por recorrer.
		 *
		 * @return true si existen más elementos, false en caso contrario.
		 */
		public boolean hasNext() {
			return actual != null;
		}

		/**
		 * Obtiene el siguiente elemento del recorrido y avanza el iterador.
		 *
		 * @return El siguiente elemento de la lista.
		 * @throws IllegalStateException Si no existen más elementos por recorrer.
		 */
		public T Next() {
			if (!hasNext()) {
				throw new IllegalStateException("No hay más elementos");
			}
			T dato = actual.getData();
			actual = actual.getNext();
			return dato;
		}
	}
}
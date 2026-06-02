package co.edu.udistrital.model.structures;

import java.io.Serializable;

/**
 * Lista simplemente enlazada
 *
 * @author Jimmy86gb
 * @param <T> el tipo de dato que queremos guardar en la lista
 */
public class SimpleList<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Node<T> head;
	private int size;

	public SimpleList() {
		head = null;
		size = 0;
	}

	// ---------------------- Operaciones Básicas ----------------------
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
	public boolean isEmpty() {
		return head == null;
	}

	public int getSize() {
		return size;
	}

	// ---------------------- Iterador Básico ----------------------
	public Iterator<T> iterador() {
		return new Iterator<>(head);
	}

	public static class Iterator<T> {
		private Node<T> actual;

		public Iterator(Node<T> head) {
			this.actual = head;
		}

		public boolean hasNext() {
			return actual != null;
		}

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

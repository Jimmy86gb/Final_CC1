package co.edu.udistrital.model.structures;

import java.io.Serializable;

public class Queue<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Node<T> head;
	private Node<T> tail;
	private int size;

	public Queue() {
		this.head = null;
		this.tail = null;
		this.size = 0;
	}

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

	public Node<T> getHead() {
		return this.head;
	}

	public T peek() {
		return this.head.getData();
	}

	public int getSize() {
		return this.size;
	}

	public boolean isEmpty() {
		return head == null;
	}
}

package co.edu.udistrital.model.structures;

import java.io.Serializable;


public class Node<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private T data;
	private Node<T> next;

	
	public Node(T data) {
		this(data, null);
	}

	
	public Node(T data, Node<T> next) {
		this.data = data;
		this.next = next;
	}

	
	public T getData() {
		return data;
	}

	
	public void setData(T data) {
		this.data = data;
	}

	
	public Node<T> getNext() {
		return next;
	}

	
	public void setNext(Node<T> next) {
		this.next = next;
	}

	
	@Override
	public String toString() {
		return data.toString();
	}
}

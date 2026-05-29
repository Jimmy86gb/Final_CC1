package co.edu.udistrital.model.structures;

public class Stack<T> {
	
	private Node<T> top;
	private int size;
	
	public Stack() {
		this.top=null;
		this.size=0;
	}
	
	public void push(T data) {
		Node<T> newNode = new Node<>(data);
		newNode.setNext(top);
	    top=newNode;
		size++;
	}
	
	public T pop(){
		if(isEmpty()) {
			return null;
		}else {
			T dataToReturn = top.getData();
			top=top.getNext();
			size--;
			return dataToReturn;
		}
	}
	
	public boolean isEmpty() {
		return top==null;
	}
	
	public Node<T> getTop(){
		return this.top;
	}
	
	public int getSize() {
		return this.size;
	}
}

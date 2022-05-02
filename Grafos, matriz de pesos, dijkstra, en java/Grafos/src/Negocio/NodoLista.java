package Negocio;

public class NodoLista<T> {
	private T dato;
	private NodoLista<T> siguiente;
	//add la funcion de coste para la arista
	private int coste;
	
	public NodoLista() {
		siguiente=null;
	}
	public NodoLista(T p) {
		siguiente=null;
		dato=p;
	}
	public NodoLista(T p, NodoLista<T> siguiente) {
		this.siguiente=siguiente;
		dato=p;
	}
	public T getDato() {
		return dato;
	}
	public void setDato(T dato) {
		this.dato = dato;
	}
	public NodoLista<T> getSiguiente() {
		return siguiente;
	}
	public void setSiguiente(NodoLista<T> siguiente) {
		this.siguiente = siguiente;
	}
	//para dar cumplimiento al grafo no dirigido y ponderado
	public int getCoste() {
		return coste;
	}
	public void setCoste(int coste) {
		this.coste = coste;
	}
	
}

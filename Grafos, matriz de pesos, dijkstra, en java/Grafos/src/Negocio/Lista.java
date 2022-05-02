package Negocio;

public class Lista<T> {
	private NodoLista<T> cabeza;
	private int tamanio;
	public NodoLista<T> getCabeza() {
		return cabeza;
	}
	public void setCabeza(NodoLista<T> cabeza) {
		this.cabeza = cabeza;
	}
	public int getTamanio() {
		return tamanio;
	}
	public void setTamanio(int tamanio) {
		this.tamanio = tamanio;
	}
	//agregar elementos a la lista
	public void Agregar(T elemento) {
		NodoLista<T> nuevo = new NodoLista<T>(elemento);
		nuevo.setDato(elemento);
		if(esVacia()) {
			cabeza=nuevo;
		}
		else {
			NodoLista<T> aux = cabeza;
			while(aux.getSiguiente()!=null) {
				aux=aux.getSiguiente();
			}
			aux.setSiguiente(nuevo);
		}
		tamanio ++;
	}
	public boolean existe(Object elemento) {
		NodoLista<T> aux = getCabeza();
		while(aux != null) {
			if(aux.getDato().equals(elemento))
				return true;
			aux = aux.getSiguiente();
		}
		return false;
	}
	//grafo no dirigido ponderado
	public void agregar(T elemento, int coste) {
		NodoLista<T> nuevo = new NodoLista<T>(elemento);
		nuevo.setDato(elemento);
		//add el coste
		nuevo.setCoste(coste);
		//
		if(esVacia()) {
			cabeza=nuevo;
		}
		else {
			NodoLista<T> aux = cabeza;
			while(aux.getSiguiente()!=null) {
				aux=aux.getSiguiente();
			}
			aux.setSiguiente(nuevo);
		}
		tamanio ++;
	}
	public void eliminar(T elemento) {
		NodoLista<T> aux = cabeza;
		if(aux.getDato().equals(elemento)) {
			cabeza = cabeza.getSiguiente();
			aux = null;
			tamanio--;
		}
		while(aux != null) {
			if(elemento.equals(aux.getSiguiente().getDato())) {
				aux.setSiguiente(aux.getSiguiente().getSiguiente());
				tamanio--;
				return;
			}
			aux = aux.getSiguiente();
		}
	}
	
	public boolean esVacia() {
		return (getCabeza()==null);
	}
	public void mostrar() {
		NodoLista<T> aux=cabeza;
		while(aux!=null) {
			System.out.println(aux.getDato());
			aux=aux.getSiguiente();
		}
	}
	public T getElemento(int indice){
		NodoLista<T> nodo = cabeza;
		for (int i = 0; i < indice; i++) {
			nodo = nodo.getSiguiente();
		}
		return nodo.getDato();
	} 
}

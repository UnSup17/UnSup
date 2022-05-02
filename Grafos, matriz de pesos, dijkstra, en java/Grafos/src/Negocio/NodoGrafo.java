package Negocio;

@SuppressWarnings("rawtypes")
public class NodoGrafo<T> {
	private T elemento;	
	private Lista<NodoGrafo> adyacencias;
	
	public NodoGrafo(T elemento) {
		this.elemento = elemento;
		adyacencias = new Lista<NodoGrafo>();
	}
	public T getElemento() {
		return elemento;
	}
	public void setElemento(T elemento) {
		this.elemento = elemento;
	}
	public Lista<NodoGrafo> getAdyacencias() {
		return adyacencias;
	}
	//adyc en los grafos no dirigidos ponderados
	public void addAdyacencias(NodoGrafo nodoAdyacente, int coste) {
		getAdyacencias().agregar(nodoAdyacente,coste);
	}
	public void delAdyacencia(NodoGrafo nodoAdyacente) {
		getAdyacencias().eliminar(nodoAdyacente);
	}
	public void editCosteAdyacencia(NodoGrafo nodoadyacente, int coste) {
		NodoLista<NodoGrafo> aux = adyacencias.getCabeza();
		while(aux != null) {
			if(aux.getDato().equals(nodoadyacente))
				aux.setCoste(coste);
			aux = aux.getSiguiente();
		}
	}
	public boolean esAdyacencia(T nodo) {
		NodoLista<NodoGrafo> aux = adyacencias.getCabeza();
		while(aux != null) {
			if(nodo.equals(aux.getDato().getElemento()))
				return true;
			aux = aux.getSiguiente();
		}
		return false;
	}
	public void mostrarAdyacencias() {
		NodoLista<NodoGrafo> aux = adyacencias.getCabeza();
		while(aux!=null) {
			//add la funcion de coste
			System.out.print("["+aux.getDato().getElemento()+","+aux.getCoste()+"]");
			//
			if(aux.getSiguiente()!=null)
				System.out.print(" -> ");
			aux=aux.getSiguiente();
		}
		System.out.print("");
		System.out.println();
	}
	//Nuevo
	public void formatoAristas() {
		NodoLista<NodoGrafo> aux = adyacencias.getCabeza();
		while(aux!=null) {
			System.out.print("{"+elemento+","+aux.getDato().getElemento()+","+aux.getCoste()+"}");
			if(aux.getSiguiente()!=null)
				System.out.print(",");
			aux=aux.getSiguiente();
		}
	}
	public int getCoste(T nodo) {
		NodoLista<NodoGrafo> aux = adyacencias.getCabeza();
		while(aux != null) {
			if(nodo.equals(aux.getDato().getElemento()))
				return aux.getCoste();
			aux = aux.getSiguiente();
		}
		return Integer.MAX_VALUE;
	}
}

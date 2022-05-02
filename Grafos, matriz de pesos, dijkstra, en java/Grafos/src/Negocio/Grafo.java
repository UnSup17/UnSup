package Negocio;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Grafo<T> {
	private Lista<NodoGrafo> vertices;
	public Grafo() {
		vertices = new Lista<NodoGrafo>();
	}
	public Lista<NodoGrafo> getVertices(){
		return vertices;
	}
	//verificar la existencia de un vertice
	
	private NodoGrafo<T> retornarNodo(Comparable elemento){
		NodoGrafo<T> nodoTemp = null;
		for (int i = 0; i <vertices.getTamanio() ; i++) {
			nodoTemp= vertices.getElemento(i);
			if(nodoTemp.getElemento().equals(elemento))
				return nodoTemp;
		}
		return null;
	}
	private NodoGrafo<T> retornarNodo(T elemento){
		NodoGrafo<T> nodoTemp = null;
		for (int i = 0; i <vertices.getTamanio() ; i++) {
			nodoTemp= vertices.getElemento(i);
			if(nodoTemp.getElemento().equals(elemento))
				return nodoTemp;
		}
		return null;
	}
	public int retornarPos(T elemento){
		NodoGrafo<T> nodoTemp = null;
		for (int i = 0; i <vertices.getTamanio() ; i++) {
			nodoTemp= vertices.getElemento(i);
			if(nodoTemp.getElemento().equals(elemento))
				return i;
		}
		return -1;
	}
	public boolean verticeExiste(T elemento) {
		if(retornarNodo(elemento)!=null)
			return true;
		else
			return false;
	}
	//agregar vertices y aristas
	public boolean agregarVertice(T vertice) {
		//cambio para verificar si un vertice existe
		if (!verticeExiste(vertice)) {
			NodoGrafo nVertice = new NodoGrafo(vertice);
			this.getVertices().Agregar(nVertice);
			return true;
		}
		else {
			System.out.println("El vertice "+ vertice+" ya existe.");
			return false;
		}
	}
	//verificamos si existe una arista en el grafo
	public boolean aristaExiste(T verticeI, T verticeF){
		if(verticeExiste(verticeF)){
			NodoGrafo<T> nodoTemp = retornarNodo(verticeI);
			if(nodoTemp != null) {
				for (int i = 0; i < nodoTemp.getAdyacencias().getTamanio(); i++) {
					if(nodoTemp.getAdyacencias().getElemento(i).getElemento().equals(verticeF)){
						return true;
					}
				}	
			}else
				System.out.println("El nodo inicial no existe");
			return false;
		}else if (verticeExiste(verticeI))
			System.out.println("El nodo final no existe");
		else
			System.out.println("Ambos nodos no existen en el grafo");
		return false;
	}
	
	public boolean agregarArista(T verticeI, T verticeF, int coste, boolean dirigido) {
		boolean eVi,eVf;
		NodoGrafo<T> nodoI=null, nodoF=null, nodoTemp=null;
		eVi=verticeExiste(verticeI);
		eVf=verticeExiste(verticeF);
		if (eVi && eVf) {
			if(coste>0) {
				if(!aristaExiste(verticeI, verticeF)){
					for(int i=0; i<vertices.getTamanio();i++) {
						nodoTemp=vertices.getElemento(i);
						if(nodoTemp.getElemento().equals(verticeI))
							nodoI=nodoTemp;
						if(nodoTemp.getElemento().equals(verticeF))
							nodoF=nodoTemp;
						if(nodoI!=null && nodoF!=null)
							break;
					}
					if(nodoI.equals(nodoF))
						System.out.println("No se permiten aristas al mismo vertice o Bucles. Arista: "+nodoI.getElemento()+" - "+nodoF.getElemento()+" Descartada.");
					else{
						nodoI.addAdyacencias(nodoF,coste);
						if(!dirigido)
							nodoF.addAdyacencias(nodoI,coste);
						return true;
					}
				}else
					System.out.println("La arista "+verticeI.toString()+" - "+verticeF.toString()+ " ya existe");
			}
			else
				System.out.println("La funcion de coste de la arista "+verticeI.toString()+" - "+verticeF.toString()+" no puede ser 0 o negativa");
			return false;
		}
		else {
			if(!eVi && !eVf)
				System.out.println("El vertice "+verticeI+" y "+ verticeF+" no existen.");
			else if (!eVi)
				System.out.println("El vertice "+verticeI+" no existe.");
			else 
				System.out.println("El vertice "+verticeF+" no existe."); 
			return false;
		}
	}
	
	public void verticesAdyacencias() {
		System.out.println("Grafo");
		System.out.println("Vertices y adyacencias");
		NodoLista<NodoGrafo> aux = vertices.getCabeza();
		while(aux!=null) {
			System.out.print(aux.getDato().getElemento()+" - ");//mostrando vertices
			aux.getDato().mostrarAdyacencias();
			aux=aux.getSiguiente();
		}
	}
	
	public void imprimirVertices() {
		NodoLista<NodoGrafo> aux = vertices.getCabeza();
		while(aux != null) {
			if(!aux.getDato().getElemento().equals(vertices.getCabeza().getDato().getElemento()))
				System.out.print(",");
			System.out.print(aux.getDato().getElemento());
			aux = aux.getSiguiente();
		}
	}
	public void imprimirAristas() {
		NodoLista<NodoGrafo> aux = vertices.getCabeza();
		while(aux != null) {
			if(!aux.getDato().getElemento().equals(vertices.getCabeza().getDato().getElemento()))
				System.out.print(",");
			if(aux.getDato().getAdyacencias().getTamanio() != 0)
				aux.getDato().formatoAristas();
			aux = aux.getSiguiente();
		}
	}

	public Comparable[][] genMatrizCostos(){
		return llenarMatriz(new Comparable[vertices.getTamanio()][vertices.getTamanio()], 0, 0, vertices.getTamanio());
	}
	private Comparable[][] llenarMatriz(Comparable[][] matriz, int i, int j, int nn){
		if(i < nn) {
			if(j <nn) {
				if(vertices.getElemento(i).esAdyacencia(vertices.getElemento(j).getElemento())) 
					matriz[i][j] = vertices.getElemento(i).getCoste((T)vertices.getElemento(j).getElemento());
				else
					matriz[i][j] = "999";
				llenarMatriz(matriz, i, j+1, nn);
			}
			if(j == nn)
				llenarMatriz(matriz, i+1, 0, nn);
			if(i == j)
				matriz[i][j] = 0;
		}
		return matriz;
	}
		
	public void eliminarArista(T verticeI, T verticeF) {
		if(aristaExiste(verticeI, verticeF)) {
			NodoGrafo<T> nodoI = retornarNodo(verticeI);
			NodoGrafo<T> nodoF = retornarNodo(verticeF);
			nodoI.delAdyacencia(nodoF);
			nodoF.delAdyacencia(nodoI);
		}else
			System.out.println("La arista entre [" + verticeI + "] y [" + verticeF + "] no existe");
	}
	public void editarPesoArista(T verticeI, T verticeF, int coste) {
		if(aristaExiste(verticeI, verticeF)) {
			NodoGrafo<T> nodoI = retornarNodo(verticeI);
			NodoGrafo<T> nodoF = retornarNodo(verticeF);
			nodoI.editCosteAdyacencia(nodoF, coste);
			nodoF.editCosteAdyacencia(nodoI, coste);
		}else
			System.out.println("La arista entre [" + verticeI + "] y [" + verticeF + "] no existe");
	}
	
	public void caminoMinimo(T verticeI, T verticeF) {
		if(casosVertices(verticeI, verticeF)) {
			List<List<String>> camino = caminoDijkstra(retornarNodo(verticeI));
			imprimirCamino(camino, verticeI, (Comparable)verticeF);
			System.out.println("\nEtiquetas finales del recorrido");
			System.out.println("Formato: [DistanciaAcumulada, VerticeAnterior, ValorVertice]");
			camino = darFormato(camino);
			System.out.println(camino);
		}
	}
	private void imprimirCamino(List<List<String>> camino, T verticeI, Comparable verticeF) {
		List<String> recorrido = new ArrayList<String>();
		int longCamino = 0;
		boolean vf = true;
		for(int i = 0; i < camino.size(); i++) {
			if(camino.get(i).get(2).equals(String.valueOf(verticeF))) {
				if(camino.get(i).get(0).equals(String.valueOf(Integer.MAX_VALUE))) {
					i = camino.size(); 
					System.out.println("\nNo existe camino, el grafo debido a que el grafo no es\n" + 
							"conexo/fuertemente conexo.");
				}else {
					recorrido.add(0, String.valueOf(verticeF));
					if(esNumero(camino.get(i).get(1))) {
						verticeF = (Comparable)Integer.parseInt(camino.get(i).get(1));
					}else
						verticeF = (Comparable)camino.get(i).get(1);
					if(vf) {
						longCamino = Integer.parseInt(camino.get(i).get(0));
						vf = false;					
					}				
					i = 0;
				}
			}
			if(verticeI.equals(verticeF)) {
				recorrido.add(0, String.valueOf(verticeI));
				i = camino.size();
			}
		}
		if(recorrido.size() > 0) {
			System.out.print("\nCamino: ");
			System.out.println(recorrido);
			System.out.println("Longitud del camino: " + longCamino);	
		}
	}
	private static List<String> visitados = new ArrayList<String>();
	public List<List<String>> caminoDijkstra(NodoGrafo verticeI) {
		visitados = new ArrayList<String>(); 
		List<List<String>> camino = iniciarCaminos(verticeI);
		NodoGrafo nAux = null;
		visitados.add(String.valueOf(verticeI.getElemento()));
		do {
			nAux = selecNodo(camino);
			if(nAux != null)
				camino = genCaminos(camino, nAux, getPeso(nAux, camino));
		}while(nAux != null);
		return camino;
	}
	private static List<List<String>> darFormato(List<List<String>> camino) {
		for(int i = 0; i < camino.size(); i++) {
			if(camino.get(i).get(0).equals(String.valueOf(Integer.MAX_VALUE))) {
				camino.get(i).set(0, "∞");
			}
		}
		return camino;
	}
	
	private NodoGrafo selecNodo(List<List<String>> camino) {
		NodoGrafo verAux = null;
		int min = Integer.MAX_VALUE, k = -1;
		for(int i = 1; i < camino.size(); i++) {
			if (Integer.parseInt(camino.get(i).get(0)) < min && !visitados.contains(camino.get(i).get(2))) {
				min = Integer.parseInt(camino.get(i).get(0));
				k = i;		
			}
		}
		if (min != Integer.MAX_VALUE) {
			if(esNumero(camino.get(k).get(2))) {
				verAux = retornarNodo(Integer.parseInt(camino.get(k).get(2)));
			}else
				verAux = retornarNodo(camino.get(k).get(2));
			visitados.add(String.valueOf(camino.get(k).get(2)));
		}
		return verAux;
	}
	@SuppressWarnings("finally")
	private static boolean esNumero(String c) {
		boolean b = true;
		c = c.replaceAll("\s", "");
		try {
			for(int i = 0; i < c.length(); i++) {
				if(!Character.isDigit(c.charAt(i)))
					b = false;
				else
					b= true;
			}
		} catch (Exception e) {
			return false;
		} finally {
			return b;
		}
	}
	private int getPeso(NodoGrafo nodo, List<List<String>> camino) {
		int peso = 0;
		for(int i = 1; i < camino.size(); i++) {
			if(camino.get(i).get(2).equals(String.valueOf(nodo.getElemento())))
				return Integer.parseInt(camino.get(i).get(0)); 
		}
		return peso;
	}
	private List<List<String>> genCaminos(List<List<String>> camino, NodoGrafo nodo, int pesoAcumulado){
		NodoLista<NodoGrafo> aux = nodo.getAdyacencias().getCabeza();
		for(int i = 1; i < camino.size(); i++) {
			while(aux != null) {
				if(camino.get(i).get(2).equals(String.valueOf(aux.getDato().getElemento()))
					&& (nodo.getCoste(aux.getDato().getElemento()) + pesoAcumulado) < Integer.parseInt(camino.get(i).get(0))) {
					camino.get(i).set(0, String.valueOf(nodo.getCoste(aux.getDato().getElemento()) + pesoAcumulado));
					camino.get(i).set(1, String.valueOf(nodo.getElemento()));
				}
				aux = aux.getSiguiente();
			}
			aux = nodo.getAdyacencias().getCabeza();
		}
		return camino;
	}
	private List<List<String>> iniciarCaminos(NodoGrafo vInicial){
		NodoLista<NodoGrafo> aux = vInicial.getAdyacencias().getCabeza();
		List<List<String>> camino = new ArrayList<List<String>>();
		camino.add(new ArrayList<String>());
		camino.get(0).add("-");
		camino.get(0).add("-");
		camino.get(0).add(String.valueOf(vInicial.getElemento()));
		int i = 1;
		while(aux != null) {
			camino.add(new ArrayList<String>());
			camino.get(i).add(String.valueOf(vInicial.getCoste(aux.getDato().getElemento())));
			camino.get(i).add(String.valueOf(vInicial.getElemento()));
			camino.get(i).add(String.valueOf(aux.getDato().getElemento()));
			i++;
			aux = aux.getSiguiente();
		}
		if(camino.size() < vertices.getTamanio()) {
			NodoLista<NodoGrafo> aux2 = vertices.getCabeza();
			while(aux2 != null) {
				if(!vInicial.esAdyacencia(aux2.getDato().getElemento()) && !vInicial.getElemento().equals(aux2.getDato().getElemento())) {
					camino.add(new ArrayList<String>());
					camino.get(i).add(String.valueOf(Integer.MAX_VALUE));
					camino.get(i).add(String.valueOf(vInicial.getElemento()));
					camino.get(i).add(String.valueOf(aux2.getDato().getElemento()));
					i++;
				}
				aux2 = aux2.getSiguiente();
			}
		}
		return camino;
	}
	
	private boolean casosVertices(T verticeI, T verticeF) {
		boolean eVi,eVf;
		eVi=verticeExiste(verticeI);
		eVf=verticeExiste(verticeF);
		if(eVi && eVf) {
			return true;
		}else {
			if(!eVi && !eVf)
				System.out.println("¡Error! No existe el vértice inicial y final. [" + verticeI + "], [" + verticeF + "]");
			else if (!eVi)
				System.out.println("¡Error! No existe el vértice inicial. [" + verticeI + "]");
			else 
				System.out.println("¡Error! No existe el vértice final. [" + verticeF + "]"); 
			return false;
		}
	}
	
	public void eliminar() {
		vertices = new Lista<NodoGrafo>();
	}
	
	public boolean esVacio() {
		return(vertices.getTamanio() == 0);
	}
	
}

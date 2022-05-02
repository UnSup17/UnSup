package Presentacion;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import Negocio.*;

@SuppressWarnings({ "rawtypes", "unchecked", "unused", "finally" })
public class Test {
	
	public static Scanner in = new Scanner(System.in);
	public static Grafo grafo = new Grafo<>();
	public static boolean dirigido = false;
	public static int tipoGrafo;
	public static void main(String[] args) throws PosicionIlegalException {
		manageMenu();
	}
	
	private static void manageMenu() throws PosicionIlegalException {
		int eleccion;
		do {
			showMenuPrincipal();
			System.out.println("Seleccione la funcion a realizar:");
			eleccion = in.nextInt();
			in.nextLine();
			switch(eleccion) {
				case 0: 
					System.out.println("\n\n*******************************************");
					System.out.println("| </Grafo Ponderado Dirigido/No Dirigido> |");
					System.out.println("*******************************************");
					break;
					
				case 1:
					if(grafo.esVacio())
						leerArchivo("./grafo.txt");
					else {
						System.out.println("El grafo ya se encuentra creado. "
								+ "Desea volver a importar los datos? Si[1]/No[0]");
						eleccion = in.nextInt();
						in.nextLine();
						if(eleccion == 1) {
							grafo.eliminar();
							leerArchivo("./grafo.txt");
						}
					}
					grafo.verticesAdyacencias();
					break;
					
				case 2:
					if(!grafo.esVacio()) {
						mostrarGrafo();
					}else
						System.out.println("Grafo vacio/nulo para el punto 2.");
					break;
					
				case 3:
					if(!grafo.esVacio()) {
						Comparable[][] mAdy = grafo.genMatrizCostos();
						System.out.println("\nMatriz de pesos del grafo:");
						mostrarYTransformarMatriz(mAdy, grafo.getVertices().getTamanio(), grafo.getVertices().getTamanio());
					}else
						System.out.println("Grafo vacio/nulo para el punto 3.");					
					break;
					
				case 4:
					if(!grafo.esVacio()) {
						System.out.println("Camino mínimo a través de Dijkstra");
						if(tipoGrafo == 0) {
							String vI, vF;
							System.out.println("Digite el valor del vértice inicial: ");
							vI = in.nextLine();
							System.out.println("Digite el valor del vértice Final: ");
							vF = in.nextLine();
							grafo.caminoMinimo(vI, vF);
						}else {
							int vI, vF;
							System.out.println("Digite el valor del vértice inicial: ");
							vI = in.nextInt();
							System.out.println("Digite el valor del vértice Final: ");
							vF = in.nextInt();
							in.nextLine();
							grafo.caminoMinimo(vI, vF);
						}
					}else
						System.out.println("Grafo vacio/nulo para el punto 4.");
					break;
					
				default:
					System.out.println("Por favor ingrese un valor permitido");
					break;
			}
			if(eleccion != 1)
				pause();
		} while (eleccion != 0);
	}
	
	private static void showMenuPrincipal() {
		System.out.println("\n\n\n|**********************************************|");
		System.out.println("|                Menu Principal                |");
		System.out.println("|**********************************************|");
		System.out.println("| 1. Importar grafo desde archivo              |");
		System.out.println("| 2. Mostrar el grafo                          |");
		System.out.println("| 3. Mostrar matriz de pesos                   |");
		System.out.println("| 4. Algoritmo Dijkstra                        |");
		System.out.println("|                                              |");
		System.out.println("| 0. Cerrar el programa                        |");
		System.out.println("|**********************************************|");
	}

	public static void leerArchivo(String ruta) {
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			archivo = new File(ruta);
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);
			String linea;
			if(esDirigido(linea = br.readLine())) {
				iniciarGrafo(linea = br.readLine());
				if((linea = br.readLine()).equals("Vertices")){
					if(genVertices(linea = br.readLine())) {
						if((linea = br.readLine()).equals("Edges")) {
							while ((linea = br.readLine()) != null)
								genAristas(linea);	
						}else
							System.out.println("Formato incorrecto (línea 5): [" + linea + "]\n"
									+ "Expecting: 'Edges'");
					}else
						System.out.println("La raíz indicada en el archivo no concuerda con el tipo de árbol\n"
								+ "o no cumple con los estándares de lectura, revise el archivo.");
				}else
					System.out.println("Formato incorrecto (línea 3): [" + linea + "]\n" 
									+ "Expecting: 'Vertices'");
			}
		} catch (Exception e) {
			System.out.println("Asegúrese de que la ruta del archivo es correcta y que el\n" +
					 		   "archivo cumpla con los requisitos para la lectura.");
		} finally {
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	private static boolean esDirigido(String linea) {
		switch(linea) {
			case "1":
				dirigido = true;
				return true;
			case "2":
				dirigido = false;
				return true;
			default:
				System.out.println("Identificador de grafo dirigido no válido,"
						+ " compruebe el archivo a importar. Linea ("+linea+")");
				return false;
		}
	}
	private static boolean iniciarGrafo(String linea) {
		switch(linea) {
			case "0": 
				tipoGrafo = 0;
				return true;
			case "1": 
				tipoGrafo = 1;
				return true;
			default:
				System.out.println("Identificador de tipo de grafo no valido,"
						+ " compruebe el archivo a importar. Linea ("+linea+")");
				return false;
		}
	}
	private static boolean genVertices(String linea) throws PosicionIlegalException {
		String[] datos = linea.split(",");
		linea = linea.replaceAll("\s", "");
		if(tipoGrafo == 0){
			for(int i = 0; i < datos.length; i++) {
				if(!esNumero(datos[i]))
					grafo.agregarVertice(datos[i]);
			}
			return true;
		}
		if(tipoGrafo == 1 && esNumero(linea)) {
			for(int i = 0; i < datos.length; i++){
				if(esNumero(datos[i]))
					grafo.agregarVertice(Integer.parseInt(datos[i]));
			}
			return true;
		}
		return false;
	}
	private static void genAristas(String linea) throws NumberFormatException, PosicionIlegalException {
		String[] datos = linea.split(",");
		linea = linea.replaceAll("\s", "");
		if(datos.length == 3) {
			String aristas = datos[0] + datos[1];
			String peso = datos[2];
			if (tipoGrafo == 0 && !esNumero(aristas) && esNumero(peso))
				grafo.agregarArista(datos[0], datos[1], Integer.parseInt(datos[2]), dirigido);
			else if (tipoGrafo == 1 && esNumero(aristas) && esNumero(peso))
				grafo.agregarArista(Integer.parseInt(datos[0]), Integer.parseInt(datos[1]), Integer.parseInt(datos[2]), dirigido);
			else
				System.out.println("Linea no aceptada para agregarse al grafo: [" + linea + "]");	
		}else
			System.out.println("La linea de datos no concuerda con el formato: [" + linea + "]");
	}
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
	
	public static void mostrarGrafo() {
		mostrarVertices();
		mostrarAristas();
	}
	private static void mostrarVertices() {
		System.out.print("V = {");
		grafo.imprimirVertices();
		System.out.println("}");
	}
	private static void mostrarAristas() {
		System.out.print("E = {");
		grafo.imprimirAristas();
		System.out.print("}\n");
	}
	
	public static void mostrarYTransformarMatriz(Comparable[][] matriz, int filas, int columnas) {
		mostrarYTransformarMatriz(matriz, 0, 0, filas, columnas);
		System.out.println("\n");
	}
	private static void mostrarYTransformarMatriz(Comparable[][] matriz, int i, int j, int filas, int columnas) {
		if (i < filas) {
			if (j < columnas) {
				String dato = String.valueOf(matriz[i][j]);
				if(dato.equals("999")) {
					dato = "∞";
				}
				System.out.print("[ " + dato + " ]" );
				mostrarYTransformarMatriz(matriz, i, j + 1, filas, columnas);
			}
			if(j == columnas) {
				System.out.println("");
				mostrarYTransformarMatriz(matriz, i + 1, 0, filas, columnas);
			}
		}
	}
	
	@SuppressWarnings("resource")
	private static void pause(){
    	System.out.println("\n\nPresione ENTER para continuar...\n\n");
        new java.util.Scanner(System.in).nextLine();   
    }
}

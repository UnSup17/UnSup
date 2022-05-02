package Negocio;

@SuppressWarnings("serial")
public class PosicionIlegalException extends Exception{
	
	public PosicionIlegalException() {
		super("Posición ileal en la lista");
	}
}
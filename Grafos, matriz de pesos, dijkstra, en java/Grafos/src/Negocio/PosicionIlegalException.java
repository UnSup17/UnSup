package Negocio;

@SuppressWarnings("serial")
public class PosicionIlegalException extends Exception{
	
	public PosicionIlegalException() {
		super("Posici�n ileal en la lista");
	}
}
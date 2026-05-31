package co.edu.udistrital.model.dtos;

/**
 * Clase de transferencia de datos que retorna el estado del caso de uso y un
 * mensaje
 *
 * @author Juan David Diaz Perez
 */
public class ResponseDTO {

	public final boolean success;
	public final String message;

	/**
	 * Constructor del DTO de respuestas a interfaz
	 * 
	 * @param success Si el caso de uso tuvo exito
	 * @param message El mensaje de error o exito de operaciones
	 */
	public ResponseDTO(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	/**
	 * @return Si el caso de uso tuvo exito
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @return El mensaje de error o exito de operaciones
	 */
	public String getMessage() {
		return message;
	}
}

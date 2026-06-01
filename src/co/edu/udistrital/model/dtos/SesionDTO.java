package co.edu.udistrital.model.dtos;

/**
 * Clase que representa el DTO de respuesta para las sesiones del sistema
 *
 * @author Juan David Diaz Perez
 */
public class SesionDTO {

	private final boolean success;
	private final String message;
	private final String role;

	/**
	 * Constructor de el DTO de respuesta frente a la sesion
	 * 
	 * @param success Si la operacion fue un exito
	 * @param message El mensaje segun el exito de la operacion
	 * @param role    El rol que representa el perfil en el sistema
	 */
	public SesionDTO(boolean success, String message, String role) {
		super();
		this.success = success;
		this.message = message;
		this.role = role;
	}

	/**
	 * @return Si la operacion fue un exito
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @return El mensaje segun el exito de la operacion
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return El rol que representa el perfil en el sistema
	 */
	public String getRole() {
		return role;
	}
}

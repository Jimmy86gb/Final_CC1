package co.edu.udistrital.model.dtos;


public class SessionDTO {

	private final boolean success;
	private final String message;
	private final String role;

	
	public SessionDTO(boolean success, String message, String role) {
		super();
		this.success = success;
		this.message = message;
		this.role = role;
	}

	
	public boolean isSuccess() {
		return success;
	}

	
	public String getMessage() {
		return message;
	}

	
	public String getRole() {
		return role;
	}
}

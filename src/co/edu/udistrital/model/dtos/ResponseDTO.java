package co.edu.udistrital.model.dtos;


public class ResponseDTO {

	public final boolean success;
	public final String message;

	
	public ResponseDTO(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	
	public boolean isSuccess() {
		return success;
	}

	
	public String getMessage() {
		return message;
	}
}

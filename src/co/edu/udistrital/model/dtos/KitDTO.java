package co.edu.udistrital.model.dtos;


public class KitDTO {

	private final String id;
	private final String type;
	private final String status;
	private final boolean isEditable;

	
	public KitDTO(String id, String type, String status, boolean isEditable) {
		this.id = id;
		this.type = type;
		this.status = status;
		this.isEditable = isEditable;
	}

	
	public String getId() {
		return id;
	}

	
	public String getType() {
		return type;
	}

	
	public String getStatus() {
		return status;
	}

	
	public boolean isEditable() {
		return isEditable;
	}
}

package co.edu.udistrital.model.dtos;


public class ServiceUnitDTO {

	private final String id;
	private final String type;
	private final String status;
	private final String zone;
	private final boolean isEditable;

	
	public ServiceUnitDTO(String id, String type, String status, String zone, boolean isEditable) {
		super();
		this.id = id;
		this.type = type;
		this.status = status;
		this.zone = zone;
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

	
	public String getZone() {
		return zone;
	}

	
	public boolean isEditable() {
		return isEditable;
	}
}

package co.edu.udistrital.model.dtos;


public class TechnicianDTO {

	private final String id;
	private final String name;
	private final String specialty;
	private final String zone;
	private final String status;
	private final boolean isEditable;

	
	public TechnicianDTO(String id, String name, String specialty, String zone, String status, boolean isEditable) {
		super();
		this.id = id;
		this.name = name;
		this.specialty = specialty;
		this.zone = zone;
		this.status = status;
		this.isEditable = isEditable;
	}

	
	public String getId() {
		return id;
	}

	
	public String getName() {
		return name;
	}

	
	public String getSpecialty() {
		return specialty;
	}

	
	public String getZone() {
		return zone;
	}

	
	public String getStatus() {
		return status;
	}

	
	public boolean isEditable() {
		return isEditable;
	}
}

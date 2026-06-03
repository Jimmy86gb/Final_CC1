package co.edu.udistrital.model.dtos;


public class ClientDTO {
	private final String id;
	private final String name;
	private final String type;
	private final String contactInfo;

	
	public ClientDTO(String id, String name, String type, String contactInfo) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.contactInfo = contactInfo;
	}

	
	public String getId() {
		return id;
	}

	
	public String getName() {
		return name;
	}

	
	public String getType() {
		return type;
	}

	
	public String getContactInfo() {
		return contactInfo;
	}
}

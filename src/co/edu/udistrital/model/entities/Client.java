package co.edu.udistrital.model.entities;
import co.edu.udistrital.model.enums.ClientType;

public class Client {
	private String id;
	private String name;
	private ClientType type;
	private String contactInfo;
	
	public Client(String id, String name, ClientType type, String contactInfo) {
		this.id=id;
		this.name=name;
		this.type=type;
		this.contactInfo=contactInfo;
	}
	
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ClientType getType() {
        return type;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
	
}

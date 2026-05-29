package co.edu.udistrital.model.entities;

import java.util.UUID;
import co.edu.udistrital.model.enums.KitType;
import co.edu.udistrital.model.enums.UnitStatus;

public class Kit {
	
	private UUID id;
	private KitType type;
	private UnitStatus status;
	
	public Kit(KitType type, UnitStatus status) {
		
		this.id = UUID.randomUUID();
		this.type=type;
		this.status=status;
	}
	
	public void setType(KitType type) {
		this.type=type;
	}
	
	public void setStatus(UnitStatus status) {
		this.status=status;
	}
	
	public UUID getId() {
		return id;
	}
	
	public KitType getType() {
		return type;
	}
	
	public UnitStatus getStatus() {
		return status;
	}
}

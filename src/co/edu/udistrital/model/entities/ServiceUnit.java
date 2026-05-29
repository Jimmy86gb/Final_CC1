package co.edu.udistrital.model.entities;

import java.util.UUID;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.enums.UnitType;

public class ServiceUnit {

	private UUID id;
	private UnitType type;
	private UnitStatus status;
	private OperationZone zone;
	
	public ServiceUnit(UnitType type, UnitStatus status, OperationZone zone) {
		this.id = UUID.randomUUID();
		this.type=type;
		this.status=status;
		this.zone=zone;
	}
	
	public void setType(UnitType type) {
		this.type=type;
	}
	
	public void setStatus(UnitStatus status) {
		this.status=status;
	}
	
	public void setZone(OperationZone zone) {
		this.zone=zone;	
	}
	
	public UUID getId() {
		return id;
	}
	
	public UnitType getType() {
		return type;
	}
	
	public UnitStatus getStatus() {
		return status;
	}
	
	public OperationZone getZone() {
		return zone;
	}
	
}

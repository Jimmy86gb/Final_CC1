package co.edu.udistrital.model.entities;

import java.util.UUID;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.enums.TechnicianStatus;

public class Technician {
	
	private UUID id;
	private String name;
	private TechnicianSpecialty specialty;
	private OperationZone zone;
	private TechnicianStatus status;
	
	public Technician(String name, TechnicianSpecialty specialty, OperationZone zone, TechnicianStatus status) {
		this.id = UUID.randomUUID();
		this.name=name;
		this.specialty=specialty;
		this.zone=zone;
		this.status=status;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public void setSpecialty(TechnicianSpecialty specialty) {
		this.specialty=specialty;
	}
	
	public void setZone(OperationZone zone) {
		this.zone=zone;
	}
	
	public void setStatus(TechnicianStatus status) {
		this.status=status;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public TechnicianSpecialty getSpecialty() {
		return specialty;
	}
	
	public OperationZone getZone() {
		return zone;
	}
	
	public TechnicianStatus getStatus() {
		return status;
	}
	
	
}

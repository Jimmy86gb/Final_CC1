package co.edu.udistrital.model.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import co.edu.udistrital.model.enums.KitType;
import co.edu.udistrital.model.enums.UnitStatus;

public class Kit implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;
	private KitType type;
	private UnitStatus status;

	public Kit(KitType type, UnitStatus status) {

		this.id = UUID.randomUUID();
		this.type = type;
		this.status = status;
	}

	public void setType(KitType type) {
		this.type = type;
	}

	public void setStatus(UnitStatus status) {
		this.status = status;
	}

	public void setId(UUID id) {
		this.id = id;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Kit kit = (Kit) obj;
		return Objects.equals(kit.id, id);
	}
}

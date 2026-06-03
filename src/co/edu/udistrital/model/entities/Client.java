package co.edu.udistrital.model.entities;

import java.io.Serializable;
import java.util.Objects;

import co.edu.udistrital.model.enums.ClientType;

/**
 * Entidad que representa a un cliente dentro del sistema.
 * Permite almacenar y gestionar sus datos de identificación, clasificación y contacto.
 * 
 * @author ChrZ
 */
public class Client implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private ClientType type;
	private String contactInfo;

	/**
	 * Constructor principal para crear una instancia de Cliente con todos sus datos.
	 * @param id El identificador único del cliente.
	 * @param name El nombre completo o razón social.
	 * @param type El tipo de cliente asignado.
	 * @param contactInfo La información de contacto.
	 */
	public Client(String id, String name, ClientType type, String contactInfo) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.contactInfo = contactInfo;
	}

	/**
	 * Obtiene el identificador único del cliente.
	 * @return El ID del cliente.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Obtiene el nombre completo del cliente.
	 * @return El nombre del cliente.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Obtiene la categoría o tipo de cliente.
	 * @return El tipo de cliente (ClientType).
	 */
	public ClientType getType() {
		return type;
	}

	/**
	 * Obtiene los datos de contacto registrados para el cliente.
	 * * @return La información de contacto.
	 */
	public String getContactInfo() {
		return contactInfo;
	}

	/**
	 * Asigna o actualiza el identificador único del cliente.
	 * * @param id El nuevo ID para el cliente.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Asigna o actualiza el nombre del cliente.
	 * * @param name El nuevo nombre.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Define o cambia el tipo de cliente en el sistema.
	 * @param type El nuevo tipo de cliente.
	 */
	public void setType(ClientType type) { 
		this.type = type;
	}

	/**
	 * Actualiza la información de contacto del cliente.
	 * @param contactInfo Los nuevos datos de contacto.
	 */
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	/**
	 * Compara este cliente con otro objeto. La igualdad se determina 
	 * exclusivamente mediante la coincidencia de sus identificadores (ID).
	 * @param obj Objeto a comparar con la instancia actual.
	 * @return true si ambos clientes tienen el mismo ID, false en caso contrario.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Client client = (Client) obj;
		return Objects.equals(client.id, id);
	}
}
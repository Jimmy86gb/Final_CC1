package co.edu.udistrital.model.entities;

import java.io.Serializable;

import co.edu.udistrital.model.enums.ProfileType;

/**
 * Clase que representa el perfil estatico dentro del sistema
 *
 * @author Juan David Diaz Perez
 */
public class Profile implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String username;
	private final String password;
	private final ProfileType profileType;

	/**
	 * Constructor de la entidad perfil que inicializa los datos en el sistema
	 * 
	 * @param username    Usuario del perfil actual
	 * @param password    Contraseña del perfil actual
	 * @param profileType Tipo del perfil actual
	 */
	public Profile(String username, String password, ProfileType profileType) {
		super();
		this.username = username;
		this.password = password;
		this.profileType = profileType;
	}

	/**
	 * @return Usuario del perfil actual
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return Contraseña del perfil actual
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return Tipo del perfil actual
	 */
	public ProfileType getProfileType() {
		return profileType;
	}

}

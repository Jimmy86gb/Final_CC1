package co.edu.udistrital.model.repositories;

import co.edu.udistrital.model.entities.Profile;
import co.edu.udistrital.model.enums.ProfileType;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

public class ProfileRepository {

	/**
	 * Lista que contiene la lista de usuarios en el sistema
	 */
	private SimpleList<Profile> usersList;

	/**
	 * Constructor que inicializa los usuarios base del sistema y la lista de
	 * usuarios
	 */
	public ProfileRepository() {
		this.usersList = new SimpleList<Profile>();
		usersList.add(new Profile("admin", "admin1234", ProfileType.ADMIN));
		usersList.add(new Profile("operario", "operario1234", ProfileType.OPERATOR));
	}

	/**
	 * Metodo que verifica si el usuario ingresado esta dentro de los casos base
	 * 
	 * @param username Usuario ingresado
	 * @param password Contraseña ingresada
	 * @return El perfil si existe dentro del sistema
	 */
	public Profile authenticate(String username, String password) {
		Iterator<Profile> iterator = usersList.iterador();

		while (iterator.hasNext()) {
			Profile current = iterator.Next();
			if (current.getUsername().equals(username) && current.getPassword().equals(password)) {
				return current;
			}
		}
		return null;
	}

	/**
	 * @return La lista guardada en la ejecucion actual
	 */
	public SimpleList<Profile> getUsersList() {
		return usersList;
	}

	/**
	 * @param usersList La lista guardada en la base de datos
	 */
	public void setUsersList(SimpleList<Profile> usersList) {
		this.usersList = usersList;
	}
}
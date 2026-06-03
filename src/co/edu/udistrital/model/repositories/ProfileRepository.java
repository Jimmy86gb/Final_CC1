package co.edu.udistrital.model.repositories;

import co.edu.udistrital.model.entities.Profile;
import co.edu.udistrital.model.enums.ProfileType;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

public class ProfileRepository {

	
	private SimpleList<Profile> usersList;

	
	public ProfileRepository() {
		this.usersList = new SimpleList<Profile>();
		usersList.add(new Profile("admin", "admin1234", ProfileType.ADMIN));
		usersList.add(new Profile("operario", "operario1234", ProfileType.OPERATOR));
	}

	
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

	
	public SimpleList<Profile> getUsersList() {
		return usersList;
	}

	
	public void setUsersList(SimpleList<Profile> usersList) {
		this.usersList = usersList;
	}
}
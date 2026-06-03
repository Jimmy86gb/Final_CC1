package co.edu.udistrital.model.entities;

import java.io.Serializable;

import co.edu.udistrital.model.enums.ProfileType;


public class Profile implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String username;
	private final String password;
	private final ProfileType profileType;

	
	public Profile(String username, String password, ProfileType profileType) {
		super();
		this.username = username;
		this.password = password;
		this.profileType = profileType;
	}

	
	public String getUsername() {
		return username;
	}

	
	public String getPassword() {
		return password;
	}

	
	public ProfileType getProfileType() {
		return profileType;
	}

}

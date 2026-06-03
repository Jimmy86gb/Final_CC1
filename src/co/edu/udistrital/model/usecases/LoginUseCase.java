package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.SessionDTO;
import co.edu.udistrital.model.entities.Profile;
import co.edu.udistrital.model.repositories.ProfileRepository;


public class LoginUseCase {

	
	private final ProfileRepository profileRepository;

	
	public LoginUseCase(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	
	public SessionDTO execute(String username, String password) {

		Profile authProfile = profileRepository.authenticate(username, password);

		if (authProfile == null) {
			return new SessionDTO(false, "Usuario no valido en el sistema", "");
		}

		String roleName = authProfile.getProfileType().name();

		return new SessionDTO(true, "Iniciando sesion", roleName);
	}
}
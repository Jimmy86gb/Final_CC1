package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.SessionDTO;
import co.edu.udistrital.model.entities.Profile;
import co.edu.udistrital.model.repositories.ProfileRepository;

/**
 * Clase que representa el caso de uso de acceder al sistema
 *
 * @author Juan David Diaz Perez
 */
public class LoginUseCase {

	/**
	 * Repositorio de memoria de los perfiles en el sistema
	 */
	private final ProfileRepository profileRepository;

	/**
	 * Constructor que inyecta el repositorio de perfiles al caso de uso
	 * 
	 * @param profileRepository Repositorio de perfiles
	 */
	public LoginUseCase(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	/**
	 * Metodo que ejecuta el login en el sistema
	 * 
	 * @return SessionDTO DTO el cual retorna si fue exitoso y el perfil del usuario
	 *         ingresado
	 */
	public SessionDTO execute(String username, String password) {

		Profile authProfile = profileRepository.authenticate(username, password);

		if (authProfile == null) {
			return new SessionDTO(false, "Usuario no valido en el sistema", "");
		}

		String roleName = authProfile.getProfileType().name();

		return new SessionDTO(true, "Iniciando sesion", roleName);
	}
}
package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.SessionDTO;
import co.edu.udistrital.model.repositories.ProfileRepository;
import co.edu.udistrital.model.usecases.LoginUseCase;
import co.edu.udistrital.view.LoginView;

/**
 * Sub-controlador encargado de gestionar el proceso de autenticacion y acceso al sistema.
 * Actua como el puente de comunicacion entre la vista de inicio de sesion (LoginView) 
 * y la logica de negocio encargada de validar las credenciales del usuario (LoginUseCase).
 * * @author Jimmy86gb
 */
public class LoginController {
	
	private final AppController appController;
	private final LoginView view;
	private final LoginUseCase loginUseCase;

	/**
	 * Constructor de la clase LoginController.
	 * Inicializa la vista de login, configura el caso de uso de autenticacion 
	 * y enlaza el evento del boton de la interfaz con la logica interna de validacion.
	 * * @param appController Controlador principal para delegar el arranque del entorno tras un acceso exitoso.
	 * @param profileRepository Repositorio que contiene los perfiles de usuario y credenciales registradas.
	 */
	public LoginController(AppController appController, ProfileRepository profileRepository) {
		this.appController = appController;
		this.view = new LoginView();
		this.loginUseCase = new LoginUseCase(profileRepository);

		this.view.setOnLoginAction(() -> handleLogin());
	}

	/**
	 * Metodo interno que procesa el intento de inicio de sesion.
	 * Captura las credenciales ingresadas en la vista y las evalua mediante el caso de uso.
	 * Dependiendo del resultado, autoriza al controlador principal a inicializar el sistema 
	 * con el rol correspondiente, o le indica a la vista que despliegue un mensaje de error.
	 */
	private void handleLogin() {
		SessionDTO session = loginUseCase.execute(view.getUsername(), view.getPassword());
		if (session.isSuccess()) {
			appController.initializeSystem(session.getRole());
		} else {
			view.showMessage(session.getMessage());
		}
	}

	/**
	 * Obtiene la vista asociada a este sub-controlador.
	 * * @return Objeto LoginView que representa la interfaz grafica de autenticacion.
	 */
	public LoginView getView() {
		return view;
	}
}
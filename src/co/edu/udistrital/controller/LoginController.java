package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.SessionDTO;
import co.edu.udistrital.model.repositories.ProfileRepository;
import co.edu.udistrital.model.usecases.LoginUseCase;
import co.edu.udistrital.view.LoginView;

public class LoginController {
	
	private final AppController appController;
	private final LoginView view;
	private final LoginUseCase loginUseCase;

	public LoginController(AppController appController, ProfileRepository profileRepository) {
		this.appController = appController;
		this.view = new LoginView();
		this.loginUseCase = new LoginUseCase(profileRepository);

		this.view.setOnLoginAction(() -> handleLogin());
	}

	private void handleLogin() {
		SessionDTO session = loginUseCase.execute(view.getUsername(), view.getPassword());
		if (session.isSuccess()) {
			appController.initializeSystem(session.getRole());
		} else {
			view.showMessage(session.getMessage());
		}
	}

	public LoginView getView() {
		return view;
	}
}
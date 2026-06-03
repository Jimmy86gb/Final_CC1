package co.edu.udistrital.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class LoginView {
	private VBox rootContainer;
	private TextField txtUsername;
	private PasswordField txtPassword;
	private Button btnLogin;
	private Label lblMessage;

	
	public LoginView() {
		rootContainer = new VBox(15);
		rootContainer.setAlignment(Pos.CENTER);
		rootContainer.setPadding(new Insets(40));
		rootContainer.setStyle("-fx-background-color: #F3F4F6;");

		Label lblTitle = new Label("AutoRescate 24/7");
		lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
		lblTitle.setStyle("-fx-text-fill: #1F2937;");

		Label lblSubtitle = new Label("Ingrese sus credenciales");
		lblSubtitle.setFont(Font.font("Segoe UI", 14));

		txtUsername = new TextField();
		txtUsername.setPromptText("Usuario (ej. operador o admin)");
		txtUsername.setMaxWidth(260);

		txtPassword = new PasswordField();
		txtPassword.setPromptText("Contrasena");
		txtPassword.setMaxWidth(260);

		btnLogin = new Button("Iniciar sesion");
		btnLogin.setPrefWidth(260);
		btnLogin.setStyle(
				"-fx-background-color:#2563EB; -fx-text-fill:white; -fx-font-weight: bold; -fx-font-size:14; -fx-cursor: hand;");

		lblMessage = new Label();
		lblMessage.setStyle("-fx-text-fill: #DC2626; -fx-font-weight: bold;");

		rootContainer.getChildren().addAll(lblTitle, lblSubtitle, txtUsername, txtPassword, btnLogin, lblMessage);
	}

	
	public Scene getScene() {
		return new Scene(rootContainer, 450, 400);
	}

	
	public void setOnLoginAction(Runnable action) {
		btnLogin.setOnAction(e -> action.run());
	}

	
	public String getUsername() {
		return txtUsername.getText();
	}

	
	public String getPassword() {
		return txtPassword.getText();
	}

	
	public void showMessage(String msg) {
		lblMessage.setText(msg);
	}
}
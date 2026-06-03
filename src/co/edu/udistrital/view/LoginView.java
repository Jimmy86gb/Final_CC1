package co.edu.udistrital.view;

import co.edu.udistrital.controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Clase encargada de la construccion y renderizado de la interfaz de autenticacion.
 * Proporciona los campos de entrada para credenciales y el disparador de inicio de sesion,
 * manteniendo una separacion clara entre los componentes visuales y la logica de control.
 * * @author Jimmy86gb
 */
public class LoginView {
    private VBox rootContainer;
    private TextField txtUsername;
    private PasswordField txtPassword;
    private Button btnLogin;
    private Label lblMessage;

    /**
     * Constructor de la clase.
     * Inicializa los contenedores (VBox), define los campos de texto, el campo de 
     * contrasena enmascarado y el boton de acceso con sus respectivos estilos CSS.
     */
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
        btnLogin.setStyle("-fx-background-color:#2563EB; -fx-text-fill:white; -fx-font-weight: bold; -fx-font-size:14; -fx-cursor: hand;");

        lblMessage = new Label();
        lblMessage.setStyle("-fx-text-fill: #DC2626; -fx-font-weight: bold;");

        rootContainer.getChildren().addAll(lblTitle, lblSubtitle, txtUsername, txtPassword, btnLogin, lblMessage);
    }

    /**
     * Crea y retorna la escena de JavaFX que contiene la interfaz de usuario.
     * * @return Objeto Scene con las dimensiones definidas para el login.
     */
    public Scene getScene() { 
        return new Scene(rootContainer, 450, 400); 
    }

    /**
     * Define la logica a ejecutar cuando el usuario presiona el boton de inicio.
     * Utiliza una interfaz funcional (Runnable) para delegar la ejecucion al controlador.
     * * @param action Logica externa (proveniente del controlador) a ejecutar en el clic.
     */
    public void setOnLoginAction(Runnable action) { 
        btnLogin.setOnAction(e -> action.run()); 
    }

    /**
     * Captura el texto ingresado en el campo de usuario.
     * * @return El nombre de usuario capturado.
     */
    public String getUsername() { 
        return txtUsername.getText(); 
    }

    /**
     * Captura la contrasena ingresada en el campo seguro.
     * * @return La contrasena capturada como String.
     */
    public String getPassword() { 
        return txtPassword.getText(); 
    }

    /**
     * Actualiza el feedback visual para el usuario en caso de error de autenticacion.
     * * @param msg El mensaje descriptivo del fallo o informacion a visualizar.
     */
    public void showMessage(String msg) { 
        lblMessage.setText(msg); 
    }
}
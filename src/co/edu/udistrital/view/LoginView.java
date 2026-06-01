package co.edu.udistrital.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Clase que define la vista de autenticacion del sistema.
 * Se encarga de construir los elementos graficos necesarios para la entrada 
 * de credenciales del usuario.
 * 
 * @author Jimmy86gb
 */
public class LoginView {
    private VBox rootContainer;
    private TextField txtUsername;
    private PasswordField txtPassword;
    private Button btnLogin;
    private Label lblMessage;

    /**
     * Constructor de la clase.
     * Se inicializan los contenedores y los controles de entrada de texto
     * junto con el boton de acceso mediante los estilos visuales definidos.
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
        txtPassword.setPromptText("Contraseña");
        txtPassword.setMaxWidth(260);

        btnLogin = new Button("Iniciar sesion");
        btnLogin.setPrefWidth(260);
        btnLogin.setStyle("-fx-background-color:#2563EB; -fx-text-fill:white; -fx-font-weight: bold; -fx-font-size:14; -fx-cursor: hand;");

        lblMessage = new Label();
        lblMessage.setStyle("-fx-text-fill: #DC2626; -fx-font-weight: bold;");

        rootContainer.getChildren().addAll(lblTitle, lblSubtitle, txtUsername, txtPassword, btnLogin, lblMessage);
    }

    /**
     * Retorna la escena de JavaFX que contiene los elementos de la interfaz.
     * 
     * @return Objeto de tipo Scene con el diseño del login.
     */
    public Scene getScene() { 
        return new Scene(rootContainer, 450, 400); 
    }

    /**
     * Define la accion que se ejecuta al presionar el boton de inicio de sesion.
     * 
     * @param action Interfaz funcional Runnable que contiene la logica de validacion.
     */
    public void setOnLoginAction(Runnable action) { 
        btnLogin.setOnAction(e -> action.run()); 
    }

    /**
     * Obtiene el nombre de usuario ingresado en el campo de texto.
     * 
     * @return String con el usuario capturado.
     */
    public String getUsername() { 
        return txtUsername.getText(); 
    }

    /**
     * Obtiene la contraseña ingresada en el campo de texto seguro.
     * 
     * @return String con la contraseña capturada.
     */
    public String getPassword() { 
        return txtPassword.getText(); 
    }

    /**
     * Muestra un mensaje de error o informacion en la etiqueta correspondiente de la interfaz.
     *
     * @param msg Texto del mensaje a visualizar.
     */
    public void showMessage(String msg) { 
        lblMessage.setText(msg); 
    }
}
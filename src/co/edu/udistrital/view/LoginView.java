package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.SessionDTO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginView {
    private final VBox root;

    public LoginView(AppController controller) {
        root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        Label title = new Label("Ingreso - AutoRescate 24/7");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField txtUser = new TextField();
        txtUser.setPromptText("Usuario (ej: admin / operario)");

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Contraseña");

        Button btnLogin = new Button("Iniciar Sesión");
        Label lblMsg = new Label();

        btnLogin.setOnAction(e -> {
            SessionDTO res = controller.loginUseCase.execute(txtUser.getText(), txtPass.getText());
            if (res.isSuccess()) {
                controller.loginSuccess(res.getRole());
            } else {
                lblMsg.setText(res.getMessage());
                lblMsg.setStyle("-fx-text-fill: red;");
            }
        });

        root.getChildren().addAll(title, txtUser, txtPass, btnLogin, lblMsg);
    }

    public VBox getView() { return root; }
}
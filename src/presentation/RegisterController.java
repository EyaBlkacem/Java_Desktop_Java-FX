package presentation;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import metier.AuthService;

public class RegisterController {

    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    private AuthService authService = new AuthService();
    @FXML
    public void goToLogin() {
        try {
            // Charger la page de login
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/presentation/LoginView.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            javafx.stage.Stage stage = (javafx.stage.Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Impossible de charger la page de login ❌");
        }
    }


    @FXML
    public void register() {

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            messageLabel.setText("Les mots de passe ne correspondent pas ❌");
            return;
        }

        try {
            authService.register(
           	usernameField.getText(),
                emailField.getText(),
                passwordField.getText()
            );
            messageLabel.setText("Inscription réussie ✅");
        } catch (Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }
}

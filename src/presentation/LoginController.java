package presentation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import metier.AuthService;
import metier.Session;
import entities.User;

import java.util.Optional;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label msgLabel;

    private AuthService authService = new AuthService();

    @FXML
    public void login() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if(email.isEmpty() || password.isEmpty()) {
            msgLabel.setText("Veuillez remplir tous les champs ❌");
            return;
        }

        Optional<User> user = authService.login(email, password);

        if(user.isPresent()) {
            Session.setUser(user.get());
            msgLabel.setText("Connexion réussie ✅");

            try {
                Stage stage = (Stage) emailField.getScene().getWindow();
                FXMLLoader loader;

                // 🔹 Redirection selon rôle
                if(user.get().getRole().equalsIgnoreCase("ADMIN")) {
                    loader = new FXMLLoader(getClass().getResource("/presentation/AdminDashboard.fxml"));
                } else {
                    loader = new FXMLLoader(getClass().getResource("/presentation/SearchView.fxml"));
                }

                Scene scene = new Scene(loader.load());
                stage.setScene(scene);

            } catch (Exception e) {
                msgLabel.setText("Erreur lors de l'ouverture de l'accueil ❌");
                e.printStackTrace();
            }

        } else {
            msgLabel.setText("Email ou mot de passe incorrect ❌");
        }
    }

    @FXML
    public void goToRegister() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentation/RegisterView.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (Exception e) {
            msgLabel.setText("Erreur lors de l'ouverture de l'inscription ❌");
            e.printStackTrace();
        }
    }
}

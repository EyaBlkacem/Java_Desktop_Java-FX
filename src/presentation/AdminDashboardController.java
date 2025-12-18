package presentation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

public class AdminDashboardController {

    @FXML private StackPane contentPane;

    @FXML
    public void showFlights() throws Exception {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(FXMLLoader.load(getClass().getResource("/presentation/AdminFlights.fxml")));
    }

    @FXML
    public void showReservations() throws Exception {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(FXMLLoader.load(getClass().getResource("/presentation/AdminReservations.fxml")));
    }

    @FXML
    public void showUsers() throws Exception {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(FXMLLoader.load(getClass().getResource("/presentation/AdminUsers.fxml")));
    }

    @FXML
    public void showStats() throws Exception {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(FXMLLoader.load(getClass().getResource("/presentation/AdminStats.fxml")));
    }
}

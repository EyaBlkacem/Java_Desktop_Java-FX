package presentation;

import entities.Flight;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import metier.FlightService;
import metier.WishlistService;

import metier.Session;

import java.util.Optional;

public class SearchController {

    @FXML private Label welcomeLabel, msgLabel;
    @FXML private TextField depField, destField;
    @FXML private ListView<Flight> flightList;

    private final FlightService service = new FlightService();
    private final WishlistService wishlistService = new WishlistService();


    @FXML
    public void initialize() {

        if (Session.getUser() != null) {
            welcomeLabel.setText("Welcome " + Session.getUser().getUsername());
        }

        // 🎨 AFFICHAGE JOLI
        flightList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Flight f, boolean empty) {
                super.updateItem(f, empty);
                if (empty || f == null) {
                    setText(null);
                } else {
                    setText(
                        "✈ " + f.getAirline()
                        + " | " + f.getDeparture() + " → " + f.getDestination()
                        + " | DT" + f.getPrice()
                    );
                }
            }
        });

        // 🔥 CHARGEMENT AUTOMATIQUE DE TOUS LES VOLS
        loadAllFlights();
    }

    // 🔍 RECHERCHE
    @FXML
    public void search() {

        flightList.getItems().setAll(
                service.search(
                        depField.getText(),
                        destField.getText()
                )
        );

        if (flightList.getItems().isEmpty()) {
            msgLabel.setText("Aucun vol trouvé ❌");
        } else {
            msgLabel.setText("Vols disponibles ✈️");
        }
    }
    @FXML
    public void openHistory() throws Exception {
        Stage stage = (Stage) flightList.getScene().getWindow();
        stage.setScene(new Scene(
            FXMLLoader.load(getClass().getResource("/presentation/HistoryView.fxml"))
        ));
    }

    // 💺 CHOISIR SIÈGE
    @FXML
    public void chooseSeats() {

        try {
            Flight selected = Optional
                    .ofNullable(flightList.getSelectionModel().getSelectedItem())
                    .orElseThrow(() ->
                            new RuntimeException("Veuillez sélectionner un vol ❌")
                    );

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/presentation/SeatView.fxml")
            );

            Scene scene = new Scene(loader.load());

            SeatController controller = loader.getController();
            controller.setFlight(selected);

            Stage stage = (Stage) flightList.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception e) {
            msgLabel.setText(e.getMessage());
        }
    }

    // 🔥 TOUS LES VOLS
    public void loadAllFlights() {

        flightList.getItems().setAll(
                service.search("", "")
        );

        msgLabel.setText("");
    }
    @FXML
    public void addToWishlist() {

        try {
            Flight f = flightList.getSelectionModel().getSelectedItem();
            if (f == null)
                throw new RuntimeException("Sélectionnez un vol ❌");

           wishlistService.add(Session.getUser().getId(), f.getId());
            msgLabel.setText("Ajouté à My Reservations ❤️");

        } catch (Exception e) {
            msgLabel.setText(e.getMessage());
        }
    }

}
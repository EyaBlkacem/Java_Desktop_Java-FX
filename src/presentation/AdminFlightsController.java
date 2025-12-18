package presentation;

import entities.Flight;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import metier.FlightService;
import metier.Session;

import java.util.Optional;

public class AdminFlightsController {

    @FXML private TableView<Flight> flightTable;
    @FXML private TableColumn<Flight, Integer> idCol;
    @FXML private TableColumn<Flight, String> airlineCol;
    @FXML private TableColumn<Flight, String> flightNoCol;
    @FXML private TableColumn<Flight, String> departureCol;
    @FXML private TableColumn<Flight, String> destinationCol;
    @FXML private TableColumn<Flight, String> departTimeCol;
    @FXML private TableColumn<Flight, String> arriveTimeCol;
    @FXML private TableColumn<Flight, String> durationCol;
    @FXML private TableColumn<Flight, Double> priceCol;

    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private FlightService flightService = new FlightService();
    private ObservableList<Flight> flights;

    @FXML
    public void initialize() {
        // 🔹 Vérifier rôle ADMIN
        boolean isAdmin = Session.isAdmin();
        addButton.setDisable(!isAdmin);
        editButton.setDisable(!isAdmin);
        deleteButton.setDisable(!isAdmin);

        // 🔹 Configurer colonnes
        idCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        airlineCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAirline()));
        flightNoCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getFlightNo()));
        departureCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getDeparture()));
        destinationCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getDestination()));
        departTimeCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getDepartTime()));
        arriveTimeCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getArriveTime()));
        durationCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getDuration()));
        priceCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getPrice()));

        loadFlights();
    }

    private void loadFlights() {
        flights = FXCollections.observableArrayList(flightService.getAllFlights());
        flightTable.setItems(flights);
    }

    @FXML
    public void addFlight() {
        Flight flight = showFlightDialog(null);
        if(flight != null) {
            try {
                flightService.addFlight(flight);
                loadFlights();
            } catch (RuntimeException e) {
                showAlert("Erreur", e.getMessage());
            }
        }
    }

    @FXML
    public void editFlight() {
        Flight selected = flightTable.getSelectionModel().getSelectedItem();
        if(selected != null) {
            Flight updated = showFlightDialog(selected);
            if(updated != null) {
                try {
                    flightService.updateFlight(updated);
                    loadFlights();
                } catch (RuntimeException e) {
                    showAlert("Erreur", e.getMessage());
                }
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un vol à modifier !");
        }
    }

    @FXML
    public void deleteFlight() {
        Flight selected = flightTable.getSelectionModel().getSelectedItem();
        if(selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText(null);
            confirm.setContentText("Voulez-vous vraiment supprimer ce vol ?");
            Optional<ButtonType> result = confirm.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    flightService.deleteFlight(selected.getId());
                    flights.remove(selected);
                } catch (RuntimeException e) {
                    showAlert("Erreur", e.getMessage());
                }
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un vol à supprimer !");
        }
    }

    private Flight showFlightDialog(Flight flight) {
        Dialog<Flight> dialog = new Dialog<>();
        dialog.setTitle(flight == null ? "Ajouter un vol" : "Modifier le vol");

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        TextField airlineField = new TextField(flight != null ? flight.getAirline() : "");
        TextField flightNoField = new TextField(flight != null ? flight.getFlightNo() : "");
        TextField departureField = new TextField(flight != null ? flight.getDeparture() : "");
        TextField destinationField = new TextField(flight != null ? flight.getDestination() : "");
        TextField departTimeField = new TextField(flight != null ? flight.getDepartTime() : "");
        TextField arriveTimeField = new TextField(flight != null ? flight.getArriveTime() : "");
        TextField durationField = new TextField(flight != null ? flight.getDuration() : "");
        TextField priceField = new TextField(flight != null ? String.valueOf(flight.getPrice()) : "");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Airline:"), airlineField);
        grid.addRow(1, new Label("Flight No:"), flightNoField);
        grid.addRow(2, new Label("Departure:"), departureField);
        grid.addRow(3, new Label("Destination:"), destinationField);
        grid.addRow(4, new Label("Depart Time:"), departTimeField);
        grid.addRow(5, new Label("Arrive Time:"), arriveTimeField);
        grid.addRow(6, new Label("Duration:"), durationField);
        grid.addRow(7, new Label("Price:"), priceField);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if(button == okButton) {
                try {
                    double price = Double.parseDouble(priceField.getText().trim());
                    if(flight == null) {
                        return new Flight(0, airlineField.getText(), flightNoField.getText(), departureField.getText(),
                                destinationField.getText(), departTimeField.getText(), arriveTimeField.getText(),
                                durationField.getText(), price);
                    } else {
                        return new Flight(flight.getId(), airlineField.getText(), flightNoField.getText(),
                                departureField.getText(), destinationField.getText(), departTimeField.getText(),
                                arriveTimeField.getText(), durationField.getText(), price);
                    }
                } catch(NumberFormatException e) {
                    showAlert("Erreur", "Le prix doit être un nombre !");
                    return null;
                }
            }
            return null;
        });

        Optional<Flight> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

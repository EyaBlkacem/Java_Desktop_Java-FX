

package presentation;



import javafx.fxml.FXML;

import javafx.scene.control.*;

import metier.FlightService;

import metier.Session;

import entities.Flight;

import entities.User;



public class FlightController {



    @FXML

    private TextField departureField;



    @FXML

    private TextField destinationField;



    @FXML

    private ListView<Flight> flightList;



    @FXML

    private Label messageLabel;



    @FXML

    private Label welcomeLabel;



    private final FlightService flightService = new FlightService();



    // 🔹 Appelé automatiquement après chargement de la vue

    @FXML

    public void initialize() {



        if (Session.getUser() != null) {

            welcomeLabel.setText(

                "Bienvenue " + Session.getUser().getUsername()

            );

        }

    }





   



    // 🔍 Recherche des vols

    @FXML

    public void search() {



        flightList.getItems().setAll(

                flightService.search(

                        departureField.getText().trim(),

                        destinationField.getText().trim()

                )

        );



        if (flightList.getItems().isEmpty()) {

            messageLabel.setText("Aucun vol disponible ❌");

        } else {

            messageLabel.setText("Vols disponibles ✈️");

        }

    }



    // ✈️ Réserver un vol

    @FXML

    public void reserve() {



        Flight selectedFlight =

                flightList.getSelectionModel().getSelectedItem();



        if (selectedFlight == null) {

            messageLabel.setText("Veuillez sélectionner un vol ❌");

            return;

        }



        try {

            User user = Session.getUser();



            if (user == null) {

                messageLabel.setText("Veuillez vous connecter ❌");

                return;

            }



            flightService.reserveFlight(

                    user.getId(),

                    selectedFlight.getId()

            );



            messageLabel.setText("Réservation confirmée ✅");



            // 🔄 Rafraîchir la liste

            search();



        } catch (Exception e) {

            messageLabel.setText(e.getMessage());

        }

    }

}
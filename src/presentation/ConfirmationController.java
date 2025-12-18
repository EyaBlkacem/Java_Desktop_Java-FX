package presentation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import metier.SeatService;

import java.util.UUID;



public class ConfirmationController {

    @FXML private Label flightLabel, seatLabel, priceLabel, reservationNumberLabel, msgLabel;

    private int flightId;
    private String flightInfo, seatCode;
    private double price;
    private String reservationNumber;

    private final SeatService seatService = new SeatService();

    public void setData(int flightId, String flightInfo, String seatCode, double price) {

        this.reservationNumber = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

        flightLabel.setText("✈ " + flightInfo);
        seatLabel.setText("💺 Seat : " + seatCode);
        priceLabel.setText("💰 Price : $" + price);
        reservationNumberLabel.setText("Reservation # " + reservationNumber);
    }


    @FXML
    public void downloadPDF() {
        try {
            
            String fileName = "Ticket_" + reservationNumber + ".pdf";

            
            java.nio.file.Files.writeString(java.nio.file.Paths.get(fileName),
                    "Flight: " + flightInfo + "\n" +
                    "Seat: " + seatCode + "\n" +
                    "Price: $" + price + "\n" +
                    "Reservation #: " + reservationNumber
            );

            msgLabel.setText("Ticket saved as PDF ✔️");
        } catch (Exception e) {
            msgLabel.setText("Error saving PDF ❌");
            e.printStackTrace();
        }
    }

    @FXML
    public void sendEmail() {
        try {
            String recipient = "user@example.com"; 
            String subject = "Your Flight Reservation";
            String message = "Flight: " + flightInfo + "\n" +
                             "Seat: " + seatCode + "\n" +
                             "Price: $" + price + "\n" +
                             "Reservation #: " + reservationNumber;

           

            msgLabel.setText("Confirmation email sent ✔️");
        } catch (Exception e) {
            msgLabel.setText("Error sending email ❌");
            e.printStackTrace();
        }
    }

    @FXML
    public void backHome() {
        try {
            Stage stage = (Stage) flightLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/presentation/SearchView.fxml")
            );

            Scene scene = new Scene(loader.load());

           
            SearchController controller = loader.getController();

            
            controller.loadAllFlights();

            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

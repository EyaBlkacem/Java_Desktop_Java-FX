package presentation;

import entities.Flight;
import entities.Seat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import metier.SeatService;

import java.util.Map;

public class SeatController {

    @FXML private GridPane seatGrid;
    @FXML private Label flightLabel;
    @FXML private Label selectedLabel;
    @FXML private Label msgLabel;

    private Flight flight;
    private String selectedSeat;

    private final SeatService service = new SeatService();
    private Map<String, Seat> seatMap;

    // ===================== SET FLIGHT =====================
    public void setFlight(Flight f) {
        this.flight = f;

        flightLabel.setText(
            f.getAirline() + " " +
            f.getDeparture() + " → " +
            f.getDestination() + "  $" +
            f.getPrice()
        );

        service.ensureSeatsExist(f.getId());
        loadSeats();
    }

    // ===================== LOAD SEATS =====================
    private void loadSeats() {
        seatGrid.getChildren().clear();
        seatMap = service.seatMap(flight.getId());

        for (int r = 0; r < 8; r++) {
            char rowChar = (char) ('A' + r);

            for (int c = 1; c <= 6; c++) {
                String code = rowChar + "" + c;
                Seat seat = seatMap.get(code);

                Button b = new Button(code);
                b.setMinSize(50, 35);

                if (seat != null && "AVAILABLE".equals(seat.getStatus())) {
                    b.setStyle("-fx-background-color:#4facfe; -fx-text-fill:white;");
                    b.setOnAction(e -> {
                        selectedSeat = code;
                        selectedLabel.setText("Selected: " + selectedSeat);
                        msgLabel.setText("");
                    });
                } else {
                    b.setStyle("-fx-background-color:#bdc3c7; -fx-text-fill:black;");
                    b.setDisable(true);
                }

                seatGrid.add(b, c - 1, r);
            }
        }
    }

    // ===================== CONFIRM =====================
    @FXML
    public void confirm() {
        try {
            if (selectedSeat == null) {
                msgLabel.setText("Choose a seat first ❗");
                return;
            }

            Stage stage = (Stage) seatGrid.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/presentation/PaymentView.fxml")
            );
            Scene scene = new Scene(loader.load());

            PaymentController controller = loader.getController();
            controller.setData(flight.getId(), flight, selectedSeat);

            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
            msgLabel.setText("Erreur ❌");
        }
    }

    // ===================== BACK (🔥 FIX ICI 🔥) =====================
    @FXML
    public void back() {
        try {
            Stage stage = (Stage) seatGrid.getScene().getWindow();
            stage.setScene(
                new Scene(
                    FXMLLoader.load(
                        getClass().getResource("/presentation/SearchView.fxml")
                    )
                )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
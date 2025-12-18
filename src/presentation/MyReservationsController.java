package presentation;

import entities.WishlistItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import metier.Session;
import metier.WishlistService;

public class MyReservationsController {

    @FXML
    private ListView<WishlistItem> reservationList;

    private final WishlistService service = new WishlistService();

    @FXML
    public void initialize() {

        if (Session.getUser() != null) {
            reservationList.getItems().setAll(
                    service.findByUser(Session.getUser().getId())
            );
        }
    }

    // ❌ Supprimer de la wishlist
    @FXML
    public void delete() {

        WishlistItem item =
                reservationList.getSelectionModel().getSelectedItem();

        if (item == null) return;

        service.delete(item.getId());
        reservationList.getItems().remove(item);
    }

    // 🔙 Retour recherche
    @FXML
    public void back() throws Exception {

        Stage stage = (Stage) reservationList.getScene().getWindow();
        stage.setScene(new Scene(
                FXMLLoader.load(
                        getClass().getResource("/presentation/SearchView.fxml")
                )
        ));
    }
}

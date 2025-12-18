package presentation;



import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;

import javafx.stage.Stage;



public class WelcomeController {

    @FXML

    public void go() {

        try {

            Stage stage = (Stage) javafx.stage.Stage.getWindows().filtered(w -> w.isShowing()).get(0);

            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/presentation/SearchView.fxml"))));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
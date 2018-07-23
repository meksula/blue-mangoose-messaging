package com.meksula.chat.client.model.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * @Author
 * Karol Meksuła
 * 15-07-2018
 * */

public class Alerts {

    public void registrationSuccessfullAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Rejestracja");
        alert.setContentText("Zarejestrowałeś konto w komunikatorze.\n" +
                "Sprawdź swojego maila i potwierdź rejestrację.");
        alert.showAndWait();
    }

    public void registrationFailAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Coś poszło nie tak...");
        alert.setContentText(text);
        alert.showAndWait();
    }

    public boolean contactAdded(String text) {
        boolean decission = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Dodaj konntakt");
        alert.setHeaderText("Czy na pewno chcesz dodać " + text + "\na do swoich kontaktów?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            decission = true;
        }

        return decission;
    }

    public void emptySearchResult() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Brak kontaktów");
        alert.setHeaderText("Przykro mi. Nie znalazłem żadnych pasujących kontaktów/");
        alert.showAndWait();
    }

    public void loginFailed() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd logowania");
        alert.setHeaderText("Logowanie się nie powiodło. Spróbuj jeszcze raz.");
        alert.showAndWait();
    }
}

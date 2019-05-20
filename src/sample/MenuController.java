package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MenuController {
  public static Stage menuStage = new Stage();

  @FXML
  private void playClick() throws IOException {
    menuStage.setTitle("Difficulty");
    // Parent parent = FXMLLoader.load(getClass().getResource("game.fxml"));
    // menuStage.setScene(new Scene(parent));
    System.out.println("Play!");
  }

  @FXML
  private void creditClick() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Credit");
    alert.setHeaderText(null);
    alert.setContentText("Author: Eryk Krupa");
    alert.showAndWait();
  }

  @FXML
  private void exitClick() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Exit");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure, that you want to exit?");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      System.exit(0);
    }
  }
}

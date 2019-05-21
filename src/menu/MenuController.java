package menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.util.Optional;

import static main.Main.stage;

public class MenuController {

  public MenuController() {
    System.out.println("Menu");
    stage.setTitle("Menu");
  }

  @FXML
  private void playClick() throws IOException {
    Parent parent = new FXMLLoader().load(getClass().getResource("../game/game.fxml").openStream());
    stage.setScene(new Scene(parent, 740, 550));
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

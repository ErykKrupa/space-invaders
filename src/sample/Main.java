package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static sample.MenuController.menuStage;

public class Main extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
    menuStage.setTitle("Space Invaders");
    menuStage.setScene(new Scene(root, 740, 500));
    menuStage.setResizable(false);
    menuStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

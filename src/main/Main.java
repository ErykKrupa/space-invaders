package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
  public static Stage stage;

  @Override
  public void start(Stage primaryStage) throws Exception {
    stage = primaryStage;
    stage.setResizable(false);

    Parent parent = new FXMLLoader().load(getClass().getResource("../menu/menu.fxml").openStream());
    stage.setScene(new Scene(parent, 740, 550));
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

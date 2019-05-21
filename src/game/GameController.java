package game;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Random;

import static main.Main.stage;

public class GameController {
  private double[] ship1 =
      new double[] {
        -25.0, 20.0, -17.0, -5.0, -15.0, 10.0, -15.0, 5.0, 0.0, -40.0, 15.0, 5.0, 15.0, 10.0, 17.0,
        -5.0, 25.0, 20.0
      };
  private double[] ship2 =
      new double[] {
        -25.0, 20.0, -12.0, -30.0, -5.0, 5.0, 0.0, -5.0, 5.0, 5.0, 12.0, -30.0, 25.0, 20.0
      };
  private double[] enemy1 =
      new double[] {
        -5.0, 10.0, -10.0, 30.0, -10.0, 10.0, -20.0, 0.0, -10.0, -10.0, 10.0, -10.0, 20.0, 0.0,
        10.0, 10.0, 10.0, 30.0, 5.0, 10.0
      };
  private double[] enemy2 =
      new double[] {
        -10.0, 10.0, -15.0, 30.0, -15.0, 5.0, -20.0, 0.0, -15.0, -5.0, -15.0, -30.0, -10.0, -10.0,
        10.0, -10.0, 15.0, -30.0, 15.0, -5.0, 20.0, 0.0, 15.0, 5.0, 15.0, 30.0, 10.0, 10.0
      };
  private Random random = new Random();

  public GameController() {
    System.out.println("Game");
    stage.setTitle("Space Invaders");
    Pane pane = new Pane();
    pane.setPrefSize(1100, 720);
    pane.setBackground(
        new Background(new BackgroundFill(Color.web("#000000"), CornerRadii.EMPTY, Insets.EMPTY)));
    Polygon[][] enemies = new Polygon[8][16];
    for (int i = 0; i < 7; i++) {
      for (int j = 0; j < 15; j++) {
        enemies[i][j] = new Polygon(random.nextBoolean() ? enemy1 : enemy2);
        switch (random.nextInt(4)) {
          case 0:
            enemies[i][j].setFill(Color.web("#800000"));
            break;
          case 1:
            enemies[i][j].setFill(Color.web("#808000"));
            break;
          case 2:
            enemies[i][j].setFill(Color.web("#008000"));
            break;
          case 3:
            enemies[i][j].setFill(Color.web("#FF00FF"));
            break;
        }
        enemies[i][j].setLayoutX(40 + j * 68);
        enemies[i][j].setLayoutY(40 + i * 70);
        pane.getChildren().add(enemies[i][j]);
      }
    }
    Polygon ship = new Polygon(random.nextBoolean() ? ship1 : ship2);
    ship.setFill(Color.web("#05719d"));
    ship.setLayoutX(400);
    ship.setLayoutY(650);
    pane.getChildren().add(ship);
    Scene scene = new Scene(pane, 1100, 720);
    stage.setScene(scene);
  }
}

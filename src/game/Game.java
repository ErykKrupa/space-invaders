package game;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static main.Main.stage;

public class Game implements Runnable {
  private Pane pane;
  private Polygon ship;
  private Direction shipDirection = Direction.NONE;
  private List<Circle> shipShots = new LinkedList<>();
  private int shipShotTimer = 30;
  private int gas;
  private List<LinkedList<Polygon>> enemies;
  private Direction enemiesDirection = Direction.RIGHT;
  private List<Circle> enemiesShots = new LinkedList<>();
  private int enemiesDirectionTimer = 180;
  private Boolean running = false;
  private Random random = new Random();
  private static Game instance = null;

  public static void createInstance() {
    if (instance == null) {
      instance = new Game();
    }
  }

  private static void destroyInstance() {
    instance = null;
  }

  private Game() {
    System.out.println("Game");
    stage.setTitle("Space Invaders");
    pane = new Pane();
    pane.setPrefSize(1100, 720);
    pane.setBackground(
        new Background(new BackgroundFill(Color.web("#000000"), CornerRadii.EMPTY, Insets.EMPTY)));
    enemies = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      enemies.add(new LinkedList<>());
      for (int j = 0; j < 15; j++) {
        double[] enemyPattern1 =
            new double[] {
              -5.0, 10.0, -10.0, 30.0, -10.0, 10.0, -20.0, 0.0, -10.0, -10.0, 10.0, -10.0, 20.0,
              0.0, 10.0, 10.0, 10.0, 30.0, 5.0, 10.0
            };
        double[] enemyPattern2 =
            new double[] {
              -10.0, 10.0, -15.0, 30.0, -15.0, 5.0, -20.0, 0.0, -15.0, -5.0, -15.0, -30.0, -10.0,
              -10.0, 10.0, -10.0, 15.0, -30.0, 15.0, -5.0, 20.0, 0.0, 15.0, 5.0, 15.0, 30.0, 10.0,
              10.0
            };
        enemies.get(i).add(new Polygon(random.nextBoolean() ? enemyPattern1 : enemyPattern2));
        switch (random.nextInt(4)) {
          case 0:
            enemies.get(i).get(j).setFill(Color.web("#800000"));
            break;
          case 1:
            enemies.get(i).get(j).setFill(Color.web("#808000"));
            break;
          case 2:
            enemies.get(i).get(j).setFill(Color.web("#008000"));
            break;
          case 3:
            enemies.get(i).get(j).setFill(Color.web("#FF00FF"));
            break;
        }
        enemies.get(i).get(j).setLayoutX(25 + j * 68);
        enemies.get(i).get(j).setLayoutY(40 + i * 70);
        pane.getChildren().add(enemies.get(i).get(j));
      }
    }
    double[] shipPattern1 =
        new double[] {
          -25.0, 20.0, -20.0, -5.0, -15.0, 10.0, -10.0, 5.0, 0.0, -40.0, 10.0, 5.0, 15.0, 10.0,
          20.0, -5.0, 25.0, 20.0, 0.0, 15.0
        };
    double[] shipPattern2 =
        new double[] {
          -25.0, 20.0, -12.0, -30.0, -5.0, 5.0, 0.0, -10.0, 5.0, 5.0, 12.0, -30.0, 25.0, 20.0, 0.0,
          15.0
        };
    ship = new Polygon(random.nextBoolean() ? shipPattern1 : shipPattern2);
    ship.setFill(Color.web("#05719d"));
    ship.setLayoutX(400);
    ship.setLayoutY(680);
    pane.getChildren().add(ship);
    Scene scene = new Scene(pane, 1100, 720);
    stage.setScene(scene);
    KeyCombination keyLeft = new KeyCodeCombination(KeyCode.LEFT);
    KeyCombination keyRight = new KeyCodeCombination(KeyCode.RIGHT);
    KeyCombination keySpace = new KeyCodeCombination(KeyCode.SPACE);
    Runnable runnableLeft = () -> setShipDirection(Direction.LEFT);
    Runnable runnableRight = () -> setShipDirection(Direction.RIGHT);
    Runnable runnableSpace = this::playerShot;
    scene.getAccelerators().put(keyLeft, runnableLeft);
    scene.getAccelerators().put(keyRight, runnableRight);
    scene.getAccelerators().put(keySpace, runnableSpace);
    Thread thread = new Thread(this);
    thread.setDaemon(true);
    thread.start();
  }

  enum Direction {
    LEFT,
    RIGHT,
    NONE
  }

  private void setShipDirection(Direction shipDirection) {
    if (this.shipDirection != shipDirection) {
      gas = 25;
    } else if (gas < 5) {
      gas = 5;
    }
    this.shipDirection = shipDirection;
  }

  private void playerShot() {
    if (shipShotTimer <= 0) {
      Circle shot = new Circle(ship.getLayoutX(), ship.getLayoutY(), 5, Color.web("#32CD32"));
      pane.getChildren().add(shot);
      shipShots.add(shot);
      shipShotTimer = 30;
    }
  }

  @Override
  public void run() {
    running = true;
    long startTime;
    long elapsedTime;
    long waitTime;
    long targetTime = 16_666_667;
    while (running) {
      startTime = System.nanoTime();
      Platform.runLater(
          () -> {
            if (running) {
              moveEnemies();
              moveShip();
              moveShots();
              checkCollisions();
              loadCannon();
            }
          });
      elapsedTime = System.nanoTime() - startTime;
      waitTime = targetTime - elapsedTime;
      try {
        if (waitTime > 0) {
          Thread.sleep(TimeUnit.NANOSECONDS.toMillis(waitTime));
        }
      } catch (InterruptedException ignored) {
      }
    }
  }

  private void moveEnemies() {
    if (enemiesDirectionTimer <= 0) {
      enemiesDirection = enemiesDirection == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT;
      for (LinkedList<Polygon> enemiesRow : enemies) {
        for (Polygon enemy : enemiesRow) {
          enemy.setLayoutY(enemy.getLayoutY() + 5);
        }
      }
      enemiesDirectionTimer = 180;
    }
    enemiesDirectionTimer--;
    for (LinkedList<Polygon> enemiesRow : enemies) {
      for (Polygon enemy : enemiesRow) {
        switch (enemiesDirection) {
          case LEFT:
            enemy.setLayoutX(enemy.getLayoutX() - 0.5);
            break;
          case RIGHT:
            enemy.setLayoutX(enemy.getLayoutX() + 0.5);
            break;
        }
        if (random.nextDouble() < 0.0015) {
          enemyShot(enemy);
        }
      }
    }
  }

  private void enemyShot(Polygon enemy) {
    Circle shot = new Circle(enemy.getLayoutX(), enemy.getLayoutY(), 5, Color.web("#ff0000"));
    pane.getChildren().add(shot);
    enemiesShots.add(shot);
  }

  private void moveShip() {
    switch (shipDirection) {
      case LEFT:
        if (ship.getLayoutX() > 25) {
          ship.setLayoutX(ship.getLayoutX() - 5);
        }
        break;
      case RIGHT:
        if (ship.getLayoutX() < 1075) {
          ship.setLayoutX(ship.getLayoutX() + 5);
        }
        break;
    }
    if (gas == 0) {
      shipDirection = Direction.NONE;
    } else {
      gas--;
    }
  }

  private void moveShots() {
    Iterator<Circle> shotIterator = shipShots.iterator();
    while (shotIterator.hasNext()) {
      Circle shot = shotIterator.next();
      if (shot.getCenterY() < -20) {
        shotIterator.remove();
      } else {
        shot.setCenterY(shot.getCenterY() - 5);
      }
    }
    shotIterator = enemiesShots.iterator();
    while (shotIterator.hasNext()) {
      Circle shot = shotIterator.next();
      if (shot.getCenterY() > 740) {
        shotIterator.remove();
      } else {
        shot.setCenterY(shot.getCenterY() + 5);
      }
    }
  }

  private void checkCollisions() {
    Iterator<Circle> shotIterator = shipShots.iterator();
    while (shotIterator.hasNext()) {
      Circle shot = shotIterator.next();
      for (int i = 0; i < enemies.size(); i++) {
        for (int j = 0; j < enemies.get(i).size(); j++) {
          Polygon enemy = enemies.get(i).get(j);
          if (Math.abs(shot.getCenterX() - enemy.getLayoutX()) < 20
              && Math.abs(shot.getCenterY() - enemy.getLayoutY()) < 20) {
            enemy.setOpacity(0.0);
            enemies.get(i).remove(j);
            shot.setOpacity(0.0);
            shotIterator.remove();
            if (enemies.get(i).size() == 0) {
              enemies.remove(i);
              if (enemies.size() == 0) {
                running = false;
                createAndShowAlert("You win!", "You have decimated all enemies!");
                backToMenu();
                destroyInstance();
              }
            }
            break;
          }
        }
      }
    }
    shotIterator = enemiesShots.iterator();
    while (shotIterator.hasNext()) {
      Circle shot = shotIterator.next();
      if (Math.abs(shot.getCenterX() - ship.getLayoutX()) < 22
          && Math.abs(shot.getCenterY() - ship.getLayoutY()) < 22) {
        running = false;
        createAndShowAlert("Game Over", "Your ship has been destroyed!");
        backToMenu();
        destroyInstance();
      }
    }
  }

  private void createAndShowAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }

  private void backToMenu() {
    Parent parent;
    try {
      parent = new FXMLLoader().load(getClass().getResource("../menu/menu.fxml").openStream());
      stage.setScene(new Scene(parent, 740, 550));
    } catch (IOException ignored) {
    }
  }

  private void loadCannon() {
    if (shipShotTimer > 0) {
      shipShotTimer--;
    }
  }
}

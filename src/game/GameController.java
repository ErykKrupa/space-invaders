package game;

import java.awt.*;

import static main.Main.stage;

public class GameController {
  private Polygon ship1 =
      new Polygon(
          new int[] {-25, -17, -15, -15, 0, 15, 15, 17, 25},
          new int[] {20, -5, 10, 5, -40, 5, 10, -5, 20},
          9);
  private Polygon ship2 =
      new Polygon(
          new int[] {-25, -12, -5, 0, 5, 12, 25}, new int[] {20, -30, 5, -5, 5, -30, 20}, 7);
  private Polygon enemy1 =
      new Polygon(
          new int[] {-5, -10, -10, -20, -10, 10, 20, 10, 10, 5},
          new int[] {10, 30, 10, 0, -10, -10, 0, 10, 30, 10},
          10);
  private Polygon enemy2 =
      new Polygon(
          new int[] {-10, -15, -15, -20, -15, -15, -10, 10, 15, 15, 20, 15, 15, 30},
          new int[] {10, 30, 5, 0, -5, -30, -10, -10, -30, -5, 0, 5, 10, 10},
          14);

  public GameController() {
    System.out.println("Game");
    stage.setTitle("Space Invaders");
  }
}

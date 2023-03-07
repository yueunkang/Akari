package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.model.CellType;
import com.comp301.a09akari.model.Model;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class PuzzleView implements FXComponent {
  private final ClassicMvcController _controller;
  private final Model _model;

  // display clues & game board inside GridPanel.
  public PuzzleView(ClassicMvcController controller, Model model) {
    _controller = controller;
    _model = model;
  }

  @Override
  public Parent render() throws IOException {
    GridPane board = new GridPane();
    board.setHgap(10);
    board.setVgap(10);
    int x = 50;

    // lit or not here
    // ControlView -->
    // set up row x column board with corridor, clue, wall cells

    for (int i = 0; i < _model.getActivePuzzle().getHeight(); i++) {
      for (int j = 0; j < _model.getActivePuzzle().getWidth(); j++) {
        AtomicBoolean light = new AtomicBoolean(false);
        if (_model.getActivePuzzle().getCellType(i, j) == CellType.CLUE) {

          StackPane stp = new StackPane();
          Rectangle rectangle = new Rectangle(x, x);
          // rectangle.setStroke(Color.BLACK);
          Text text = new Text("" + _model.getActivePuzzle().getClue(i, j));
          text.getStyleClass().add("white-text");
          text.setFill(Color.WHITE);
          rectangle.setFill(Color.BLACK);
          stp.getChildren().addAll(rectangle, text);
          if (_model.isClueSatisfied(i, j)) {
            rectangle.setFill(Color.rgb(73, 196, 106));
          }

          board.add(stp, j, i);
          // cell.getStyleClass().add("clue");
          // cell.getStyleClass().add("clue");

        } else if (_model.getActivePuzzle().getCellType(i, j) == CellType.WALL) {
          Rectangle cell = new Rectangle(x, x);
          cell.setFill(Color.BLACK);
          // cell.setStroke(Color.BLACK);

          // cell.getStyleClass().add("clue");
          board.add(cell, j, i);
          // cell.getStyleClass().add("wall");

        } else if (_model.getActivePuzzle().getCellType(i, j) == CellType.CORRIDOR) {
          Rectangle cell = new Rectangle(x, x);
          cell.setFill(Color.rgb(230, 230, 230));
          // cell.setStroke(Color.BLACK);
          int finalI = i;
          int finalJ = j;
          boolean notisLamp = true;

          if (_model.isLamp(i, j) && _model.isLampIllegal(i, j)) {
            Image img = new Image("Illegal.png");
            cell.setFill(new ImagePattern(img));
            notisLamp = false;
          } else if (_model.isLamp(i, j)) {
            Image img = new Image("light-bulb.png");
            cell.setFill(new ImagePattern(img));
            notisLamp = false;
          }

          if (_model.isLit(i, j) && notisLamp) {
            cell.setFill(Color.rgb(252, 204, 51));
          }

          cell.setOnMouseClicked(
              mouseEvent -> {
                if (!_model.isLamp(finalI, finalJ)) {
                  light.set(true);

                  // cell.setStyle(".image");
                  // cell.getStyleClass().add("image");
                  try {
                    _model.addLamp(finalI, finalJ);

                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  }

                } else {
                  light.set(false);
                  // set button to white
                  try {
                    _model.removeLamp(finalI, finalJ);
                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  }

                  cell.setFill(Color.WHITE);
                }
              });

          board.add(cell, j, i);
        }
      }
    }
    return board;
  }
}

package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.model.Model;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.IOException;

public class ControlView implements FXComponent {
  private final ClassicMvcController _controller;
  private final Model _model;
  /*
  display puzzle controls, including buttons,
  to move through puzzle library.
  */

  public ControlView(ClassicMvcController controller, Model model) {
    _controller = controller;
    _model = model;
    // do same thing for controllView and MessageView
  }

  @Override
  public Parent render() {

    HBox layout = new HBox(5);

    Button prevButton = new Button("Previous");
    prevButton.getStyleClass().add("prevButton");
    prevButton.setOnAction(
        (ActionEvent event) -> {
          try {
            _controller.clickPrevPuzzle();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
    layout.getChildren().add(prevButton);

    Button nextButton = new Button("Next");
    nextButton.getStyleClass().add("nextButton");
    nextButton.setOnAction(
        (ActionEvent event) -> {
          try {
            _controller.clickNextPuzzle();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
    layout.getChildren().add(nextButton);

    Button randButton = new Button("Random");
    randButton.getStyleClass().add("randButton");

    randButton.setOnAction(
        (ActionEvent event) -> {
          try {
            int before = _model.getActivePuzzleIndex();
            _controller.clickRandPuzzle();
            int after = _model.getActivePuzzleIndex();

            while (before == after) {
              before = _model.getActivePuzzleIndex();
              _controller.clickRandPuzzle();
              after = _model.getActivePuzzleIndex();
            }

          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
    layout.getChildren().add(randButton);

    Button resetButton = new Button("Reset");
    resetButton.getStyleClass().add("reset");
    resetButton.setOnAction(
        (ActionEvent event) -> {
          try {
            _controller.clickResetPuzzle();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
    layout.getChildren().add(resetButton);

    HBox.setHgrow(resetButton, Priority.ALWAYS);

    HBox labelBox = new HBox(5);
    layout.getChildren().add(labelBox);

    // display how many puzzles are in the library
    int num = _model.getActivePuzzleIndex() + 1;
    Label numOfPuzzle = new Label(num + "/" + _model.getPuzzleLibrarySize());
    numOfPuzzle.getStyleClass().add("numOfPuzzle");
    labelBox.getChildren().add(numOfPuzzle);

    return layout;
  }
}

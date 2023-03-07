package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.model.Model;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MessageView implements FXComponent {
  private final ClassicMvcController _controller;
  private final Model _model;
  /*
  May show "success" message when successfully finishes puzzle.
   */

  public MessageView(ClassicMvcController controller, Model model) {
    _controller = controller;
    _model = model;
  }

  @Override
  public Parent render() {
    StackPane layout = new StackPane();
    if (_model.isSolved()) {
      int puzzle = _model.getActivePuzzleIndex() + 1;
      Label instructions = new Label("You solved number " + puzzle + " puzzle! Congrats!");
      instructions.getStyleClass().add("instructions");
      instructions.setAlignment(Pos.CENTER);
      layout.getChildren().add(instructions);
    }
    return layout;
  }
}

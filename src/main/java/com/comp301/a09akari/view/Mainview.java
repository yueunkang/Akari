package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.ModelObserver;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Mainview implements FXComponent, ModelObserver {
  private final FXComponent puzzleView;
  private final FXComponent messageView;
  private final FXComponent controlView;

  private final Scene _scene;

  public Mainview(ClassicMvcController controller, Model model) throws IOException {
    model.addObserver(this);
    // creating sub Views.
    puzzleView = new PuzzleView(controller, model);
    messageView = new MessageView(controller, model);
    controlView = new ControlView(controller, model);
    _scene = new Scene(this.render());
  }

  public Scene getScene() {
    return _scene;
  }

  @Override
  public Parent render() throws IOException {
    Pane layout = new VBox();

    // pane.setTop and .setBottom
    layout.getChildren().add(puzzleView.render());
    layout.getChildren().add(messageView.render());
    layout.getChildren().add(controlView.render());

    return layout;
  }

  @Override
  public void update(Model model) throws IOException {
    _scene.setRoot(render());
  }
}

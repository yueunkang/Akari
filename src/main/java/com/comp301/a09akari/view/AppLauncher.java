package com.comp301.a09akari.view;

import com.comp301.a09akari.SamplePuzzles;
import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.controller.ControllerImpl;
import com.comp301.a09akari.model.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class AppLauncher extends Application {
  // launching point of application

  @Override
  public void start(Stage stage) throws IOException {

    // TODO: Create your Model, View, and Controller instances and launch your GUI

    /*
    - should create Model instance & Controller instance
    - use puzzle data in SamplePuzzles & PuzzleLibraryImpl to populate
    suitable PuzzleLirary to be passed in ModelImpl.
    - Add all puzzles from SamplePuzzles to PuzzleLibrary.
    - Three FXComponent objects (ControlView, MessageView, PuzzleView)
    instantiated & composed together in start() method.
    - Must re-render every time, value in model changed.
    So, view must register an active ModelObserver to observer model instance.
    Use lambda expression in start() method as this is where I have reference to model.
     */
    // Set the Stage title

    // Model & Controller
    PuzzleLibrary puzzleLibrary = new PuzzleLibraryImpl();
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_01));
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_02));
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_03));
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_04));
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_05));

    Model model = new ModelImpl(puzzleLibrary);
    ClassicMvcController controller = new ControllerImpl(model);
    // add all puzzles

    // View
    Mainview view = new Mainview(controller, model);

    // Make scene
    // scene.getStylesheets().add("main.css");
    view.getScene().getStylesheets().add("main.css");
    stage.setScene(view.getScene());
    stage.sizeToScene();

    // Refresh view when model changes
    model.addObserver(
        (Model m) -> {
          stage.sizeToScene();
        });

    stage.setTitle("Akari Game");
    stage.show();
  }
}

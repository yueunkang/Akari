package com.comp301.a09akari.view;

import javafx.scene.Parent;

import java.io.IOException;

public interface FXComponent {
  /** Render the component and return the resulting Parent object */
  Parent render() throws IOException;
  /*
  - can use FXComponent to represent a compound UI component.
  - Can make PuzzleView, ControlView, MessageView implementing FXComponent.
  - non-static factory method which constructs compound component's UI tree
  - Should instantiate & return new JavaFX "Parent" object --
  up to date scene graph for that section of UI.
  - Each time render() called on FXComponent, Parent re-built from scratch,
  using controller
  - Each FXComponent class must encapsulate reference to Controller or Model
  (my case Model)
  - When model change occurs, render() methods called on each FXComponent instance
  & clear old UI components.
   */
}

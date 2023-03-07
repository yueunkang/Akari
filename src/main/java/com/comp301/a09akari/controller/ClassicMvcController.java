package com.comp301.a09akari.controller;

import java.io.IOException;

public interface ClassicMvcController {
  /** Handles the click action to go to the next puzzle */
  void clickNextPuzzle() throws IOException;

  /** Handles the click action to go to the previous puzzle */
  void clickPrevPuzzle() throws IOException;

  /** Handles the click action to go to a random puzzle */
  void clickRandPuzzle() throws IOException;

  /** Handles the click action to reset the currently active puzzle */
  void clickResetPuzzle() throws IOException;

  /** Handles the click event on the cell at row r, column c */
  void clickCell(int r, int c) throws IOException;
}

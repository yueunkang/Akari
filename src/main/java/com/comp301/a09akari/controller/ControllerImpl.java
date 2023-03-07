package com.comp301.a09akari.controller;

import com.comp301.a09akari.model.Model;

import java.io.IOException;

public class ControllerImpl implements ClassicMvcController {
  private final Model _model;
  private final int _maxIndex;
  private int current_index;

  public ControllerImpl(Model model) {
    // Constructor code goes here
    _model = model;
    current_index = 0;
    _maxIndex = _model.getPuzzleLibrarySize() - 1;
  }

  @Override
  public void clickNextPuzzle() throws IOException {
    if (current_index >= _maxIndex) {
      current_index = _maxIndex;
    } else {
      current_index++;
    }
    _model.setActivePuzzleIndex(current_index);
  }

  @Override
  public void clickPrevPuzzle() throws IOException {
    // need code
    if (current_index <= 0) {
      current_index = 0;
    } else {
      current_index--;
    }
    _model.setActivePuzzleIndex(current_index);
  }

  @Override
  public void clickRandPuzzle() throws IOException {
    // need code
    int rand = (int) (Math.random() * _model.getPuzzleLibrarySize()); // includes 0 and 9
    current_index = rand;
    _model.setActivePuzzleIndex(rand);
  }

  @Override
  public void clickResetPuzzle() throws IOException {
    _model.resetPuzzle();
  }

  @Override
  public void clickCell(int r, int c) throws IOException {
    if (_model.isLamp(r, c)) {
      _model.removeLamp(r, c);
    } else {
      _model.addLamp(r, c);
    }
  }
}

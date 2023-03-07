package com.comp301.a09akari.model;

public class PuzzleImpl implements Puzzle {

  int[][] _board;

  // first: row
  // second: column
  public PuzzleImpl(int[][] board) {
    // Your constructor code here
    if (board == null) {
      throw new IllegalArgumentException("board is null");
    }
    _board = board;
  }

  @Override
  public int getWidth() {
    // number of columns it has
    return _board[0].length;
  }

  @Override
  public int getHeight() {
    // number of rows it has
    return _board.length;
  }

  @Override
  public CellType getCellType(int r, int c) {
    if (r >= getHeight() || c >= getWidth()) {
      throw new IndexOutOfBoundsException("r and/or c out of bounds!");
    }

    switch (_board[r][c]) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
        return CellType.CLUE;
      case 5:
        return CellType.WALL;
      case 6:
        return CellType.CORRIDOR;
    }
    return CellType.CORRIDOR;
  }

  @Override
  public int getClue(int r, int c) {
    if (r >= getHeight() || c >= getWidth()) {
      throw new IndexOutOfBoundsException("r and/or c out of bounds!");
    }
    if (getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException("used getClue when not a clue cell");
    }

    return _board[r][c];
  }
}

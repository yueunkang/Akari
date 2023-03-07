package com.comp301.a09akari.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  /*
   ** ModelImpl is "subject" of observer design pattern.
   ** View class will register 1 or more active observers of the model
   *  and re-render itself when Model changes.
   * Must notify actibe abservers when any Model filed value is changed
   * 4/14 (excluding addObserver() & removeObserver)
   */

  private final PuzzleLibrary _library;
  Puzzle _activepuzzle;
  int _activepuzzleindex;
  int[][] _lamplocation;
  /*
  CORRIDOR:
   lamp: 1
   no lamp: 0 (by default) -- including corridor, wall, clue
   lit: 2

   clue: 8
   wall: 9
   */
  List<ModelObserver> _observers;

  public ModelImpl(PuzzleLibrary library) {
    // Your constructor code here
    _library = library;
    _activepuzzleindex = 0;
    _activepuzzle = library.getPuzzle(_activepuzzleindex);

    _observers = new ArrayList<>();

    /**
     * My logic: 0: not lit (corridor only) 1: LAMP (corridor only) 2: lit (corridor only)
     *
     * <p>8: clue 9: wall
     */
    _lamplocation = new int[_activepuzzle.getHeight()][_activepuzzle.getWidth()];
    // setting up _lamplocation 2-dim integer array
    for (int row = 0; row < _activepuzzle.getHeight(); row++) {
      for (int column = 0; column < _activepuzzle.getWidth(); column++) {
        if (_activepuzzle.getCellType(row, column) == CellType.WALL) {
          _lamplocation[row][column] = 9;
        } else if (_activepuzzle.getCellType(row, column) == CellType.CLUE) {
          _lamplocation[row][column] = 8;
        } else if (_activepuzzle.getCellType(row, column) == CellType.CORRIDOR) {
          _lamplocation[row][column] = 0;
        }
      }
    }
  }

  private void lamplocationreset() {
    for (int row = 0; row < _activepuzzle.getHeight(); row++) {
      for (int column = 0; column < _activepuzzle.getWidth(); column++) {
        if (_activepuzzle.getCellType(row, column) == CellType.WALL) {
          _lamplocation[row][column] = 9;
        } else if (_activepuzzle.getCellType(row, column) == CellType.CLUE) {
          _lamplocation[row][column] = 8;
        }
      }
    }
  }

  @Override
  public void addLamp(int r, int c) throws IOException {
    // exception if r or c is out of bounds
    // getWidth --> # of columns
    // getHeight --> # of rows
    lamplocationreset();
    if (r >= _activepuzzle.getHeight() || c >= _activepuzzle.getWidth() || r < 0 || c < 0) {
      throw new IndexOutOfBoundsException("addLamp - r and/or c out of bounds");
    }
    // exception if cell is not type CORRIDOR
    if (_activepuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("addLamp - cell not corridor");
    }

    /* if I can place the lamp (isLampIlegal is false):
    check:
    * doesn't override Wall and Clue cells (8, 9)
    * when encountering wall and clue cell, don't lit the cell behind it.
     */

    _lamplocation[r][c] = 1;

    // move left (down&up , left&right)
    for (int left = c - 1; left >= 0; left--) {
      // if clue(8) or wall(9)
      if (_lamplocation[r][left] == 8 || _lamplocation[r][left] == 9) {
        // if blocked by clue or wall stop litting that and what comes after.
        break;
      } else if (_lamplocation[r][left] == 1) {
        // skip
      } else {
        // lit if not blocked by clue or wall
        _lamplocation[r][left] = 2;
      }
    }

    // move right (down&up , left&right)
    for (int right = c + 1; right < _activepuzzle.getWidth(); right++) {
      // if clue(8) or wall(9)
      if (_lamplocation[r][right] == 8 || _lamplocation[r][right] == 9) {
        // if blocked by clue or wall stop litting that and what comes after.

        break;
      } else if (_lamplocation[r][right] == 1) {
        // skip
      } else {
        // lit if not blocked by clue or wall

        _lamplocation[r][right] = 2;
      }
    }

    // move up (down&up , left&right)
    for (int up = r - 1; up >= 0; up--) {
      // if clue(8) or wall(9)
      if (_lamplocation[up][c] == 8 || _lamplocation[up][c] == 9) {
        // if blocked by clue or wall stop litting that and what comes after.
        break;
      } else if (_lamplocation[up][c] == 1) {
        // skip
      } else {
        // lit if not blocked by clue or wall
        _lamplocation[up][c] = 2;
      }
    }

    // move down (down&up , left&right)
    for (int down = r + 1; down < _activepuzzle.getHeight(); down++) {
      // if clue(8) or wall(9)
      if (_lamplocation[down][c] == 8 || _lamplocation[down][c] == 9) {
        // if blocked by clue or wall stop litting that and what comes after.
        break;
      } else if (_lamplocation[down][c] == 1) {
        // skip
      } else {
        // lit if not blocked by clue or wall

        _lamplocation[down][c] = 2;
      }
    }

    notifyObservers();
  }

  private boolean lampInRow(int r, int c) {
    lamplocationreset();

    // check left
    for (int left = c - 1; left >= 0; left--) {
      if (_lamplocation[r][left] == 8 || _lamplocation[r][left] == 9) {
        // blocked by clue or wall cell
        break;
      } else if (_lamplocation[r][left] == 1) {
        return true;
      }
    }
    // check right
    for (int right = c + 1; right < _activepuzzle.getWidth(); right++) {
      if (_lamplocation[r][right] == 8 || _lamplocation[r][right] == 9) {
        break;
      } else if (_lamplocation[r][right] == 1) {
        return true;
      }
    }
    // if there is no blockage AND there is no lamp
    return false;
  }

  private boolean lampInColumn(int r, int c) {
    lamplocationreset();
    if (_activepuzzle.getCellType(r, c) == CellType.CLUE) {}

    // check up
    for (int up = r - 1; up >= 0; up--) {
      if (_lamplocation[up][c] == 8 || _lamplocation[up][c] == 9) {
        // blocked by clue or wall cell
        break;
      } else if (_lamplocation[up][c] == 1) {
        return true;
      }
    }
    // check down
    for (int down = r + 1; down < _activepuzzle.getHeight(); down++) {
      if (_lamplocation[down][c] == 8 || _lamplocation[down][c] == 9) {
        break;
      } else if (_lamplocation[down][c] == 1) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void removeLamp(int r, int c) throws IOException {
    // 1) check if if it is illegal lamp --> if it is, make that cell still lit
    // 2) check top & botton cells. if to the right and left of that cell there is
    // lamp, don't turn off. If there is not, turn off.
    // 3) check left & right. if to the top and botton of that cell there is lamp,
    // don't turn off. it there is not, turn off.

    lamplocationreset();

    if (isLampIllegal(r, c)) {
      _lamplocation[r][c] = 2;
    } else {
      _lamplocation[r][c] = 0;
    }

    // 2: top cell. check if there is lamp to the left or right of the cell that is not blocked
    // by wall or clue cell.

    for (int up = r - 1; up >= 0; up--) {
      if (_lamplocation[up][c] == 1) {
        _lamplocation[up][c] = 1;
      } else if (lampInRow(up, c) || lampInColumn(up, c)) {
        _lamplocation[up][c] = 2;
      } else {
        _lamplocation[up][c] = 0;
      }
    }

    for (int down = r + 1; down < _activepuzzle.getHeight(); down++) {
      if (_lamplocation[down][c] == 1) {
        _lamplocation[down][c] = 1;
      } else if (lampInRow(down, c) || lampInColumn(down, c)) {
        _lamplocation[down][c] = 2;
      } else {
        _lamplocation[down][c] = 0;
      }
    }

    // 3

    for (int left = c - 1; left >= 0; left--) {
      if (_lamplocation[r][left] == 1) {
        _lamplocation[r][left] = 1;
      } else if (lampInColumn(r, left) || lampInRow(r, left)) {
        _lamplocation[r][left] = 2;
      } else {
        _lamplocation[r][left] = 0;
      }
    }

    for (int right = c + 1; right < _activepuzzle.getWidth(); right++) {
      if (_lamplocation[r][right] == 1) {
        _lamplocation[r][right] = 1;
      } else if (lampInColumn(r, right) || lampInRow(r, right)) {
        _lamplocation[r][right] = 2;

      } else {
        _lamplocation[r][right] = 0;
      }
    }

    notifyObservers();
  }

  @Override
  public boolean isLit(int r, int c) {
    if (r >= _activepuzzle.getHeight() || c >= _activepuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("isLit: r and/or c out of bounds");
    }
    if (_activepuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("isLit: cell not corridor");
    }
    return _lamplocation[r][c] == 1 || _lamplocation[r][c] == 2;
  }

  @Override
  public boolean isLamp(int r, int c) {
    if (r >= _activepuzzle.getHeight() || c >= _activepuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("isLamp: r and/or c out of bounds");
    }
    if (_activepuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("isLamp: cell not corridor");
    }

    return _lamplocation[r][c] == 1;
  }

  @Override
  public boolean isLampIllegal(int r, int c) {

    if (r >= _activepuzzle.getHeight() || c >= _activepuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("isLampIllegal: r and/or c out of bounds");
    }

    // there is no lamp placed in that cell
    // or give IllegalArgumentException if clue or wall cell
    if (!isLamp(r, c)) {
      throw new IllegalArgumentException("isLampIllegal: cell doesn't contain lamp");
    }

    // check: if row: down & up, column: left & right contains lamp

    // move left (down&up , left&right)
    for (int left = c - 1; left >= 0; left--) {
      // if clue(8) or wall(9)
      if (_lamplocation[r][left] == 8 || _lamplocation[r][left] == 9) {
        // if blocked by clue or wall stop litting that and what comes after.
        break;
      } else if (_lamplocation[r][left] == 1) {

        return true;
      }
    }

    // move right (down&up , left&right)
    for (int right = c + 1; right < _activepuzzle.getWidth(); right++) {
      // if clue(8) or wall(9)
      if (_lamplocation[r][right] == 8 || _lamplocation[r][right] == 9) {
        // if blocked by clue or wall stop litting that and what comes after.
        break;
      } else if (_lamplocation[r][right] == 1) {

        return true;
      }
    }

    // move up (down&up , left&right)
    for (int up = r - 1; up >= 0; up--) {
      // if clue(8) or wall(9)
      if (_lamplocation[up][c] == 8 || _lamplocation[up][c] == 9) {
        // if blocked by clue or wall stop litting that and what comes after.
        break;
      } else if (_lamplocation[up][c] == 1) {
        return true;
      }
    }

    // move down (down&up , left&right)
    for (int down = r + 1; down < _activepuzzle.getHeight(); down++) {
      // if clue(8) or wall(9)
      if (_lamplocation[down][c] == 8 || _lamplocation[down][c] == 9) {
        // if blocked by clue or wall stop litting that and what comes after.
        break;
      } else if (_lamplocation[down][c] == 1) {
        return true;
      }
    }

    // isLegal --> isLampIllegal returns false
    return false;
  }

  @Override
  public Puzzle getActivePuzzle() {
    return _activepuzzle;
  }

  @Override
  public int getActivePuzzleIndex() {
    return _activepuzzleindex;
  }

  @Override
  public void setActivePuzzleIndex(int index) throws IOException {
    if (index < 0 || index >= getPuzzleLibrarySize()) {
      throw new IndexOutOfBoundsException("setActivePuzzleIndex: index out of bounds");
    }

    _activepuzzleindex = index;
    _activepuzzle = _library.getPuzzle(index);

    _lamplocation = new int[_activepuzzle.getHeight()][_activepuzzle.getWidth()];
    // setting up _lamplocation 2-dim integer array
    for (int row = 0; row < _activepuzzle.getHeight(); row++) {
      for (int column = 0; column < _activepuzzle.getWidth(); column++) {
        if (_activepuzzle.getCellType(row, column) == CellType.WALL) {
          _lamplocation[row][column] = 9;
        } else if (_activepuzzle.getCellType(row, column) == CellType.CLUE) {
          _lamplocation[row][column] = 8;
        } else if (_activepuzzle.getCellType(row, column) == CellType.CORRIDOR) {
          _lamplocation[row][column] = 0;
        }
      }
    }

    notifyObservers();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return _library.size();
  }

  @Override
  public void resetPuzzle() throws IOException {
    for (int row = 0; row < _activepuzzle.getHeight(); row++) {
      for (int column = 0; column < _activepuzzle.getWidth(); column++) {
        if (_activepuzzle.getCellType(row, column) == CellType.WALL) {
          _lamplocation[row][column] = 9;
        } else if (_activepuzzle.getCellType(row, column) == CellType.CLUE) {
          _lamplocation[row][column] = 8;
        } else {
          _lamplocation[row][column] = 0;
        }
      }
    }
    notifyObservers();
  }

  @Override
  public boolean isSolved() {
    /* check
    // 1. all corridors in the board are lit (2 or 1)
    if 1(lamp) check isLampIlegal is false
    if 8(clue) check is cluesatisfied is true
    if 9(wall) skip
    check no cells are 0(not lit) if 0 any cell immediately return false.

    // 2. all clues are satisfied
    // 3. no lamps are illegally placed
     */
    for (int roww = 0; roww < _activepuzzle.getHeight(); roww++) {
      for (int columnn = 0; columnn < _activepuzzle.getWidth(); columnn++) {
        // not Lit immediately returns false

        if (_lamplocation[roww][columnn] == 1) {}
      }
    }

    for (int row = 0; row < _activepuzzle.getHeight(); row++) {
      for (int column = 0; column < _activepuzzle.getWidth(); column++) {
        // not Lit immediately returns false

        // System.out.println("lit locations");
        // if (_lamplocation[row][column] == 2) {
        // System.out.println("row: " + row + ";column: " + column);
        // }

        if (_lamplocation[row][column] == 0) {
          return false;
          // if lamp(1)
        }
        if (_lamplocation[row][column] == 1) {
          if (isLampIllegal(row, column)) {
            return false;
          }
          // if clue cell(8)
        }
        if (_lamplocation[row][column] == 8) {
          if (!(isClueSatisfied(row, column))) {
            return false;
          }
        }
      }
    }
    return true;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    int count = 0;
    if (_activepuzzle.getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException("isClueSatisfied: not clue cell");
    }

    if (r >= _activepuzzle.getHeight() || c >= _activepuzzle.getWidth() || r < 0 || c < 0) {
      throw new IndexOutOfBoundsException("isClueSatisfied: out of bounds");
    }
    if (r + 1 < _activepuzzle.getHeight()) {
      if (_lamplocation[r + 1][c] == 1) {
        count++;
      }
    }

    if (r - 1 >= 0) {
      if (_lamplocation[r - 1][c] == 1) {
        count++;
      }
    }

    if (c + 1 < _activepuzzle.getWidth()) {
      if (_lamplocation[r][c + 1] == 1) {
        count++;
      }
    }

    if (c - 1 >= 0) {
      if (_lamplocation[r][c - 1] == 1) {
        count++;
      }
    }

    return count == _activepuzzle.getClue(r, c);
  }

  @Override
  public void addObserver(ModelObserver observer) {
    _observers.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    _observers.remove(observer);
  }

  private void notifyObservers() throws IOException {
    for (ModelObserver observer : _observers) {
      observer.update(this);
    }
  }
}

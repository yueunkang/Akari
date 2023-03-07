package com.comp301.a09akari.model;

import java.io.IOException;

public interface ModelObserver {
  /** When a model value is changed, the model calls update() on all active ModelObserver objects */
  void update(Model model) throws IOException;
  /*
  don't need to implement. but may want to implement it in View class
  so view can respond to model changes.
   */
}

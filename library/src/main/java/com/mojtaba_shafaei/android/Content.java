package com.mojtaba_shafaei.android;

import com.mojtaba_shafaei.android.LovMultiSelect.Item;
import java.util.List;

class Content {

  private List<? extends Item> dataSet;
  private List<String> selectedTags;

  public Content(List<? extends Item> dataSet,List<String> selectedTags) {
    this.dataSet = dataSet;
    this.selectedTags = selectedTags;
  }

  public List<? extends Item> getDataSet() {
    return dataSet;
  }

  public List<String> getSelectedTags() {
    return selectedTags;
  }
}

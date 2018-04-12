package com.mojtaba_shafaei.android;

import com.mojtaba_shafaei.android.LovMultiSelect.Item;
import java.util.List;

public class ContentDataSetAndQueryText {

  private List<Item> items;
  private String query;

  public ContentDataSetAndQueryText(List<Item> items, String query) {
    this.items = items;
    this.query = query;
  }

  public List<Item> getList() {
    return items;
  }

  public String getQuery() {
    return query;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }
}

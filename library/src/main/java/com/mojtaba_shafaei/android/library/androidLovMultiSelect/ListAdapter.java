package com.mojtaba_shafaei.android.library.androidLovMultiSelect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.mojtaba_shafaei.android.library.androidLovMultiSelect.LovMultiSelect.Item;
import java.util.LinkedList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListHolder> {

  private final List<Item> data = new LinkedList<>();
  private final LayoutInflater inflater;
  private final OnListItemClickListener onListItemClickListener;

  public ListAdapter(Context context, OnListItemClickListener onListItemClickListener) {
    inflater = LayoutInflater.from(context);
    this.onListItemClickListener = onListItemClickListener;
  }

  @Override
  public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ListHolder(inflater.inflate(R.layout.lov_multi_select_list_item, parent, false));
  }

  @Override
  public void onBindViewHolder(ListHolder holder, int _position) {
    final int position = holder.getAdapterPosition();
    Item item = data.get(position);
    holder.initWith(data.get(position), (view, checked) -> {
      item.setChecked(checked);
      data.set(position, item);

      onListItemClickListener.onListItemClicked(position, item);
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public void setData(List<? extends Item> data) {
    this.data.clear();
    this.data.addAll(data);
    notifyDataSetChanged();
  }

  public List<Item> getSelectedItems() {
    List<Item> items = new LinkedList<>();
    for (Item i : data) {
      if (i.isChecked()) {
        items.add(i);
      }
    }
    return items;
  }

  public void unCheckTag(String des) {
    int len = data.size();
    for (int i = 0; i < len; i++) {
      Item item = data.get(i);
      if (item.getDes().equals(des)) {
        item.setChecked(false);
        data.set(i, item);
        notifyItemChanged(i);
        break;
      }
    }
  }

  public void chooseItem(Item item) {
    int position = -1;
    int len = data.size();
    for (int i = 0; i < len; i++) {
      if (data.get(i).getDes().equals(item.getDes())) {
        position = i;
        break;
      }
    }

    item.setChecked(true);
    data.set(position, item);
    notifyDataSetChanged();
  }

  public void chooseItems(List<? extends Item> selectedItems) {
    if (selectedItems == null || selectedItems.size() == 0) {
      return;
    }

    int len = data.size();
    for (int i = 0; i < len; i++) {
      Item item0 = data.get(i);
      item0.setChecked(false);
      data.set(i, item0);
    }

    for (int i = 0; i < len; i++) {
      Item item0 = data.get(i);

      for (Item item : selectedItems) {
        if (item0.getDes().equals(item.getDes())) {
          item0.setChecked(true);
          data.set(i, item0);
        }
      }
    }
    notifyDataSetChanged();
  }
}

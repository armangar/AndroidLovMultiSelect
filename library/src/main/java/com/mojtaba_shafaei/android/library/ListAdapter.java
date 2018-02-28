package com.mojtaba_shafaei.android.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.mojtaba_shafaei.android.library.LovMultiSelect.Item;
import java.util.LinkedList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListHolder> {

  private final List<Item> data = new LinkedList<>();
  private final LayoutInflater inflater;

  public ListAdapter(Context context) {
    inflater = LayoutInflater.from(context);
  }

  @Override
  public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ListHolder(inflater.inflate(R.layout.lov_multi_select_list_item, parent, false));
  }

  @Override
  public void onBindViewHolder(ListHolder holder, int _position) {
    int position = holder.getAdapterPosition();
    Item item = data.get(position);
    holder.initWith(data.get(position), (view, checked) -> {
      item.setChecked(checked);
      data.set(position, item);
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
}

package com.mojtaba_shafaei.android.library.androidLovMultiSelect;

import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import com.mojtaba_shafaei.android.library.androidLovMultiSelect.LovMultiSelect.Item;

class ListHolder extends ViewHolder {

  private final String TAG = "ListHolder";

  private AppCompatCheckedTextView des;

  ListHolder(View itemView) {
    super(itemView);
    des = itemView.findViewById(R.id.des);
  }

  void initWith(Item item, OnCheckChangeListener onCheckedChangeListener) {
    des.setText(item.getDes());
    des.setChecked(item.isChecked());

    des.setOnClickListener(view -> {
      des.toggle();
      onCheckedChangeListener.onCheckedChanged(view, des.isChecked());
    });

    if (LovMultiSelect.defaultTypeface != null) {
      des.setTypeface(LovMultiSelect.defaultTypeface);
    }
  }
}

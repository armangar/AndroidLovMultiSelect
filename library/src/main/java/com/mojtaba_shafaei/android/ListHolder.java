package com.mojtaba_shafaei.android;

import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import com.mojtaba_shafaei.android.LovMultiSelect.Item;
import com.mojtaba_shafaei.android.library.androidLovMultiSelect.R;

class ListHolder extends ViewHolder {

  private final String TAG = "ListHolder";

  protected AppCompatCheckedTextView des;

  ListHolder(View itemView) {
    super(itemView);
    des = itemView.findViewById(R.id.des);
  }
}

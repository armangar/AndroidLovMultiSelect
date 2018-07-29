package com.mojtaba_shafaei.android;

import android.view.View;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.mojtaba_shafaei.android.library.androidLovMultiSelect.R;

class ListHolder extends ViewHolder {

  private final String TAG = "ListHolder";

  protected AppCompatCheckedTextView des;

  ListHolder(View itemView) {
    super(itemView);
    des = itemView.findViewById(R.id.des);
  }
}

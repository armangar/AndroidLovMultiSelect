package com.mojtaba_shafaei.android.library;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import java.util.Collection;
import org.parceler.Parcels;

public class LovMultiSelect extends AppCompatActivity {

  private AppCompatEditText searchView;
  private AppCompatImageButton btnBack;
  private AppCompatImageButton btnClearSearch;
  private AppCompatButton btnOk;

  private Collection<? extends Item> dataSet;

  public interface Item {

    String getCod();

    String getDes();
  }

  public static void startForResult(Fragment fragment,
      int requestCod,
      Collection<? extends Item> dataSet,
      View viewTransition) {
    Intent starter = new Intent(fragment.getActivity(), LovMultiSelect.class);
    fragment.startActivityForResult(starter, requestCod);
  }

  public static void startForResult(Activity activity,
      int requestCod,
      Collection<? extends Item> dataSet,
      View viewTransition) {
    Intent starter = new Intent(activity, LovMultiSelect.class);
    starter.putExtra("data", Parcels.wrap(dataSet));
    activity.startActivityForResult(starter, requestCod);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lov_multi_select_layout1);

    //<editor-fold desc="Ui Binding">
    searchView = findViewById(R.id.search_view);
    btnClearSearch = findViewById(R.id.lov_multi_select_btn_clear_search);
    btnBack = findViewById(R.id.lov_multi_select_btn_back);
    btnOk = findViewById(R.id.lov_multi_select_btn_ok);
    //</editor-fold>

    btnBack.setOnClickListener((view) -> {
      setResult(RESULT_CANCELED);
      getActivity().finish();
    });

    btnClearSearch.setOnClickListener((view) -> searchView.setText(""));

    btnOk.setOnClickListener((view) -> {
      Intent result = new Intent();
      result.putExtra("data", "todo");
      setResult(RESULT_OK, result);
      getActivity().finish();
    });

    dataSet = Parcels.unwrap(getIntent().getParcelableExtra("data"));

  }


  private Activity getActivity() {
    return LovMultiSelect.this;
  }
}

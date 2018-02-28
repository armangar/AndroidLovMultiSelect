package com.mojtaba_shafaei.android.library;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Checkable;
import java.util.Collection;
import java.util.List;
import org.parceler.Parcels;

public class LovMultiSelect extends AppCompatActivity {

  private AppCompatEditText searchView;
  private AppCompatImageButton btnBack;
  private AppCompatImageButton btnClearSearch;
  private AppCompatButton btnOk;
  private RecyclerView list;

  private ListAdapter listAdapter;

  private List<? extends Item> dataSet;

  public interface Item extends Checkable {

    String getCod();

    String getDes();

  }

  public static void startForResult(Fragment fragment,
      int requestCod,
      @NonNull Collection<? extends Item> dataSet,
      @Nullable View viewTransition) {
    Intent starter = new Intent(fragment.getActivity(), LovMultiSelect.class);
    fragment.startActivityForResult(starter, requestCod);
  }

  public static void startForResult(Activity activity,
      int requestCod,
      @NonNull Collection<? extends Item> dataSet,
      @Nullable View viewTransition) {

    Intent starter = new Intent(activity, LovMultiSelect.class);
    starter.putExtra("data", Parcels.wrap(dataSet));
    activity.startActivityForResult(starter, requestCod);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lov_multi_select_layout1);

    ViewCompat.setLayoutDirection(findViewById(R.id.root), ViewCompat.LAYOUT_DIRECTION_RTL);

    dataSet = Parcels.unwrap(getIntent().getParcelableExtra("data"));

    //<editor-fold desc="Ui Binding">
    searchView = findViewById(R.id.search_view);
    btnClearSearch = findViewById(R.id.lov_multi_select_btn_clear_search);
    btnBack = findViewById(R.id.lov_multi_select_btn_back);
    btnOk = findViewById(R.id.lov_multi_select_btn_ok);
    list = findViewById(R.id.list);
    //</editor-fold>

    btnBack.setOnClickListener((view) -> {
      setResult(RESULT_CANCELED);
      getActivity().finish();
    });

    btnClearSearch.setOnClickListener((view) -> searchView.setText(""));

    btnOk.setOnClickListener((view) -> {
      Intent result = new Intent();
      result.putExtra("data", Parcels.wrap(listAdapter.getSelectedItems()));
      setResult(RESULT_OK, result);
      getActivity().finish();
    });

    listAdapter = new ListAdapter(this);
    list.setLayoutManager(new LinearLayoutManager(this));
    list.setAdapter(listAdapter);
    listAdapter.setData(dataSet);

  }


  private Activity getActivity() {
    return LovMultiSelect.this;
  }
}

package com.mojtaba_shafaei.android.library;

import static com.mojtaba_shafaei.android.library.ViewStatus.LOADING;
import static com.mojtaba_shafaei.android.library.ViewStatus.READY;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Checkable;
import android.widget.ProgressBar;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView.OnTagClickListener;
import com.mojtaba_shafaei.android.androidBottomDialog.BottomDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.parceler.Parcels;

public class LovMultiSelect extends AppCompatActivity {

  private final String TAG = "LovMultiSelect";

  private View toolbar;
  private ProgressBar progressBar;
  private AppCompatEditText searchView;
  private AppCompatImageButton btnBack;
  private AppCompatImageButton btnClearSearch;
  private AppCompatButton btnOk;
  private RecyclerView list;
  private TagContainerLayout tagView;


  private ListAdapter listAdapter;

  private List<? extends Item> dataSet;
  private TextWatcher textWatcher;

  private final BehaviorSubject<String> subject = BehaviorSubject.create();
  private final CompositeDisposable disposable = new CompositeDisposable();

  private final Locale FA_LOCALE = new Locale("fa");

  public static Typeface defaultTypeface = null;
  private Property properties;

  public static final String LOV_MULTI_SELECT_TRANSITION_NAME = "LOV_TRANSITION";

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
      @Nullable View viewTransition,
      Typeface typeface,
      Property params) {

    Intent starter = new Intent(activity, LovMultiSelect.class);
    starter.putExtra("data", Parcels.wrap(dataSet));
    starter.putExtra("properties", Parcels.wrap(params));
    defaultTypeface = typeface;

    if (VERSION.SDK_INT >= 21 && viewTransition != null) {
      ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
          activity, viewTransition, LOV_MULTI_SELECT_TRANSITION_NAME);
      activity.startActivityForResult(starter, requestCod, activityOptions.toBundle());
    } else {
      activity.startActivityForResult(starter, requestCod);
    }
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lov_multi_select_layout1);

    ViewCompat.setLayoutDirection(findViewById(R.id.root), ViewCompat.LAYOUT_DIRECTION_RTL);

    dataSet = Parcels.unwrap(getIntent().getParcelableExtra("data"));

    //<editor-fold desc="Ui Binding">
    toolbar = findViewById(R.id.toolbar);
    progressBar = findViewById(R.id.progressBar);
    searchView = findViewById(R.id.search_view);
    btnClearSearch = findViewById(R.id.lov_multi_select_btn_clear_search);
    btnBack = findViewById(R.id.lov_multi_select_btn_back);
    btnOk = findViewById(R.id.lov_multi_select_btn_ok);
    list = findViewById(R.id.list);
    tagView = findViewById(R.id.tag_group);
    //</editor-fold>
    btnClearSearch.setVisibility(View.INVISIBLE);

    if (defaultTypeface != null) {
      searchView.setTypeface(defaultTypeface);
      btnOk.setTypeface(defaultTypeface);
      tagView.setTagTypeface(defaultTypeface);
    }

    properties = Parcels.unwrap(getIntent().getParcelableExtra("properties"));
    if (properties != null) {
      if (properties.getButtonOkBackgroundDrawable() != null) {
        btnOk.setBackgroundDrawable(ContextCompat.getDrawable(this,
            properties.getButtonOkBackgroundDrawable()));
      }

      if (properties.getTagBackgroundColor() != null) {
        tagView.setTagBackgroundColor(ContextCompat.getColor(this,
            properties.getTagBackgroundColor()));
      }

      if (properties.getTagBorderColor() != null) {
        tagView.setTagBorderColor(ContextCompat.getColor(this,
            properties.getTagBorderColor()));
      }
    }

    ViewCompat.setElevation(toolbar, dpToPx(4));
    btnBack.setOnClickListener((view) -> {
      setResult(RESULT_CANCELED);
      if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
        getActivity().finishAfterTransition();
      } else {
        finish();
      }
    });

    btnClearSearch.setOnClickListener((view) -> searchView.setText(""));

    btnOk.setOnClickListener((view) -> {
      Intent result = new Intent();
      result.putExtra("data", Parcels.wrap(listAdapter.getSelectedItems()));
      setResult(RESULT_OK, result);
      if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
        getActivity().finishAfterTransition();
      } else {
        finish();
      }
    });

    tagView.setOnTagClickListener(new OnTagClickListener() {
      @Override
      public void onTagClick(int position, String text) {

      }

      @Override
      public void onTagLongClick(int position, String text) {
      }

      @Override
      public void onTagCrossClick(int position) {
        removeTag(tagView.getTagText(position));
      }
    });

    listAdapter = new ListAdapter(this, (position, item) -> {
      if (item.isChecked()) {
        tagView.addTag(item.getDes(), 0);
        refreshSelectedCounter(tagView.getTags().size());
      } else {
        removeTag(item.getDes());
      }
    });

    list.setLayoutManager(new LinearLayoutManager(this));
    list.setAdapter(listAdapter);
    listAdapter.setData(dataSet);

    textWatcher = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() == 0) {
          btnClearSearch.setVisibility(View.INVISIBLE);
        } else {
          btnClearSearch.setVisibility(View.VISIBLE);
        }
        subject.onNext(charSequence.toString());
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    };
    searchView.addTextChangedListener(textWatcher);

    disposable.add(
        subject
            .subscribeOn(Schedulers.io())
            .debounce(
                getResources().getInteger(R.integer.lov_multi_select_config_debounce_duration),
                TimeUnit.MILLISECONDS)
            .throttleWithTimeout(
                getResources().getInteger(R.integer.lov_multi_select_config_throttle_duration),
                TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .map(_data -> {
              if (!disposable.isDisposed()) {
                setFormStatus(LOADING);
              }
              return _data;
            })
            .observeOn(Schedulers.computation())
            .switchMap(query -> Observable.defer(() -> {
              List<Item> items = findExpectedData(query);
              if (items != null) {
                return Observable.just(items);
              } else {
                return Observable.error(new NetworkErrorException());
              }
            }))
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturnItem(new ArrayList<>())
            .subscribeWith(new DisposableObserver<List<Item>>() {
              @Override
              public void onNext(List<Item> items) {
                if (isDisposed()) {
                  return;
                }
                setFormStatus(READY);
                refreshAdapter(items);
              }

              @Override
              public void onError(Throwable e) {
                if (isDisposed()) {
                  return;
                }
                setFormStatus(READY);
                showError(e);
              }

              @Override
              public void onComplete() {

              }
            })
    );

    refreshSelectedCounter(0);
  }

  private void removeTag(String des) {
    List<String> tags = tagView.getTags();
    final int len = tags.size();
    for (int i = 0; i < len; i++) {
      if (tags.get(i).equals(des)) {
        tags.remove(i);
        break;
      }
    }

    tagView.setTags(tags);
    listAdapter.unCheckTag(des);

    refreshSelectedCounter(tagView.getTags().size());
  }

  private void refreshSelectedCounter(int count) {
    if (count != 0) {
      btnOk.setText(String
          .format(FA_LOCALE,
              getString(R.string.lov_multi_select_btn_ok_text),
              (properties != null && properties.getBtnOkText() != null) ? properties.getBtnOkText()
                  : "انتخاب کن",
              count)
      );
      btnOk.setEnabled(true);
    } else {
      btnOk.setText(R.string.lov_multi_select_choose_at_least_one);
      btnOk.setEnabled(false);
    }


  }

  private void showError(Throwable e) {
    try {
      new BottomDialog.Builder()
          .withHiddenHeader(true)
          .withDirection(BottomDialog.RTL)
          .withCancelable(false)
          .withHiddenNegativeButton(true)
          .withPositiveText(getString(R.string.lov_multi_select_i_got_it))
          .withDefaultTypeface(defaultTypeface)
          .withContent(e.getMessage())
          .build()
          .show(LovMultiSelect.this);

    } catch (Exception error) {
      Log.e(TAG, "showErrors: " + error.getMessage(), error);
    }
  }

  private void refreshAdapter(List<Item> items) {
    listAdapter.setData(items);
  }

  private List<Item> findExpectedData(String query) {
    List<Item> result = new LinkedList<>();
    for (Item item : this.dataSet) {
      if (item.getDes().contains(query)) {
        result.add(item);
      }
    }

    return result;
  }

  public void setFormStatus(int status) {
    switch (status) {
      case READY: {
        hideLoading();
        hideErrors();
      }
      break;

      case LOADING: {
        showLoading();
      }
      break;
    }
  }

  private void showLoading() {
    progressBar.setVisibility(View.VISIBLE);
  }

  private void hideErrors() {

  }

  private void hideLoading() {
    progressBar.setVisibility(View.INVISIBLE);
  }


  private Activity getActivity() {
    return LovMultiSelect.this;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (textWatcher != null) {
      searchView.removeTextChangedListener(textWatcher);
    }
    defaultTypeface = null;
  }

  int dpToPx(int dp) {
        /*DisplayMetrics displayMetrics = MyApplication.getInstance().getResources().getDisplayMetrics();
        int px = Math.round((dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)) + .5f);*/

    int px = (int) TypedValue
        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());

    return px;
  }

}

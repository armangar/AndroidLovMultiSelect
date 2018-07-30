package com.mojtaba_shafaei.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Checkable;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView.OnTagClickListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.rxrelay2.PublishRelay;
import com.mojtaba_shafaei.android.androidBottomDialog.BottomDialog;
import com.mojtaba_shafaei.android.library.androidLovMultiSelect.R;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.parceler.Parcels;

public class LovMultiSelect extends AppCompatActivity {

  private final String TAG = "LovMultiSelect";

  private ContentLoadingProgressBar progressBar;
  private TextInputEditText searchView;
  private AppCompatImageButton btnClearSearch;
  private MaterialButton btnOk;
  private RecyclerView recyclerView;
  private TagContainerLayout tagView;


  private ListAdapter listAdapter;

  private TextWatcher textWatcher;

  private final CompositeDisposable disposable = new CompositeDisposable();
  private final Locale FA_LOCALE = new Locale("fa");

  protected static Typeface sDefaultTypeface = null;
  private static Property sProperties;
  private static List<Item> sLoader;
  private static List<Item> sDefaultItems = new ArrayList<>();

  public static final String LOV_MULTI_SELECT_TRANSITION_NAME = "LOV_TRANSITION";

  private final String KEY_SELECTED_ITEMS = "selectedItems";
  private final String SPACE = String.valueOf(' ');

  private Observable<Lce<List<Item>>> lceObservable;

  public interface Item extends Checkable {

    String getCode();

    String getDes();

    int getPriority();

    void setCode(String code);

    void setDes(String des);

    void setPriority(int priority);
  }

  interface FetchDataListener {

    //    Observable<Lce<List<Item>>> fetch();
    List<Item> fetch();
  }

  public static void startForResult(Fragment fragment,
      int requestCod,
      @Nullable View viewTransition,
      Typeface typeface,
      Property uiParams,
      List<Item> fetchDataListener,
      List<Item> defaultItems) {

    Intent starter = new Intent(fragment.getActivity(), LovMultiSelect.class);
    sDefaultTypeface = typeface;
    sProperties = uiParams;
    sLoader = fetchDataListener;
    sDefaultItems = defaultItems;

    if (VERSION.SDK_INT >= 21 && viewTransition != null) {
      ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
          fragment.getActivity(), viewTransition, LOV_MULTI_SELECT_TRANSITION_NAME);
      fragment.startActivityForResult(starter, requestCod, activityOptions.toBundle());
    } else {
      fragment.startActivityForResult(starter, requestCod);
    }
  }

  public static void startForResult(Activity activity,
      int requestCod,
      @Nullable View viewTransition,
      Typeface typeface,
      Property uiParams,
      List<Item> fetchDataListener,
      List<Item> defaultItems) {

    Intent starter = new Intent(activity, LovMultiSelect.class);
    sDefaultTypeface = typeface;
    sProperties = uiParams;
    sLoader = fetchDataListener;
    sDefaultItems = defaultItems;

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

    //<editor-fold desc="Ui Binding">
    progressBar = findViewById(R.id.progressBar);
    searchView = findViewById(R.id.search_view);
    btnClearSearch = findViewById(R.id.lov_multi_select_btn_clear_search);
    AppCompatImageButton btnBack = findViewById(R.id.lov_multi_select_btn_back);
    btnOk = findViewById(R.id.lov_multi_select_btn_ok);
    recyclerView = findViewById(R.id.list);
    tagView = findViewById(R.id.tag_group);
    //</editor-fold>
    btnClearSearch.setVisibility(View.GONE);
    ViewCompat.setNestedScrollingEnabled(tagView, false);

    btnClearSearch.setImageDrawable(
        ContextCompat.getDrawable(this, R.drawable.lov_multi_select_ic_clear_light_theme));
    btnBack.setImageDrawable(
        ContextCompat.getDrawable(this, R.drawable.lov_multi_select_ic_back_dark));

    if (sDefaultTypeface != null) {
      searchView.setTypeface(sDefaultTypeface);
      btnOk.setTypeface(sDefaultTypeface);
      tagView.setTagTypeface(sDefaultTypeface);
    }

    if (sProperties != null) {
      if (sProperties.getButtonOkBackgroundTint() != null) {
        btnOk.setBackgroundTintMode(Mode.MULTIPLY);
        btnOk.setBackgroundTintList(sProperties.getButtonOkBackgroundTint());
      }

      if (sProperties.getButtonOkTextColorState() != null) {
        btnOk.setTextColor(sProperties.getButtonOkTextColorState());
      }

      if (sProperties.getTagBackgroundColor() != null) {
        tagView.setTagBackgroundColor(ContextCompat.getColor(this,
            sProperties.getTagBackgroundColor()));
      }

      if (sProperties.getTagBorderColor() != null) {
        tagView.setTagBorderColor(ContextCompat.getColor(this,
            sProperties.getTagBorderColor()));
      }
    }

    btnBack.setOnClickListener((view) -> onBackPressed());

    btnClearSearch.setOnClickListener((view) -> searchView.setText(""));

    lceObservable = Observable.just(Lce.data(sLoader));

    btnOk.setOnClickListener((view) -> {
      disposable.add(
          lceObservable
              .subscribeOn(Schedulers.io())
              .zipWith(Observable.just(tagView.getTags())
                      .subscribeOn(AndroidSchedulers.mainThread())
                      .observeOn(Schedulers.computation()),
                  (listLce, strings) -> {
                    if (listLce.hasError() || listLce.getData() == null) {
                      return Lce.<Content>error(
                          new NullPointerException("can not get list of source data"));
                    } else {
                      return Lce.data(new Content(listLce.getData(), strings));
                    }
                  })
              .observeOn(Schedulers.computation())
              .switchMap(lce -> {
                if (lce.hasError() || lce.getData() == null) {
                  return Observable.<Lce<List<Item>>>just(Lce.error(lce.getError()));
                } else {
                  if (lce.getData().getSelectedTags() != null) {
                    List<Item> result = new ArrayList<>();
                    for (String tagDes : lce.getData().getSelectedTags()) {
                      for (Item item : lce.getData().getDataSet()) {
                        if (item.getDes().contentEquals(tagDes)) {
                          result.add(item);
                          break;
                        }
                      }
                    }
                    return Observable.just(Lce.data(result));
                  } else {
                    return Observable.<Lce<List<Item>>>just(Lce.error(new Exception("TET")));
                  }
                }
              })
              .observeOn(AndroidSchedulers.mainThread())
              .doOnComplete(disposable::clear)
              .subscribeWith(new DisposableObserver<Lce<List<Item>>>() {
                @Override
                public void onNext(Lce<List<Item>> lce) {
                  if (lce.hasError()) {
                    showError(lce.getError());
                  } else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("data", Parcels.wrap(lce.getData()));
                    setResult(RESULT_OK, resultIntent);
                    finishActivity();
                  }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
              })
      );
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
        removeTagFromSelectedList(tagView.getTags().get(position));
        listAdapter.refreshAdapter();
        refreshSelectedCounter();
      }
    });

    listAdapter = new ListAdapter(this
        , () -> tagView.getTags()
        , (view, item, isChecked) -> {
      if (isChecked) {
        addTag(item.getDes());
      } else {
        removeTagFromSelectedList(item.getDes());
      }
    });

    listAdapter.setHasStableIds(true);

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(listAdapter);
    recyclerView.setHasFixedSize(true);

    //<editor-fold desc="Create Observable">
    final PublishRelay<String> subject = PublishRelay.create();
    textWatcher = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        subject.accept(charSequence.toString());
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    };
    //</editor-fold>

    searchView.addTextChangedListener(textWatcher);

    disposable.add(
        subject.subscribeOn(Schedulers.io())
            .startWith(getQueryText())
            .observeOn(AndroidSchedulers.mainThread())
            .map(query -> {
              if (query.isEmpty()) {
                btnClearSearch.setVisibility(View.GONE);
              } else {
                btnClearSearch.setVisibility(View.VISIBLE);
              }
              return query;
            })
            .debounce(
                getResources().getInteger(R.integer.lov_multi_select_config_debounce_duration),
                TimeUnit.MILLISECONDS)
            .throttleWithTimeout(
                getResources().getInteger(R.integer.lov_multi_select_config_throttle_duration),
                TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(Schedulers.computation())
            .switchMap(query ->
                Observable.just(query.toLowerCase())
                    .zipWith(lceObservable,
                        (BiFunction<String, Lce<List<Item>>, Lce<ContentDataSetAndQueryText>>) (query1, listLce) -> {
                          if (listLce.hasError()) {
                            return Lce.error(listLce.getError());
                          } else {
                            return Lce.data(new ContentDataSetAndQueryText(listLce.getData()
                                , query1)
                            );
                          }
                        })
                    .startWith(Lce.<ContentDataSetAndQueryText>loading()))
            .switchMap(lce -> {
              if (!lce.hasError() && !lce.isLoading() && lce.getData().getQuery().length() > 0) {
                final String fQuery = lce.getData().getQuery();
                String[] queries = fQuery.split(SPACE);
                //remove space and 1 char length parts
                final List<String> ss = new ArrayList<>(Arrays.asList(queries));
                for (Iterator<String> iterator = ss.iterator(); iterator.hasNext(); ) {
                  if (iterator.next().length() <= 1) {
                    iterator.remove();
                  }
                }
                queries = new String[ss.size()];
                ss.toArray(queries);
                //
                final List<Item> results = new ArrayList<>();
                int priority;
                for (Item item : lce.getData().getList()) {
                  priority = Integer.MAX_VALUE;
                  for (String k : queries) {
                    if (k.length() > 1) {
                      if (item.getDes().toLowerCase().contains(k.toLowerCase())) {
                        priority--;
                      }
                    }
                  }

                  if (item.getDes().contentEquals(fQuery)) {
                    priority--;
                  }

                  if (item.getDes().toLowerCase().startsWith(fQuery)) {
                    priority--;
                  }

                  item.setPriority(priority);
                  //Add item if it is desired one.
                  if (priority != Integer.MAX_VALUE) {
                    results.add(item);
                  }
                }
                lce.getData().setItems(results);
                return Observable.just(lce);
              } else {
                return Observable.just(lce);
              }
            })
//            .onErrorResumeNext(
//                Observable.just(Lce.data(new ContentDataSetAndQueryText(new ArrayList<>(), ""))))
            .toFlowable(BackpressureStrategy.BUFFER)
            .toObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<Lce<ContentDataSetAndQueryText>>() {
              @Override
              public void onNext(Lce<ContentDataSetAndQueryText> lce) {
                if (isDisposed()) {
                  return;
                }
                if (lce.isLoading()) {
                  hideErrors();
                  showContentLoading(true);

                } else if (lce.hasError()) {
                  showContentLoading(false);
                  showInternetError();

                } else {
                  hideErrors();
                  showContentLoading(false);
                  if (lce.getData() != null) {
                    listAdapter.setData(lce.getData().getList());
                    recyclerView.getLayoutManager().scrollToPosition(0);
                    refreshSelectedCounter();
                  } else {
                    showInternetError();
                  }
                }
              }

              @Override
              public void onError(Throwable e) {
                if (isDisposed()) {
                  return;
                }
                setFormStatus(ViewStatus.READY);
                showError(e);
                Log.e(TAG, "onError: ", e);
              }

              @Override
              public void onComplete() {

              }
            })
    );

    if (savedInstanceState == null) {
      tagView.removeAllTags();
      if (sDefaultItems != null) {
        for (Item item : sDefaultItems) {
          addTag(item.getDes());
        }
      }
    } else {
      List<CharSequence> savedTags = savedInstanceState
          .getCharSequenceArrayList(KEY_SELECTED_ITEMS);
      tagView.removeAllTags();
      if (savedTags != null) {
        for (CharSequence des : savedTags) {
          addTag(des.toString());
        }
      }
    }

    refreshSelectedCounter();
  }

  private void observeAdapter() {
    refreshSelectedCounter();

    if (listAdapter.getItemCount() == 0) {
      showError(new Exception("مقداری یافت نشد"));
    } else {
      hideErrors();
    }
  }

  private void removeTagFromSelectedList(String tag) {
    List<String> tags = tagView.getTags();
    if (tags != null) {
      final int len = tags.size();
      for (int i = 0; i < len; i++) {
        if (tags.get(i).contentEquals(tag)) {
          tags.remove(i);
          break;
        }
      }
      tagView.setTags(tags);
    }
    observeAdapter();
  }

  private void hideErrors() {
    findViewById(R.id.tv_message).setVisibility(View.GONE);
  }

  private void showInternetError() {
    Log.e(TAG, "showInternetError: ");
  }

  private void showContentLoading(boolean b) {
    if (b) {
      progressBar.show();
    } else {
      progressBar.hide();
    }
  }

  private String getQueryText() {
    return searchView.getText().toString();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  private void addTag(String des) {
    tagView.addTag(des, 0);
    refreshSelectedCounter();
  }

  private void refreshSelectedCounter() {
    int count = tagView.getTags() != null ? tagView.getTags().size() : 0;
    try {
      if (sProperties.getMinLimit() != -1 && count < sProperties.getMinLimit()) {
        btnOk.setText(
            String.format(getString(R.string.lov_multi_select_choose_at_least_one),
                sProperties.getMinLimit())
        );
        btnOk.setEnabled(false);

      } else {
        if (sProperties.getMaxLimit() != -1 && count > sProperties.getMaxLimit()) {
          btnOk.setText(
              String.format(getString(R.string.lov_multi_select_choose_at_most),
                  sProperties.getMaxLimit())
          );
          btnOk.setEnabled(false);
        } else {
          btnOk.setText(String
              .format(FA_LOCALE,
                  getString(R.string.lov_multi_select_btn_ok_text),
                  (sProperties != null && sProperties.getBtnOkText() != null) ? sProperties
                      .getBtnOkText()
                      : getString(R.string.lov_multi_select_choose_it),
                  count)
          );
          btnOk.setEnabled(true);
        }
      }
    } catch (Exception e) {
      Log.e(TAG, "refreshSelectedCounter: ", e);
    }
  }

  private void showError(Throwable e) {
    try {
      TextView tvMessage = findViewById(R.id.tv_message);
      tvMessage.setVisibility(View.VISIBLE);
      if (sDefaultTypeface != null) {
        tvMessage.setTypeface(sDefaultTypeface);
      }
      tvMessage.setText(e.getMessage());
      tvMessage
          .setTextColor(ContextCompat.getColor(this, R.color.lov_multi_select_primaryTextDark));

    } catch (Exception error) {
      Log.e(TAG, "showErrors: " + error.getMessage(), error);
    }
  }

  public void setFormStatus(int status) {
    switch (status) {
      case ViewStatus.READY: {
        showContentLoading(false);
      }
      break;

      case ViewStatus.LOADING: {
        showContentLoading(true);
      }
      break;
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (textWatcher != null) {
      searchView.removeTextChangedListener(textWatcher);
    }
    sDefaultTypeface = null;
    sLoader = null;
    sDefaultItems = null;
    sProperties = null;
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (tagView.getTags() != null) {
      ArrayList<CharSequence> selectedTags = new ArrayList<>(tagView.getTags());
      outState.putCharSequenceArrayList(KEY_SELECTED_ITEMS, selectedTags);
    }
  }

  @Override
  protected void onPause() {
    hideKeyboard(getCurrentFocus());
    super.onPause();
  }

  private void hideKeyboard(View view) {
    if (view == null) {
      return;
    }
    InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    inputManager.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
    inputManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

    getWindow().setSoftInputMode(
        WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    view.clearFocus();
    view.setSelected(false);
  }

  public void finishActivity() {
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      finishAfterTransition();
    } else {
      finish();
    }
  }

  @Override
  public void onBackPressed() {
    //dismiss all changes
    BottomDialog.builder()
        .withCancelable(true)
        /*.withTitle(R.string.lov_multi_select_cancel_dialog_title)
        .withIcon(ContextCompat
            .getDrawable(getApplicationContext(), R.drawable.lov_multi_select_ic_warning))*/
        .withHiddenHeader(true)
        .withContent(getContentString())
        .withPositiveText(R.string.lov_multi_select_yes)
        .withNegativeText(R.string.lov_multi_select_no)
        .withDirection(BottomDialog.RTL)
        .withDefaultTypeface(sDefaultTypeface)
        .withOnNegative(bottomDialog -> {
          //No
          setResult(RESULT_CANCELED);
          finishActivity();
        })
        .build()
        .show(this);
  }

  private String getContentString() {
    StringBuilder content = new StringBuilder();

    content.append("با خروج از این صفحه، دوباره باید موارد مورد نظرتان را انتخاب نمایید.")
        .append("\n\n")
        .append("در این صفحه ادامه میدهید؟");

    return content.toString();
  }
}

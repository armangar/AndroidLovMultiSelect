package com.mojtaba_shafaei.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Checkable;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView.OnTagClickListener;
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
  private AppCompatButton btnOk;
  private RecyclerView recyclerView;
  private TagContainerLayout tagView;


  private ListAdapter listAdapter;

  private TextWatcher textWatcher;

  private final CompositeDisposable disposable = new CompositeDisposable();
  private final Locale FA_LOCALE = new Locale("fa");

  protected static Typeface sDefaultTypeface = null;
  private static Property sProperties;
  private static FetchDataListener sLoader;
  private static List<Item> sDefaultItems = new ArrayList<>();

  public static final String LOV_MULTI_SELECT_TRANSITION_NAME = "LOV_TRANSITION";

  private final String KEY_SELECTED_ITEMS = "selectedItems";
  private final String SPACE = String.valueOf(' ');

  public interface Item extends Checkable {

    String getCode();

    String getDes();

    int getPriority();

    void setCode(String code);

    void setDes(String des);

    void setPriority(int priority);
  }

  public interface FetchDataListener {

    Observable<Lce<List<Item>>> fetch();
  }

  public static void startForResult(Fragment fragment,
      int requestCod,
      @Nullable View viewTransition,
      Typeface typeface,
      Property uiParams,
      FetchDataListener fetchDataListener,
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
      FetchDataListener fetchDataListener,
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

    if (sDefaultTypeface != null) {
      searchView.setTypeface(sDefaultTypeface);
      btnOk.setTypeface(sDefaultTypeface);
      tagView.setTagTypeface(sDefaultTypeface);
    }

    if (sProperties != null) {
      if (sProperties.getButtonOkBackgroundDrawable() != null) {
        btnOk.setBackgroundDrawable(ContextCompat.getDrawable(this,
            sProperties.getButtonOkBackgroundDrawable()));
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

    btnOk.setOnClickListener((view) -> {
      sLoader.fetch()
          .subscribeOn(Schedulers.io())
          .zipWith(Observable.just(tagView.getTags())
                  .subscribeOn(AndroidSchedulers.mainThread())
                  .observeOn(Schedulers.computation()),
              (listLce, strings) -> {
                if (listLce.hasError() || listLce.getData() == null) {
                  return Lce.<Content>error(new NullPointerException());
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
                return Observable.<Lce<List<Item>>>just(Lce.error(new Exception("")));
              }
            }
          })
          .observeOn(AndroidSchedulers.mainThread())
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
          });
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

    listAdapter = new ListAdapter(this
        , () -> tagView.getTags()
        , (view, item, isChecked) -> {
      if (isChecked) {
        addTag(item.getDes());
      } else {
        removeTag(item.getDes());
      }
    });

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(listAdapter);

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
            .switchMap(query ->
                Observable.just(query.toLowerCase())
                    .zipWith(sLoader.fetch(),
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
                List<String> ss = new ArrayList<>(Arrays.asList(queries));
                for (Iterator<String> iterator = ss.iterator(); iterator.hasNext(); ) {
                  if (iterator.next().length() <= 1) {
                    iterator.remove();
                  }
                }
                queries = new String[ss.size()];
                ss.toArray(queries);
                //
                List<Item> results = new ArrayList<>();
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
                    String[] splitDesiredHighlight = searchView.getText().toString().trim()
                        .split(SPACE);

//                    listAdapter.setHighlightFor(splitDesiredHighlight);
                    listAdapter.setData(lce.getData().getList());
                    recyclerView.getLayoutManager().scrollToPosition(0);

//                    recyclerView.observeAdapter();
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
              }

              @Override
              public void onComplete() {

              }
            })
    );

    if (savedInstanceState == null) {
      tagView.removeAllTags();
      for (Item item : sDefaultItems) {
        addTag(item.getDes());
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

    //refreshSelectedCounter(0);
  }

  private void hideErrors() {

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

  private void addTag(Item item) {
    tagView.addTag(item.getDes(), 0);
    refreshSelectedCounter(tagView.getTags().size());
  }

  private void addTag(String des) {
    tagView.addTag(des, 0);
    refreshSelectedCounter(tagView.getTags().size());
  }

  private void removeTag(String des) {
    // Remove tag from tagViewGroup
    List<String> tags;
    try {
      tags = tagView.getTags();
      if (tags == null) {
        tags = new ArrayList<>();
      }

      final int len = tags.size();
      for (int i = 0; i < len; i++) {
        if (tags.get(i).contentEquals(des)) {
          tags.remove(i);
          break;
        }
      }
      tagView.setTags(tags);
    } catch (Exception e) {
      Log.e(TAG, "removeTag: ", e);
    }
    // Remove tag from adapter
    listAdapter.unCheckTag(des);

    refreshSelectedCounter(tagView.getTags().size());
  }

  private void refreshSelectedCounter(int count) {
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
    Log.e(TAG, "showError: ", e);
    try {
      new BottomDialog.Builder()
          .withHiddenHeader(true)
          .withDirection(BottomDialog.RTL)
          .withCancelable(false)
          .withHiddenNegativeButton(true)
          .withPositiveText(getString(R.string.lov_multi_select_i_got_it))
          .withDefaultTypeface(sDefaultTypeface)
          .withContent(e.getMessage())
          .build()
          .show(LovMultiSelect.this);

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

  private Activity getActivity() {
    return LovMultiSelect.this;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (textWatcher != null) {
      searchView.removeTextChangedListener(textWatcher);
    }
    sDefaultTypeface = null;
  }

  int dpToPx(int dp) {
        /*DisplayMetrics displayMetrics = MyApplication.getInstance().getResources().getDisplayMetrics();
        int px = Math.round((dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)) + .5f);*/

    int px = (int) TypedValue
        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());

    return px;
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
        .withTitle(R.string.lov_multi_select_cancel_dialog_title)
        .withIcon(R.drawable.lov_multi_select_ic_warning)
        .withContent(getContentString())
        .withPositiveText(R.string.lov_multi_select_yes)
        .withNegativeText(R.string.lov_multi_select_no,
            R.color.lov_multi_select_color_error)
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

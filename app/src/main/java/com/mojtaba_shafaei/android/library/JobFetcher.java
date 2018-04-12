package com.mojtaba_shafaei.android.library;

import android.os.SystemClock;
import com.mojtaba_shafaei.android.Lce;
import com.mojtaba_shafaei.android.LovMultiSelect;
import com.mojtaba_shafaei.android.LovMultiSelect.Item;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class JobFetcher implements LovMultiSelect.FetchDataListener {

  @Override
  public Observable<Lce<List<Item>>> fetch() {
    List<Item> jobs = new ArrayList<>();
    jobs.add(new Job("1", "یک"));
    jobs.add(new Job("2", "دو"));
    jobs.add(new Job("3", "سه"));
    jobs.add(new Job("4", "چهارمین آیتم از لیست موجود این است"));
    jobs.add(new Job("5", "پنجمین آیتم از لیست موجود این میباشد"));
    jobs.add(new Job("6", "شش"));
    jobs.add(new Job("7", "SEVEN"));
    jobs.add(new Job("8", "EIGHT"));
    jobs.add(new Job("9", "NINE"));
    jobs.add(new Job("10", "TEN"));
    jobs.add(new Job("11", "ELEVEN"));
    jobs.add(new Job("12", "TWELVE"));
    jobs.add(new Job("13", "THIRTEEN"));
    jobs.add(new Job("14", "FOURTEEN"));
    jobs.add(new Job("15", "FIFTEEN"));

    return Observable.just(Lce.data(jobs))
        .subscribeOn(Schedulers.io())
        .switchMap(data -> {
          return Observable.just(data);
        })
       // .switchMap(data->Observable.error(new Exception("SALAM")))
        ;
  }
}

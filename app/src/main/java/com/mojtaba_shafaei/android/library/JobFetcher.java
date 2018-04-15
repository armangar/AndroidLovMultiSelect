package com.mojtaba_shafaei.android.library;

import com.mojtaba_shafaei.android.LovMultiSelect.Item;
import java.util.ArrayList;
import java.util.List;

public class JobFetcher {

  public List<Item> fetch() {
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

    return jobs;
  }
}

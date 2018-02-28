package com.mojtaba_shafaei.android.library;

import org.parceler.Parcel;

/**
 * Created by mojtaba on 2/28/18.
 */
@Parcel
public class Job implements LovMultiSelect.Item {

  private String cod, des;

  public Job() {
  }

  public Job(String cod, String des) {
    this.cod = cod;
    this.des = des;
  }

  @Override
  public String getCod() {
    return cod;
  }

  @Override
  public String getDes() {
    return des;
  }
}

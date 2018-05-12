package com.mojtaba_shafaei.android.library;

import com.mojtaba_shafaei.android.LovMultiSelect;
import org.parceler.Parcel;

/**
 * Created by mojtaba on 2/28/18.
 */
@Parcel
public class Job implements LovMultiSelect.Item {

  boolean checked;
  String cod, des;

  public Job() {
  }

  public Job(String cod, String des) {
    this.cod = cod;
    this.des = des;
  }

  public Job(String cod, String des, boolean checked) {
    this.cod = cod;
    this.des = des;
    this.checked = checked;
  }

  @Override
  public int getPriority() {
    return 0;
  }

  @Override
  public void setCode(String code) {

  }

  @Override
  public void setDes(String des) {

  }

  @Override
  public void setPriority(int priority) {

  }

  @Override
  public String getCode() {
    return cod;
  }

  @Override
  public String getDes() {
    return des;
  }

  @Override
  public void setChecked(boolean b) {
    checked = b;
  }

  @Override
  public boolean isChecked() {
    return checked;
  }

  @Override
  public void toggle() {
    checked = !checked;
  }

  @Override
  public String toString() {
    return "Job{" +
        "checked=" + checked +
        ", cod='" + cod + '\'' +
        ", des='" + des + '\'' +
        '}';
  }
}

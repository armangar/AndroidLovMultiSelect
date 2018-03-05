package com.mojtaba_shafaei.android.library.androidLovMultiSelect;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import org.parceler.Parcel;

/**
 * Created by mojtaba on 3/5/18.
 */
@Parcel
public final class Property {

  @DrawableRes
  private Integer buttonOkBackgroundDrawable = null;

  @ColorRes
  private Integer tagBackgroundColor = null;

  @ColorRes
  private Integer tagBorderColor = null;

  private String btnOkText = "";

  public Property() {
  }

  private Property(Builder builder) {
    buttonOkBackgroundDrawable = builder.buttonOkBackgroundDrawable;
    tagBackgroundColor = builder.tagBackgroundColor;
    tagBorderColor = builder.tagBorderColor;
    btnOkText = builder.btnOkText;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  @DrawableRes
  public Integer getButtonOkBackgroundDrawable() {
    return buttonOkBackgroundDrawable;
  }

  @ColorRes
  public Integer getTagBackgroundColor() {
    return tagBackgroundColor;
  }

  public Integer getTagBorderColor() {
    return tagBorderColor;
  }

  public String getBtnOkText() {
    return btnOkText;
  }

  public static final class Builder {

    private Integer buttonOkBackgroundDrawable;
    private Integer tagBackgroundColor;
    private Integer tagBorderColor;
    private String btnOkText;

    private Builder() {
    }

    public Builder withButtonOkBackgroundDrawable(Integer buttonOkBackgroundDrawable) {
      this.buttonOkBackgroundDrawable = buttonOkBackgroundDrawable;
      return this;
    }

    public Builder withTagBackgroundColor(Integer tagBackgroundColor) {
      this.tagBackgroundColor = tagBackgroundColor;
      return this;
    }

    public Builder withTagBorderColor(Integer tagBorderColor) {
      this.tagBorderColor = tagBorderColor;
      return this;
    }

    public Builder withBtnOkText(String text) {
      this.btnOkText = text;
      return this;
    }

    public Property build() {
      return new Property(this);
    }
  }
}

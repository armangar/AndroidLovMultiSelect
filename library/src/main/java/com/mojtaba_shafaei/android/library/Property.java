package com.mojtaba_shafaei.android.library;

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


  public Property() {
  }

  private Property(Builder builder) {
    buttonOkBackgroundDrawable = builder.buttonOkBackgroundDrawable;
    tagBackgroundColor = builder.tagBackgroundColor;
    tagBorderColor = builder.tagBorderColor;
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

  public static final class Builder {

    private Integer buttonOkBackgroundDrawable;
    private Integer tagBackgroundColor;
    private Integer tagBorderColor;

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

    public Property build() {
      return new Property(this);
    }
  }
}

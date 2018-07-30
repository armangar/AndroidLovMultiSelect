package com.mojtaba_shafaei.android;

import android.content.res.ColorStateList;
import androidx.annotation.ColorRes;
import androidx.annotation.IntRange;

/**
 * Created by mojtaba on 3/5/18.
 */
public final class Property {

  private ColorStateList buttonOkTextColorState;

  private android.content.res.ColorStateList buttonOkBackgroundTint = null;

  @ColorRes
  private Integer tagBackgroundColor = null;

  @ColorRes
  private Integer tagBorderColor = null;

  private String btnOkText = "";

  @IntRange(from = -1)
  private int minLimit;

  @IntRange(from = -1)
  private int maxLimit;

  public Property() {
  }

  private Property(Builder builder) {
    buttonOkBackgroundTint = builder.buttonOkBackgroundDrawable;
    buttonOkTextColorState = builder.buttonOkTextColorState;
    tagBackgroundColor = builder.tagBackgroundColor;
    tagBorderColor = builder.tagBorderColor;
    btnOkText = builder.btnOkText;
    minLimit = builder.minLimit;
    maxLimit = builder.maxLimit;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public ColorStateList getButtonOkBackgroundTint() {
    return buttonOkBackgroundTint;
  }

  public ColorStateList getButtonOkTextColorState() {
    return buttonOkTextColorState;
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

  public int getMinLimit() {
    return minLimit;
  }

  public int getMaxLimit() {
    return maxLimit;
  }

  public static final class Builder {

    private ColorStateList buttonOkBackgroundDrawable;
    private ColorStateList buttonOkTextColorState;
    private Integer tagBackgroundColor;
    private Integer tagBorderColor;
    private String btnOkText;
    private int minLimit = -1;
    private int maxLimit = -1;

    private Builder() {
    }

    public Builder withButtonOkBackgroundTint(ColorStateList tint) {
      this.buttonOkBackgroundDrawable = tint;
      return this;
    }

    public Builder withButtonOkTextColor(ColorStateList tint) {
      this.buttonOkTextColorState = tint;
      return this;
    }

    public Builder withTagBackgroundColor(@ColorRes Integer tagBackgroundColor) {
      this.tagBackgroundColor = tagBackgroundColor;
      return this;
    }

    public Builder withTagBorderColor(@ColorRes Integer tagBorderColor) {
      this.tagBorderColor = tagBorderColor;
      return this;
    }

    public Builder withBtnOkText(String text) {
      this.btnOkText = text;
      return this;
    }

    /**
     * @param minLimit from -1 to .... <br/> -1 is default, and wont control minimum of selected
     * items.In other word -1 make LOV Optional.
     * @return {@link Builder} instance
     */
    public Builder withMinLimit(@IntRange(from = -1) int minLimit) {
      this.minLimit = minLimit;
      return this;
    }

    /**
     * @param maxLimit from -1 to .... <br/> -1 is default, and wont control
     * <strong>maximum</strong> of selected items.
     * @return {@link Builder} instance
     */
    public Builder withMaxLimit(@IntRange(from = -1) int maxLimit) {
      this.maxLimit = maxLimit;
      return this;
    }

    public Property build() {
      return new Property(this);
    }
  }
}

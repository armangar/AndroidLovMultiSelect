package com.mojtaba_shafaei.android.library;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({ViewStatus.DISPOSED
    , ViewStatus.LOADING
    , ViewStatus.READY
    , ViewStatus.SAVING
    , ViewStatus.SAVING_DELETES
    , ViewStatus.DOWNLOADING
    , ViewStatus.CHECKING})

@Retention(RetentionPolicy.SOURCE)
public @interface ViewStatus {

  int DISPOSED = -1;
  int LOADING = 1;
  int READY = 2;
  int SAVING = 3;
  int SAVING_DELETES = 4;
  int DOWNLOADING = 5;
  int CHECKING = 6;
}

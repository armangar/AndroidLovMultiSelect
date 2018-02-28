package com.mojtaba_shafaei.android.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    List<Job> jobs = new ArrayList<>();
    jobs.add(new Job("1", "ONE"));
    jobs.add(new Job("2", "TWO"));
    jobs.add(new Job("3", "THREE"));
    jobs.add(new Job("4", "FOUR"));
    jobs.add(new Job("5", "FIVE"));
    jobs.add(new Job("6", "SIX"));
    jobs.add(new Job("7", "SEVEN"));

    findViewById(R.id.btn_call_lov).setOnClickListener(
        view -> LovMultiSelect.startForResult(MainActivity.this,
            11,
            jobs,
            null));
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

  }
}

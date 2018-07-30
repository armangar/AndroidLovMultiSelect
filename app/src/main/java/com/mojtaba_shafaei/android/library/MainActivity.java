package com.mojtaba_shafaei.android.library;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.mojtaba_shafaei.android.LovMultiSelect;
import com.mojtaba_shafaei.android.LovMultiSelect.Item;
import com.mojtaba_shafaei.android.Property;
import java.util.ArrayList;
import java.util.List;
import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    List<Job> defaults = new ArrayList<>();
    defaults.add(new Job("1", "یک"));
    defaults.add(new Job("5", "پنجمین آیتم از لیست موجود این میباشد"));
    defaults.add(new Job("9", "NINE"));
    defaults.add(new Job("14", "FOURTEEN"));

    Typeface typeface = Typeface.createFromAsset(getAssets(), "IRANSansMobile.ttf");

    findViewById(R.id.btn_call_lov).setOnClickListener(
        view -> LovMultiSelect.startForResult(MainActivity.this,
            11,
            findViewById(R.id.btn_call_lov),
            typeface,
            Property.newBuilder()
//                .withButtonOkBackgroundDrawable(R.drawable.btn_ok_bg)
                .withTagBackgroundColor(R.color.colorPrimary)
                .withTagBorderColor(R.color.colorAccent)
                .withBtnOkText("باشه")
                .withMinLimit(1)
                .withMaxLimit(3)
                .build(),
            new JobFetcher().fetch(),
            new ArrayList<>(defaults)
        ));
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      List<Item> returnedData = Parcels.unwrap(data.getParcelableExtra("data"));
      String result = "";
      for (Item dd : returnedData) {
        Log.d("MainActivity", "onActivityResult: " + dd);
        result += dd.toString() + "\n\n";
      }
      ((TextView) findViewById(R.id.tvResult)).setText(result);
    }

  }
}

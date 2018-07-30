package com.mojtaba_shafaei.android.library;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.mojtaba_shafaei.android.LovMultiSelect;
import com.mojtaba_shafaei.android.LovMultiSelect.Item;
import com.mojtaba_shafaei.android.Property;
import java.util.ArrayList;
import java.util.List;

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
        view -> LovMultiSelect.start(getSupportFragmentManager()
            , typeface
            , Property.newBuilder()
                .withButtonOkBackgroundTint(
                    ContextCompat.getColorStateList(this, R.color.colors_btn))
                .withTagBackgroundColor(R.color.colorPrimary)
                .withTagBorderColor(R.color.colorAccent)
                .withBtnOkText("باشه")
                .withMinLimit(1)
                .withMaxLimit(3)
                .build(),
            new JobFetcher().fetch(),
            new ArrayList<>(defaults),
            items -> {
              String result = "";
              for (Item dd : items) {
                Log.d("MainActivity", "onActivityResult: " + dd);
                result += dd.toString() + "\n\n";
              }
              ((TextView) findViewById(R.id.tvResult)).setText(result);
            }
            , cancelListener -> {
              Log.d("MainActivity", "cancelled");
            }
            , dismissListener -> {
              Log.d("MainActivity", "dismissed");
            }
        ));
  }
}

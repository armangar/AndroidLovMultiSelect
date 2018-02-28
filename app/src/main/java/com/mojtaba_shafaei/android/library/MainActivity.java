package com.mojtaba_shafaei.android.library;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_call_lov).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LovMultiSelect.start(view.getContext());
            }
        });
    }


}

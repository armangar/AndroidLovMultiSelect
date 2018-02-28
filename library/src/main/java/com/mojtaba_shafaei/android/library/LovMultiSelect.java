package com.mojtaba_shafaei.android.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class LovMultiSelect extends AppCompatActivity {
    public static void start(Context context) {
        Intent starter = new Intent(context, LovMultiSelect.class);
        //starter.putExtra();
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lov_multi_select_layout1);


    }
}

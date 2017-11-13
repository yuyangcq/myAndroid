package com.dhu.android.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.dhu.android.R;

public class SelectionActivity extends Activity {
    private ImageView scene1;
    private ImageView scene2;
    private ImageView scene3;
    private ImageView scene4;



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection);

        scene1 =(ImageView)findViewById(R.id.scene1);
        scene2 =(ImageView)findViewById(R.id.scene2);
        scene3 =(ImageView)findViewById(R.id.scene3);

        scene1.setOnClickListener(new scene1_Listener());

    }

    class scene1_Listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(SelectionActivity.this, MainActivity.class);
            SelectionActivity.this.startActivity(intent);
        }
    }
}

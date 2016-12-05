package kr.soen.termproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;


public class Statistics extends Activity{


    TextView sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        sure = (TextView)findViewById(R.id.result);
        sure.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = getIntent();

        sure.setText(intent.getStringExtra("final"));

    }
}

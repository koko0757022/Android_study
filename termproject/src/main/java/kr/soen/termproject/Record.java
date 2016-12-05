package kr.soen.termproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class Record extends Activity{

    EditText Record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        Record = (EditText)findViewById(R.id.record);

    }

    public void mOnClick(View v){
        switch(v.getId()){
            case R.id.store:
                Intent intent = new Intent();
                intent.putExtra("RecordOut",Record.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
                Log.d("TAG","저장완료");
                break;
            case R.id.cancel:
                setResult(RESULT_CANCELED);
                finish();
                Log.d("TAG","취소");
                break;
        }
    }
}

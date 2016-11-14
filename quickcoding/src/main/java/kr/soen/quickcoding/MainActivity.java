package kr.soen.quickcoding;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.LinkedList;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinkedList<String> s = new LinkedList<>();
        final LinkedList<Integer> i = new LinkedList<>();
        Button btn = (Button)findViewById(R.id.button);
        Button btn2 = (Button)findViewById(R.id.button2);
        final EditText edit = (EditText)findViewById(R.id.edit);
        final TextView t1 = (TextView)findViewById(R.id.textView3);
        final TextView t2 = (TextView)findViewById(R.id.textView5);

        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                String value =  edit.getText().toString();

                try {
                    int value2 =  Integer.parseInt(value);
                    i.add(value2);
                }catch(NumberFormatException e){
                    s.add(value);
                }
            }
        });
        btn2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                for(Integer num : i){
                    t1.setText(num);
                }
                for(String num : s){
                    t2.setText(num);
                }
            }
        });
    }
}

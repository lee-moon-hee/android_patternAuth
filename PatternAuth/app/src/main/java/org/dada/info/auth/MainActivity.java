package org.dada.info.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.main_pattern_btn)).setOnClickListener(this);
        ((Button)findViewById(R.id.main_pin_btn)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.main_pattern_btn:{
                startActivity(new Intent(MainActivity.this, PatternLockActivity.class));
                break;
            }
            case R.id.main_pin_btn:{
                startActivity(new Intent(this, PinLockActivity.class));
                break;
            }


        }
    }
}
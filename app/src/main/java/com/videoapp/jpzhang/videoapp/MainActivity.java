package com.videoapp.jpzhang.videoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.videoapp.jpzhang.videoapp.activity.LoginActivity;
import com.videoapp.jpzhang.videoapp.activity.RegisterActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login = findViewById(R.id.btn_login);
        login.setOnClickListener(this);
        Button register = findViewById(R.id.btn_register);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_login:
                Intent intent1 = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_register:
                Intent intent2 = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent2);
                break;
        }
    }
}

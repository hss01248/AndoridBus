package com.hss01248.androidbusdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hss01248.bus.AndroidBus;
import com.hss01248.bus.BusObserver;
import com.hss01248.login.LoginEvent2;
import com.hss01248.login.LogoutEvent2;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        observers();
    }

    private void observers() {
        AndroidBus.observer(true, null, new BusObserver<LoginEvent2>() {
            @Override
            public void observer(LoginEvent2 obj) {
                Log.i("observer","once, no life "+ obj);
            }
        });
        AndroidBus.observer(true, this, new BusObserver<LoginEvent2>() {
            @Override
            public void observer(LoginEvent2 obj) {
                Log.i("observer","once, with life "+ obj);
            }
        });


        AndroidBus.observer(false, null, new BusObserver<LoginEvent2>() {
            @Override
            public void observer(LoginEvent2 obj) {
                Log.i("observer","not once, no life-> forever "+ obj);
            }
        });

   /*     AndroidBus.observer(false, this, new BusObserver<LoginEvent2>() {
            @Override
            public void observer(LoginEvent2 obj) {
                Log.i("observer","not once, with life "+ obj);
            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AndroidBus.post(new LoginEvent2("user detail...onDestroy"));
    }

    public void login(View view) {
        AndroidBus.post(new LoginEvent2("user detail..."));
    }

    public void logout(View view) {
        AndroidBus.post(new LogoutEvent2().setFromLoginPage(true));
    }

    public void addObserver(View view) {
        AndroidBus.observer(false, this, new BusObserver<LoginEvent2>() {
            @Override
            public void observer(LoginEvent2 obj) {
                Log.i("observer","not once, with life 3 "+ obj);
            }
        });
    }
}
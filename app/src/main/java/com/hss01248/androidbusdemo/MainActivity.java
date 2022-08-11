package com.hss01248.androidbusdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hss01248.bus.AndroidBus;
import com.hss01248.bus.BusObserver;
import com.hss01248.bus.GenericClassUtil;
import com.hss01248.bus.ContextBusObserver;

import com.hss01248.bus.NoOuterRefBusObserver;
import com.hss01248.login.LoginLogOutEvent;
import com.hss01248.login.LoginLogoutObserver;
import com.hss01248.status.foreground.AppForegroundBackgroundEvent;
import com.hss01248.status.foreground.AppForegroundBackgroundManager;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        observers();
    }

    private void observers() {
        AndroidBus.observer( null, new BusObserver<LoginLogOutEvent>() {
            @Override
            public void observer(LoginLogOutEvent obj) {
                Log.i("observer","once, no life "+ obj);
            }
        });
        AndroidBus.observer( this, new BusObserver<LoginLogOutEvent>() {
            @Override
            public void observer(LoginLogOutEvent obj) {
                Log.i("observer","once, with life "+ obj);
            }
        });


        AndroidBus.observer( null, new BusObserver<LoginLogOutEvent>() {
            @Override
            public void observer(LoginLogOutEvent obj) {
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
        AndroidBus.post(LoginLogOutEvent.successLogout());
    }

    public void login(View view) {
        AndroidBus.post( LoginLogOutEvent.successLogin("user detail...."));
    }

    public void logout(View view) {
        AndroidBus.post(new LogoutEvent2().setFromLoginPage(true));
    }

    public void addObserver(View view) {
        AndroidBus.observer( this, new BusObserver<LoginLogOutEvent>() {
            @Override
            public void observer(LoginLogOutEvent obj) {
                Log.i("observer","not once, with life 3 "+ obj);
            }
        });

        LoginLogoutObserver.observer( null, new LoginLogoutObserver<Object>() {
            @Override
            public void login(Object userDetail) {

            }

            @Override
            public void logout(boolean isFromLoginPage) {

            }
        });
    }

    public void loginByTag(View view) {
        AndroidBus.postByTag("userAction",LoginLogOutEvent.successLogout());
    }

    public void registerLoginByTag(View view) {
        AndroidBus.observerByTag("userAction",null,new BusObserver<LoginLogOutEvent>(){

            @Override
            public void observer(LoginLogOutEvent obj) {
                Log.e(AndroidBus.TAG,"tag -> userAction:"+ obj);
            }
        });

        AndroidBus.observerByTag("userAction", new ContextBusObserver<LoginLogOutEvent>(this) {
            @Override
            protected void doObserverReally(LoginLogOutEvent obj) {
                Log.e(AndroidBus.TAG,"tag -> ContextBusObserver userAction:"+ obj);
            }
        });
    }

    public void registerAppForeground(View view) {
        AppForegroundBackgroundManager.observer(null, new NoOuterRefBusObserver<AppForegroundBackgroundEvent>() {
            {
                Log.w(AndroidBus.TAG,"this$0: "+ Arrays.toString(getClass().getDeclaredFields()));
                //final com.hss01248.androidbusdemo.MainActivity com.hss01248.androidbusdemo.MainActivity$7.this$0
                // 持有外部类引用; this$0  final, 不可置空
            }

            @Override
            public void observer(AppForegroundBackgroundEvent obj) {
                Log.e(AndroidBus.TAG,"app foreground: "+ obj);

            }
        });


    }

    public void fromClass(View view) {
        Class genericsClass = GenericClassUtil.getGenericFromSuperClass(GenericTest2Impl.class, 0);
        Log.w("class","genericsClass fromClass:"+genericsClass);



    }

    public void fromInterface(View view) {
        Class genericsClass = GenericClassUtil.getGenericFromInterfaces(GenericTest1Impl.class, 0,1);
        Log.w("class","genericsClass fromInterface:"+genericsClass);
    }

    public void gson(View view) {
        Class genericsClass =new TypeToken<List<String>>(){}.getClass();
        Type type = new TypeToken<List<String>>(){}.getRawType();
        Log.w("class","TypeToken class:"+genericsClass+", \n raw type: "+type);
        //Class genericsClass2 = GenericClassUtil2.getGenericFromSuperClass(genericsClass, 0);
        //Log.w("class","genericsClass from gson:"+genericsClass2);
        //gson的getRawType被覆写,所以无效
    }

    public void myTypeToken(View view) {
        Class genericsClass =GenericTest2Impl.class;
       // Class genericsClass2 = GenericClassUtil2.getGenericFromSuperClass(genericsClass, 0);
        //Log.w("class","genericsClass from MyTypeToken:"+genericsClass2);
    }
}
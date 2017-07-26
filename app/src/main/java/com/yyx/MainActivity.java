package com.yyx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gresturepassword.GresturePasswordView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GresturePasswordView view = (GresturePasswordView) findViewById(R.id.password);
        view.setCallback(new GresturePasswordView.ViewCallback() {
            @Override
            public void init(GresturePasswordView view) {
                try {
                    view.setType(GresturePasswordView.GetPassword, "0584");
                }catch (Exception e){
                    Log.e("201707261250",e.toString());
                }
            }

            @Override
            public void checkPassword(GresturePasswordView view, boolean check, String printfpassword) {
                if (check){
                    Toast.makeText(MainActivity.this,printfpassword,Toast.LENGTH_SHORT).show();
                }else
                {
                    view.setStauts(GresturePasswordView.Error);
                    view.shakeAnimation();
                }
            }

            @Override
            public void getPassword(GresturePasswordView view, String password) {
                if (password!= null && !password.isEmpty())
                    Toast.makeText(MainActivity.this,password,Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.adiputra.sewainbali;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    String frgm = "kosong";

    FragmentManager manager = getSupportFragmentManager();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    manager.popBackStack();
                    manager.beginTransaction()
                            .add(R.id.frmMain, new HomeFragment())
                            .addToBackStack("fragment")
                            .commit();
//                    getSupportActionBar().hide();
//                    getSupportActionBar().setTitle("Sewain");
                    return true;
                case R.id.navigation_chat:
                    manager.popBackStack();
                    manager.beginTransaction()
                            .add(R.id.frmMain, new ChatFragment())
                            .addToBackStack("fragment")
                            .commit();
//                    getSupportActionBar().hide();
//                    getSupportActionBar().setTitle("Chat");
                    return true;
                case R.id.navigation_history:
                    manager.popBackStack();
                    manager.beginTransaction()
                            .add(R.id.frmMain, new HistoryFragment())
                            .addToBackStack("fragment")
                            .commit();
//                    getSupportActionBar().hide();
//                    getSupportActionBar().setTitle("History");
                    return true;
                case R.id.navigation_account:
                    manager.popBackStack();
                    manager.beginTransaction()
                            .add(R.id.frmMain, new AccountFragment())
                            .addToBackStack("fragment")
                            .commit();
//                    getSupportActionBar().show();
                    getSupportActionBar().setTitle("Account");
//                    mTextMessage.setText(R.string.title_account);
                    return true;
            }
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
//To do//
                            return;
                        }

// Get the Instance ID token//
                        String token = task.getResult().getToken();
                        String msg = getString(R.string.fcm_token, token);
                        Preferences.setKeyFcm(MainActivity.this,msg);
                        Log.d("TAGTAGTAG", msg);

                    }
                });

        frgm = getIntent().getStringExtra("FRAGMENT");

        if (frgm == null){
            manager.popBackStack();
            manager.beginTransaction()
                    .add(R.id.frmMain, new HomeFragment())
                    .addToBackStack("fragment")
                    .commit();
        } else if (frgm.equals("chat")){
            manager.popBackStack();
            manager.beginTransaction()
                    .add(R.id.frmMain, new ChatFragment())
                    .addToBackStack("fragment")
                    .commit();
        }
        getWindow().setStatusBarColor(Color.parseColor("#262ca6"));
        getSupportActionBar().hide();
//        getSupportActionBar().setTitle("Sewain");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed(){
//        this.finish();
        moveTaskToBack(true);
    }


}

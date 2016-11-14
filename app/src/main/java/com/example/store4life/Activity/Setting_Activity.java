package com.example.store4life.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.store4life.Controller.Nofication;
import com.example.store4life.R;

import java.util.Calendar;

public class Setting_Activity extends AppCompatActivity {
    Switch aSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Cài Đặt");
        aSwitch = (Switch) findViewById(R.id.OnOff);

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("setting", MODE_PRIVATE).edit();
                editor.putBoolean("status", aSwitch.isChecked());
                editor.commit();
            }
        });

        SharedPreferences prefs = getSharedPreferences("setting", MODE_PRIVATE);
        boolean switchState = prefs.getBoolean("status", true);

        if(switchState){
            aSwitch.setChecked(true);
            Toast.makeText(getApplication(),"Ban Da Bat Tinh Nang Nhan Thong Bao!",Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY,10);
            calendar.set(Calendar.MINUTE,40);
            calendar.set(Calendar.SECOND,0);
            Intent intent = new Intent(getApplication(),Nofication.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager =(AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(alarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);

        } else {
            PackageManager pm  = getApplication().getPackageManager();
            ComponentName componentName = new ComponentName(getApplication(), Nofication.class);
            pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            aSwitch.setChecked(false);
            Toast.makeText(getApplication(),"Ban Da Tat Tinh Nang Nhan Thong Bao!",Toast.LENGTH_SHORT).show();
        }
    }


}

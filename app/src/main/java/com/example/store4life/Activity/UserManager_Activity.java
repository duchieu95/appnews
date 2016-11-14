package com.example.store4life.Activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Controller.SessionManager;
import com.example.store4life.Database.Database_4life;
import com.example.store4life.MainActivity;
import com.example.store4life.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserManager_Activity extends AppCompatActivity {

    Button btnYeuThich, btnLichSu,btndangnhap,btnDangXuat,btnThaydoi;
    SessionManager session;
    Database_4life db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Tài Khoản");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplication());
        CheckLogin();

    }

    private void LogOut(){
        session.setLogin(false);
        db.LogOutUser();
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void CheckLogin() {
        if(!session.isLoggedIn())
        {
            setContentView(R.layout.nologin);
            btndangnhap = (Button) findViewById(R.id.button);

            btndangnhap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplication(),Login_Activity.class);
                    startActivity(intent);
                }
            });
        }else{
            setContentView(R.layout.activity_user_manager);
            db = new Database_4life(getApplication());
            HashMap<String, String> user = db.GetUserHeader();
            CircleImageView ivUser = (CircleImageView) findViewById(R.id.imgUSER);
            TextView Ten = (TextView) findViewById(R.id.txtTen);
            TextView Email = (TextView) findViewById(R.id.txtMail);
            TextView DT = (TextView) findViewById(R.id.txtDT);
            String img = user.get("HINH");
            String ten = user.get("TEN");
            String mail = user.get("EMAIL");
            String dt = user.get("SDT");
            String_To_ImageView(img,ivUser);
            Ten.setText(ten);
            Email.setText(mail);
            DT.setText(dt);

            btnThaydoi = (Button) findViewById(R.id.btnThayDoi);
            btnDangXuat = (Button) findViewById(R.id.btnDangXuat);

            btnLichSu = (Button) findViewById(R.id.btnLichSu);

            btnYeuThich = (Button) findViewById(R.id.btnYeuThich);

            btnDangXuat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       LogOut();

                   }
               });
                }
            });

            btnYeuThich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplication(),LikeProduct_Activity.class);
                    startActivity(intent);
                }
            });

            btnLichSu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplication(),History_Activity.class);
                    startActivity(intent);
                }
            });


            btnThaydoi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplication(),ChangeInfo_Activity.class);
                    startActivity(intent);
                }
            });
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void String_To_ImageView(String strBase64, ImageView iv){
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        iv.setImageBitmap(decodedByte);
    }
}

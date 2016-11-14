package com.example.store4life;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.store4life.Activity.Cart_Activity;
import com.example.store4life.Activity.Login_Activity;
import com.example.store4life.Activity.Search_Activity;
import com.example.store4life.Activity.Setting_Activity;
import com.example.store4life.Activity.UserManager_Activity;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Controller.SessionManager;
import com.example.store4life.Database.Database_4life;
import com.example.store4life.Fragment.Aboutus_Fragment;
import com.example.store4life.Fragment.Category_Fragment;
import com.example.store4life.Fragment.Home_Fragment;
import com.example.store4life.Fragment.News_Fragment;
import com.example.store4life.Fragment.Support_Fragment;
import com.example.store4life.Model.Image;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    MenuItem searchItem, micro;
    SessionManager sessionManager;
    Database_4life db;
    TextView txtMail, txtName;
    CircleImageView iv;
    private SearchView searchView;
    private static final int VOICE_RECONGNITION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new Database_4life(getApplication());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open, R.string.close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.content_main, new Home_Fragment()).commit();
        setTitle("Trang Chủ");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        txtMail = (TextView) header.findViewById(R.id.txtMAIL);
        txtName = (TextView) header.findViewById(R.id.txtTEN);
        iv = (CircleImageView) header.findViewById(R.id.imageView);
        sessionManager = new SessionManager(getApplication());
        if (!sessionManager.isLoggedIn()) {

            txtMail.setText("Bạn Chưa Đăng Nhập");

        } else {
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   HashMap<String, String> user = db.GetUserHeader();
                   String hinh = user.get("HINH");
                   String mail = user.get("EMAIL");
                   String name = user.get("TEN");
                   txtMail.setText(mail);
                   txtName.setText(name);
                   String_To_ImageView(hinh, iv);
               }
           });}

    }

    public void String_To_ImageView(String strBase64, ImageView iv) {
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        iv.setImageBitmap(decodedByte);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        searchItem = menu.findItem(R.id.search);
        micro = menu.findItem(R.id.micro);
        micro.setVisible(false);

        MenuItem myActionMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(getApplicationContext(), "Close", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplication(), Search_Activity.class);
                String a = query.replace(" ", "%20");
                intent.putExtra("cc", a);
                startActivity(intent);
                return false;

            }


            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    Log.d("", "" + newText.length());
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.search) {
            micro.setVisible(true);
            backsearch();
            return true;
        }
        if (id == R.id.micro) {
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.EXTRA_RESULTS);
            i.putExtra(RecognizerIntent.EXTRA_RESULTS, Locale.getDefault());
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Nói Bây Giờ");
            startActivityForResult(i, VOICE_RECONGNITION_REQUEST_CODE);
        }
        if (id == R.id.cart) {
            Intent intent = new Intent(getApplication(), Cart_Activity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void backsearch(){
        searchItem.expandActionView();
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                micro.setVisible(false);
                FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                xfragmentTransaction.replace(R.id.content_main,new Home_Fragment()).commit();
                setTitle("Trang Chủ");
                return true;
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
           FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.replace(R.id.content_main,new Home_Fragment()).commit();
            setTitle("Trang Chủ");
        } else if (id == R.id.nav_user) {
            if (!sessionManager.isLoggedIn()) {
                Intent intent = new Intent(getApplication(), Login_Activity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplication(), UserManager_Activity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_cate) {
           FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.replace(R.id.content_main,new Category_Fragment()).commit();
            setTitle("Danh Mục");
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(getApplication(), Setting_Activity.class);
            startActivity(intent);
        }else if(id == R.id.nav_news){
            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.replace(R.id.content_main,new News_Fragment()).commit();
            setTitle("Tin Tức");
        }else if (id == R.id.nav_support) {
            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.replace(R.id.content_main,new Support_Fragment()).commit();
            setTitle("Hỗ Trợ Và Liên Hệ");
        }else if (id == R.id.nav_about_us) {
            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.replace(R.id.content_main,new Aboutus_Fragment()).commit();
            setTitle("Về Chúng Tôi");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    //trả dữ liệu bằng âm thanh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            ArrayList<String> texMatchlist = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String result = "";
            for(String s : texMatchlist){
                result = s;
            }
            searchView.onActionViewExpanded();
            searchView.setQuery(result,false);
            searchView.setFocusable(true);
            new java.util.Timer().schedule(

                    new java.util.TimerTask() {

                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplication(),Search_Activity.class);
                            startActivity(intent);
                        }
                    },
                    2000

            );
        }
        super.onActivityResult(requestCode, resultCode, data );

    }

}

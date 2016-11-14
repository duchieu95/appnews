package com.example.store4life.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.store4life.Controller.CheckNetworkConnection;
import com.example.store4life.MainActivity;
import com.example.store4life.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.store4life.Activity.Cart_Activity;
import com.example.store4life.Activity.DetailCategory_Activity;
import com.example.store4life.Adapter.DetailCategory_Adapter;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Model.Product;
import com.example.store4life.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Search_Activity extends AppCompatActivity {

    View view;

    private static String TAG = "timkiem";
    public static String TAG_SUCCES = "success";
    String myJSON;
    JSONArray jsonArray = null;
    RecyclerView recyclerView;
    MenuItem searchItem,micro;
    ArrayList<Product> list = new ArrayList<>();
    DetailCategory_Adapter adapter;
    String TENSP ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Tìm Kiếm");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (CheckNetworkConnection.isConnectionAvailable(getApplication())) {
            setContentView(R.layout.activity_search);
            Intent intent = getIntent();
            String nhan = intent.getStringExtra("cc");
            recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_Search);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplication(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            getData(AppConfig.URL_SEARCH + nhan);
        }else {
            setContentView(R.layout.nointernet);
        }
    }



    //nhận giá trị từ server trả về
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            int success = jsonObj.getInt(TAG_SUCCES);
            if(success == 1) {
                jsonArray = jsonObj.getJSONArray(TAG);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    list.add(new Product(
                            c.getInt("ID_SP"),
                            c.getString("TENSP"),
                            c.getString("HinhSP"),
                            c.getInt("GIASP"),
                            c.getInt("ID_LOAI")));
                }
                adapter = new DetailCategory_Adapter(getApplication(), list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else {
            setContentView(R.layout.nodata);
                ImageView imageView = (ImageView) findViewById(R.id.logo);
                int color = Color.parseColor("#FF1493");
                imageView.setColorFilter(color);
                Button btnMua = (Button) findViewById(R.id.btnMuamoi);
                btnMua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplication(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(final String URL){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost(URL);
                httppost.setHeader("Content-type", "application/json");
                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
                Log.d("Da Doc",result);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
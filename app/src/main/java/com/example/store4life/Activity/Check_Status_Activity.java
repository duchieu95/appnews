package com.example.store4life.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.store4life.Adapter.DetailBill_Adapter;
import com.example.store4life.Adapter.History_Adapter;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Model.History;
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
import java.util.List;

public class Check_Status_Activity extends AppCompatActivity {
    private static final String SUCCESS = "success";
    private static String TAG = "chitiet";
    String myJSON;
    JSONArray jsonArray = null;
    List<Product> list = new ArrayList<>();
    RecyclerView recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check__status_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Chi Tiết Hóa Đơn");
        recycler = (RecyclerView) findViewById(R.id.recycler_LichSu);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        int ID_HD = intent.getIntExtra("ID_HD",1);
        getData(AppConfig.URL_GETDETAILHD+ID_HD);
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

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
                jsonArray = jsonObj.getJSONArray(TAG);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    list.add(new Product(
                            c.getInt("ID_SP"),
                            c.getString("TENSP"),
                            c.getString("HinhSP"),
                            c.getInt("GIASP"),
                            c.getInt("SOLUONG"),
                            c.getString("NGAYTAO_HD")
                    ));
                }
            DetailBill_Adapter adater = new DetailBill_Adapter(getApplication(),list);
            recycler.setAdapter(adater);
            adater.notifyDataSetChanged();
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
                Log.d("ChiTiet",result);
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
}

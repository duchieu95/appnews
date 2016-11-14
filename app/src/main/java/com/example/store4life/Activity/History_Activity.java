package com.example.store4life.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.store4life.Adapter.DetailCategory_Adapter;
import com.example.store4life.Adapter.History_Adapter;
import com.example.store4life.Adapter.LikeProduct_Adapter;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Database.Database_4life;
import com.example.store4life.Model.Cart;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class History_Activity extends AppCompatActivity {
    private static final String SUCCESS = "success";
    RecyclerView recyclerView;
    List<History> list ;
    History_Adapter adapter;
    private static String TAG = "hoadon";
    private static String TAG_TONG = "TONGTIEN";
    String myJSON;
    JSONArray jsonArray = null;
    Database_4life db;
    TextView tong;
    String IDTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_LichSu);
        setTitle("Lịch Sử Giao Dịch");
        list = new ArrayList<>();
        db = new Database_4life(getApplication());
        HashMap<String, String> user = db.GetUserHeader();
        IDTV = user.get("ID_TV");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getData();
        getURL();
    }



    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);

            int succes = jsonObj.getInt(SUCCESS);
            if(succes == 1) {
                jsonArray = jsonObj.getJSONArray(TAG);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject c = jsonArray.getJSONObject(i);
                    list.add(new History(
                            c.getInt("ID_HD"),
                            c.getString("NGAYTAO_HD"),
                            c.getInt("TONGTIEN"),
                            c.getInt("ID_TV"),
                            c.getInt("TRANGTHAI")
                    ));
                }
                adapter = new History_Adapter(getApplication(), list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else {
                Message();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost(AppConfig.URL_GETHOADON+IDTV);
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
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }


    //check tong tien
    protected void SHOWTONGTIEN(){
       String a = null;
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            int sucess = jsonObj.getInt(SUCCESS);
            if(sucess  == 1) {
                jsonArray = jsonObj.getJSONArray(TAG_TONG);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    a = c.getString("tien");
                }
              tong = (TextView) findViewById(R.id.txtTongTien);
                if (a == "null") {
                    tong.setText("0");
                } else {
                    tong.setText(doitien(a));
                }
            }else{
                tong.setText("0");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getURL(){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost(AppConfig.URL_TONTIEN+IDTV);
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
                Log.d("TONG TIEN",result);
                SHOWTONGTIEN();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
    public String doitien(String x)
    {
        String chuoi=String.valueOf(x);
        Float d = Float.parseFloat(chuoi);
        DecimalFormat f=new DecimalFormat("#,### VNĐ");
        return f.format(d);
    }

    private void Message(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Thông Báo");
        dialog.setMessage("Không Có Dữ Liệu");
        dialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

}

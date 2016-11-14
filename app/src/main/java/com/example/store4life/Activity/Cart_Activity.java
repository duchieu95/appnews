package com.example.store4life.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.store4life.Adapter.Cart_Adapter;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Controller.CheckNetworkConnection;
import com.example.store4life.Database.Database_4life;
import com.example.store4life.MainActivity;
import com.example.store4life.Model.Cart;
import com.example.store4life.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Cart_Activity extends AppCompatActivity {
    private List<Cart> list = new ArrayList<>();
    private RecyclerView recycleGiohang;
    private Cart_Adapter Adt_Giohang;
    Database_4life db;
    Button btnDatHang;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Giỏ Hàng");
        if (CheckNetworkConnection.isConnectionAvailable(getApplication())) {
            setContentView(R.layout.activity_cart);
        db = new Database_4life(getApplication());
        btnDatHang = (Button) findViewById(R.id.btnDatHang);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        recycleGiohang = (RecyclerView) findViewById(R.id.recycle_giohang);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleGiohang.setLayoutManager(mLayoutManager);
        recycleGiohang.setItemAnimator(new DefaultItemAnimator());
        ShowList();

        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.checkForTables()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MessageOder();
                        }
                    });
                } else {
                    Message("Không Có Dữ Liệu");
                }
            }
        });
        }else {
            setContentView(R.layout.nointernet);
        }
    }

    private void MessageOder(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Thông Báo");
        dialog.setMessage("Bạn có muốn đặt mua sản phẩm không");
        dialog.setNegativeButton("Không",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                showDialog();
                  HashMap<String, String> ThanhVien = db.GetUserHeader();
                     final String ID_TV = ThanhVien.get("ID_TV");
                     HashMap<String, String> Tien = db.TongTien();
                     final String TongTien = Tien.get("TIEN");
                OderProduct(TongTien,ID_TV);

            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
    private void ShowList() {
        if (db.checkForTables()) {
            list = db.GetAllCart();
            Adt_Giohang = new Cart_Adapter(getApplication(), list);
            recycleGiohang.setAdapter(Adt_Giohang);
            Adt_Giohang.notifyDataSetChanged();
        } else {
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
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void Message(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Thông Báo");
        dialog.setMessage(message);
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplication(),History_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }


  private void OderProduct(final String tong,String idtv) {

      class OderAsync extends AsyncTask<String, Void, String> {


          @Override
          protected void onPreExecute() {
              super.onPreExecute();}

          @Override
          protected String doInBackground(String... params) {
              String tongtien = params[0];
              String idtvv = params[1];
              InputStream is = null;
              List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
              nameValuePairs.add(new BasicNameValuePair("TONGTIEN", tongtien));
              nameValuePairs.add(new BasicNameValuePair("ID_TV", idtvv));
              String result = null;

              try{
                  HttpClient httpClient = new DefaultHttpClient();
                  HttpPost httpPost = new HttpPost(AppConfig.URL_POSTHD);
                  httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                  HttpResponse response = httpClient.execute(httpPost);
                  HttpEntity entity = response.getEntity();
                  is = entity.getContent();
                  BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                  StringBuilder sb = new StringBuilder();
                  String line = null;
                  while ((line = reader.readLine()) != null)
                  {
                      sb.append(line + "\n");
                  }
                  result = sb.toString();
              } catch (ClientProtocolException e) {
                  e.printStackTrace();
              } catch (UnsupportedEncodingException e) {
                  e.printStackTrace();
              } catch (IOException e) {
                  e.printStackTrace();
              }
              return result;
          }

          @Override
          protected void onPostExecute(String result){
              String s = result.trim();
              if(s.equalsIgnoreCase("1")){
                  list = db.CHECK();
                  for(int i =0;i < list.size();i++){
                      OderProduct(list.get(i));
                  }
              }else {
                 Message("Có Lỗi Xảy Ra");
              }
          }
      }
      OderAsync la = new OderAsync();
      la.execute(tong,idtv);
  }

    private void OderProduct(final Cart c) {
        pDialog.setMessage("Vui Lòng Chờ...");
        class AddWorkDetails extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equalsIgnoreCase("thanhcong")){
                    new java.util.Timer().schedule(

                            new java.util.TimerTask() {

                                @Override
                                public void run() {
                                    hideDialog();

                                    DELETECART();
                                }
                            },
                            2000
                    );
                }else{
                    Message("Có Lỗi Xảy Ra! Vui Lòng Thử Lại Sau");
                    hideDialog();
                }
            }
            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String, String>();
                HashMap<String, String> ThanhVien = db.GetUserHeader();
                final String ID_TV = ThanhVien.get("ID_TV");
                data.put("ID_TV",ID_TV);
                data.put("ID_SP",String.valueOf(c.IDSP));
                data.put("SOLUONG",String.valueOf(c.SOLUONG));
                RequestHandler rh = new RequestHandler();
                String result = rh.sendPostRequest(AppConfig.URL_ORDER, data);
                return result;
            }
        }
        AddWorkDetails ru = new AddWorkDetails();
        ru.execute();
    }

    public class RequestHandler {

        public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams) {
            URL url;
            StringBuilder sb = new StringBuilder();
            try {
                url = new URL(requestURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    sb = new StringBuilder();
                    String response;
                    while ((response = br.readLine()) != null) {
                        sb.append(response);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            return result.toString();
        }
    }

    private void DELETECART(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                db.DeleteGioHang();
                Message("Đặt Mua Thành Công");
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (db != null) {
            db.close();
        }
    }
}

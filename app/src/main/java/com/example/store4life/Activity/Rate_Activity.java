package com.example.store4life.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Rating;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.store4life.Adapter.Comment_Adapter;
import com.example.store4life.Adapter.DetailCategory_Adapter;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Controller.SessionManager;
import com.example.store4life.Database.Database_4life;
import com.example.store4life.Model.Comment;
import com.example.store4life.Model.Product;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Rate_Activity extends AppCompatActivity {

    private static String TAG = "solan";
    private static String TAG_COMMENT = "BinhLuan";
    public static String TAG_SUCCES = "success";
    String myJSON;
    JSONArray jsonArray = null;
    int solan;
    Integer SAO ;
    TextView txtSolan1,txtSolan2,txtSolan3,txtSolan4,txtSolan5;
    ArrayList<Comment> bluan = new ArrayList<>();
    ListView lvBinhLuan;
    EditText edtNoiDung;
    Button btnDang;
    Database_4life db;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new Database_4life(getApplication());
        sessionManager= new SessionManager(getApplication());

        txtSolan1 = (TextView) findViewById(R.id.xem1);
        txtSolan2 = (TextView) findViewById(R.id.xem2);
        txtSolan3 = (TextView) findViewById(R.id.xem3);
        txtSolan4 = (TextView) findViewById(R.id.xem4);
        txtSolan5 = (TextView) findViewById(R.id.xem5);
        lvBinhLuan = (ListView)findViewById(R.id.danhsachbinhluan);
        edtNoiDung = (EditText) findViewById(R.id.etBinhluan);
        btnDang = (Button) findViewById(R.id.BtBinhLuan);


        setTitle("Đánh Giá Sản Phẩm");
        Intent intent = getIntent();
        final String IDsp = intent.getStringExtra("ID_SP");
        //Toast.makeText(getApplication(),IDsp,Toast.LENGTH_SHORT).show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getData(AppConfig.URL_GETRATE+"?SO_SAO="+1+"&ID_SP="+IDsp);

                getData(AppConfig.URL_GETRATE+"?SO_SAO="+2+"&ID_SP="+IDsp);

                getData(AppConfig.URL_GETRATE+"?SO_SAO="+3+"&ID_SP="+IDsp);

                getData(AppConfig.URL_GETRATE+"?SO_SAO="+4+"&ID_SP="+IDsp);

                getData(AppConfig.URL_GETRATE+"?SO_SAO="+5+"&ID_SP="+IDsp);

                getData(AppConfig.URL_GETCOMMENT+IDsp);
            }
        });


        btnDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> getUser = db.GetUserHeader();
                String ID_TV = getUser.get("ID_TV");
                String BINHLUAN = edtNoiDung.getText().toString();
                if(!sessionManager.isLoggedIn()) {
                    MessageOder();
                }else if(BINHLUAN.trim().equals("")){
                    edtNoiDung.setError("Bạn Chưa Nhập Lời Bình Luận");
                }else{
                    Dangnhanxet(ID_TV,IDsp,BINHLUAN);
                }

            }
        });
    }
    protected void showDSBL(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            jsonArray = jsonObj.getJSONArray(TAG_COMMENT);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                bluan.add(new Comment(c.getString("TEN"),
                        c.getString("Ngay"),
                        c.getString("NoiDung")));
            }
            Comment_Adapter adapter = new Comment_Adapter(getApplication(),bluan);
            lvBinhLuan.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
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
                    solan = c.getInt("luotdanhgianh");
                    SAO = c.getInt("SO_SAO");
                }
                if(SAO == 1){
                    txtSolan1.setText(solan + " Lượt Đánh Giá");
                } if(SAO == 2){
                    txtSolan2.setText(solan + " Lượt Đánh Giá");
                } if(SAO == 3){
                    txtSolan3.setText(solan + " Lượt Đánh Giá");
                } if(SAO == 4){
                    txtSolan4.setText(solan + " Lượt Đánh Giá");
                } if (SAO == 5){
                    txtSolan5.setText(solan + " Lượt Đánh Giá");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    //get data
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
                showDSBL();
                Log.d("Da Doc",result);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }


    private void Dangnhanxet(final String idtv, String idsp,String binhluan) {

        class NhanxetAsync extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();}

            @Override
            protected String doInBackground(String... params) {
                String idtv = params[0];
                String itsp = params[1];
                String binhluan = params[2];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("ID_TV", idtv));
                nameValuePairs.add(new BasicNameValuePair("ID_SP", itsp));
                nameValuePairs.add(new BasicNameValuePair("NoiDung", binhluan));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(AppConfig.URL_POSTCOMMENT);
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
                if(s.equalsIgnoreCase("ThanhCong")){

                    Toast.makeText(getApplicationContext(),"Thanh Cong",Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }else{
                    Toast.makeText(getApplicationContext(),"Loi",Toast.LENGTH_SHORT).show();

                }
            }
        }
        NhanxetAsync la = new NhanxetAsync();
        la.execute(idtv,idsp,binhluan);
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


    private void MessageOder(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Thông Báo");
        dialog.setMessage("Bạn Chưa Đăng Nhập!");
        dialog.setNegativeButton("Không",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        dialog.setPositiveButton("Đăng Nhập Ngay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(getApplication(),Login_Activity.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

}

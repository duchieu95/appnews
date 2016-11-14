package com.example.store4life.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Controller.SessionManager;
import com.example.store4life.Database.Database_4life;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;




public class Login_Activity extends AppCompatActivity {
    private static final String SUCCESS = "success";
    private static String TAGTHANHVIEN = "thanhvien";
    EditText edtEmail,edtPass;
    Button btnDangNhap;
    TextView tv1, tv2;
    SessionManager sessionManager;
    private ProgressDialog pDialog;
    String m,MK;
    String myJSON;
    JSONArray jsonArray = null;
    int a,a7;
    String a1,a3,a4,a5,a6;
    String a2;
    Database_4life db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        setTitle("Đăng Nhập");
        db = new Database_4life(getApplication());
        edtEmail = (EditText) findViewById(R.id.edtMail);
        edtPass = (EditText) findViewById(R.id.edtMatKhau);
        Intent intent = getIntent();
        String mail = intent.getStringExtra("KEY_MAIL");
        String pass = intent.getStringExtra("KEY_PASS");
        edtEmail.setText(mail);
        edtPass.setText(pass);
        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        sessionManager = new SessionManager(getApplication());

        tv1 = (TextView) findViewById(R.id.link_signup);
        tv2 = (TextView) findViewById(R.id.link_forgot);
        tv1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(getApplication(), signup_Activity.class);
                startActivity(i);
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(getApplication(), ForgotPass_Activity.class);
                startActivity(i);
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m = edtEmail.getText().toString().trim();
                String MK = edtPass.getText().toString();
                if(!isValidEmail(m)){
                    Toast.makeText(getApplication(),"Email Không Đúng Định Dạng",Toast.LENGTH_SHORT).show();

                } if(MK.length() < 8 || MK.trim().equals("") ){
                    Toast.makeText(getApplication(),"Mật Khẩu Phải Nhiều Hơn 8 Kí Tự",Toast.LENGTH_SHORT).show();
                }else{
                    pDialog.setMessage("Đang Đăng Nhập ...");
                    showDialog();
                    Login(m,MK);
                }
            }
        });

    }
    public final static boolean isValidEmail(CharSequence target)
    {
        if (TextUtils.isEmpty(target))
        {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    private void Login(final String EMAIL, String PASSWORD) {

        class LoginAsync extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();}

            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];
                String pass = params[1];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("EMAIL", uname));
                nameValuePairs.add(new BasicNameValuePair("MK", pass));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(AppConfig.URL_LOGIN);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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
                    getData();
                    sessionManager.setLogin(true);

                }else{
                    Message();
                    hideDialog();
                }
            }
        }
        LoginAsync la = new LoginAsync();
        la.execute(EMAIL,PASSWORD);
    }

    public void Message(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Lỗi");
        dialog.setMessage("Sai Email Hoặc Mật Khẩu");
        dialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            jsonArray = jsonObj.getJSONArray(TAGTHANHVIEN);
           int success = jsonObj.getInt(SUCCESS);
            if(success == 1 ){
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                a = c.getInt("ID_TV");
                a1 = c.getString("TEN");
                a2 = c.getString("HINH");
                a3 = c.getString("EMAIL");
                a4 = c.getString("MK");
                a5 = c.getString("DIACHI");
                a6 = c.getString("SDT");
                a7 = c.getInt("GioiTinh");
            }
            db.CreateTableUser(a,a1,a2,a3,a4,a5,a6,a7);
                hideDialog();
                Intent intent = new Intent(Login_Activity.this, UserManager_Activity.class);
                startActivity(intent);
                finish();
            }else{
                hideDialog();
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
                HttpPost httppost = new HttpPost(AppConfig.URL_GETUSER+m);
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
}

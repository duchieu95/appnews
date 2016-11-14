package com.example.store4life.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserDetail_Activity extends AppCompatActivity {
    private static final String SUCCESS = "success";
    private static String TAGTHANHVIEN = "thanhvien";
    String ten,mk,email,dt;
    EditText edtTen,edtMK,edtEMAIL,edtDT,edtDC;
    Button btnChonHinh,btnHoanThanh;
    private Bitmap bitmap;
    ImageView imageView;
    private Uri filePath;
    private ProgressDialog pDialog;
    SessionManager sessionManager;
    String myJSON;
    JSONArray jsonArray = null;
    int a,a7;
    String a1,a3,a4,a5,a6;
    String a2;
    Database_4life db;
    RadioGroup radioGroup;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_);

        db = new Database_4life(getApplication());


        radioGroup = (RadioGroup) findViewById(R.id.radioGioiTinh);
        edtTen = (EditText) findViewById(R.id.TEN);
        edtMK = (EditText) findViewById(R.id.MATKHAU);
        edtEMAIL = (EditText) findViewById(R.id.EMAIL);
        edtDT = (EditText) findViewById(R.id.DIENTHOAI);
        edtDC = (EditText) findViewById(R.id.DIACHI);
        imageView = (ImageView) findViewById(R.id.IMGUSER);
        btnChonHinh = (Button) findViewById(R.id.btnChonHinh);
        btnHoanThanh = (Button) findViewById(R.id.Xong);
        sessionManager = new SessionManager(getApplication());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
      Intent intent = getIntent();
       ten = intent.getStringExtra("TEN");
        dt = intent.getStringExtra("SDT");
        mk = intent.getStringExtra("MK");
        email = intent.getStringExtra("EMAIL");
        edtTen.setText(ten);
        edtMK.setText(mk);
        edtEMAIL.setText(email);
        edtDT.setText(dt);
        edtTen.setEnabled(false);
        edtMK.setEnabled(false);
        edtEMAIL.setEnabled(false);
        edtDT.setEnabled(false);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                index = radioGroup.indexOfChild(radioButton);
            }
        });
        btnHoanThanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                String dc = edtDC.getText().toString();
                Update(email,dc,ImageView_To_String(imageView),index+"");
            }
        });


        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 999);

            }
        });

    }


    public String ImageView_To_String(ImageView iv){
        BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        bmp=Bitmap.createScaledBitmap(bmp, 100,100, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String strHinh = Base64.encodeToString(byteArray, 0);
        return strHinh;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 999 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap=Bitmap.createScaledBitmap(bitmap, 100,100, true);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void Update(final String EMAIL, final String DC, final String HINH,final String GioiTinh) {

        class SignupAsync extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();}

            @Override
            protected String doInBackground(String... params) {
                String EMAIL = params[0];
                String DC = params[1];
                String HINH = params[2];
                String GioiTinh = params[3];
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("EMAIL", EMAIL));
                nameValuePairs.add(new BasicNameValuePair("DIACHI", DC));
                nameValuePairs.add(new BasicNameValuePair("HINH", HINH));
                nameValuePairs.add(new BasicNameValuePair("GioiTinh", GioiTinh));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(AppConfig.URL_UPDATEUSER);
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
                if(s.equalsIgnoreCase("1")) {
                    Toast.makeText(getApplication(),"Loi",Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    Toast.makeText(getApplication(),"Dang Ki THanh Cong",Toast.LENGTH_SHORT).show();
                    hideDialog();
                    Login(email,mk);
                }
            }
        }
        SignupAsync s = new SignupAsync();
        s.execute(EMAIL,DC,HINH,GioiTinh);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
                if(s.equalsIgnoreCase("0")){
                    Message();
                    hideDialog();
                }else {
                    getData();
                    sessionManager.setLogin(true);

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
                Intent intent = new Intent(getApplication(), UserManager_Activity.class);
                startActivity(intent);
                finish();
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
                HttpPost httppost = new HttpPost(AppConfig.URL_GETUSER+email);
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

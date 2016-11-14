package com.example.store4life.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Controller.SessionManager;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
public class signup_Activity extends AppCompatActivity{
    EditText edtTen,edtMK,edtEmail,edtDT;
    private ProgressDialog pDialog;
   String Ten,MK,MAIL,DT;
    Button btnDangki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Đăng Kí Tài Khoản");


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        edtTen = (EditText) findViewById(R.id.edtTen);

        edtMK = (EditText) findViewById(R.id.edtMK);

        edtEmail = (EditText) findViewById(R.id.edtEmail);

        edtDT = (EditText) findViewById(R.id.edtDT);

        btnDangki = (Button) findViewById(R.id.btnDangKi);

        btnDangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Ten = edtTen.getText().toString();
             MK = edtMK.getText().toString();
             MAIL = edtEmail.getText().toString();
             DT = edtDT.getText().toString();
            if(!isValidEmail(MAIL)){
                Toast.makeText(getApplication(),"Email Không Đúng Định Dạng",Toast.LENGTH_SHORT).show();
            }else if(MK.length() < 8 || MK.trim().equals("")){
                Toast.makeText(getApplication(),"Nhập Mật Khẩu Lớn Hơn Hoặc Bằng 8 kí tự và không được để trống",Toast.LENGTH_SHORT).show();
            }else if(Ten.trim().equals("")){
                Toast.makeText(getApplication(),"Tên Không Được Bỏ Trống",Toast.LENGTH_SHORT).show();
            }else if(DT.trim().equals("") || DT.length() < 10){
                Toast.makeText(getApplication(),"Số Điện Thoại Lớn Hơn Hoặc bằng 10 kí tự",Toast.LENGTH_SHORT).show();
            }
            else{
                Signup(Ten,MK,MAIL,DT);
                showDialog();
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

    private void Signup(final String Ten, final String MK, final String EMAIL, final String SDT) {
        class SignupAsync extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();}

            @Override
            protected String doInBackground(String... params) {
                String TEN = params[0];
                String MK = params[1];
                String EMAIL = params[2];
                String SDT = params[3];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("TEN", TEN));
                nameValuePairs.add(new BasicNameValuePair("MK", MK));
                nameValuePairs.add(new BasicNameValuePair("EMAIL", EMAIL));
                nameValuePairs.add(new BasicNameValuePair("SDT", SDT));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(AppConfig.URL_SINGUP);
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
              if(s.equalsIgnoreCase("0")) {
                    Message();
                  hideDialog();
                }else if(s.equalsIgnoreCase("2")){
                  Toast.makeText(getApplication(),"Loi",Toast.LENGTH_SHORT).show();
                  hideDialog();
              }
              else {
                  Toast.makeText(getApplication(),"Đăng Kí Thành Công",Toast.LENGTH_SHORT).show();
                  Intent intent = new Intent(getApplication(),UserDetail_Activity.class);
                  intent.putExtra("TEN",Ten);
                  intent.putExtra("MK",MK);
                  intent.putExtra("EMAIL",EMAIL);
                  intent.putExtra("SDT",SDT);
                  startActivity(intent);
                  finish();
              }
            }
        }
        SignupAsync s = new SignupAsync();
        s.execute(Ten,MK,EMAIL,SDT);
    }

    public void Message(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Lỗi");
        dialog.setMessage("Email Da Ton Tai,Vui Long Chon Email Khac");
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
}

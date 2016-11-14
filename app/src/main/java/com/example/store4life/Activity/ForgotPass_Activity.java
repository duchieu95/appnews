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
import java.util.List;

public class ForgotPass_Activity extends AppCompatActivity {
    EditText edtTen,edtMail,edtDT;
    Button btnForgot;
    private ProgressDialog pDialog;

    String ten,email,sdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_);
        setTitle("Quên Mật Khẩu");
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        edtTen = (EditText) findViewById(R.id.edtTen);
        edtMail = (EditText) findViewById(R.id.edtMail);
        edtDT = (EditText) findViewById(R.id.edtDT);

        btnForgot = (Button) findViewById(R.id.btnQuenMK);

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             ten = edtTen.getText().toString();
             email = edtMail.getText().toString();
             sdt = edtDT.getText().toString();
              if(!isValidEmail(email)){
                  Toast.makeText(getApplication(),"Email Không Đúng Định Dạng",Toast.LENGTH_SHORT).show();
              }else if(ten.trim().equals("")){
                  Toast.makeText(getApplication(),"Tên Không Được Bỏ Trống",Toast.LENGTH_SHORT).show();
              }else if(sdt.trim().equals("")|| sdt.length() < 10){
                  Toast.makeText(getApplication(),"Số Điện Thoại Lớn Hơn Hoặc bằng 10 kí tự",Toast.LENGTH_SHORT).show();
              }else{
                  showDialog();
                  QuenMK(ten,email,sdt);
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
    private void QuenMK(final String TEN, String EMAIL,String DIENTHOAI) {

        class TimMKAsync extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();}

            @Override
            protected String doInBackground(String... params) {
                String ten = params[0];
                String mail = params[1];
                String mk = params[2];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("TEN", ten));
                nameValuePairs.add(new BasicNameValuePair("EMAIL", mail));
                nameValuePairs.add(new BasicNameValuePair("SDT", mk));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(AppConfig.URL_FORGOTPASS);
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
            protected void onPostExecute(final String result){
                String s = result.trim();
                if(s.equalsIgnoreCase("2")){
                    Message("Vui Lòng Kiểm Tra Lại");
                    hideDialog();
                }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ForgotPass_Activity.this);
                    dialog.setTitle("Mật Khẩu Mới Của Bạn Là: ");
                    dialog.setMessage(result);
                    dialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newpass= result.replace("\n","");
                            Intent intent = new Intent(getApplication(),Login_Activity.class);
                            intent.putExtra("KEY_MAIL",email);
                            intent.putExtra("KEY_PASS",newpass);
                            Log.d("PASS MOI:",newpass);
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                    hideDialog();

                }
            }
        }
        TimMKAsync la = new TimMKAsync();
        la.execute(TEN,EMAIL,DIENTHOAI);
    }

    public void Message(String mess){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Lỗi");
        dialog.setMessage(mess);
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

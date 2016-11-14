package com.example.store4life.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Database.Database_4life;
import com.example.store4life.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeInfo_Activity extends AppCompatActivity {
    Database_4life db;
    EditText etTen, etMail, etMK, etSDT, etDiachi;
    Button btThayDoi, btDoiHinh;
    RadioGroup rbGioitinh;
    RadioButton Nam,Nu;
    CircleImageView ivUser;
    int index;
    private Bitmap bitmap;
    private Uri filePath;
    String TEN,MAIL,DT,DC,GIOITINH,MK,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new Database_4life(getApplication());

        HashMap<String,String> Info = db.GetUserHeader();

        etTen = (EditText)findViewById(R.id.etTen);
        etMail = (EditText)findViewById(R.id.etMail);
        etMK = (EditText)findViewById(R.id.etMK);
        etSDT = (EditText)findViewById(R.id.etSDT);
        etDiachi = (EditText)findViewById(R.id.etDiaChi);
        rbGioitinh = (RadioGroup) findViewById(R.id.rb);
        Nam = (RadioButton)findViewById(R.id.rbNam);
        Nu = (RadioButton)findViewById(R.id.rbNu);
        btThayDoi=(Button)findViewById(R.id.btChange);
        btDoiHinh=(Button)findViewById(R.id.btnDoiHinh);
        ivUser = (CircleImageView) findViewById(R.id.imgUSER);

        String img = Info.get("HINH");
        id = Info.get("ID_TV");
        String ten = Info.get("TEN");
        String mail = Info.get("EMAIL");
        String sdt = Info.get("SDT");
        final String diachi = Info.get("DIACHI");
        final String gioitinh = Info.get("GioiTinh");

        String_To_ImageView(img,ivUser);
        etTen.setText(ten);
        etMail.setText(mail);
        etMail.setEnabled(false);
        etSDT.setText(sdt);
        etDiachi.setText(diachi);
        int check = Integer.parseInt(gioitinh);
        if(check == 0){
            Nu.setChecked(true);
        } else if(check == 1){
            Nam.setChecked(true);
        }
        rbGioitinh.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = rbGioitinh.findViewById(checkedId);
                index = rbGioitinh.indexOfChild(radioButton);

            }
        });


        btThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(etMK.getText().toString().isEmpty()){
                     Toast.makeText(getApplication(),"Vui Lòng nhập Mật khẩu và lớn hơn 7 kí tự",Toast.LENGTH_SHORT).show();
                 }if(etDiachi.getText().toString().isEmpty()){
                    Toast.makeText(getApplication(),"Vui Lòng nhập địa chỉ",Toast.LENGTH_SHORT).show();
                }else{
                    TEN = etTen.getText().toString();
                    MAIL = etMail.getText().toString();
                    DT = etSDT.getText().toString();
                    DC = etDiachi.getText().toString();
                    GIOITINH = String.valueOf(index);
                    MK = etMK.getText().toString();
                    update(id,TEN,ImageView_To_String(ivUser),MAIL,MK,DC,DT,GIOITINH);
                }

            }
        });

        btDoiHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 999);
            }
        });


    }
    private void update(final String id, String ten, String hinh, String email,String mk,String diachi, String sdt,  String gioitinh) {

        class updateAsync extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();}

            @Override
            protected String doInBackground(String... params) {
                String id = params[0];
                String ten = params[1];
                String hinh = params[2];
                String email = params[3];
                String mk = params[4];
                String diachi = params[5];
                String sdt = params[6];
                String gioitinh = params[7];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("ID_TV", id));
                nameValuePairs.add(new BasicNameValuePair("TEN", ten));
                nameValuePairs.add(new BasicNameValuePair("HINH", hinh));
                nameValuePairs.add(new BasicNameValuePair("EMAIL", email));
                nameValuePairs.add(new BasicNameValuePair("MK", mk));
                nameValuePairs.add(new BasicNameValuePair("DIACHI", diachi));
                nameValuePairs.add(new BasicNameValuePair("SDT", sdt));
                nameValuePairs.add(new BasicNameValuePair("GioiTinh", gioitinh));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(AppConfig.URL_UPDATEINFO);
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
                    Toast.makeText(getApplication(),"Thành Công",Toast.LENGTH_LONG).show();
                    int IDTV= Integer.parseInt(id);
                    db.UpdateUser(IDTV,TEN,ImageView_To_String(ivUser),MAIL,MK,DC,DT,index);
                    finish();
                }else {

                    Toast.makeText(getApplication(),"Thất Bại",Toast.LENGTH_LONG).show();

                }
            }
        }
        updateAsync la = new updateAsync();
        la.execute(id,ten,hinh,email,mk,diachi,sdt,gioitinh);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 999 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap=Bitmap.createScaledBitmap(bitmap, 100,100, true);
                ivUser.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public void String_To_ImageView(String strBase64, ImageView iv){
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        iv.setImageBitmap(decodedByte);
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

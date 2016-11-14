package com.example.store4life.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.store4life.Adapter.Cart_Adapter;
import com.example.store4life.Adapter.HomeData_Adapter;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Controller.SessionManager;
import com.example.store4life.Database.Database_4life;
import com.example.store4life.Model.Image;
import com.example.store4life.Model.Product;
import com.example.store4life.Model.SectionDataModel;
import com.example.store4life.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailProduct_Activity extends AppCompatActivity {

    ArrayList<Product> list = new ArrayList<>();
    private static String TAGGY= "goiy";
    String myJSON;
    JSONArray jsonArray = null;
    private static String TAG= "sanpham";
    private static String TAG_CT="chitiet";
    int ID_loai,ID_SP;
    ArrayList<Image> listimage = new ArrayList<>();
    ArrayList<Product> singleItem = new ArrayList<>();
    ArrayList<SectionDataModel> allSampleData = new ArrayList<>();
    HomeData_Adapter adaptera;
    ViewPager viewPager;
    RecyclerView recyclerView;
     TextView tv_ten,tv_gia,tv_mota,xemdanhgia;
    public ImageButton imbYeuThich,imbGioHang;
    String Ten,Hinh,MoTa;
    int Gia;
    Database_4life db;
    SessionManager sessionManager;
    RatingBar rt;
    int star;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new Database_4life(getApplication());
        sessionManager= new SessionManager(getApplication());
        final Intent intent = getIntent();
        final String Ten = intent.getStringExtra("TENSP");
        ID_SP = intent.getIntExtra("ID_SP",1);
        ID_loai =intent.getIntExtra("ID_LOAI",1);
        setTitle(Ten);
        list = new ArrayList<>();
        rt = (RatingBar)findViewById(R.id.rtbar);
        ImageView back = (ImageView) findViewById(R.id.back);
        ImageView next = (ImageView) findViewById(R.id.next);
        viewPager = (ViewPager)findViewById(R.id.pager);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        tv_ten = (TextView) findViewById(R.id.tv_ten);
        tv_gia = (TextView) findViewById(R.id.tv_gia);
        tv_mota = (TextView) findViewById(R.id.mota);
        xemdanhgia = (TextView) findViewById(R.id.xemdanhgia);
        imbYeuThich = (ImageButton)findViewById(R.id.imbYeuThich);
        imbGioHang = (ImageButton)findViewById(R.id.imbGioHang);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.VERTICAL, false));
        adaptera = new HomeData_Adapter(getApplication(), allSampleData);
        HashMap<String, String> user = db.GetUserHeader();
         id = user.get("ID_TV");
        GetData(AppConfig.URL_GetSlide+ID_SP);
        GetData(AppConfig.URL_GOIY+ID_loai);
        GetData(AppConfig.URL_GETDETAILPRODUCT+ID_SP);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                viewPager.setCurrentItem(currentItem-1);


            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                viewPager.setCurrentItem(currentItem+1);
            }
        });
        int color = Color.parseColor("#FF1493");
        next.setColorFilter(color);
        back.setColorFilter(color);
        imbYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sessionManager.isLoggedIn())
                {
                    MessageOder();
                }else{
                    YEUTHICH(ID_SP+"",id);

                }

            }
        });

        imbGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!sessionManager.isLoggedIn())
                {
                    MessageOder();
                }else{
                    if(db.InsertCart(ID_SP,Ten,Hinh,Gia,1,Gia,id)){
                        Toast.makeText(getApplication(),"Đã Tăng Số Lượng "+Ten+" Vào Giỏ Hàng",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplication(),"Đã Thêm "+Ten+" Vào Giỏ Hàng",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        xemdanhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(),Rate_Activity.class);
                i.putExtra("ID_SP",ID_SP+"");
                startActivity(i);
            }
        });

        rt.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                 star = (int) rating;

                if(!sessionManager.isLoggedIn())
                {
                    MessageOder();
                }else{
                    RatingSP(star+"",ID_SP+"",id);
                }
            }


        });
    }
    //danh gia san pham
    private void RatingSP(final String sosao,String idsp,String id_tv) {

        class RatingAsync extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();}

            @Override
            protected String doInBackground(String... params) {

                String so_sao = params[0];
                String id_sp = params[1];
                String id_tv = params[2];
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("SO_SAO", so_sao));
                nameValuePairs.add(new BasicNameValuePair("ID_SP", id_sp));
                nameValuePairs.add(new BasicNameValuePair("ID_TV", id_tv));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(AppConfig.URL_RATE);
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
                   Toast.makeText(getApplication(),"Bạn Đã Sửa Số Sao thành: "+ star + " Sao",Toast.LENGTH_SHORT).show();
                }else if(s.equalsIgnoreCase("0")){
                    Toast.makeText(getApplication(),"Bạn Đã Đánh Giá Sản Phẩm Này là: "+star + " Sao",Toast.LENGTH_SHORT).show();
                }
            }
        }
        RatingAsync la = new RatingAsync();
        la.execute(sosao,idsp,id_tv);
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
    class CustomPagerAdapter extends PagerAdapter {
        ArrayList<Image> list;
        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context,ArrayList<Image> list) {
            this.mContext = context;
            this.list = list;
            this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }
        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.items_pager, container, false);
            final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Hello",position+"Da click");

                }
            });

            Image s = list.get(position);
            ImageLoader.getInstance().displayImage(AppConfig.URL_FolderUpload+s.Hinh,imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                }
            });

            container.addView(itemView);
            return itemView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
////lay slideshow
    protected void showListSlide(){
        try {
            JSONObject jsonObject = new JSONObject(myJSON);
            jsonArray = jsonObject.getJSONArray(TAG_CT);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                listimage.add(new Image(c.getInt("ID_CT"),
                        c.getInt("ID_SP"),
                        c.getString("HINHCT")
                        ));
            }
           CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getApplication(),listimage);
            viewPager.setAdapter(mCustomPagerAdapter);
            mCustomPagerAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //get goi y
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            jsonArray = jsonObj.getJSONArray(TAGGY);

            for(int i = 0;i<jsonArray.length();i++){
                JSONObject c = jsonArray.getJSONObject(i);
                singleItem.add(new Product(
                        c.getInt("ID_SP"),
                        c.getString("TENSP"),
                        c.getString("HinhSP"),
                        c.getInt("GIASP"),
                        c.getInt("ID_LOAI")));
            }
            SectionDataModel dm = new SectionDataModel();
            dm.setHeaderTitle("Gợi Ý :");
            dm.setAllItemsInSection(singleItem);
            allSampleData.add(dm);
            recyclerView.setAdapter(adaptera);
            adaptera.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    ////get chi tiet san pham
    protected void showChitietSP(){
        try {

            JSONObject jsonObj = new JSONObject(myJSON);
            jsonArray = jsonObj.getJSONArray(TAG);
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject c = jsonArray.getJSONObject(i);
                c.getString("ID_SP");
                c.getString("MASP");
                Ten = c.getString("TENSP");
                Hinh= c.getString("HinhSP");
                Gia = c.getInt("GIASP");
                MoTa = c.getString("MOTA");
            }
            tv_ten.setText(Ten);
            tv_gia.setText(doitien(Gia+""));
            tv_mota.setText(MoTa);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GetData(final String URL){


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
                showListSlide();
                showChitietSP();
                showList();
                Log.d("Da Doc : ",result);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }



    //Post Yeu Thich

    private void YEUTHICH(final String ID_SP, String ID_TV) {

        class AddYT extends AsyncTask<String, Integer, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();}

            @Override
            protected String doInBackground(String... params) {
                String idsp = params[0];
                String idtv = params[1];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("ID_SP", idsp));
                nameValuePairs.add(new BasicNameValuePair("ID_TV", idtv));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(AppConfig.URL_ADDLIKE);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
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
                    Toast.makeText(getApplication(),"Sản Phẩm Đã Yêu Thích",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplication(),"Đã Thêm Sản Phẩm Vào Yêu Thích",Toast.LENGTH_SHORT).show();
                }
            }
        }
        AddYT la = new AddYT();
        la.execute(ID_SP,ID_TV);
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

    public String doitien(String x)
    {
        String chuoi=String.valueOf(x);
        double d=Double.parseDouble(chuoi);
        DecimalFormat f=new DecimalFormat("#,### VNĐ");
        return f.format(d);
    }

}
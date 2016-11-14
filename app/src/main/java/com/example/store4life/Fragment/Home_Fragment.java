package com.example.store4life.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.store4life.Activity.DetailNew_Activity;
import com.example.store4life.Adapter.HomeData_Adapter;

import com.example.store4life.Adapter.News_Adapter;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Controller.CheckNetworkConnection;
import com.example.store4life.Controller.SessionManager;
import com.example.store4life.Database.Database_4life;
import com.example.store4life.Model.Image;
import com.example.store4life.Model.News;
import com.example.store4life.Model.Product;
import com.example.store4life.Model.SectionDataModel;
import com.example.store4life.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

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
import java.util.HashMap;


public class Home_Fragment extends Fragment {

    private static String TAGKM = "khuyen_mai";
    private static String TAG1YT = "chitiet";
    private static String TAG_SLIDE ="slide";
    String myJSON;
    JSONArray jsonArray = null;

    View view;
    CarouselView carouselView;
    RecyclerView my_recycler_view;
    ArrayList<SectionDataModel> allSampleData = new ArrayList<>();
    ArrayList<Image> list = new ArrayList<>();
    SessionManager sessionManager;
    Database_4life db;
    CollapsingToolbarLayout collapsingToolbar;
    AppBarLayout appBarLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (CheckNetworkConnection.isConnectionAvailable(getActivity())) {
            view = inflater.inflate(R.layout.fragment_home,container,false);
            sessionManager = new SessionManager(getActivity());
            db = new Database_4life(getActivity());
            carouselView = (CarouselView) view.findViewById(R.id.carouselView);
           collapsingToolbar =
                    (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(" ");
             appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
            my_recycler_view = (RecyclerView) view.findViewById(R.id.my_recycler_view);
            my_recycler_view.setHasFixedSize(true);
            my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            HomeData_Adapter adapter = new HomeData_Adapter(getActivity(), allSampleData);
            my_recycler_view.setAdapter(adapter);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getData(AppConfig.URL_SLIDE);
                    getData(AppConfig.URL_GETHOME);
                    list.clear();
                    GoiYTheoGioiTinh();
                }
            });
            initCollapsingToolbar();

        } else {
            view = inflater.inflate(R.layout.nointernet,container,false);
        }


        return view;

    }


    private void initCollapsingToolbar() {

        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


///get goi y theo gioi tinh

    private void GoiYTheoGioiTinh(){
        if(!sessionManager.isLoggedIn())
        {
            getData(AppConfig.URL_GETHOME1);
        }else {
            HashMap<String,String> user = db.GetUserHeader();
            String GioiTinh = user.get("GioiTinh");
            int gt = Integer.valueOf(GioiTinh);
            if(gt==0){
                list.clear();
                getData(AppConfig.URL_GETGOIYNU);
            }else if(gt==1){
                list.clear();
                getData(AppConfig.URL_GETGOIYNAM);
            }else{
                list.clear();
               getData(AppConfig.URL_GETHOME1);
            }

        }
    }



    //show slideshow
    protected void showSlide(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            jsonArray = jsonObj.getJSONArray(TAG_SLIDE);
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                    list.add(new Image(
                            object.getInt("ID"),
                            object.getString("TieuDe"),
                            object.getString("Hinh")));
            }
            carouselView.setImageListener(imageListener);
            carouselView.setPageCount(list.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    ImageListener imageListener = new ImageListener() {

        @Override
        public void setImageForPosition(final int position, ImageView imageView) {

             final Image image = list.get(position);
             imageView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(getContext(), DetailNew_Activity.class);
                     intent.putExtra("ID",list.get(position).ID+"");
                     intent.putExtra("TenKM",list.get(position).TieuDe);
                     startActivity(intent);

                 }
             });
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                    .defaultDisplayImageOptions(defaultOptions)
                    .build();
            ImageLoader.getInstance().init(config);
            ImageLoader.getInstance().displayImage(AppConfig.URL_FolderUpload+list.get(position).Hinh,imageView, new ImageLoadingListener() {
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

        }
    };


    protected void showListKM(){
        try {
            ArrayList<Product> list = new  ArrayList<>();
            SectionDataModel dm = new SectionDataModel();
            dm.setHeaderTitle("Sản Phẩm Đang Khuyến Mãi ");
            JSONObject json = new JSONObject(myJSON);
            jsonArray = json.getJSONArray(TAGKM);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                list.add(new Product(c.getInt("ID_SP"),
                        c.getString("TENSP"),
                        c.getString("HinhSP"),
                        c.getInt("GIASP"),
                        c.getInt("ID_LOAI")));
            }

            dm.setAllItemsInSection(list);
            allSampleData.add(dm);
            HomeData_Adapter adapter = new HomeData_Adapter(getActivity(), allSampleData);
            my_recycler_view.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void showListGOIY(){
        try {
            ArrayList<Product> list = new  ArrayList<>();
            SectionDataModel dm = new SectionDataModel();
            dm.setHeaderTitle("Đề Xuất Cho Bạn ");
            JSONObject json = new JSONObject(myJSON);
            jsonArray = json.getJSONArray(TAG1YT);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                list.add(new Product(c.getInt("ID_SP"),
                        c.getString("TENSP"),
                        c.getString("HinhSP"),
                        c.getInt("GIASP"),
                        c.getInt("ID_LOAI")));
            }

            dm.setAllItemsInSection(list);
            allSampleData.add(dm);
            HomeData_Adapter  adapter = new HomeData_Adapter(getActivity(), allSampleData);
            my_recycler_view.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getData(final String URL){
        class GetData1JSON extends AsyncTask<String, Void, String> {
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
                showListKM();
                showListGOIY();
                showSlide();
            }
        }
        GetData1JSON g = new GetData1JSON();
        g.execute();
    }




}

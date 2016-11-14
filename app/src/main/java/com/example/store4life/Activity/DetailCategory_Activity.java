package com.example.store4life.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.store4life.Adapter.DetailCategory_Adapter;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Controller.SessionManager;
import com.example.store4life.Database.Database_4life;
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
import java.util.ArrayList;
import java.util.HashMap;

        public class DetailCategory_Activity extends AppCompatActivity {
            private static final String SUCCESS = "success";
            private static String KEY_NAME = "TENLOAI";
            private static String KEY_ID = "ID_LOAI";
            private static String TAG = "chitiet";
            String myJSON;
            JSONArray jsonArray = null;
            RecyclerView recyclerView;
            ArrayList<Product> list = new ArrayList<>();
            DetailCategory_Adapter adapter;
            Spinner spinner, spinner2;
            int ID_LOAI ;
            Database_4life db;
            SessionManager sessionManager;
            String Search[] = {"Tất Cả Sản Phẩm","Giá dưới 100k","Giá Từ 100k -> 150k","Giá Từ 150k -> 200k",
                    "Giá Từ 200k -> 250k","Giá Từ 250k trở lên","Sản Phẩm Đang Được Khuyến Mãi","Sản Phẩm Mới"};
            int gioitinh;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_detail_category_);
                db = new Database_4life(getApplication());
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                sessionManager = new SessionManager(getApplication());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_dropdown_item_1line, Search);
                Intent intent = getIntent();
                String Ten = intent.getStringExtra(KEY_NAME);
                setTitle(Ten);
                ID_LOAI = intent.getIntExtra(KEY_ID,1);
                int KEY = intent.getIntExtra("KEYCODE",2);
                HashMap<String,String> user = db.GetUserHeader();
                final String  GioiTinh = user.get("GioiTinh");
                if(GioiTinh != null){
                    gioitinh = Integer.valueOf(GioiTinh);
                }
                if(KEY == 99){

                    setContentView(R.layout.layout_category2);
                   arrayAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_dropdown_item_1line, Search);
                    spinner2 = (Spinner) findViewById(R.id.spinner2);
                    spinner2.setAdapter(arrayAdapter);

                    recyclerView = (RecyclerView) findViewById(R.id.recycler_product2);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.addItemDecoration(new DetailCategory_Activity.GridSpacingItemDecoration(2, dpToPx(10), true));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    LOCKM();

                }else if(KEY == 100){
                    setContentView(R.layout.layout_category2);
                    arrayAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_dropdown_item_1line, Search);
                    spinner2 = (Spinner) findViewById(R.id.spinner2);
                    spinner2.setAdapter(arrayAdapter);

                    recyclerView = (RecyclerView) findViewById(R.id.recycler_product2);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.addItemDecoration(new DetailCategory_Activity.GridSpacingItemDecoration(2, dpToPx(10), true));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    LOCDX();

                }else {

                    arrayAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_dropdown_item_1line, Search);
                    spinner = (Spinner) findViewById(R.id.spinner);
                    spinner.setAdapter(arrayAdapter);
                    recyclerView = (RecyclerView) findViewById(R.id.recycler_product);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.addItemDecoration(new DetailCategory_Activity.GridSpacingItemDecoration(2, dpToPx(10), true));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                            if (position == 1) {
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData(AppConfig.URL_LOCSP1 + ID_LOAI);
                                    }
                                });

                            } else if (position == 2) {
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        list.clear();
                                        getData(AppConfig.URL_LOCSP2 + ID_LOAI);

                                    }
                                });

                            } else if (position == 3) {
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        list.clear();
                                        getData(AppConfig.URL_LOCSP3 + ID_LOAI);

                                    }
                                });
                            } else if (position == 4) {
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        list.clear();
                                        getData(AppConfig.URL_LOCSP4 + ID_LOAI);

                                    }
                                });


                            } else if (position == 5) {
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        list.clear();
                                        getData(AppConfig.URL_LOCSP5 + ID_LOAI);
                                    }
                                });

                            } else if (position == 6) {
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        list.clear();
                                        getData(AppConfig.URL_LOCSP6 + ID_LOAI);
                                    }
                                });

                            } else if (position == 7) {
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        list.clear();
                                        getData(AppConfig.URL_LOCSP7 + ID_LOAI);
                                    }
                                });

                            } else {
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        list.clear();
                                        getData(AppConfig.URL_DETAILDM + ID_LOAI);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }


            }

            public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

                private int spanCount;
                private int spacing;
                private boolean includeEdge;

                public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
                    this.spanCount = spanCount;
                    this.spacing = spacing;
                    this.includeEdge = includeEdge;
                }

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    int position = parent.getChildAdapterPosition(view);
                    int column = position % spanCount;

                    if (includeEdge) {
                        outRect.left = spacing - column * spacing / spanCount;
                        outRect.right = (column + 1) * spacing / spanCount;

                        if (position < spanCount) {
                            outRect.top = spacing;
                        }
                        outRect.bottom = spacing;
                    } else {
                        outRect.left = column * spacing / spanCount;
                        outRect.right = spacing - (column + 1) * spacing / spanCount;
                        if (position >= spanCount) {
                            outRect.top = spacing;
                        }
                    }
                }
            }

            private int dpToPx(int dp) {
                Resources r = getResources();
                return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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


            protected void showList(){
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    int success = jsonObj.getInt(SUCCESS);
                    if(success == 1) {
                        jsonArray = jsonObj.getJSONArray(TAG);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            list.add(new Product(
                                    c.getInt("ID_SP"),
                                    c.getString("TENSP"),
                                    c.getString("HinhSP"),
                                    c.getInt("GIASP"),
                                    c.getInt("ID_LOAI")));
                        }
                    adapter = new DetailCategory_Adapter(getApplication(),list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    }else {
                     Message();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

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
                        Log.d("data",result);
                        showList();
                    }
                }
                GetDataJSON g = new GetDataJSON();
                g.execute();
            }
            private void GoiYTheoGioiTinh(){
                HashMap<String,String> user = db.GetUserHeader();
                String  GioiTinh = user.get("GioiTinh");
                if(!sessionManager.isLoggedIn())
                {
                    list.clear();
                    getData(AppConfig.URL_GETHOME1);
                }else {
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

            private void Message(){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Thông Báo");
                dialog.setMessage("Không Tìm Thấy Sản Phẩm Phù Hợp");
                dialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();


                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }



            public void LOCKM(){

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                        if (position == 1){
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                 getData(AppConfig.URL_LOCGIAKM+"GIASP=0&GIASP1=100000");
                                }
                            });

                        }
                        else if (position == 2){
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                    getData(AppConfig.URL_LOCGIAKM+"GIASP=100000&GIASP1=150000");
                                }
                            });

                        }
                        else if (position == 3){
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                    getData(AppConfig.URL_LOCGIAKM+"GIASP=150000&GIASP1=200000");
                                }
                            });
                        }
                        else if (position == 4){
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                    getData(AppConfig.URL_LOCGIAKM+"GIASP=200000&GIASP1=250000");
                                }
                            });


                        } else if (position == 5){
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                    getData(AppConfig.URL_LOCGIAKM+"GIASP=250000&GIASP1=100000000");
                                }
                            });

                        }else if (position == 6){
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                    getData(AppConfig.URL_GETKM);

                                }
                            });

                        }else if (position == 7){
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                    getData(AppConfig.URL_LOCGIAKMMOI);
                                }
                            });

                        }else {
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                    getData(AppConfig.URL_GETKM);
                                }
                            });
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            public void LOCDX(){
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                        if (position == 1){

                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(!sessionManager.isLoggedIn()){
                                        list.clear();
                                        getData(AppConfig.URL_GETHOME1);
                                    }else{
                                        if(gioitinh==0){
                                            //list.clear();
                                            getData(AppConfig.URL_LOCGIADXNU+"GIASP=0&GIASP1=100000");

                                        }else if(gioitinh==1){
                                            list.clear();
                                            getData(AppConfig.URL_LOCGIADXNAM+"GIASP=0&GIASP1=100000");
                                        }
                                    }
                                }
                            });

                        }
                        else if (position == 2){

                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(!sessionManager.isLoggedIn()){
                                        list.clear();
                                        getData(AppConfig.URL_GETHOME1);
                                    }else{
                                        if(gioitinh==0){
                                            list.clear();
                                            getData(AppConfig.URL_LOCGIADXNU+"GIASP=100000&GIASP1=150000");
                                        }else if(gioitinh==1){
                                            list.clear();
                                            getData(AppConfig.URL_LOCGIADXNAM+"GIASP=100000&GIASP1=150000");
                                        }
                                    }
                                }
                            });

                        }
                        else if (position == 3){
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {

                                    if(!sessionManager.isLoggedIn()){
                                        list.clear();
                                        getData(AppConfig.URL_GETHOME1);
                                    }else{
                                        if(gioitinh==0){
                                            list.clear();
                                            getData(AppConfig.URL_LOCGIADXNU+"GIASP=150000&GIASP1=200000");
                                        }else if(gioitinh==1){
                                            list.clear();
                                            getData(AppConfig.URL_LOCGIADXNAM+"GIASP=150000&GIASP1=200000");
                                        }
                                    }

                                }
                            });
                        }
                        else if (position == 4){
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(!sessionManager.isLoggedIn()){
                                        list.clear();
                                        getData(AppConfig.URL_GETHOME1);
                                    }else{
                                        if(gioitinh==0){
                                            list.clear();
                                            getData(AppConfig.URL_LOCGIADXNU+"GIASP=200000&GIASP1=250000");
                                        }else if(gioitinh==1){
                                            list.clear();
                                            getData(AppConfig.URL_LOCGIADXNAM+"GIASP=200000&GIASP1=250000");
                                        }
                                    }

                                }
                            });


                        } else if (position == 5){
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(!sessionManager.isLoggedIn()){
                                        list.clear();
                                        getData(AppConfig.URL_GETHOME1);
                                    }else{
                                        if(gioitinh==0){
                                            list.clear();
                                            getData(AppConfig.URL_LOCGIADXNU+"GIASP=250000&GIASP1=100000000");
                                        }else if(gioitinh==1){
                                            list.clear();
                                            getData(AppConfig.URL_LOCGIADXNAM+"GIASP=250000&GIASP1=100000000");
                                        }
                                    }
                                }
                            });

                        }else if (position == 6){
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(!sessionManager.isLoggedIn()){
                                        list.clear();
                                        getData(AppConfig.URL_GETHOME1);
                                    }else{
                                        if(gioitinh==0){
                                            list.clear();
                                            getData(AppConfig.URL_LOCGIAKMNU);
                                        }else if(gioitinh==1){
                                            list.clear();
                                            getData(AppConfig.URL_LOCGIAKMNAM);
                                        }
                                    }
                                }
                            });

                        }else if (position == 7){
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(!sessionManager.isLoggedIn()){
                                        list.clear();
                                        getData(AppConfig.URL_GETHOME1);
                                    }else{
                                        if(gioitinh==0){
                                            list.clear();
                                            getData(AppConfig.URL_LOCGIAMOINU);
                                        }else if(gioitinh==1){
                                            list.clear();
                                            getData(AppConfig.URL_LOCGIAMOINAM);
                                        }
                                    }
                                }
                            });

                        }else {
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                    GoiYTheoGioiTinh();
                                }
                            });
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }

package com.example.store4life.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.store4life.Activity.DetailCategory_Activity;
import com.example.store4life.Adapter.Category_Adapter;
import com.example.store4life.Adapter.News_Adapter;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Controller.CheckNetworkConnection;
import com.example.store4life.Model.Category;
import com.example.store4life.Model.News;
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



public class Womenclothes_Fragment extends Fragment {
    private static String KEY_NAME = "TENLOAI";
    public static String TAG = "LOAI";
    private static String KEY_ID = "ID_LOAI";
    ListView listView;
    ArrayList<Category> data = new ArrayList<>();
    Category_Adapter adapter;
    String myJSON;
    JSONArray jsonArray = null;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (CheckNetworkConnection.isConnectionAvailable(getActivity())) {
            view = inflater.inflate(R.layout.womenclothes_fragment,container,false);
            listView = (ListView) view.findViewById(R.id.listview);
            getData();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Category category = data.get(position);
                    Intent intent = new Intent(getActivity(), DetailCategory_Activity.class);
                    intent.putExtra(KEY_NAME,category.Ten);
                    intent.putExtra(KEY_ID,category.ID_LOAI);
                    Log.d("Da Chon: ","ID_LOAI = "+category.ID_LOAI);
                    startActivity(intent);
                }
            });
        } else {
            view = inflater.inflate(R.layout.nointernet,container,false);
        }
        return view;
    }
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            jsonArray = jsonObj.getJSONArray(TAG);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                data.add( new Category(c.getInt("ID_LOAI"),
                        c.getString("TENLOAI"),
                        c.getString("HINH")));
            }
            adapter = new Category_Adapter(getActivity(),data);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost(AppConfig.URL_LOAIDM+1);
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

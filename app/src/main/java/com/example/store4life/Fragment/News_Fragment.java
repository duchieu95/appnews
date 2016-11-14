package com.example.store4life.Fragment;

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

import com.example.store4life.Adapter.News_Adapter;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Controller.CheckNetworkConnection;
import com.example.store4life.Model.News;
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
import java.util.List;

/**
 * Created by duchieu on 10/10/2016.
 */

public class News_Fragment extends Fragment {
    private static String TAG = "tintuc";
    String myJSON;
    JSONArray jsonArray = null;
    ArrayList<News> data = new ArrayList<>();

    News_Adapter adapter;
    RecyclerView recyclerView;

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (CheckNetworkConnection.isConnectionAvailable(getActivity())) {
            view = inflater.inflate(R.layout.fragment_home,container,false);
            view = inflater.inflate(R.layout.news_fragment,null);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            getData();
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
            data.add( new News(c.getInt("ID"),
                    c.getString("TieuDe"),
                    c.getString("Hinh"),
                    c.getString("ChiTiet"),
                    c.getString("ngay")));
            }
            adapter = new News_Adapter(getActivity(),data);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost(AppConfig.URL_NEW);
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



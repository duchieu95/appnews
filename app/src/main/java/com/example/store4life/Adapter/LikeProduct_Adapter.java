package com.example.store4life.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.view.MenuItem;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.store4life.Activity.DetailProduct_Activity;
import com.example.store4life.Activity.Login_Activity;
import com.example.store4life.Activity.UserManager_Activity;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Database.Database_4life;
import com.example.store4life.Model.Product;
import com.example.store4life.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LikeProduct_Adapter extends RecyclerView.Adapter<LikeProduct_Adapter.MyViewHolder> {
    public Context mContext;
    public List<Product> list;

    Database_4life db;

    public LikeProduct_Adapter (Context mContext, List<Product> list){
        this.mContext = mContext;
        this.list = list;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TenSP, Gia;
        public ImageView Hinh;
        public ImageButton IMBDELETE,IMBADDCARD;
        public MyViewHolder(View view) {
            super(view);
            TenSP = (TextView) view.findViewById(R.id.title);
            Gia = (TextView) view.findViewById(R.id.count);
            Hinh = (ImageView) view.findViewById(R.id.thumbnail);
            IMBDELETE = (ImageButton) view.findViewById(R.id.IMBDELETE);
            IMBADDCARD = (ImageButton) view.findViewById(R.id.IMBADDCARD);
        }

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_like, parent, false);
        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {

        db = new Database_4life(mContext);
        HashMap<String,String> user = db.GetUserHeader();
        final  String IDTV = user.get("ID_TV");
        final Product p = list.get(position);
        holder.TenSP.setText(p.TenSP);
        holder.Gia.setText(p.GiaSP+"");

        Glide.with(mContext).load(AppConfig.URL_FolderUpload+p.HinhSP).into(holder.Hinh);

        holder.IMBDELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                        dialog.setTitle("Thông Báo");
                        dialog.setMessage("Bạn có muốn xóa không?");
                        dialog.setNeutralButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        dialog.setNegativeButton("Có",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DELETEYT(p.ID_YT);
                                list.remove(position);
                                notifyDataSetChanged();
                            }
                        });

                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();

                    }
                }, 0);
            }
        });

        holder.IMBADDCARD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Thông Báo");
                dialog.setMessage("Bạn Có Muốn Thêm Sản Phẩm Vào Giỏ Hàng Không?");
                dialog.setNeutralButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.setNegativeButton("Có",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(db.InsertCart(p.ID_SP,p.TenSP,p.HinhSP,p.GiaSP,1,p.GiaSP,IDTV)){
                            Toast.makeText(mContext,"Đã Tăng Số Lượng "+p.TenSP+" Vào Giỏ Hàng",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext,"Đã Thêm "+p.TenSP+" Vào Giỏ Hàng",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

    }


    private void DELETEYT(final String idyeuthich) {

        class XoaAsync extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();}

            @Override
            protected String doInBackground(String... params) {
                String id = params[0];
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("ID_YT", id));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(AppConfig.URL_DELETEYT);
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
                Toast.makeText(mContext,"Thành Công",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext,"Lỗi",Toast.LENGTH_SHORT).show();
                }
            }
        }
        XoaAsync la = new XoaAsync();
        la.execute(idyeuthich);
    }

}

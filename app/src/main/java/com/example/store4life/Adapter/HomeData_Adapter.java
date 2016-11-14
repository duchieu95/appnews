package com.example.store4life.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.store4life.Activity.DetailCategory_Activity;
import com.example.store4life.Model.SectionDataModel;
import com.example.store4life.R;

import java.util.ArrayList;
import java.util.List;


public class HomeData_Adapter extends RecyclerView.Adapter<HomeData_Adapter.ItemRowHolder> {

    public List<SectionDataModel> dataList;
    public Context mContext;


    public HomeData_Adapter(Context context, List<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }
    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder itemRowHolder, final int position) {

        final String sectionName = dataList.get(position).getHeaderTitle();

        ArrayList singleSectionItems = dataList.get(position).getAllItemsInSection();

        itemRowHolder.itemTitle.setText(sectionName);

        SectionListHomeData itemListDataAdapter = new SectionListHomeData(mContext, singleSectionItems);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);


        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = position+99;
                Log.d("Da Chon",sectionName);
                Intent intent = new Intent(mContext, DetailCategory_Activity.class);
                intent.putExtra("TENLOAI",sectionName);
                intent.putExtra("KEYCODE",check);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;

        protected RecyclerView recycler_view_list;

        protected TextView btnMore;

        public ItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.btnMore= (TextView) view.findViewById(R.id.btnMore);


        }

    }

}

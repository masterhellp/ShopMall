package com.wythe.mall.adapter.prodetails;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coorchice.library.SuperTextView;
import com.wythe.mall.R;
import com.wythe.mall.beans.Goods;
import com.wythe.mall.beans.NormBean;
import com.wythe.mall.listeners.MyItemClickListener;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SpecAdapter extends RecyclerView.Adapter<SpecAdapter.ViewHolder> {

    private Context context;

    public void setDataList(List<NormBean> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public List<NormBean> getDataList() {
        return dataList;
    }

    private List<NormBean> dataList;

    private LayoutInflater mInflater;

    private MyItemClickListener listener;

    public void setCurForcused(int curForcused) {
        this.curForcused = curForcused;
    }

    private int curForcused = -1; //当前选中的选项

    public SpecAdapter(Context context, MyItemClickListener myItemClickListener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.listener = myItemClickListener;
    }

    @Override
    public SpecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = mInflater.inflate(R.layout.spec_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view, listener);

        return viewHolder;
    }


    @Override
    public int getItemCount() {
        if (null != dataList) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameView.setText(dataList.get(position).getNormName());
        if (curForcused == position) {
            holder.nameView.setSolid(Color.parseColor("#f89628"));
            holder.nameView.setTextColor(Color.WHITE);
        } else {
            holder.nameView.setSolid(Color.parseColor("#f0f0f0"));
            holder.nameView.setTextColor(Color.parseColor("#585858"));
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        SuperTextView nameView;

        public ViewHolder(final View itemView, final MyItemClickListener listener) {
            super(itemView);
            nameView = itemView.findViewById(R.id.spec_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }
    }

}

package com.wythe.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wythe.mall.R;
import com.wythe.mall.utils.TransformationUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public List<String> getDataList() {
        return dataList;
    }

    private List<String> dataList;

    private LayoutInflater mInflater;

    private int curForcused; //当前选中的选项

    private String pageType;


    public CommentAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = mInflater.inflate(R.layout.pro_detail_imgitems, null, false);

        ViewHolder viewHolder = new ViewHolder(view);

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

//        CommonUtils.loadIntoUseWidth(context,dataList.get(position),R.mipmap.a,holder.nameView);

        Glide.with(context)
                .load(dataList.get(position))
                .asBitmap()
                .placeholder(R.mipmap.a)
                .into(new TransformationUtils(context, holder.nameView));

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView nameView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameView = (ImageView) itemView.findViewById(R.id.imgView);
        }
    }

    public void setForcused(int curForcused) {
        this.curForcused = curForcused;
    }
}

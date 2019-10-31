package com.example.odm.garbagesorthelper.ui.knowledge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.widget.AnchorView;

/**
 * description: 锚点RecyclerView适配器
 * author: ODM
 * date: 2019/9/22
 */
public class AnchorRecyclerViewAdapter extends RecyclerView.Adapter<AnchorRecyclerViewAdapter.AnchorViewHolder> {

    private Context context;
    private int[] imgs;
    private int finalHeight;
    private String[] imgUrls;

    public AnchorRecyclerViewAdapter(Context context, int[] imgs, int lastH) {
        this.context = context;
        this.imgs = imgs;
        this.finalHeight = lastH;
    }

    public AnchorRecyclerViewAdapter(Context context, String[] imgUrls) {
        this.context = context;
        this.imgUrls = imgUrls;
    }

    @Override
    @NonNull
    public AnchorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_anchor_knowledge, parent, false);
        return new AnchorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnchorViewHolder holder, int position) {
//        holder.anchorView.setContentRes(imgs[position]);
          holder.anchorView.setContentUrl(imgUrls[position]);
//        //判断最后一个view
//        if (position == tabTxt.length - 1) {
//
//            if (holder.anchorView.getHeight() < finalHeight) {
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                params.height = finalHeight ;
//                holder.anchorView.setLayoutParams(params);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return imgUrls.length;
    }

    public static class AnchorViewHolder extends RecyclerView.ViewHolder {
        private AnchorView anchorView;

        public AnchorViewHolder(View itemView) {
            super(itemView);
            anchorView = itemView.findViewById(R.id.anchor_knowledge);
        }
    }
}

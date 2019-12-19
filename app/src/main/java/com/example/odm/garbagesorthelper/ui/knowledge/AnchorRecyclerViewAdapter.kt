package com.example.odm.garbagesorthelper.ui.knowledge

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.ui.knowledge.AnchorRecyclerViewAdapter.AnchorViewHolder
import com.example.odm.garbagesorthelper.widget.AnchorView

/**
 * description: 锚点RecyclerView适配器
 * author: ODM
 * date: 2019/9/22
 */
class AnchorRecyclerViewAdapter : RecyclerView.Adapter<AnchorViewHolder> {
    private var context: Context
    private lateinit var imgs: IntArray
    private var finalHeight = 0
    private lateinit var imgUrls: Array<String>

    constructor(context: Context, imgs: IntArray, lastH: Int) {
        this.context = context
        this.imgs = imgs
        finalHeight = lastH
    }

    constructor(context: Context, imgUrls: Array<String>) {
        this.context = context
        this.imgUrls = imgUrls
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnchorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_anchor_knowledge, parent, false)
        return AnchorViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnchorViewHolder, position: Int) { //        holder.anchorView.setContentRes(imgs[position]);
        holder.anchorView.setContentUrl(imgUrls[position])
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

    override fun getItemCount(): Int {
        return imgUrls.size
    }

    class AnchorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val anchorView: AnchorView

        init {
            anchorView = itemView.findViewById(R.id.anchor_knowledge)
        }
    }
}
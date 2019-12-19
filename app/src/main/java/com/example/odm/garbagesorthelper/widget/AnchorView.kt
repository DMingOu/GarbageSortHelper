package com.example.odm.garbagesorthelper.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.odm.garbagesorthelper.R

/**
 * description: 锚点itemView
 * author: ODM
 * date: 2019/9/22
 */
class AnchorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    private val tvAnchor: TextView? = null
    private var ivContent: ImageView? = null
    private fun init(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.widget_anchor, this, true)
        ivContent = view.findViewById(R.id.iv_anchor_content)
    }

    /**
     * 加载资源图片
     * @param ResId
     */
    fun setContentRes(ResId: Int) {
        ivContent!!.setImageResource(ResId)
    }

    /**
     * 加载Url图片
     * @param imgUtl
     */
    fun setContentUrl(imgUtl: String?) {
        Glide.with(context.applicationContext).load(imgUtl).placeholder(R.drawable.module_glide_load_default_image).error(R.drawable.module_search_cookiebar_fail_garbage).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(ivContent!!)
    }

    init {
        init(context)
    }
}
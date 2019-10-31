package com.example.odm.garbagesorthelper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.odm.garbagesorthelper.R;

/**
 * description: 锚点itemView
 * author: ODM
 * date: 2019/9/22
 */
public class AnchorView extends LinearLayout {

    private TextView tvAnchor;
    private ImageView ivContent;
    private Context context;


    public AnchorView(Context context) {
        this(context, null);
    }

    public AnchorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnchorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_anchor, this, true);
        ivContent = view.findViewById(R.id.iv_anchor_content);
        this.context = context;
    }

    /**
     * 加载资源图片
     * @param ResId
     */
    public void setContentRes(int ResId) {
        ivContent.setImageResource(ResId);
    }

    /**
     * 加载Url图片
     * @param imgUtl
     */
    public void setContentUrl(String imgUtl) {

        Glide.with(context.getApplicationContext()).load(imgUtl).
                placeholder(R.drawable.module_glide_load_default_image).
                error(R.drawable.module_search_cookiebar_fail_garbage).
                override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).
                into(ivContent);

    }
}

package com.example.odm.garbagesorthelper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.odm.garbagesorthelper.R;

/**
 * description: 锚点itemView
 * author: ODM
 * date: 2019/9/22
 */
public class AnchorView extends LinearLayout {

    private TextView tvAnchor;
    private ImageView ivContent;

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
    }

    public void setContentRes(int ResId) {
        ivContent.setImageResource(ResId);
    }
}

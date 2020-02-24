/*
 * *************************************************************************************
 *   Copyright © 2014-2018 Ontology Foundation Ltd.
 *   All rights reserved.
 *
 *   This software is supplied only under the terms of a license agreement,
 *   nondisclosure agreement or other written agreement with Ontology Foundation Ltd.
 *   Use, redistribution or other disclosure of any parts of this
 *   software is prohibited except in accordance with the terms of such written
 *   agreement with Ontology Foundation Ltd. This software is confidential
 *   and proprietary information of Ontology Foundation Ltd.
 *
 * *************************************************************************************
 */

package com.github.ont.connector.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ont.connector.R;


/**
 * Created by zhugang on 2018/8/23.
 */

public class ArcTitleView extends RelativeLayout {

    private ArkTitleLeftListener listener;

    public ArcTitleView(Context context) {
        super(context);
    }

    public ArcTitleView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_arc, this, true);
        LinearLayout layoutBack = (LinearLayout) findViewById(R.id.layout_back);
        TextView tvName = (TextView) findViewById(R.id.tv_name);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.arkTitle);
        if (attributes != null) {
            String name = attributes.getString(R.styleable.arkTitle_name);
            if (name != null && !name.isEmpty()) {
                tvName.setText(name);
            }
        }

        layoutBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.leftButtonClick();
                }
            }

        });
    }

    public ArcTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public ArcTitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setArkTitleLeftListener(ArkTitleLeftListener listener) {
        this.listener = listener;
    }

    public interface ArkTitleLeftListener {
        void leftButtonClick();
    }
}

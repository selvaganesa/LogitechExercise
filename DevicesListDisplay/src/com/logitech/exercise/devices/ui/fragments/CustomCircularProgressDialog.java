/**
 * Copyright Trimble Inc., 2013 - 2014 All rights reserved.
 * 
 *  Licensed Software Confidential and Proprietary Information of Trimble Inc., made available under Non-Disclosure Agreement OR License as applicable.
 *  
 *  Product Name:
 *  Machine Inspection Checklist 
 *  
 *  Module Name:
 *  src/com/trimble/android/mic/dialog/CustomProgressDialog.java
 *  
 *  File name:
 *  CustomProgressDialog.java 
 * 
 *  Author:
 *  ssamina 
 * 
 *  Created On:
 *  
 *  
 * 
 *  Abstract:
 *  
 * 
 *  Environment:
 *  Mobile Profile          :
 *  Mobile Configuration    :
 * 
 *  Notes:
 * 
 * 
 *  Revision History:
 */

package com.logitech.exercise.devices.ui.fragments;

import com.logitech.exercise.devices.R;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomCircularProgressDialog extends Dialog {

    private String mStringTitle = null;
    TextView mDetailMessage = null;
    private ProgressBar mProgressBar;

    public CustomCircularProgressDialog(Activity activity, int title) {
        super(activity);
    }

    public CustomCircularProgressDialog(Activity activity, String title) {
        super(activity);
        mStringTitle = title;
    }

    public CustomCircularProgressDialog(Activity activity) {
        super(activity);
        mStringTitle = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_progress);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mProgressBar = (ProgressBar) findViewById(R.id.dialog_progressBar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(
                getContext().getResources().getColor(android.R.color.holo_orange_light),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        mDetailMessage = (TextView) findViewById(R.id.textview_progress_msg);
        mDetailMessage.setVisibility(View.VISIBLE);
        mDetailMessage.setText(mStringTitle);

    }

    public void setMessage(String text) {
        if (mDetailMessage != null) {
            mDetailMessage.setVisibility(View.VISIBLE);
            mDetailMessage.setText(text);
        }
    }

}

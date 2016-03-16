
/*
 * Copyright Trimble Inc., 2013 - 2014 All rights reserved.
 *
 *  Licensed Software Confidential and Proprietary Information of Trimble Inc., made available under Non-Disclosure Agreement OR License as applicable.
 *
 *  Product Name:
 *  QML800
 *
 *  Module Name:
 *  XSpotCU
 *
 *  File name:
 *  EngineMessageReceiver.java
 *
 *  package
 *  /Users/SelvaGanesan/Official/Projects/QML800/git/QML800/XSpotCU/src/com/trimble/android/xspot/EngineMessageReceiver.java
 *
 *  Class Name
 *  EngineMessageReceiver
 *
 *  Author:
 *  SelvaGanesan
 *
 *  Created On:
 *  24/4/15 3:11 PM
 *
 *  Last modified:
 *  24/4/15 3:04 PM
 *
 *  Environment:
 *  Mobile Profile          :
 *  Mobile Configuration    :
 *
 *  Notes:
 *
 *  Revision History:
 */

package com.logitech.exercise;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocalMessageReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "LocalMessageReceiver-->";
    /*
     * (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
     * android.content.Intent)
     */
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        Log.i(LOG_TAG, "broadcast event - original");
    }
}

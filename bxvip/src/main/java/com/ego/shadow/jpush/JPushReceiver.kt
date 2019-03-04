package com.ego.shadow.jpush

import android.content.Context
import android.os.Bundle
import co.bxvip.sdk.jpush.BxJPushReceiverImpl

/**
 * 自定义接收器
 */
class JPushReceiver : BxJPushReceiverImpl() {

    @Override
    override fun isGoToReleaseMain(): Boolean {
        return true
    }

    @Override
    override fun toGoYouMain(context: Context, bundle: Bundle?) {

    }
}

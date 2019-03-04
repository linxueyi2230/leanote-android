package co.bxvip.android.commonlib.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

@SuppressLint("StaticFieldLeak")
/**
 * <pre>
 *     author: vic
 *     time  : 18-1-23
 *     desc  : 全局的工具类
 * </pre>
 */

object CommonInit {
    lateinit var ctx: Application

    fun init(app: Application) {
        this.ctx = app
    }
}
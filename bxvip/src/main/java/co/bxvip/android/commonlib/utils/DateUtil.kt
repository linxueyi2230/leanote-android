package co.bxvip.android.commonlib.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by SillySnnall on 2018/1/21.
 */
object DateUtil {
    /**
     * 获取指定月份的总天数
     */
    fun getDaysByYearMonth(year: Int, month: Int): Int {
        val a = Calendar.getInstance()
        a.set(Calendar.YEAR, year)
        a.set(Calendar.MONTH, month - 1)
        a.set(Calendar.DATE, 1)
        a.roll(Calendar.DATE, -1)
        return a.get(Calendar.DATE)
    }

    /*
       * 将秒数转为时分秒
       * */
    fun change(time: Long): String {
        var timeStr: String? = null
        var hour: Long = 0
        var minute: Long = 0
        var second: Long = 0
        if (time <= 0)
            return "00:00:00"
        else {
            minute = time / 60
            if (minute < 60) {
                second = time % 60
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second)
            } else {
                hour = minute / 60
                if (hour > 99)
                    return "99:59:59"
                minute = minute % 60
                second = time - hour * 3600 - minute * 60
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second)
            }
        }
        return timeStr
    }

    fun unitFormat(i: Int): String {
        var retStr: String? = null
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i)
        else
            retStr = "" + i
        return retStr
    }


    fun unitFormat(i: Long): String {
        var retStr: String? = null
        if (i >= 0 && i < 10)
            retStr = "0" + java.lang.Long.toString(i)
        else
            retStr = "" + i
        return retStr
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14  16:09:00"）
     *
     * @param time
     * @return
     */
    fun timedate(time: String): String {
        val sdr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        //        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm:ss");
        val lcc = java.lang.Long.valueOf(time)!!
        val i = Integer.parseInt(time)
        return sdr.format(Date(i * 1000L))

    }
}
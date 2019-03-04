package co.bxvip.android.commonlib.db.handler;

import android.os.Message;

import co.bxvip.android.commonlib.db.callback.DBRun;

/**
 * Handler 信息体
 */

public class MessageInfo<T> {

    public int what;

    public DBRun<T> easyRun;

    public T model;

    public MessageInfo() {
    }

    public Message build(){
        Message msg = new Message();
        msg.what = this.what;
        msg.obj = this;
        return msg;
    }
}

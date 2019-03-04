package co.bxvip.android.commonlib.db.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import co.bxvip.android.commonlib.db.callback.DBRun;

/**
 * Handler辅助类：用于线程切换
 */

public class HandlerHelper extends Handler {

    public static final int WHAT_CALLBACK = 1;

    private static HandlerHelper helper;

    public static HandlerHelper get(){
        if(helper == null){
            synchronized (HandlerHelper.class){
                if(helper == null)
                    helper = new HandlerHelper();
            }
        }
        return helper;
    }


    private HandlerHelper() {
        super(Looper.getMainLooper());
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case WHAT_CALLBACK:
                MessageInfo model_msg = (MessageInfo)msg.obj;
                DBRun run_model = model_msg.easyRun;
                try {
                    if(null != run_model)
                        run_model.onMainThread(model_msg.model);
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }





}

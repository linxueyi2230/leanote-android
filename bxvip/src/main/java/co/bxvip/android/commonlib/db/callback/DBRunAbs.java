package co.bxvip.android.commonlib.db.callback;

/**
 * 任务回调接口基类
 */

public abstract class DBRunAbs<T> {

    public abstract T run() throws Exception;

    public abstract void onMainThread(T data) throws Exception;

}

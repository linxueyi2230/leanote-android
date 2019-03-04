package co.bxvip.android.commonlib.db.handler;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;
import android.util.Log;

import co.bxvip.android.commonlib.db.dao.RealBaseDao;
import co.bxvip.android.commonlib.db.info.Result;
import co.bxvip.android.commonlib.db.utils.DBInnerUtils;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;

/**
 * @description : 数据库操作代理类：主要进行预处理、日志打印
 */
@SuppressWarnings(value = "all")
public class DBProxyHandler<T> implements InvocationHandler {

    private Object obj;
    private OrmLiteSqliteOpenHelper helper;
    private Dao<T, Long> dao;
    private Class<T> mClass;
    private String databaseName;

    public DBProxyHandler(OrmLiteSqliteOpenHelper helper, Dao<T, Long> dao, Class<T> mClass, String databaseName) {
        this.helper = helper;
        this.dao = dao;
        this.mClass = mClass;
        this.databaseName = databaseName;
    }

    public RealBaseDao<T> getProxy(Object targetObject) {
        this.obj = targetObject;
        Object proxy = Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(), this);
        return (RealBaseDao<T>) proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = getTime();
        doBefore();
        Object result = method.invoke(obj, args);
        doAfter(method, result, startTime);
        return result;
    }

    /**
     * 执行前操作
     */
    private void doBefore() {
        prepareDeal();
    }

    /**
     * 执行后操作
     */
    private void doAfter(Method method, Object result, long startTime) {
        if (result != null) {
            if (result instanceof Result) {
                Result<T> real = (Result<T>) result;
                String methodName = method.getName();
                if (Result.LINE == real.getType()) {
                    showLog(methodName + "[" + (getTime() - startTime) + "ms] 影响行数：" + real.getLine());
                } else if (Result.COUNT == real.getType()) {
                    showLog(methodName + "[" + (getTime() - startTime) + "ms] 影响行数：" + real.getCount());
                } else if (Result.LIST == real.getType()) {
                    showLog(methodName + "[" + (getTime() - startTime) + "ms] 影响行数：" + real.getList().size());
                } else if (Result.IS_EXIST == real.getType()) {
                    showLog(methodName + "[" + (getTime() - startTime) + "ms] ：" + real.isExist());
                } else {
                    showLog(methodName + "[" + (getTime() - startTime) + "ms] ");
                }
                //异常处理
                Exception exception = real.getException();
                if (exception != null) {
                    if (exception instanceof ClassCastException) {
                        showErr("列值类型不正确：" + exception.getMessage());
                    }
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * 预处理
     */
    private void prepareDeal() {
        checkTable();
    }

    /**
     * 检查数据表
     */
    private void checkTable() {
        try {
            if (!dao.isTableExists()) {
                TableUtils.createTableIfNotExists(dao.getConnectionSource(), mClass);
            }
//            else {
//                checkTableField(dao.getConnectionSource(), mClass);
//            }
        } catch (Exception e) {
            if (e instanceof SQLiteDiskIOException) {
                //当用户误删除.db数据库文件时进行数据库恢复(若.db-journal日志文件删除则无法恢复)
//                helper.onOpen(helper.getWritableDatabase());
                SQLiteDatabase db = helper.getWritableDatabase();
                helper.getWritableDatabase().openOrCreateDatabase(db.getPath(), null);
                try {
                    dao = helper.getDao(mClass);
                    if (!dao.isTableExists()) {
                        TableUtils.createTableIfNotExists(dao.getConnectionSource(), mClass);
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                showErr("恢复数据库：" + databaseName);
            } else {
                e.printStackTrace();
            }
        }
    }

//    /**
//     * @param connectionSource
//     * @param dataClass
//     * @throws SQLException
//     */
//    private void checkTableField(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
//        if (dataClass != null) {
//            Dao<T, ?> dao = DaoManager.createDao(connectionSource, dataClass);
//            TableInfo tableInfo = null;
//            if (dao instanceof BaseDaoImpl<?, ?>) {
//                tableInfo = ((BaseDaoImpl<?, ?>) dao).getTableInfo();
//            } else {
//                tableInfo = new TableInfo<T, ?>(dao.getConnectionSource(), null, dao.getDataClass());
//            }
//            if (tableInfo != null) {
//                // 检查该表是否有这个字段
//                HashMap<String, Boolean> map = new HashMap<String, Boolean>();
//                // 查询 现有的数据库表字段
//                SQLiteDatabase db = helper.getWritableDatabase();
//                Cursor columns = db.rawQuery("PRAGMA table_info(" + tableInfo.getTableName() + ")", null);
//                while (columns.moveToNext()) {
//                    map.put(columns.getString(1), false);
//                }
//                // 获取 class 的定义的表字段集合
//
////
////                DatabaseType databaseType = connectionSource.getDatabaseType();
////                List<String> statements = new ArrayList<String>();
////                List<String> queriesAfter = new ArrayList<String>();
////                addCreateTableStatements(databaseType, tableInfo, statements, queriesAfter, ifNotExists);
////                DatabaseConnection connection = connectionSource.getReadWriteConnection(tableInfo.getTableName());
////                try {
////                    int stmtC = doStatements(connection, "create", statements, false,
////                            databaseType.isCreateTableReturnsNegative(), databaseType.isCreateTableReturnsZero());
////                    stmtC += doCreateTestQueries(connection, databaseType, queriesAfter);
////                    return stmtC;
////                } finally {
////                    connectionSource.releaseConnection(connection);
////                }
//            }
//        }
//    }

    private long getTime() {
        return System.currentTimeMillis();
    }

    /**
     * 打印日志
     */
    private void showLog(String msg) {
        if (DBInnerUtils.Companion.getShowDBLog())
            Log.d(DBInnerUtils.Companion.getLogTAG(), msg + " | " + mClass.getSimpleName() + " | " + databaseName);
    }

    private void showErr(String msg) {
        if (DBInnerUtils.Companion.getShowDBLog())
            Log.e(DBInnerUtils.Companion.getLogTAG(), msg + " | " + mClass.getSimpleName() + " | " + databaseName);
    }
}




package co.bxvip.android.commonlib.db

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.stmt.QueryBuilder

import java.lang.reflect.ParameterizedType
import java.util.concurrent.Callable

import co.bxvip.android.commonlib.db.callback.DBRun
import co.bxvip.android.commonlib.db.dao.DBDao
import co.bxvip.android.commonlib.db.dao.RealBaseDao
import co.bxvip.android.commonlib.db.dao.RealBaseDaoImpl
import co.bxvip.android.commonlib.db.handler.DBProxyHandler
import co.bxvip.android.commonlib.db.info.OrderInfo
import co.bxvip.android.commonlib.db.info.Result
import co.bxvip.android.commonlib.db.info.WhereInfo

/**
 * 数据库操作接口实现类
 *class TableInit(@DatabaseField(id = true, canBeNull = false)
var dbVersion: String = "")
 */
open class BaseDao<T> : DBDao<T> {

    private var dao: Dao<T, Long>? = null

    private var simpleClassName = ""
    private var baseDao: RealBaseDao<T>? = null

    init {
        try {
            val clazz = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T> // 获取真实类型信息
            simpleClassName = clazz.simpleName
            dao = database.getDao(clazz)
            baseDao = DBProxyHandler(database, dao, clazz, simpleClassName).getProxy(RealBaseDaoImpl(dao))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun add(model: T): Int {
        return baseDao?.add(model)?.line ?: 0
    }

    override fun add(list: List<T>): Int {
        return baseDao?.add(list)?.line ?: 0
    }

    override fun addOrUpdate(model: T): Int {
        return baseDao?.addOrUpdate(model)?.line ?: 0
    }

    override fun addIfNotExists(model: T): T? {
        return baseDao?.addIfNotExists(model)?.model
    }

    override fun delete(model: T): Int {
        return baseDao?.delete(model)?.line ?: 0
    }

    override fun delete(list: List<T>): Int {
        return baseDao?.delete(list)?.line ?: 0
    }

    override fun delete(whereInfo: WhereInfo): Int {
        return baseDao?.delete(whereInfo)?.line ?: 0
    }

    override fun update(model: T): Int {
        return baseDao?.update(model)?.line ?: 0
    }

    override fun update(whereInfo: WhereInfo): Int {
        return baseDao?.update(whereInfo)?.line ?: 0
    }

    override fun queryAll(): List<T> {
        return baseDao?.queryAll()?.list ?: arrayListOf()
    }

    override fun queryAll(orderInfo: OrderInfo): List<T> {
        return baseDao?.queryAll(orderInfo)?.list ?: arrayListOf()
    }

    override fun query(whereInfo: WhereInfo): List<T> {
        return baseDao?.query(whereInfo)?.list ?: arrayListOf()
    }

    override fun queryLimit(whereInfo: WhereInfo): List<T> {
        return baseDao?.queryLimit(whereInfo)?.list ?: arrayListOf()
    }

    override fun query(queryBuilder: QueryBuilder<T, Int>): List<T> {
        return baseDao?.query(queryBuilder)?.list ?: arrayListOf()
    }

    override fun countOf(): Long {
        return baseDao?.countOf(null)?.count ?: 0
    }

    override fun countOf(whereInfo: WhereInfo): Long {
        return baseDao?.countOf(whereInfo)?.count ?: 0
    }

    override fun isExist(whereInfo: WhereInfo): Boolean {
        return baseDao?.isExist(whereInfo)?.isExist ?: false
    }

    override fun executeRaw(statement: String, vararg arguments: String): Int {
        return baseDao?.executeRaw(statement, *arguments)?.line ?: 0
    }

    override fun fetchDao(): Dao<T, Long>? {
        return dao
    }

    override fun getTableName(): String {
        return baseDao?.tableName ?: ""
    }

    override fun clearTable(): Int {
        return baseDao?.clearTable()?.line ?: 0
    }

    override fun dropTable(): Int {
        return baseDao?.dropTable()?.line ?: 0
    }

    override fun callInTransaction(callable: Callable<T>) {
        baseDao?.callInTransaction(callable)
    }

    /**
     * 反回会为null
     */
    override fun <CT> callBatchTasks(callable: Callable<CT>): CT? {
        return baseDao?.callBatchTasks(callable)
    }

    override fun <T> asyncTask(easyRun: DBRun<T>) {
        baseDao?.asyncTask(easyRun)
    }

    /**
     * 反回会为null
     */
    override fun addOrUpdate(ts: List<T>): Result<T>? {
        return baseDao?.addOrUpdate(ts)
    }

    override fun deleteAll(): Int {
        return baseDao?.deleteAll() ?: 0
    }

    /**
     * 反回会为null
     */
    override fun findTopOne(): T? {
        return baseDao?.findTopOne()
    }

    override fun findAll(): List<T> {
        return baseDao?.findAll() ?: arrayListOf()
    }

    override fun findListByKeyValues(vararg args: String): List<T> {
        return baseDao?.findListByKeyValues(*args) ?: arrayListOf()
    }

    /**
     * 反回会为null
     */
    override fun findByKeyValues(vararg args: String): T? {
        return baseDao?.findByKeyValues(*args)
    }
}

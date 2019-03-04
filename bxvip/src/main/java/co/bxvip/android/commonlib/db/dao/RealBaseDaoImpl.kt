package co.bxvip.android.commonlib.db.dao

import android.text.TextUtils

import co.bxvip.android.commonlib.db.callback.DBRun
import co.bxvip.android.commonlib.db.handler.HandlerHelper
import co.bxvip.android.commonlib.db.handler.MessageInfo
import co.bxvip.android.commonlib.db.info.OrderInfo
import co.bxvip.android.commonlib.db.info.Result
import co.bxvip.android.commonlib.db.info.WhereInfo

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.misc.TransactionManager
import com.j256.ormlite.stmt.DeleteBuilder
import com.j256.ormlite.stmt.QueryBuilder
import com.j256.ormlite.stmt.StatementBuilder
import com.j256.ormlite.stmt.Where
import com.j256.ormlite.table.TableUtils

import java.sql.SQLException
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author : zhousf
 */
class RealBaseDaoImpl<T>(private val dao: Dao<T, Long>?) : RealBaseDao<T> {

    init {
        if (executorService == null)
            executorService = Executors.newCachedThreadPool()

    }

    override fun add(model: T): Result<T> {
        val result = Result<T>(Result.LINE)
        try {
            result.line = dao?.create(model) ?: 0
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun add(list: List<T>): Result<T> {
        val result = Result<T>(Result.LINE)
        try {
            result.line = dao?.create(list) ?: 0
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun addOrUpdate(model: T): Result<T> {
        val result = Result<T>(Result.LINE)
        try {
            val status = dao?.createOrUpdate(model)
            result.line = status?.numLinesChanged ?: 0
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun addOrUpdate(ts: List<T>): Result<T> {
        val result = Result<T>(Result.LINE)
        for (t in ts) {
            val tResult = addOrUpdate(t)
            if (tResult.exception != null) {
                result.line = result.line + tResult.line
            }
        }
        return result
    }

    override fun addIfNotExists(model: T): Result<T> {
        val result = Result<T>(Result.MODEL)
        try {
            result.model = dao?.createIfNotExists(model)
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun delete(model: T): Result<T> {
        var line = 0
        val result = Result<T>(Result.LINE)
        try {
            line = dao?.delete(model) ?: 0
            result.line = line
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun delete(list: List<T>): Result<T> {
        val result = Result<T>(Result.LINE)
        try {
            result.line = dao?.delete(list) ?: 0
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun delete(whereInfo: WhereInfo): Result<T> {
        val result = Result<T>(Result.LINE)
        try {
            var deleteBuilder = dao?.deleteBuilder()
            deleteBuilder = fetchQueryBuilder(deleteBuilder, whereInfo) as DeleteBuilder<T, Long>
            result.line = dao?.delete(deleteBuilder.prepare()) ?: 0
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun deleteAll(): Int {
        return executeRaw("DELETE FROM $tableName").line
    }

    override fun update(model: T): Result<T> {
        val result = Result<T>(Result.LINE)
        try {
            result.line = dao?.update(model) ?: 0
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun update(whereInfo: WhereInfo): Result<T> {
        val result = Result<T>(Result.LINE)
        try {
            val queryBuilder = dao?.queryBuilder()
            val preparedUpdate = dao?.updateBuilder()
            for (where in whereInfo.wheres) {
                if (where.op == co.bxvip.android.commonlib.db.info.Where.UPDATE) {
                    preparedUpdate?.updateColumnValue(where.name, where.value)
                }
            }
            preparedUpdate?.setWhere(fetchWhere(queryBuilder, whereInfo))
            result.line = preparedUpdate?.update() ?: 0
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun queryAll(): Result<T> {
        val result = Result<T>(Result.LIST)
        try {
            result.list = dao?.queryForAll()
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun queryAll(orderInfo: OrderInfo): Result<T> {
        val result = Result<T>(Result.LIST)
        try {
            val queryBuilder = dao?.queryBuilder()
            orderBy(queryBuilder, orderInfo.orders)
            result.list = dao?.query(queryBuilder?.prepare())
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun query(whereInfo: WhereInfo): Result<T> {
        val result = Result<T>(Result.LIST)
        try {
            var queryBuilder = dao?.queryBuilder()
            orderBy(queryBuilder, whereInfo.orders)
            queryBuilder = fetchQueryBuilder(queryBuilder, whereInfo) as QueryBuilder<T, Long>
            result.list = dao?.query(queryBuilder.prepare())
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun queryLimit(whereInfo: WhereInfo): Result<T> {
        var all: List<T> = ArrayList()
        val result = Result<T>(Result.LIST)
        try {
            var queryBuilder = dao?.queryBuilder()
            orderBy(queryBuilder, whereInfo.orders)
            var offset = whereInfo.currentPage
            if (offset != 0) {
                offset = (whereInfo.currentPage - 1) * whereInfo.limit + whereInfo.size
            }
            queryBuilder = fetchQueryBuilder(queryBuilder, whereInfo) as QueryBuilder<T, Long>
            queryBuilder.offset(offset.toLong())
            queryBuilder.limit(whereInfo.limit.toLong())
            all = dao?.query(queryBuilder.prepare()) ?: arrayListOf()
            whereInfo.currentPage = ++whereInfo.currentPage
            whereInfo.size = all.size
            result.list = all
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun query(queryBuilder: QueryBuilder<T, Int>): Result<T> {
        val result = Result<T>(Result.LIST)
        try {
            result.list = dao?.query(queryBuilder.prepare())
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun countOf(): Result<T> {
        return countOf(null)
    }

    override fun countOf(whereInfo: WhereInfo?): Result<T> {
        val result = Result<T>(Result.COUNT)
        try {
            var queryBuilder = dao?.queryBuilder()
            queryBuilder?.setCountOf(true)
            if (null != whereInfo) {
                queryBuilder = fetchQueryBuilder(queryBuilder, whereInfo) as QueryBuilder<T, Long>
                result.setCount(dao?.countOf(queryBuilder.prepare()) ?: 0)
            } else {
                result.setCount(dao?.countOf() ?: 0)
            }
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun isExist(whereInfo: WhereInfo): Result<T> {
        val result = Result<T>(Result.IS_EXIST)
        try {
            var queryBuilder = dao?.queryBuilder()
            queryBuilder = fetchQueryBuilder(queryBuilder, whereInfo) as QueryBuilder<T, Long>
            queryBuilder.setCountOf(true)
            result.isExist = dao?.countOf(queryBuilder.prepare()) ?: 0 > 0
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun executeRaw(statement: String, vararg arguments: String): Result<T> {
        val result = Result<T>(Result.LINE)
        try {
            result.line = dao?.executeRaw(statement, *arguments) ?: 0
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun clearTable(): Result<T> {
        val result = Result<T>(Result.LINE)
        try {
            result.line = TableUtils.clearTable(dao?.connectionSource, dao?.dataClass)
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun dropTable(): Result<T> {
        val result = Result<T>(Result.LINE)
        try {
            if (dao?.isTableExists == true) {
                result.line = TableUtils.dropTable<T, Any>(dao.connectionSource, dao.dataClass, false)
            }
        } catch (e: Exception) {
            result.exception = e
        }

        return result
    }

    override fun fetchDao(): Dao<T, Long>? {
        return dao
    }

    override fun getTableName(): String {
        return dao?.tableName ?: ""
    }

    override fun callInTransaction(callable: Callable<T>) {
        val transactionManager = TransactionManager(dao?.connectionSource)
        try {
            transactionManager.callInTransaction(callable)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun <CT> callBatchTasks(callable: Callable<CT>): CT? {
        try {
            return dao?.callBatchTasks(callable)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    override fun <T> asyncTask(easyRun: DBRun<T>?) {
        if (null != easyRun) {
            executorService?.execute {
                try {
                    val data = easyRun.run()
                    val info = MessageInfo<T>()
                    info.what = HandlerHelper.WHAT_CALLBACK
                    info.model = data
                    info.easyRun = easyRun
                    HandlerHelper.get().sendMessage(info.build())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    @Throws(SQLException::class)
    private fun fetchWhere(queryBuilder: StatementBuilder<T, Long>?, whereInfo: WhereInfo): Where<T, Long>? {
        val wheres = whereInfo.wheres
        var whereBuilder: Where<T, Long>? = null
        if (!wheres.isEmpty()) {
            whereBuilder = queryBuilder?.where()
            var isFirst = true
            for (i in wheres.indices) {
                val where = wheres[i]
                if (TextUtils.isEmpty(where.name))
                    continue
                // 处理and-or
                if (co.bxvip.android.commonlib.db.info.Where.UPDATE != where.op) {
                    isFirst = appendAnd(whereBuilder, where, isFirst)
                }
                // 等于
                if (co.bxvip.android.commonlib.db.info.Where.EQ == where.op) {
                    whereBuilder?.eq(where.name, where.value)
                }
                // 模糊查询
                if (co.bxvip.android.commonlib.db.info.Where.LIKE == where.op) {
                    whereBuilder?.like(where.name, where.value)
                }
                // between
                if (co.bxvip.android.commonlib.db.info.Where.BETWEEN == where.op) {
                    whereBuilder?.between(where.name, where.low, where.high)
                }
                // lt 小于
                if (co.bxvip.android.commonlib.db.info.Where.LT.endsWith(where.op)) {
                    whereBuilder?.lt(where.name, where.value)
                }
                // gt 大于
                if (co.bxvip.android.commonlib.db.info.Where.GT.endsWith(where.op)) {
                    whereBuilder?.gt(where.name, where.value)
                }
                // ge 大于等于
                if (co.bxvip.android.commonlib.db.info.Where.GE.endsWith(where.op)) {
                    whereBuilder?.ge(where.name, where.value)
                }
                // le 小于等于
                if (co.bxvip.android.commonlib.db.info.Where.LE.endsWith(where.op)) {
                    whereBuilder?.le(where.name, where.value)
                }
                // ne 不等于
                if (co.bxvip.android.commonlib.db.info.Where.NE.endsWith(where.op)) {
                    whereBuilder?.ne(where.name, where.value)
                }
                // in 包含
                if (co.bxvip.android.commonlib.db.info.Where.IN.endsWith(where.op)) {
                    whereBuilder?.`in`(where.name, *where.values)
                }
                // notIn 不包含
                if (co.bxvip.android.commonlib.db.info.Where.NOT_IN.endsWith(where.op)) {
                    whereBuilder?.notIn(where.name, *where.values)
                }
            }
        }
        return whereBuilder
    }


    /**
     * 构建查询条件
     */
    @Throws(SQLException::class)
    private fun fetchQueryBuilder(queryBuilder: StatementBuilder<T, Long>?, whereInfo: WhereInfo): StatementBuilder<T, Long>? {
        fetchWhere(queryBuilder, whereInfo)
        return queryBuilder
    }

    /**
     * 追加连接符
     */
    private fun appendAnd(whereBuilder: Where<T, Long>?, where: co.bxvip.android.commonlib.db.info.Where, isFirst: Boolean): Boolean {
        if (!isFirst) {
            if (co.bxvip.android.commonlib.db.info.Where.AND == where.andOr) {
                whereBuilder?.and()
            }
            if (co.bxvip.android.commonlib.db.info.Where.OR.endsWith(where.andOr)) {
                whereBuilder?.or()
            }
        }
        return false
    }

    /**
     * 排序
     */
    private fun orderBy(queryBuilder: QueryBuilder<T, Long>?, orders: Map<String, Boolean>) {
        if (!orders.isEmpty()) {
            for ((key, value) in orders) {
                queryBuilder?.orderBy(key, value)
            }
        }
    }

    override fun findTopOne(): T? {
        return queryAll().list.firstOrNull()
    }

    override fun findAll(): List<T> {
        return queryAll().list
    }

    override fun findListByKeyValues(vararg args: String): List<T> {
        val list = ArrayList<T>()
        val map = HashMap<String, Any>()
        var i = 0
        while (i < args.size) {
            map.put(args[i], args[i + 1])
            i += 2
        }
        try {
            list.addAll(dao?.queryForFieldValuesArgs(map) ?: arrayListOf())
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return list
    }

    override fun findByKeyValues(vararg args: String): T? {
        try {
            val builder = dao?.queryBuilder()
            val where = builder?.where()
            var i = 0
            while (i < args.size) {
                if (i == 0)
                    where?.eq(args[i], args[i + 1])
                else
                    where?.and()?.eq(args[i], args[i + 1])
                i += 2
            }
            return dao?.queryForFirst(builder?.prepare())
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return null
    }

    companion object {
        private var executorService: ExecutorService? = null
    }
}

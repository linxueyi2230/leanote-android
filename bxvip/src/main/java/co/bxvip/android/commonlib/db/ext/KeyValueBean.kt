package co.bxvip.android.commonlib.db.ext

import co.bxvip.android.commonlib.db.BaseDao
import co.bxvip.android.commonlib.db.info.WhereInfo
import com.j256.ormlite.field.DatabaseField

/**
 *
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 *
 * <pre>
 *     author: vic
 *     time  : 18-5-29
 *     desc  : key-value 缓存
 * </pre>
 */
data class KeyValueCache(@DatabaseField(id = true, canBeNull = false) var key: String = "",
                         @DatabaseField var value: String = "",
                         @DatabaseField var encode: Boolean = true)

class KeyValueCacheDao : BaseDao<KeyValueCache>() {

    /**
     * 获取缓存结果
     */
    fun getValue(key: String, defaultValue: String = "", k: Int = 100): String {
        val valueBean = findByKeyValues("key", key)
        if (valueBean != null) {
            return if (valueBean.encode) valueBean.value.dc(k) else valueBean.value
        }
        return defaultValue
    }

    /**
     * 精准查询
     * @param key
     */
    fun getValue(vararg keys: String, k: Int = 100): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        keys.map {
            val values = findByKeyValues("key", it)
            if (values != null)
                map[values.key] = if (values.encode) values.value.dc(k) else values.value
        }
        return map
    }

    /**
     * 模糊前缀查询
     */
    fun getValueBuPreFix(prefix: String, k: Int = 100): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        query(WhereInfo.get().like("key", "$prefix%")).map {
            map.put(it.key, if (it.encode) it.value.dc(k) else it.value)
        }
        return map
    }

    fun setValue(key: String, value: String, encode: Boolean = true, k: Int = 100) {
        addOrUpdate(KeyValueCache(key, if (encode) value.ec(k) else value, encode))
    }

    /**
     * @param args key 键值对
     */
    fun setValue(vararg args: String, prefix: String = "", encode: Boolean = true, k: Int = 100) {
        if (args != null && args.size % 2 == 0) {
            args.forEachIndexed { index, value ->
                if (index % 2 == 0) {
                    setValue("$prefix$value", args[index + 1], encode, k)
                }
            }
        }
    }

    fun setValue(map: Map<String, String>, prefix: String = "", encode: Boolean = true, k: Int = 100) {
        map.map {
            setValue("$prefix${it.key}", it.value, encode, k)
        }
    }

    /**
     * 删除前缀 key
     */
    fun deleteByPrefix(prefix: String) {
        delete(WhereInfo.get().like("key", "$prefix%"))
    }

    fun deleteByKeys(vararg keys: String) {
        keys.map {
            delete(WhereInfo.get().equal("key", it))
        }
    }
}
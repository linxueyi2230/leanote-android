package co.bxvip.android.commonlib.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
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
 * <p>
 * <pre>
 *     author: vic
 *     time  : 18-6-26
 *     updateTime: 18-6-26
 *     desc  : @since 1.0.8 fix version you need add try catch() , some plugin-sdk not include this class
 * </pre>
 */
public class Storage {
    private static final String IMAGELOADER_SEPARATOR = "imageloader";
    private static final String DATABASE_SEPARATOR = "cache";

    /**
     * 得到该应用的缓存根目录 * * @param context 上下文 * @param appName 应用名称 * @return 应用的缓存根目录
     */
    public static File getRootCache(Context context, String appName) {
        String appDirRootPath;
        if (context != null) {
            try {
                context.getExternalCacheDir();
                context.getCacheDir();
            } catch (Exception e) {
            }
        }
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            appDirRootPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "Android" + File.separator + "data" + File.separator + appName + File.separator;
        } else {
            appDirRootPath = context.getCacheDir().getPath() + File.separator + appName + File.separator;
        }
        File dirCache = new File(appDirRootPath);
        if (!dirCache.exists()) {
            dirCache.mkdirs();
        }
        return dirCache;
    }

    /**
     * 得到该应用的imageloader的缓存目录
     *
     * @param context 上下文
     * @param appName 应用名称
     * @return imageloader的缓存目录
     */
    public static File getImageLoaderCache(Context context, String appName) {
        String imageLoaderCachePath = getRootCache(context, appName).getPath() + File.separator + IMAGELOADER_SEPARATOR + File.separator;
        File file = new File(imageLoaderCachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 得到数据库目录
     *
     * @param context 上下文
     * @param appName 应用名称
     * @return 数据库
     */
    public static File getDataBaseCache(Context context, String appName) {
        String avatarCachePath = getRootCache(context, appName).getPath() + File.separator + DATABASE_SEPARATOR + File.separator;
        File file = new File(avatarCachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}

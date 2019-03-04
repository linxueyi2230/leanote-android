package co.bxvip.android.commonlib.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <pre>
 *     author: vic
 *     time  : 2017/8/2
 *     desc  : glide 工具类
 * </pre>
 */

public class BitmapCache {
    public final static int IMG_WIDTH = 100;
    public final static int IMG_HEIGHT = 100;

    public static String BITMAP_PAHT = "";

    /**
     * 保存图片到本地库  位置 ：Environment.getExternalStorageDirectory(), context.getPackageName()  =fileName
     */
    public static void saveImageToGallery(Context context, Bitmap bmp, String fileName) {
        // 首先保存图片
        File appDir = Storage.getImageLoaderCache(context, context.getPackageName());
        File file = new File(appDir, fileName);
        if (!file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        }
    }

    /**
     * 存储图片到本地
     *
     * @param fileName 图片名称
     * @param bitmap   需要存储的图片
     */
    public static void saveBitmapToLocal(Context context, String fileName, Bitmap bitmap) {
        if (TextUtils.isEmpty(BITMAP_PAHT)) {
            BITMAP_PAHT = Storage.getImageLoaderCache(context, context.getPackageName()).getAbsolutePath() + "/lottery/icon_pics";
        }
        saveBitmapToLocal(context, BITMAP_PAHT, fileName, bitmap);
    }

    /**
     * 存储图片到本地
     *
     * @param fileName 图片名称
     * @param bitmap   需要存储的图片
     */
    public static void saveBitmapToLocal(Context context, String fileName, Bitmap bitmap, int quality) {
        if (TextUtils.isEmpty(BITMAP_PAHT)) {
            BITMAP_PAHT = Storage.getImageLoaderCache(context, context.getPackageName()).getAbsolutePath() + "/lottery/icon_pics";
        }
        saveBitmapToLocal(context, BITMAP_PAHT, fileName, bitmap, quality);
    }

    /**
     * 存储图片到本地
     *
     * @param filePath 存储图片的路径
     * @param fileName 图片名称
     * @param bitmap   需要存储的图片
     */
    public static void saveBitmapToLocal(Context context, String filePath, String fileName, Bitmap bitmap, int quality) {
        if (TextUtils.isEmpty(BITMAP_PAHT)) {
            BITMAP_PAHT = Storage.getImageLoaderCache(context, context.getPackageName()).getAbsolutePath() + "/lottery/icon_pics";
        }
        try {
            // 创建文件流，指向该路径，文件名叫做fileName
            File file = new File(filePath, fileName);
            // file其实是图片，它的父级File是文件夹，判断一下文件夹是否存在，如果不存在，创建文件夹
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                // 文件夹不存在
                fileParent.mkdirs();// 创建文件夹
            }
            // 将图片保存到本地
            bitmap.compress(Bitmap.CompressFormat.PNG, quality,
                    new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 存储图片到本地
     *
     * @param filePath 存储图片的路径
     * @param fileName 图片名称
     * @param bitmap   需要存储的图片
     */
    public static void saveBitmapToLocal(Context context, String filePath, String fileName, Bitmap bitmap) {
        if (TextUtils.isEmpty(BITMAP_PAHT)) {
            BITMAP_PAHT = Storage.getImageLoaderCache(context, context.getPackageName()).getAbsolutePath() + "/lottery/icon_pics";
        }
        try {
            // 创建文件流，指向该路径，文件名叫做fileName
            File file = new File(filePath, fileName);
            // file其实是图片，它的父级File是文件夹，判断一下文件夹是否存在，如果不存在，创建文件夹
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                // 文件夹不存在
                fileParent.mkdirs();// 创建文件夹
            }
            // 将图片保存到本地
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    *  从本地读取bitmap
    * */
    public static Bitmap getBitmapFromLocal(Context context, String fileName) {
        if (TextUtils.isEmpty(BITMAP_PAHT)) {
            BITMAP_PAHT = Storage.getImageLoaderCache(context, context.getPackageName()).getAbsolutePath() + "/lottery/icon_pics";
        }
        if (fileName != null)
            try {
                File file = new File(BITMAP_PAHT, fileName);
                if (file.exists()) {
//                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
//                        file));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
//                    Bitmap bitmap = BitmapFactory.decodeFile(BITMAP_PAHT + "/" + fileName, options);
                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), new Rect(), options);
                    return bitmap;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }


    public static Bitmap loadIcon(Context context, String url_1, String url_2, String tag, ImageView imgView) {
        try {
            Bitmap mBitmap = Glide.with(context).load(url_1).asBitmap().centerCrop().into(IMG_WIDTH, IMG_HEIGHT).get();
            BitmapCache.saveBitmapToLocal(context, tag, mBitmap);
            return mBitmap;
        } catch (Exception e) {
            try {
                Bitmap mbitmap = Glide.with(context).load(url_2).asBitmap().fitCenter().into(IMG_WIDTH, IMG_HEIGHT).get();
                BitmapCache.saveBitmapToLocal(context, tag, mbitmap);
                return mbitmap;
            } catch (Exception e1) {
            }
        }
        return null;
    }

    public static void deleteFile(String fileName) {
        File file = new File(fileName);
//        if(file.isFile() && file.exists()){
        getDelete(file);
//        }
    }


    private static void getDelete(File file) {
        //生成File[]数组   listFiles()方法获取当前目录里的文件夹  文件
        File[] files = file.listFiles();
        //判断是否为空   //有没有发现讨论基本一样
        if (files != null) {
            //遍历
            for (File file2 : files) {
                //是文件就删除
                if (file2.isFile()) {
                    file2.delete();
                } else if (file2.isDirectory()) {
                    //是文件夹就递归
                    getDelete(file2);
                    //空文件夹直接删除
                    file2.delete();
                }
            }
        }
    }
}

package com.legendwd.hyperpay.aelf.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.legendwd.hyperpay.lib.FileProvider7;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImgUtils {
    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp, String fileName) {
        if (bmp == null) {
            return false;
        }

        File image = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera/");
        String storePath = image.getAbsolutePath();
        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".png")) {
            fileName += ".jpg";
        }

        if (!new File(storePath).exists()) {
            new File(storePath).mkdirs();
        }
        File file = new File(storePath, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = FileProvider7.getUriForFile(context, file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
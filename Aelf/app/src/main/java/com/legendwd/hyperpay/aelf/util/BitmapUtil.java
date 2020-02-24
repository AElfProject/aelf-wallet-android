/**
 * BitmapUtil.java [V 1.0.0]
 * classes:com.elfin.utils.BitmapUtil
 * JUNJ Create at 2014-3-18 下午3:24:47
 */
package com.legendwd.hyperpay.aelf.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Images.Thumbnails;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

/**
 * @author JUNJ</               br> 图片常用方法类 create at 2014-3-18 下午3:24:47
 */
public class BitmapUtil {
    /* Options used internally. */
    public static final int OPTIONS_SCALE_UP = 0x1;

    /**
     * Constants used to indicate we should recycle the input in
     * {@link #//extractThumbnail(Bitmap, int, int, int)} unless the output is the
     * input.
     */
    public static final int OPTIONS_RECYCLE_INPUT = 0x2;
    private static final int BLACK = 0xff000000;

    /**
     * bitmap 转化成字节码
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 字节码转化成bitmap
     */
    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 放大bitmap
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        float scale = Math.min(scaleWidth, scaleHeight);
        matrix.postScale(scale, scale);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /**
     * drawable 转化成 bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                : Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 创建带圆角的图片
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 创建带倒影bitmap
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
                h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
                Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * 放大drawable
     */
    @SuppressWarnings("deprecation")
    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        // drawable转换成bitmap
        Bitmap oldbmp = drawableToBitmap(drawable);
        // 创建操作图片用的Matrix对象
        Matrix matrix = new Matrix();
        // 计算缩放比例
        float sx = ((float) w / width);
        float sy = ((float) h / height);
        // 设置缩放比例
        matrix.postScale(sx, sy);
        // 建立新的bitmap，其内容是对原bitmap的缩放后的图
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(newbmp);
    }

    /**
     * 获取缩略图
     */
    public static Bitmap getThumbnailsBitmap(Context context, int id,
                                             int width, int height) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{Thumbnails.DATA}, Thumbnails.IMAGE_ID + " = ?",
                new String[]{id + ""}, null);
        if (cursor.moveToFirst()) {
            String path = cursor.getString(cursor
                    .getColumnIndex(Thumbnails.DATA));
            cursor.close();
            if (width == 0 && height == 0) {
                return Thumbnails.getThumbnail(cr, id, Thumbnails.MICRO_KIND,
                        null);
            } else {
                return ThumbnailUtils.extractThumbnail(Thumbnails.getThumbnail(
                        cr, id, Thumbnails.MICRO_KIND, null), width, height,
                        ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            }
        }
        return null;
    }

    /**
     * Transform source Bitmap to targeted width and height.
     */
    public static Bitmap transform(Matrix scaler, Bitmap source,
                                   int targetWidth, int targetHeight, int options) {
        if (source != null) {
            boolean scaleUp = (options & OPTIONS_SCALE_UP) != 0;
            boolean recycle = (options & OPTIONS_RECYCLE_INPUT) != 0;

            int deltaX = source.getWidth() - targetWidth;
            int deltaY = source.getHeight() - targetHeight;
            if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
                /*
                 * In this case the bitmap is smaller, at least in one
                 * dimension, than the target. Transform it by placing as much
                 * of the image as possible into the target and leaving the
                 * top/bottom or left/right (or both) black.
                 */
                Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
                        Config.ARGB_8888);
                Canvas c = new Canvas(b2);

                int deltaXHalf = Math.max(0, deltaX / 2);
                int deltaYHalf = Math.max(0, deltaY / 2);
                Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
                        + Math.min(targetWidth, source.getWidth()), deltaYHalf
                        + Math.min(targetHeight, source.getHeight()));
                int dstX = (targetWidth - src.width()) / 2;
                int dstY = (targetHeight - src.height()) / 2;
                Rect dst = new Rect(dstX, dstY, targetWidth - dstX,
                        targetHeight - dstY);
                c.drawBitmap(source, src, dst, null);
                if (recycle) {
                    source.recycle();
                }
                c.setBitmap(null);
                return b2;
            }
            float bitmapWidthF = source.getWidth();
            float bitmapHeightF = source.getHeight();

            float bitmapAspect = bitmapWidthF / bitmapHeightF;
            float viewAspect = (float) targetWidth / targetHeight;

            if (bitmapAspect > viewAspect) {
                float scale = targetHeight / bitmapHeightF;
                if (scale < .9F || scale > 1F) {
                    scaler.setScale(scale, scale);
                } else {
                    scaler = null;
                }
            } else {
                float scale = targetWidth / bitmapWidthF;
                if (scale < .9F || scale > 1F) {
                    scaler.setScale(scale, scale);
                } else {
                    scaler = null;
                }
            }

            Bitmap b1;
            if (scaler != null) {
                // this is used for minithumb and crop, so we want to filter
                // here.
                b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                        source.getHeight(), scaler, true);
            } else {
                b1 = source;
            }

            if (recycle && b1 != source) {
                source.recycle();
            }

            int dx1 = Math.max(0, b1.getWidth() - targetWidth);
            int dy1 = Math.max(0, b1.getHeight() - targetHeight);

            Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
                    targetHeight);

            if (b2 != b1) {
                if (recycle || b1 != source) {
                    b1.recycle();
                }
            }
            return b2;
        }
        return null;
    }

    /**
     * 图片转灰度
     *
     * @param bmSrc
     * @return
     */
    public static Bitmap bitmap2Gray(Bitmap bmSrc) {
        int width, height;
        height = bmSrc.getHeight();
        width = bmSrc.getWidth();
        Bitmap bmpGray = null;
        bmpGray = Bitmap.createBitmap(width, height, Config.RGB_565);
        Canvas c = new Canvas(bmpGray);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmSrc, 0, 0, paint);
        return bmpGray;
    }

    /**
     * Bitmap 转换成文件
     */
    public static void bitmapToFile(Bitmap bmp, File file) {
        if (bmp != null && file != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                bmp.recycle();
            }
        }
    }

    /**
     * 压缩Bitmap文件，方便上传到服务端 注意：android加载Bitmap占用内存的大小还是不变
     */
    public static void compressBmpToFile(File inFile, File file) {
        Bitmap bmp = null;
        int degress = readPicDegree(inFile.getPath());
        Options opt = new Options();
        opt.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(inFile.getPath(), opt);
        if (opt.outHeight >= 1280 || opt.outWidth >= 1280) {
            int hs = Math.round(opt.outHeight / (float) 1280);
            int ws = Math.round(opt.outWidth / (float) 1280);
            opt.inSampleSize = Math.max(hs, ws);
            opt.inSampleSize = Math.max(opt.inSampleSize, 1);
        }
        opt.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(inFile.getPath(), opt);
        if (degress != 0) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degress);
            bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            bitmap.recycle();
        } else {
            bmp = bitmap;
        }
        if (bmp == null)
            return;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;// 个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 150) {
            baos.reset();
            if (options > 5) {
                options -= 5;
                bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            } else {
                bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
                break;
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bmp.recycle();
        }
    }

    public static void compressBitmapFileToFile(File infile, File outFile) {
        compressBmpToFile(infile, outFile);
    }

    /**
     * 获取指定宽高Bitmap
     *
     * @param file   图片文件
     * @param width  目标宽度
     * @param height 目标高度
     */
    public static Bitmap getBitmap(File file, int width, int height) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
        int heightRatio = 1;
        int widthRatio = 1;
        if (height > 0)
            heightRatio = (int) Math.ceil(options.outHeight / height);
        if (width > 0)
            widthRatio = (int) Math.ceil(options.outWidth / width);
        if (heightRatio > 1 && widthRatio > 1) {
            if (heightRatio > widthRatio) {
                options.inSampleSize = heightRatio;
            } else {
                options.inSampleSize = widthRatio;
            }
        }
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file.getPath(), options);
        return bitmap;
    }

    public static Bitmap getBitmap(InputStream inputStream, int width,
                                   int height) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        int heightRatio = 1;
        int widthRatio = 1;
        if (height > 0)
            heightRatio = (int) Math.ceil(options.outHeight / height);
        if (width > 0)
            widthRatio = (int) Math.ceil(options.outWidth / width);
        if (heightRatio > 1 && widthRatio > 1) {
            if (heightRatio > widthRatio) {
                options.inSampleSize = heightRatio;
            } else {
                options.inSampleSize = widthRatio;
            }
        }
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        return bitmap;
    }

    public static Bitmap createBitmap(Bitmap src, Bitmap floatBitmap, int aplha) {
        if (src == null || floatBitmap == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图

        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);
        if (aplha != 0)
            paint.setAlpha(aplha);
//		paint.setXfermode(new PorterDuffXfermode(
//				android.graphics.PorterDuff.Mode.SRC_ATOP));
        // draw watermark into
        // Rect rect = new Rect(0, 0, w, h);

        cv.drawBitmap(floatBitmap, 0, 0, paint);
        // save all clip
        cv.save();
        // store
        cv.restore();

        floatBitmap.recycle();

        return newb;
    }

    /**
     * 通过ExifInterface类读取图片文件的被旋转角度
     *
     * @param path ： 图片文件的路径
     * @return 图片文件的被旋转角度
     */
    public static int readPicDegree(String path) {
        int degree = 0;

        // 读取图片文件信息的类ExifInterface
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {

            e.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }

        return degree;
    }

    /**
     * 调整图片角度
     */
    public static Bitmap adjustRotationBitmap(String path, Bitmap bitmap) {
        Bitmap newBitmap = null;
        int degrees = 0;
        if ((degrees = readPicDegree(path)) != 0) {
            Matrix m = new Matrix();
            m.postRotate(degrees);
            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, false);
            bitmap.recycle();
        }
        return degrees == 0 ? bitmap : newBitmap;
    }

    /**
     * 读取图片高度和宽度
     */
    public static int[] getBitmpaSize(String filePath, int[] value) {
        if (filePath == null || value == null) {
            throw new IllegalArgumentException("filePath or value can't be null!");
        }

        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        value[0] = options.outWidth;
        value[1] = options.outHeight;
        return value;
    }

    /**
     * 生成一个二维码图像
     *
     * @param url       传入的字符串，通常是一个URL
     * @param QR_WIDTH  宽度（像素值px）
     * @param QR_HEIGHT 高度（像素值px）
     * @return
     */
    public static final Bitmap create2DCoderBitmap(String url, int QR_WIDTH,
                                                   int QR_HEIGHT) {
        try {
//			// 判断URL合法性
//			if (url == null || "".equals(url) || url.length() < 1) {
//				return null;
//			}
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            // 显示到一个ImageView上面
            // sweepIV.setImageBitmap(bitmap);
            return bitmap;
        } catch (WriterException e) {
            return null;
        }
    }

    /**
     * 生成一个二维码图像
     * <p>
     * dddu  rl
     * xxx   传入的字符串，通常是一个URL
     *
     * @param widthAndHeight 图像的宽高
     * @return
     */
    public static Bitmap createQRCode(String str, int widthAndHeight)
            throws WriterException {

        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 300, 300);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        //通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;


    }


    //生成二维码
    public static void create2QR(Context context, ImageView view, String str) {

        Bitmap bitmap;
        try {

            bitmap = BitmapUtil.createQRCode(str, view.getWidth());
            // 调整颜色
            bitmap = DrawableUtil.translatBitmap(bitmap,
                    context.getResources().getColor(android.R.color.black));
            Drawable drawable = new BitmapDrawable(bitmap);

            if (bitmap != null) {
                view.setImageDrawable(drawable);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}

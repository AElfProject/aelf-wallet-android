package com.github.ont.connector.update;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;

import com.github.ont.connector.ontid.OntIdWebActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.List;

/**
 * @author zhugang
 * 图片选择 重写里面代码
 * OntIdWebActivity.REQUEST_ALBUM_CODE 为返回结果
 * permissions 里面为请求权限，不需要可以去掉
 */
public class ImageUtil {

    public static void setImage(Activity activity) {
        //        PhotoPicker.builder().setPhotoCount(1).setShowCamera(false).setShowGif(false).setPreviewEnabled(true).start(this, REQUEST_ALBUM_CODE);
        Matisse.from(activity).choose(MimeType.allOf()) // 选择 mime 的类型                             .countable(true)
                .maxSelectable(9) // 图片选择的最多数量
//                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED).thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(OntIdWebActivity.REQUEST_ALBUM_CODE); // 设置作为标记的请求码
    }

    public static List<Uri> getChooseResult(Intent data) {
        return Matisse.obtainResult(data);
    }
}

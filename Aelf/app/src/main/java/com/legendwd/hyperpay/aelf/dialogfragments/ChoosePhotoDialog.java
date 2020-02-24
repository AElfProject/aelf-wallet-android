package com.legendwd.hyperpay.aelf.dialogfragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseBottomSheetDialogFragment;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;
import com.legendwd.hyperpay.lib.FileProvider7;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.OnClick;

/**
 * created by joseph at 2019/6/18
 */

public class ChoosePhotoDialog extends BaseBottomSheetDialogFragment {


    public final int RC_CHOOSE_PHOTO = 2;
    public final int RC_TAKE_PHOTO = 1;
    public final int CROP_ALBUM_PHOTO = 3;
    public final int CROP_CAMERA_PHOTO = 4;
    private String mTempPhotoPath;
    private Uri mUri;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_choose_photo;
    }

    @OnClick({R.id.tv_cancel, R.id.tv_album, R.id.tv_camera})
    void onClickCancel(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_camera:
                takePhoto();
                break;
            case R.id.tv_album:
                choosePhoto();
                break;
        }
    }

    private void choosePhoto() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_CHOOSE_PHOTO);
        } else {
            Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
            intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
        }

    }

    private void takePhoto() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, RC_TAKE_PHOTO);
        } else {
            Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = getPhotoFile();
            mTempPhotoPath = photoFile.getAbsolutePath();
            mUri = FileProvider7.getUriForFile(getActivity(), photoFile);
            if (Build.VERSION.SDK_INT >= 24) {
                intentToTakePhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case RC_TAKE_PHOTO:
                if (!isMIUI()) {
                    startPhotoZoom(mUri, CROP_CAMERA_PHOTO);
                } else {
                    if (mHandleCallback != null) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeFile(mTempPhotoPath);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            if (bos.size() / 1024 > 500) {
                                compressBitmap(mTempPhotoPath, mTempPhotoPath);
                            }
                            bos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mHandleCallback.onHandle(mTempPhotoPath);

                    }
                    dismiss();
                }
                break;
            case RC_CHOOSE_PHOTO:
                if (mHandleCallback != null && data != null && data.getData() != null) {
                    if (!isMIUI()) {
                        startPhotoZoom(data.getData(), CROP_ALBUM_PHOTO);
                    } else {
                        try {
                            String path = FileProvider7.uriToFile(data.getData(), getContext()).getAbsolutePath();
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            if (bos.size() / 1024 > 500) {
                                File photoFile = getPhotoFile();
                                if (!photoFile.exists()) {
                                    photoFile.createNewFile();
                                }
                                compressBitmap(path, photoFile.getAbsolutePath());
                            }
                            bos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mHandleCallback.onHandle(data.getData());
                        dismiss();
                    }
                }
                break;
            case CROP_ALBUM_PHOTO:
                if (mHandleCallback != null && data != null && data.getExtras() != null) {
                    try {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        File photoFile = getPhotoFile();
                        if (!photoFile.exists()) {
                            photoFile.createNewFile();
                        }
                        saveBitmap(bitmap, photoFile.getAbsolutePath());
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        if (bos.size() / 1024 > 500) {
                            compressBitmap(photoFile.getAbsolutePath(), photoFile.getAbsolutePath());
                        }
                        bos.close();
                        mHandleCallback.onHandle(photoFile.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                dismiss();
                break;
            case CROP_CAMERA_PHOTO:
                if (mHandleCallback != null) {
                    try {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        saveBitmap(bitmap, mTempPhotoPath);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        if (bos.size() / 1024 > 500) {
                            compressBitmap(mTempPhotoPath, mTempPhotoPath);
                        }
                        bos.close();
                        mHandleCallback.onHandle(mTempPhotoPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                dismiss();
                break;
        }
    }

    /**
     * 保存方法
     */
    private void saveBitmap(Bitmap bm, String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private File getPhotoFile() {
        File fileDir = new File(getActivity().getFilesDir() + "/photoCover" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        String cover = CacheUtil.getInstance().getProperty(Constant.Sp.ACCOUNT_PORTRAIT);
        if (!TextUtils.isEmpty(cover)) {
            File[] files = fileDir.listFiles();
            for (File file : files) {
                if (!file.getAbsolutePath().equals(cover)) {
                    file.delete();
                }
            }
        }
        File photoFile = new File(fileDir, System.currentTimeMillis() + "_cover.jpeg");
        if (!photoFile.exists()) {
            try {
                photoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return photoFile;
    }

    private void compressBitmap(String path, String output) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bm = BitmapFactory.decodeFile(path, options);
        bm = rotateBitmap(bm, getBitmapDegree(path));
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(output));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degree;
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return bmp;
    }

    /**
     * 权限申请结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_TAKE_PHOTO:   //拍照权限申请返回
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                }
                break;
            case RC_CHOOSE_PHOTO:   //相册选择照片权限申请返回
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();
                }
                break;
        }
    }

    private void startPhotoZoom(Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra("crop", "true");
        intent.setDataAndType(uri, "image/*");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, requestCode);
    }

    public boolean isMIUI() {
        String manufacturer = Build.MANUFACTURER;
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

}

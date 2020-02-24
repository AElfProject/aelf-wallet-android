package com.legendwd.hyperpay.aelf.util.zxing;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.gyf.immersionbar.ImmersionBar;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseActivity;
import com.legendwd.hyperpay.aelf.dialogfragments.ToastDialog;
import com.legendwd.hyperpay.aelf.util.DialogUtils;
import com.legendwd.hyperpay.aelf.util.ShowMessage;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.aelf.util.zxing.camera.CameraManager;
import com.legendwd.hyperpay.aelf.util.zxing.camera.PlanarYUVLuminanceSource;
import com.legendwd.hyperpay.aelf.util.zxing.decoding.CaptureActivityHandler;
import com.legendwd.hyperpay.aelf.util.zxing.decoding.InactivityTimer;
import com.legendwd.hyperpay.aelf.util.zxing.view.ViewfinderView;
import com.legendwd.hyperpay.lib.Constant;
import com.legendwd.hyperpay.lib.Logger;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends BaseActivity implements Callback {

    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;
    private final int REQUEST_CODE_SCAN_GALLERY = 10003;
    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    /**
     * 绑定
     */


    Handler handler2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 重新扫描
            continuePreview();
        }
    };
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private boolean vibrate;
    private TextView lightBtn;
    private TextView tvLight;
    private boolean isLight;
    private String photo_path;
    private ProgressDialog mProgress;
    private Bitmap scanBitmap;
    private String oauthCode = "";

    public static Bitmap getSmallerBitmap(Bitmap bitmap) {
        int size = bitmap.getWidth() * bitmap.getHeight() / 160000;
        if (size <= 1) return bitmap; // 如果小于
        else {
            Matrix matrix = new Matrix();
            matrix.postScale((float) (1 / Math.sqrt(size)), (float) (1 / Math.sqrt(size)));
            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return resizeBitmap;
        }
    }

    @OnClick(R.id.tv_title_right)
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.tv_title_right:
                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                innerIntent.setType("image/*");
                startActivityForResult(innerIntent, REQUEST_CODE_SCAN_GALLERY);
                break;
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImmersionBar.with(this).keyboardEnable(true)
                .statusBarColorInt(Color.WHITE)
                .navigationBarColorInt(Color.WHITE)
                .autoDarkModeEnable(true, 0.2f).init();

        Intent intent = getIntent();
        if (null == intent) {
            finish();
            return;
        }
        initToolbarNav(mToolbar, R.string.scan, true);

        tv_title_right.setText(R.string.photo);

        //版本大于6.0的情况
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {
                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                // 方法 checkSelfPermission  在 android 6.0以下没有该方法
                if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }

        tvLight = (TextView) findViewById(R.id.tv_light);
        lightBtn = (TextView) findViewById(R.id.btn_light);
        lightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //版本大于6.0的情况
                if (Build.VERSION.SDK_INT >= 23) {
                    // 方法 checkSelfPermission  在 android 6.0以下没有该方法
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        isLight = !isLight;
                        lightBtn.setSelected(isLight);

                        if (isLight) {
                            CameraManager.get().openLight();
                            tvLight.setText("Tap to turn light off");
                        } else {
                            CameraManager.get().offLight();
                            tvLight.setText("Tap to turn light on");
                        }
                    }
                } else {
                    isLight = !isLight;
                    lightBtn.setSelected(isLight);

                    if (isLight) {
                        CameraManager.get().openLight();
                        tvLight.setText("Tap to turn light off");
                    } else {
                        CameraManager.get().offLight();
                        tvLight.setText("Tap to turn light on");
                    }
                }

            }
        });
        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

    }

    protected void initToolbarNav(Toolbar toolbar, int title, boolean backAble) {
        if (backAble) {
            toolbar.findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CaptureActivity.this.finish();
                }
            });

        } else {
            toolbar.findViewById(R.id.img_back).setVisibility(View.INVISIBLE);
        }
        ((TextView) toolbar.findViewById(R.id.tv_title)).setText(title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_capture;
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_GALLERY:
                    handleAlbumPic(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 处理选择的图片
     *
     * @param data
     */
    private void handleAlbumPic(Intent data) {
        mProgress = new ProgressDialog(CaptureActivity.this);
        mProgress.setMessage(getString(R.string.start_scanning));
        mProgress.setCancelable(false);
        mProgress.show();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgress.dismiss();
                // 首先获取到此图片的Uri
                // 下面这句话可以通过URi获取到文件的bitmap
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(CaptureActivity.this.getContentResolver(), data.getData());
                    // 在这里我用到的 getSmallerBitmap 非常重要，下面就要说到
//                    bitmap = getSmallerBitmap(bitmap);

                    // 获取bitmap的宽高，像素矩阵
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int[] pixels = new int[width * height];
                    bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

                    // 最新的库中，RGBLuminanceSource 的构造器参数不只是bitmap了
                    RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                    BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
                    Reader reader = new MultiFormatReader();
                    Result result = null;
                    // 尝试解析此bitmap，！！注意！！ 这个部分一定写到外层的try之中，因为只有在bitmap获取到之后才能解析。写外部可能会有异步的问题。（开始解析时bitmap为空）
                    try {
                        result = reader.decode(binaryBitmap);

                        if (result != null) {

                            String resultString = result.getText();

                            if (resultString.equals("")) {
                                Toast.makeText(CaptureActivity.this, getResources().getString(R.string.scan_failed), Toast.LENGTH_SHORT).show();

                            } else {
                                switchBranch(resultString);
                            }

                        } else {
                            DialogUtils.showDialog(ToastDialog.class, getSupportFragmentManager()).setToast(R.string.qr_recognise_error);
                        }
                    } catch (NotFoundException e) {
                        Logger.e("onActivityResult: notFind");
                        DialogUtils.showDialog(ToastDialog.class, getSupportFragmentManager()).setToast(R.string.qr_recognise_error);
                        e.printStackTrace();
                    } catch (ChecksumException e) {
                        e.printStackTrace();
                    } catch (FormatException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        //获取选中图片的路径
//		photo_path = UriUtil1.getRealPathFromUri(CaptureActivity.this, data.getData());
//		photo_path = Util.getPath(getApplicationContext(),data.getData());
//
//		mProgress = new ProgressDialog(CaptureActivity.this);
//		mProgress.setMessage("正在扫描...");
//		mProgress.setCancelable(false);
//		mProgress.show();
//
//		runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				mProgress.dismiss();
//
//				Result result = scanningImage(photo_path);
//				if (result != null) {
//
//					switchBranch(result.getText());
//
//				} else {
//					Toast.makeText(CaptureActivity.this, getResources().getString(R.string.qr_recognise_error), Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
    }

    /**
     * 扫描二维码图片的方法
     *
     * @param path
     * @return
     */
    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);

        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        LuminanceSource source1 = new PlanarYUVLuminanceSource(
                rgb2YUV(scanBitmap), scanBitmap.getWidth(),
                scanBitmap.getHeight(), 0, 0, scanBitmap.getWidth(),
                scanBitmap.getHeight());
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source1));
        MultiFormatReader reader1 = new MultiFormatReader();
        Result result1 = null;
        try {
            result1 = reader1.decode(binaryBitmap);
            String content = result1.getText();
            Logger.e(content);
            return result1;
        } catch (NotFoundException e1) {
            e1.printStackTrace();
        }

        return result1;
    }

    public byte[] rgb2YUV(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int len = width * height;
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = pixels[i * width + j] & 0x00FFFFFF;

                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;

                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;

                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);

                yuv[i * width + j] = (byte) y;
//                yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
//                yuv[len + (i >> 1) * width + (j & ~1) + 1] = (byte) v;
            }
        }
        return yuv;
    }

    /**
     * 授权回调处理
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (checkPermissionResult(grantResults)) {

        } else {
            DialogUtils.showDialog(ToastDialog.class, getSupportFragmentManager()).setToast(R.string.permisson_denied);
        }
    }

    /**
     * 检测请求结果码判定是否授权
     *
     * @param grantResults
     * @return
     */
    private boolean checkPermissionResult(int[] grantResults) {
        if (grantResults != null) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void initView() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    void restartCamera() {

        viewfinderView.setVisibility(View.VISIBLE);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        initCamera(surfaceHolder);

        // 恢复活动监控器
    }

    /**
     * 使Zxing能够继续扫描
     */
    public void continuePreview() {
        if (handler != null) {
            handler.restartPreviewAndDecode();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();

        if (resultString.equals("")) {
            DialogUtils.showDialog(ToastDialog.class, getSupportFragmentManager()).setToast(R.string.scan_failed);

        } else {
            switchBranch(resultString);
        }
    }

    private void switchBranch(String resultString) {
        String from = getIntent().getStringExtra(Constant.IntentKey.Scan_Zxing);
        if (TextUtils.isEmpty(from)) {
            return;
        }

        switch (from) {
            case Constant.IntentValue.SCAN_IMPORT_MNEMONIC: {
                if (resultString.split("\\s").length != 12) {
                    ShowMessage.toastErrorMsg(CaptureActivity.this, getString(R.string.mnemonic_not_correct));
                    continuePreview();
                    return;
                }

                boolean flag = true;
                String[] data = resultString.split("\\s");
                for (String s : data) {
                    if (!isWord(s)) {
                        flag = false;
                        break;
                    }
                }

                if (!flag) {
                    ShowMessage.toastErrorMsg(CaptureActivity.this, getString(R.string.mnemonic_not_correct));
                    continuePreview();
                    return;
                }


                Intent intent = new Intent();
                intent.putExtra(Constant.IntentKey.Scan_Zxing, resultString);
                setResult(RESULT_OK, intent);
                CaptureActivity.this.finish();
                break;

            }

            case Constant.IntentValue.SCAN_TRANSFER: {
                String newAddress = "";
                if (resultString.contains("_") && resultString.length() > 1) {
                    String str = resultString.substring(resultString.indexOf("_") + 1);
                    if (str.contains("_")) {
                        String str2 = str.substring(0, str.indexOf("_"));
                        newAddress = str2;
                    }
                } else {
                    newAddress = resultString;
                }
                if (StringUtil.isAddressValid(newAddress)) {
                    if (StringUtil.isAELFAddress(newAddress)) {
                        Intent intent = new Intent();
                        intent.putExtra(Constant.IntentKey.Scan_Zxing, resultString);
                        setResult(RESULT_OK, intent);
                        CaptureActivity.this.finish();
                    } else {
                        ShowMessage.toastErrorMsg(CaptureActivity.this, getString(R.string.not_elf_address));
                        continuePreview();
                    }
                } else {
                    ShowMessage.toastErrorMsg(CaptureActivity.this, getString(R.string.not_elf_address));
                    continuePreview();
                }
                break;

            }
            case Constant.IntentValue.SCAN_ADD_ADDRESS: {
                Intent intent = new Intent();
                intent.putExtra(Constant.IntentKey.Scan_Zxing, resultString);
                setResult(RESULT_OK, intent);
                CaptureActivity.this.finish();
                break;
            }
            case Constant.IntentValue.SCAN_DISCOVERY: {
                Intent intent = new Intent();
                intent.putExtra(Constant.IntentKey.Scan_Zxing, resultString);
                setResult(RESULT_OK, intent);
                CaptureActivity.this.finish();
                break;
            }
            case Constant.IntentValue.SCAN_KEY_MNEMONIC: {
                Intent intent = new Intent();
                intent.putExtra(Constant.IntentKey.Scan_Zxing, resultString);
                setResult(RESULT_OK, intent);
                CaptureActivity.this.finish();
                break;
            }
        }

    }

    private boolean isWord(String mnemonic) {
        return Pattern.compile("[a-z]+").matcher(mnemonic).matches();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }
}

package com.uniquedu.cemetery.zbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uniquedu.cemetery.BaseActivity;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.activity.DeadHomePageActivity;
import com.uniquedu.cemetery.utils.APKDownload;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

/**
 * Created by ZhongHang on 2016/4/12.
 */
public class CameraTestActivity extends BaseActivity {

    private static final int CAMERA_PERMISSION = 0x23;
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    TextView scanText;
    ImageScanner scanner;
    ImageView mImageViewLine;
    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        autoFocusHandler = new Handler();
        mImageViewLine = (ImageView) findViewById(R.id.imageview_line);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.up_down);
        mImageViewLine.startAnimation(animation);
        //判断有无该权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请Camera权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION);
        } else {
            initMethod();
        }
    }


    private void initMethod() {
        mCamera = getCameraInstance();
        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanText = (TextView) findViewById(R.id.scanText);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                initMethod();
            } else {
                // Permission Denied
                Toast.makeText(CameraTestActivity.this, "请打开使用相机的权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    String results = sym.getData();
                    if (!TextUtils.isEmpty(results)) {
                        scanText.setText("barcode result " + sym.getData());
                        barcodeScanned = true;
                        String content = sym.getData();
                        if (!TextUtils.isEmpty(content) && content.contains("www.whjfs.com")) {
//                            http://www.whjfs.com/online/grave.html?id=1065
                            Intent intent = new Intent(getApplicationContext(), DeadHomePageActivity.class);
                            intent.putExtra(DeadHomePageActivity.EXTRA_DEAID, results.substring(results.lastIndexOf('=') + 1, results.length()).trim());
                            startActivity(intent);
                            finish();
                        } else {
                            AlertDialog dialog = new AlertDialog.Builder(CameraTestActivity.this).setTitle("扫描错误").setMessage("扫描二维码信息有误，请重新扫描").setNeutralButton("重新扫描", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (barcodeScanned) {
                                        barcodeScanned = false;
                                        scanText.setText("Scanning...");
                                        mCamera.setPreviewCallback(previewCb);
                                        mCamera.startPreview();
                                        previewing = true;
                                        mCamera.autoFocus(autoFocusCB);
                                    }
                                }
                            }).setCancelable(false).show();
                        }

                    }
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
}



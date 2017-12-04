package com.jidu.scan;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jidu.scan.order.OrderEntity;
import com.jidu.scan.retorift.RequestCallBack;
import com.jidu.scan.utils.MyDialog;

import java.io.InputStream;
import java.util.ArrayList;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

public class TestScanActivity extends AppCompatActivity implements QRCodeView.Delegate {
    private static final String TAG = TestScanActivity.class.getSimpleName();

    private QRCodeView mQRCodeView;
    //    private ArrayList<String> mBarCodeList = new ArrayList<>();
    private String mType = "1";
    private MediaPlayer mediaPlayer;
    private AudioManager mAudioManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scan);
        Toolbar mToolbarTb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTb.setTitle("扫描条形码");

        mToolbarTb.setNavigationOnClickListener(v -> finish());

        mQRCodeView = (ZBarView) findViewById(R.id.zbarview);
        mQRCodeView.setDelegate(this);

        Intent intent = getIntent();
//        mBarCodeList = intent.getStringArrayListExtra("list");
        mType = intent.getStringExtra("type");

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mQRCodeView.showScanRect();
        mQRCodeView.changeToScanBarcodeStyle();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
//        Log.i(TAG, "result:" + result);
//        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
//        if (mBarCodeList.size() > 0) {
//            if (!mBarCodeList.contains(result)) {
//                Toast.makeText(this, "该商品不在列表内", Toast.LENGTH_SHORT).show();
//                mQRCodeView.startSpot();
//                return;
//            }
//        }

        MyDialog myDialog = new MyDialog();
        myDialog.showDialog(this, "扫描结果:" + result + "。\n是否入库？");
        myDialog.setSureText("确认入库");
        myDialog.setCallBack(new MyDialog.DialogCallBack() {
            @Override
            public void handle() {
                check(result);
                mQRCodeView.startSpot();
            }

            @Override
            public void cancelHandle() {
                mQRCodeView.startSpot();
            }
        });


        vibrate();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_spot:
                mQRCodeView.startSpot();
                break;
            case R.id.stop_spot:
                mQRCodeView.stopSpot();
                break;
            case R.id.start_spot_showrect:
                mQRCodeView.startSpotAndShowRect();
                break;
            case R.id.stop_spot_hiddenrect:
                mQRCodeView.stopSpotAndHiddenRect();
                break;
            case R.id.show_rect:
                mQRCodeView.showScanRect();
                break;
            case R.id.hidden_rect:
                mQRCodeView.hiddenScanRect();
                break;
            case R.id.start_preview:
                mQRCodeView.startCamera();
                break;
            case R.id.stop_preview:
                mQRCodeView.stopCamera();
                break;
            case R.id.open_flashlight:
                mQRCodeView.openFlashlight();
                break;
            case R.id.close_flashlight:
                mQRCodeView.closeFlashlight();
                break;
            case R.id.scan_barcode:
                mQRCodeView.changeToScanBarcodeStyle();
                break;
            case R.id.scan_qrcode:
                mQRCodeView.changeToScanQRCodeStyle();
                break;
        }
    }


    private void check(String result) {
//        String type = "1";
//        if (mBarCodeList.size() > 0) {
//            type = "2";
//        }
        Api.getInstance()
                .check(result, mType)
                .callBack(new RequestCallBack() {
                    @Override
                    public void onSuccess(int code, String msg, Object object) {
                        OrderEntity entity = (OrderEntity) object;
                        Toast.makeText(TestScanActivity.this, entity.mess, Toast.LENGTH_LONG).show();
                        /**
                         * 0：入库成功
                         2：订单库无此订单
                         3：非本订单部件
                         */
                        playWav(entity.code);
                    }

                    @Override
                    public void onFailure(int code, String msg, Object object) {

                    }
                }).post();
    }

    public void playWav(int code) {
        //当前音量
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); //tempVolume:音量绝对值
        try {
            switch (code) {
                case 0:
                    mediaPlayer = MediaPlayer.create(this, R.raw.success);
                    break;
                case 2:
                    mediaPlayer = MediaPlayer.create(this, R.raw.none);
                    break;
                case 3:
                    mediaPlayer = MediaPlayer.create(this, R.raw.nothis);
                    break;
                default:

                    break;
            }
            mediaPlayer.start();//开始播放
            mediaPlayer.setOnCompletionListener(arg0 -> {
                mediaPlayer.release();
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0); //tempVolume:音量绝对值
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setVolume() {


    }
}
package com.codetravel.mediarecorder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements SurfaceHolder.Callback{

    private final static String TAG = "MainActivity";

    MediaRecorder mRecorder = null;
    String mPath = null;

    MediaPlayer mPlayer = null;

    boolean isRecording = false;
    boolean isPlaying = false;
    boolean hasVideo = false;

    Button mBtRecord = null;
    Button mBtPlay = null;
    Button mBtCamcording = null;

    SurfaceView mSurface = null;
    SurfaceHolder mSurfaceHolder = null;

    Camera mCamera = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtRecord = (Button) findViewById(R.id.bt_record);
        mBtRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasVideo = false;
                if (isRecording == false) {
                    initAudioRecorder();
                    mRecorder.start();

                    isRecording = true;
                    mBtRecord.setText("Stop Recording");
                } else {
                    mRecorder.stop();
                    isRecording = false;
                    mBtRecord.setText("Start Recording");
                }
            }
        });


        mBtPlay = (Button) findViewById(R.id.bt_play);
        mBtPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying == false) {
                    try {
                        mPlayer.setDataSource(mPath);
                        if(hasVideo == true) {
                            mPlayer.setDisplay(mSurfaceHolder);
                            mPlayer.setOnCompletionListener(mListener);
                        }
                        mPlayer.prepare();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    mPlayer.start();

                    isPlaying = true;
                    mBtPlay.setText("Stop Playing");
                }
                else {
                    mPlayer.stop();

                    isPlaying = false;
                    mBtPlay.setText("Start Playing");
                }
            }
        });

        mRecorder = new MediaRecorder();
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion");
                isPlaying = false;
                mBtPlay.setText("Start Playing");
            }
        });


        mBtCamcording = (Button)findViewById(R.id.bt_camcording);
        mBtCamcording.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                hasVideo = true;
                initVideoRecorder();
                startVideoRecorder();
            }
        });

        mSurface = (SurfaceView)findViewById(R.id.sv);

    }

    MediaPlayer.OnCompletionListener mListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mBtPlay.setText("Start Playing");
        }
    };

    void initVideoRecorder() {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
        mSurfaceHolder = mSurface.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    void initAudioRecorder() {
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/record.aac";
        Log.d(TAG, "file path is " + mPath);
        mRecorder.setOutputFile(mPath);
        try {
            mRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void startVideoRecorder() {
        if(isRecording) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;

            mCamera.lock();
            isRecording = false;

            mBtCamcording.setText("Start Camcording");
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecorder = new MediaRecorder();
                    mCamera.unlock();
                    mRecorder.setCamera(mCamera);
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                    mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
                    mRecorder.setOrientationHint(90);

                    mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/record.mp4";
                    Log.d(TAG, "file path is " + mPath);
                    mRecorder.setOutputFile(mPath);

                    mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
                    try {
                        mRecorder.prepare();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    mRecorder.start();
                    isRecording = true;

                    mBtCamcording.setText("Stop Camcording");
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

        if(mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (mCamera == null) {
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
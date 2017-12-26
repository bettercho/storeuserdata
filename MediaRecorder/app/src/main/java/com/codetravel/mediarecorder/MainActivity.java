package com.codetravel.mediarecorder;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private final static String TAG = "MainActivity";

    MediaRecorder mRecorder = null;
    String mPath = null;

    MediaPlayer mPlayer = null;

    boolean isRecording = false;
    boolean isPlaying = false;

    Button mBtRecord = null;
    Button mBtPlay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtRecord = (Button) findViewById(R.id.bt_record);
        mBtRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}
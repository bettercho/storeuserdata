package com.codetravel.storeuserdata;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.EventListener;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    Button mBtInternal = null;
    Button mBtExternal = null;
    Button mBtPrint = null;

    EditText mEtInput = null;
    TextView mTvOutput = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtInternal = (Button)findViewById(R.id.bt_internal);
        mBtExternal = (Button)findViewById(R.id.bt_external);
        mBtPrint = (Button)findViewById(R.id.bt_print);

        mEtInput = (EditText)findViewById(R.id.et_intput);
        mTvOutput = (TextView)findViewById(R.id.tv_output);

        mBtInternal.setOnClickListener(listener);
        mBtExternal.setOnClickListener(listener);
        mBtPrint.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String inputData = mEtInput.getText().toString();

            switch(view.getId()) {
                case R.id.bt_internal:
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput("internal.txt", Context.MODE_PRIVATE);
                        fos.write(inputData.getBytes());
                        fos.close();;

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.bt_external:
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File file = new File(Environment.getExternalStorageDirectory(), "External.txt");
                        try {
                            FileWriter fw = new FileWriter(file, false);
                            fw.write(inputData);
                            fw.close();
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Log.d(TAG, "External Storage is not ready");
                    }

                    break;
                case R.id.bt_print:
                    StringBuffer buffer = new StringBuffer();
                    String data = null;
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput("internal.txt");
                        BufferedReader iReader = new BufferedReader(new InputStreamReader((fis)));

                        data = iReader.readLine();
                        while(data != null)
                        {
                            buffer.append(data);
                            data = iReader.readLine();
                        }
                        buffer.append("\n");
                        iReader.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String path = Environment.getExternalStorageDirectory() + "/External.txt";

                    try {
                        BufferedReader eReader = new BufferedReader(new FileReader(path));
                        data = eReader.readLine();
                        while(data != null)
                        {
                            buffer.append(data);
                            data = eReader.readLine();
                        }
                        mTvOutput.setText(buffer.toString()+"\n");
                        eReader.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };
}

package com.codetravel.storeuserdata;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Created by 조연진 on 2018-02-05.
 */

public class DBActivity extends Activity {

    private final String TAG = "DBActivity";
    private String DB_PATH =  "/storage/emulated/0/person.db";

    ListView mLvDatabase = null;
    Button mBtInsert = null;

    EditText mEtName = null;
    EditText mEtAge = null;
    EditText mEtAddress = null;

    MyDBHandler mHandler = null;
    Cursor mCursor = null;
    SimpleCursorAdapter mAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        mLvDatabase = (ListView)findViewById(R.id.lv_database);
        mLvDatabase.setOnItemLongClickListener(mLongClickListener);

        mBtInsert = (Button)findViewById(R.id.bt_insert);
        mBtInsert.setOnClickListener(mListener);

        mEtName = (EditText)findViewById(R.id.et_name);
        mEtAge = (EditText)findViewById(R.id.et_age);
        mEtAddress = (EditText)findViewById(R.id.et_address);

        if( mHandler == null ) {
            mHandler = MyDBHandler.open(DBActivity.this, DB_PATH);
        }
        mHandler.insert("홍길동", 47, "경기도 고양시");

        mCursor = mHandler.select();
        mAdapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_activated_2,
                mCursor, new String[]{"name", "age"}, new int[]{android.R.id.text1, android.R.id.text2}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        mLvDatabase.setAdapter(mAdapter);
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_insert:
                    insertToDB();
                    break;
                default:
                    break;
            }
        }
    };

    AdapterView.OnItemLongClickListener mLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            mCursor.moveToPosition(position);
            Log.d(TAG, "index : "+mCursor.getString(0) + "name : " + mCursor.getString(1));
            mHandler.delete(mCursor.getString(1));

            mCursor = mHandler.select();  // DB 새로 가져오기
            mAdapter.changeCursor(mCursor); // Adapter에 변경된 Cursor 설정하기
            mAdapter.notifyDataSetChanged(); // 업데이트 하기

            return true;
        }
    };

    void insertToDB() {
        Log.d(TAG, "insertToDB");

        String name, address;
        int age;

        if( (mEtName.getText() != null) && (mEtAge.getText() != null)
                && (mEtAddress.getText() != null) ) {
            name = mEtName.getText().toString();
            age = Integer.parseInt(mEtAge.getText().toString());
            address = mEtAddress.getText().toString();
        }
        else {
            Toast.makeText(DBActivity.this, "Please enter the all members", Toast.LENGTH_SHORT).show();
            return;
        }

        mHandler.insert(name, age, address);

        mCursor = mHandler.select();  // DB 새로 가져오기
        mAdapter.changeCursor(mCursor); // Adapter에 변경된 Cursor 설정하기
        mAdapter.notifyDataSetChanged(); // 업데이트 하기

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.close();
    }
}

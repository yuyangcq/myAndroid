package com.dhu.android.activity;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.dhu.android.R;
import com.dhu.android.utils.MySortBySSID;
import com.dhu.android.utils.WifiUtil;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    private final String DEBUG_TAG = "MainActivity";

    private TextView mTextView = null;
    private EditText mEditText = null;
    private Button mButton = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mButton = (Button) findViewById(R.id.Button01);
        mTextView = (TextView) findViewById(R.id.TextView01);
        mEditText = (EditText) findViewById(R.id.EditText01);
        mButton.setOnClickListener(new editListener());

    }

    class editListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Socket socket = null;
            //获取扫描出的网络连接列表
            List<ScanResult> results = WifiUtil.getAllNetWorkList(MainActivity.this);
            //去除同名SSID
            List<ScanResult> noSameNameResults = WifiUtil.noSameName(results);
            //对集合中的对象按照SSID名称进行排序
            Collections.sort(noSameNameResults, new MySortBySSID());

            String message = mEditText.getText().toString();
            try {
                //创建Socket
                socket = new Socket("172.16.201.176", 54321); //IP：172.16.201.176，端口54321
                //向服务器发送消息
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.println(message);

                //接收来自服务器的消息
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg = br.readLine();

                if (msg != null) {
                    mTextView.setText(msg);
                } else {
                    mTextView.setText("数据错误!");
                }
                //关闭流
                out.close();
                br.close();
                //关闭Socket
                socket.close();
            } catch (Exception e) {
                // TODO: handle exception
                Log.e(DEBUG_TAG, e.toString());
            }
        }
    }
}




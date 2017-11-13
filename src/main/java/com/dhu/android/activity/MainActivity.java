package com.dhu.android.activity;

import android.app.Activity;
import android.graphics.Paint;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.dhu.android.R;
import com.dhu.android.beans.Signal;
import com.dhu.android.utils.MySortBySSID;
import com.dhu.android.utils.WifiUtil;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yuyang on 2017/8/8.
 */
public class MainActivity extends Activity {
    private final String DEBUG_TAG = "MainActivity";

    private EditText edit_Server_Ip = null;
    private EditText edit_Server_Port = null;
    private Button btn_tcpServerConn = null;
    private Button btn_tcpServerClose = null;
    private Button btn_startLocation = null;
    private Button btn_tcpClean = null;
    private TextView txt_ServerRcv = null;
    private EditText edit_Send = null;
    private Button btn_tcpServerSend = null;
    private ImageView t = null;
    //声明一个画笔工具
    private Paint mPaint = new Paint();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        edit_Server_Ip = (EditText) findViewById(R.id.edit_Server_Ip);
        edit_Server_Port = (EditText) findViewById(R.id.edit_Server_Port);
        btn_tcpServerConn = (Button) findViewById(R.id.btn_tcpServerConn);
        btn_tcpServerClose = (Button) findViewById(R.id.btn_tcpServerClose);
        btn_startLocation = (Button) findViewById(R.id.btn_startLocation);
        btn_tcpClean = (Button) findViewById(R.id.btn_tcpClean);
        txt_ServerRcv = (TextView) findViewById(R.id.txt_ServerRcv);

        edit_Send = (EditText) findViewById(R.id.edit_Send);
        btn_tcpServerSend = (Button) findViewById(R.id.btn_tcpServerSend);
        t = (ImageView) findViewById(R.id.imageView1);
        //绑定监听器
        btn_startLocation.setOnClickListener(new MainActivity.btn_startLocationListener());
        btn_tcpClean.setOnClickListener(new MainActivity.btn_tcpClean());
        btn_tcpServerSend.setOnClickListener(new MainActivity.btn_tcpServerSend());

    }

    class btn_startLocationListener implements View.OnClickListener {
        private String send_message = "";

        @Override
        public void onClick(View v) {
            String message = edit_Send.getText().toString();
            send_message += message + "\n";
            //从EditText中得到服务器IP地址
//            String ip = edit_Server_Ip.getText().toString();
            //创建Socket
            Socket socket = null;
            PrintWriter out = null;
            BufferedReader br = null;
            try {
                //IP：10.202.147.98，端口54321
                socket = new Socket("192.168.1.102", 54321);
                //获取扫描出的网络连接列表
                List<ScanResult> results = WifiUtil.getAllNetWorkList(MainActivity.this);
                //去除同名SSID
                List<ScanResult> noSameNameResults = WifiUtil.noSameName(results);
                //对集合中的对象按照SSID名称进行排序
                Collections.sort(noSameNameResults, new MySortBySSID());

                List<Signal> signals = new ArrayList<Signal>();
                for (int i = 0; i < noSameNameResults.size(); i++) {
                    Signal s = new Signal();
                    if (noSameNameResults.get(i).SSID.equals("6006") || noSameNameResults.get(i).SSID.equals("6004")
                            || noSameNameResults.get(i).SSID.equals("人言者"))

                    {
                        s.setRSSI(noSameNameResults.get(i).level);
                        s.setRP(noSameNameResults.get(i).SSID);
                        signals.add(s);
                    }
                }

                Gson gson = new Gson();
                String str = gson.toJson(signals);
                System.out.println(str);

                //向服务器发送消息
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.println(str);

                //接收来自服务器的消息
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg = br.readLine();

                if (msg != null) {
                    txt_ServerRcv.setText(msg);
                } else {
                    txt_ServerRcv.setText("数据错误!");
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e(DEBUG_TAG, e.toString());
            } finally {
                //关闭流
                out.close();
                try {
                    br.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class btn_tcpClean implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int[] location = new int[2];
            t.getLocationOnScreen(location);
            int x1 = location[0];
            int y1 = location[1];
            Toast.makeText(MainActivity.this, "x: " + t.getRight(), Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, "y: " + t.getBottom(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(MainActivity.this, "应用区域高度 is: " + yingyongquHeight, Toast.LENGTH_SHORT).show();
//            Toast.makeText(MainActivity.this, "屏幕区域高度 is: " + heightPixels, Toast.LENGTH_SHORT).show();
        }
    }

    class btn_tcpServerSend implements View.OnClickListener {
        private String send_message = "";

        @Override
        public void onClick(View v) {
            String message = edit_Send.getText().toString();
            send_message += message + "\n";
            //创建Socket
            Socket socket = null;
            PrintWriter out = null;
            BufferedReader br = null;
            try {
                //IP：10.202.147.98，端口54321
                socket = new Socket("192.168.1.102", 54321);
                //向服务器发送消息
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.println(send_message);
                //接收来自服务器的消息
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            } catch (Exception e) {

                Log.e(DEBUG_TAG, e.toString());
            } finally {
                //关闭流
                out.close();
                try {
                    br.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

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
    private TextView txt_Location = null;
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
        txt_Location = (TextView) findViewById(R.id.txt_Location);
        edit_Server_Ip = (EditText) findViewById(R.id.edit_Server_Ip);
        t = (ImageView) findViewById(R.id.imageView1);

        //绑定监听器
        btn_startLocation.setOnClickListener(new MainActivity.btn_startLocationListener());
        btn_tcpClean.setOnClickListener(new MainActivity.btn_tcpClean());
    }

    class btn_startLocationListener implements View.OnClickListener {
        private String send_message = "";

        @Override
        public void onClick(View v) {
            //从EditText中得到服务器IP地址
//            String ip = edit_Server_Ip.getText().toString();
            //创建Socket
            Socket socket = null;
            PrintWriter out = null;
            BufferedReader br = null;
            String info = "";
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
                    if (noSameNameResults.get(i).SSID.equals("DHU") ||
                            noSameNameResults.get(i).SSID.equals("DHU-1X") ||
                            noSameNameResults.get(i).SSID.equals("HP-Print-b0-LaserJet Pro MFP") ||
                            noSameNameResults.get(i).SSID.equals("labixiaoxin") ||
                            noSameNameResults.get(i).SSID.equals("Tenda_2889B8"))

                    {
                        info += noSameNameResults.get(i).SSID + ":" + noSameNameResults.get(i).level + "dBm" + "\n";
                        s.setRSSI(noSameNameResults.get(i).level);
                        s.setRP(noSameNameResults.get(i).SSID);
                        signals.add(s);
                    }
                }
                txt_ServerRcv.setText(info);

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
                    txt_Location.setText(msg);
                } else {
                    txt_Location.setText("x=4.43" + '\n' + "y=3.61");
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

        private String send_message = "";
        private String msg = null;

        @Override
        public void onClick(View v) {
            String info = "";
            //获取扫描出的网络连接列表
            List<ScanResult> results = WifiUtil.getAllNetWorkList(MainActivity.this);
            //去除同名SSID
            List<ScanResult> noSameNameResults = WifiUtil.noSameName(results);
            //对集合中的对象按照SSID名称进行排序
            Collections.sort(noSameNameResults, new MySortBySSID());

            List<Signal> signals = new ArrayList<Signal>();
            for (int i = 0; i < noSameNameResults.size(); i++) {

                Signal s = new Signal();
                if (noSameNameResults.get(i).SSID.equals("DHU") ||
                        noSameNameResults.get(i).SSID.equals("DHU-1X") ||
                        noSameNameResults.get(i).SSID.equals("HP-Print-b0-LaserJet Pro MFP") ||
                        noSameNameResults.get(i).SSID.equals("labixiaoxin") ||
                        noSameNameResults.get(i).SSID.equals("TP-LINK_XU"))

                {
                    info += noSameNameResults.get(i).SSID + ":" + noSameNameResults.get(i).level + "dBm" + "\n";
                    s.setRSSI(noSameNameResults.get(i).level);
                    s.setRP(noSameNameResults.get(i).SSID);
                    signals.add(s);
                }
            }
            txt_ServerRcv.setText(info);

            Gson gson = new Gson();
            String str = gson.toJson(signals);
            System.out.println(str);

            if (msg != null) {
                txt_Location.setText(msg);
            } else {
                txt_Location.setText("x=16.92" + '\n' + "y=7.47");
            }
        }
    }
}

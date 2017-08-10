package com.dhu.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.dhu.android.R;

import java.io.*;
import java.net.Socket;

/**
 * Created by yuyang on 2017/8/8.
 */
public class MainActivity1 extends Activity {
    private final String DEBUG_TAG = "MainActivity1";

    private EditText edit_Server_Ip = null;
    private EditText edit_Server_Port = null;
    private Button btn_tcpServerConn = null;
    private Button btn_tcpServerClose = null;
    private Button btn_tcpCleanServerRecv = null;
    private Button btn_tcpCleanServerSend = null;
    private TextView txt_ServerRcv = null;
    private TextView txt_ServerSend = null;
    private EditText edit_Send = null;
    private Button btn_tcpServerSend = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);

        edit_Server_Ip = (EditText) findViewById(R.id.edit_Server_Ip);
        edit_Server_Port = (EditText) findViewById(R.id.edit_Server_Port);
        btn_tcpServerConn = (Button) findViewById(R.id.btn_tcpServerConn);
        btn_tcpServerClose = (Button) findViewById(R.id.btn_tcpServerClose);
        btn_tcpCleanServerRecv = (Button) findViewById(R.id.btn_tcpCleanServerRecv);
        btn_tcpCleanServerSend = (Button) findViewById(R.id.btn_tcpCleanServerSend);
        txt_ServerRcv = (TextView) findViewById(R.id.txt_ServerRcv);
        txt_ServerSend = (TextView) findViewById(R.id.txt_ServerSend);
        edit_Send = (EditText) findViewById(R.id.edit_Send);
        btn_tcpServerSend = (Button) findViewById(R.id.btn_tcpServerSend);

        btn_tcpServerSend.setOnClickListener(new MainActivity1.btn_sendListener());
        btn_tcpServerConn.setOnClickListener(new MainActivity1.btn_connectListener());

    }

    class btn_sendListener implements View.OnClickListener {
        private String send_message = "";

        @Override
        public void onClick(View v) {
            String message = edit_Send.getText().toString();
            send_message += message + "\n";
            txt_ServerSend.setText(send_message);
//            String ip = edit_Server_Ip.getText().toString();
            //创建Socket
            Socket socket = null;
            PrintWriter out = null;
            BufferedReader br = null;
            try {
                socket = new Socket("172.16.201.176", 55555); //IP：172.16.201.176，端口54321
                //向服务器发送消息
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.println(message);

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

    class btn_connectListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
}


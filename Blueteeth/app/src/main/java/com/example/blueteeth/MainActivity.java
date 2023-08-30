package com.example.blueteeth;


import static com.example.blueteeth.Constant.info;
import static com.example.blueteeth.Constant.stringBuilder;
import static com.example.blueteeth.Constant.sw1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import world.shanya.serialport.SerialPort;
import world.shanya.serialport.SerialPortBuilder;
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        SerialPort serialPort = SerialPortBuilder.INSTANCE
//                .setReceivedDataCallback( (data) -> {
//
//                    return null;
//                })
//                .build(this);
//
//        serialPort.doDiscovery(this);//search equipment;
//        serialPort.getPairedDevicesListBD();	//获取已配对设备列表
//        serialPort.getUnPairedDevicesListBD();	//获取未配对设备列表
//        //SerialPort.Companion.setLegacyUUID("00001101-0000-1000-8000-00805F9B34FB");
//        SerialPort.Companion.setBleUUID("0000ffe1-0000-1000-8000-00805f9b34fb");
//        serialPort.openDiscoveryActivity();
//        serialPort.sendData("Hello World");
//
//
//    }
//}
//import androidx.appcompat.app.AppCompatActivity;
//
//        import android.annotation.SuppressLint;
//        import android.bluetooth.BluetoothDevice;
//        import android.os.Bundle;
//        import android.widget.Button;
//        import android.widget.EditText;
//        import android.widget.TextView;
//
//        import kotlin.Unit;
//        import kotlin.jvm.functions.Function1;
//        import kotlin.jvm.functions.Function2;
//        import world.shanya.serialport.SerialPort;
//        import world.shanya.serialport.SerialPortBuilder;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //StringBuilder stringBuilder = new StringBuilder();
        TextView textViewReceived= findViewById(R.id.textViewReceiced);
        TextView textViewConnectInfo = findViewById(R.id.textViewConnectInfo);
        Button buttonConnect = findViewById(R.id.buttonConnect);
        Button buttonDisconnect = findViewById(R.id.buttonDisconnect);
        EditText editTextSendData = findViewById(R.id.editTextTextSend);
        Button buttonSend = findViewById(R.id.buttonSend);
        Button buttonOutputwave = findViewById(R.id.buttonOutputwave);//输出波形按钮
        Button buttonClear= findViewById(R.id.buttonClear);

        //xml2



        SerialPort serialPort = SerialPortBuilder.INSTANCE
                .setReadDataType(SerialPort.READ_HEX)
                /**creat an receive massaage监听器**/
                .setReceivedDataCallback((Function1<String, Unit>) s -> {
                    runOnUiThread(() -> {
                        stringBuilder.append(s);//
                        textViewReceived.setText(stringBuilder);
                        if(sw1==0&(stringBuilder.length()-Constant.q*Constant.ys2.length>16)){
                            sw1=1;Toast.makeText(MainActivity.this, "enough", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return null;
                })
                .setConnectionStatusCallback((aBoolean, bluetoothDevice) -> {
                    runOnUiThread(() -> {


                        if (aBoolean) {
                            info = "设备名称:\t" + bluetoothDevice.getName() +
                                    "\n设备地址:\t" + bluetoothDevice.getAddress() +
                                    "\n 设备类型:\t" + bluetoothDevice.getType();

                        }else{
                            info = "No connection";
                        }
                        textViewConnectInfo.setText(info);
                    });
                    return null;
                })
                .build(this);

/*/listener(v)->"serialport" internal UI, internally crated;_opendiscovery UI*/
        buttonConnect.setOnClickListener((v) -> serialPort.openDiscoveryActivity());

        buttonDisconnect.setOnClickListener((v)-> serialPort.disconnect());

        buttonSend.setOnClickListener((v-> serialPort.sendData(editTextSendData.getText().toString())));
//click 输出波形按钮；
        buttonClear.setOnClickListener((v->{
        stringBuilder.delete(0,100000);
        sw1=0;
        textViewReceived.setText("");}));
        /*buttonClear.setOnClickListener((v->{
       }));*/
        textViewConnectInfo.setText(info);
        //按钮进行监听
        buttonOutputwave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "跳转输出波形页面", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();
                intent1.putExtra("str","123");
                intent1.setClass(MainActivity.this, MainActivity2.class);
                startActivity(intent1);
           }
        });
    }
}

//        buttonOutputwave.setOnClickListener((v)-> {
//            Toast.makeText(MainActivity.this, "该部分代码思考中——画图的代码", Toast.LENGTH_LONG).show();
//        }

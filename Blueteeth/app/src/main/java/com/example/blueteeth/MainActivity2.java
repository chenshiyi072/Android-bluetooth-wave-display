package com.example.blueteeth;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.blueteeth.Constant.buff;
import static com.example.blueteeth.Constant.ys2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import world.shanya.serialport.SerialPort;
import world.shanya.serialport.SerialPortBuilder;

public class MainActivity2 extends AppCompatActivity {
    private LineChart lineChart; //折线图控件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏
//        //设置窗体全屏，进行全屏显示。否则横屏时，会出现状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();
        TextView textViewVT=findViewById(R.id.textViewVT);
        //TextView textViewR=findViewById(R.id.textViewR);
        Button buttonBack=findViewById(R.id.buttonBack);
        Button buttonStart=findViewById(R.id.buttonStart);
        Button buttonStop=findViewById(R.id.buttonStop);
        Button buttonA=findViewById(R.id.buttonA);
        Button buttonB=findViewById(R.id.buttonB);
        Button buttonE=findViewById(R.id.buttonE);
        Button buttonVpp=findViewById(R.id.buttonVpp);
        Button buttonFFTA=findViewById(R.id. buttonFFTA);
         lineChart=findViewById(R.id.Wave1);

        Intent intent2=getIntent();
        String value=intent2.getStringExtra("str");
        //textViewR.setText(value);


        SerialPort serialPort2 = SerialPortBuilder.INSTANCE
                /**creat an receive massaage监听器**/
                  .build(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }//draw 隐藏系统默认标题
       initLineChart();//图形刷新由动画控制；
   //goBack button listener;
     buttonBack.setOnClickListener((v)->
     {
         Toast.makeText(MainActivity2.this, "正在返回上一级菜单", Toast.LENGTH_SHORT).show();
     intent2.setClass(MainActivity2.this,MainActivity.class);
     startActivity(intent2);
     } );

     buttonStart.setOnClickListener((v)->{/*START*/
         if (Constant.stringBuilder.length()>=Constant.q*Constant.ys2.length)
         {
             //发现问题ys2数据不能及时更新，至少点三次；------>解决，点数不够多的问题；
             //vref只有第一位有用；
             buff=Constant.stringBuilder.substring(0,Constant.q*Constant.ys2.length-1);//长度足够
             buff.replaceAll(" ","");
            Constant.stringBuilder.delete(0,Constant.q*Constant.ys2.length-1);
             Log.e(TAG, "onCreate: hex     +代码运行到89行：");

         float[] data_decimal = new float[Constant.ys2.length];

         serialPort2.sendData("D");int dataN=0;
         Constant.sw=1;//画图switch

         for(int n=1;n<buff.length()-3;n=n+1)
         {

            if(buff.charAt(n)<='Z'&buff.charAt(n)>='F')
            {
                String data_hex=buff.substring(0,n-1);//FF之前的数据
                if (buff.length()<=5)break;//理论上不会出发这个break；
                buff=buff.substring(n+2);//剩下的数据
                n=0;//标号重新开始
                data_decimal[dataN]=decimalToVref(data_hex)-2.5f;//之前数据转电压
                dataN=dataN+1;
                Log.e(TAG, "onCreate: hex");

            }

            if (dataN==Constant.ys2.length)
            {
//                for(int n2=1;n2<Constant.ys2.length-2)
//                {
//                    if(n2)
//                }
                Constant.ys2=data_decimal;
                Log.e(TAG, "onCreate: 135数据已经更新");
                break;
            }//为什么不退出啊

         }
         }

         else{Toast.makeText(MainActivity2.this,"数据不够",Toast.LENGTH_SHORT);}
         initLineChart();
       // textViewVT.setText(caculateVpp()+"V");
     });


     buttonStop.setOnClickListener((v)->{

         serialPort2.sendData("CFF");
         Constant.sw=0;
         Toast.makeText(MainActivity2.this,"停止获取信号"+Constant.sw,Toast.LENGTH_SHORT).show();
     });
     buttonA.setOnClickListener((v)->{
            Toast.makeText(MainActivity2.this,"混合信号",Toast.LENGTH_SHORT).show();
         serialPort2.sendData("AFF");

     });
     buttonB.setOnClickListener((v)->{
            Toast.makeText(MainActivity2.this,"噪声信号",Toast.LENGTH_SHORT).show();
         serialPort2.sendData("BFF");
     });
     buttonE.setOnClickListener((v)->{
            Toast.makeText(MainActivity2.this,"输出信号",Toast.LENGTH_SHORT).show();
         serialPort2.sendData("EFF");
     });
        buttonVpp.setOnClickListener((v)->{
           Float Vpp=caculateVpp();
           textViewVT.setText(String.valueOf(Vpp)+" V");
        });
        buttonFFTA.setOnClickListener((v)->{
            Intent intent3 = new Intent();
            intent3.setClass(MainActivity2.this, MainActivity3.class);
            startActivity(intent3);
        });
    }
    /**
     * 初始化图表数据
     */
    private void initLineChart(){
        lineChart.animateXY(500, 500); // 呈现动画speed
        Description description = new Description();
        description.setText("滤波后的信号"); //自定义描述
        lineChart.setDescription(description);
        Legend legend = lineChart.getLegend();
        legend.setTextColor(Color.WHITE);
        setYAxis();
        setXAxis();
        setData();

    }
    /**
         * 设置Y轴数据
         */
        private void setYAxis(){
            YAxis yAxisLeft = lineChart.getAxisLeft();// 左边Y轴
            yAxisLeft.setDrawAxisLine(true); // 绘制Y轴
            yAxisLeft.setDrawLabels(true); // 绘制标签
            yAxisLeft.setAxisMaxValue(3); // 设置Y轴最大值
            yAxisLeft.setAxisMinValue(-3); // 设置Y轴最小值
            yAxisLeft.setGranularity(0.001f); // 设置间隔尺寸
            yAxisLeft.setTextColor(Color.WHITE); //设置颜色
            yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return value + "V";//单位
                }
            });
            // 右侧Y轴
            lineChart.getAxisRight().setEnabled(false); // 不启用
        }

        /**
         * 设置X轴数据
         */
        private void setXAxis(){
            // X轴
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setDrawAxisLine(true); // 绘制X轴
            xAxis.setDrawLabels(true); // 绘制标签
            xAxis.setAxisMaxValue(Constant.ys2.length); // 设置X轴最大值
           xAxis.setAxisMinValue(0); // 设置X轴最小值
            xAxis.setGranularity(1f); // 设置间隔尺寸

//            xAxis.setDrawAxisLine(true); // 绘制X轴
//            xAxis.setDrawGridLines(false); // 不绘制网格线
//            // 模拟X轴标签数据
//            final String[] weekStrs = new String[]{"1", "2", "3", "4", "5", "6","7"};
//            xAxis.setLabelCount(weekStrs.length); // 设置标签数量
//            xAxis.setTextColor(Color.GREEN); // 文本颜色
//            xAxis.setTextSize(5f); // 文本大小为18dp
//            xAxis.setGranularity(1f); // 设置间隔尺寸
            // 使图表左右留出点空位
//            xAxis.setAxisMinimum(-0.01f); // 设置X轴最小值
//          xAxis.setAxisMaximum(6f);
            //设置颜色
            xAxis.setTextColor(Color.WHITE);
            // 设置标签的显示格式
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {

                    return  value*1/Constant.frequecy+"ms";}
                });

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 在底部显示
        }

        /**
         * 填充数据
         */
        private void setData(){
            // 模拟数据1
            List<Entry> yVals1 = new ArrayList<>();
            float[] ys1 = new float[Constant.ys2.length];
            // 模拟数据2
            List<Entry> yVals2 = new ArrayList<>();
            float[] ys2 = Constant.ys2;

            for (int i = 0; i < ys1.length; i++) {
                yVals1.add(new Entry(i, ys1[i]));

            }
            for (int i = 0; i < ys2.length; i++) {
                //加一个滤波，左右数据进行一个比较
                if(i<10||i>ys2.length-4){}
                //一个异常点的处理： handle
                else {
                    if((Math.abs(ys2[i]-ys2[i-1])-0.5>Math.abs(ys2[i-1]-ys2[i-2])&
                        Math.abs(ys2[i+1]-ys2[i])-0.5>Math.abs(ys2[i+2]-ys2[i+1])))
                    {ys2[i]= (ys2[i-1]+ ys2[i+1])/2;}
                    if (Math.abs(ys2[i]-ys2[i-1])>1&ys2[i]==0&Math.abs(ys2[i+2]-ys2[i+1])>0.2){
                        ys2[i]= (ys2[i-1]+ ys2[i+1])/2;}
                }
                yVals2.add(new Entry(i, ys2[i]));
            }
            // 2. 分别通过每一组Entry对象集合的数据创建折线数据集
            LineDataSet lineDataSet1 = new LineDataSet(yVals1, "0点基准电压");
            LineDataSet lineDataSet2 = new LineDataSet(yVals2, "信号波形");
            lineDataSet2.setCircleColor(Color.RED); //设置点圆的颜色
            lineDataSet1.setCircleRadius(1); //设置点圆的半径
            lineDataSet2.setCircleRadius(1); //设置点圆的半径
            lineDataSet1.setDrawCircleHole(false); // 不绘制圆洞，即为实心圆点
            lineDataSet2.setDrawCircleHole(false); // 不绘制圆洞，即为实心圆点
            lineDataSet2.setColor(Color.RED); // 设置为红色
            // 值的字体大小为12dp
            lineDataSet1.setValueTextSize(10f);
            lineDataSet2.setValueTextSize(10f);
            //将每一组折线数据集添加到折线数据中
            LineData lineData = new LineData(lineDataSet1,lineDataSet2);
            //设置颜色
            lineData.setValueTextColor(Color.WHITE);
            //将折线数据设置给图表
            lineChart.setData(lineData);
        }
private float decimalToVref(@NonNull String hex){//一个点的电压,默认进来是30，32，41mode
            float vref=0.000f;
            int []decimal=new int [4];
    if(hex.length()<2)return vref;
    //只有第一位数据有效，实测；大小约大了三倍；
            int i=0;float data=0;String hex1;//最多四位
            for(i=0;i<4;i++){//i就是位数,0-默认一位
                if(hex.length()<=1)break;
                if(hex.charAt(0)=='3'){
            switch(hex.charAt(1)){
                case(Constant.S0):decimal[i]=0;hex1=hex.substring(2);break;
                case(Constant.S1):decimal[i]=1;hex1=hex.substring(2);break;
                case(Constant.S2):decimal[i]=2;hex1=hex.substring(2);break;
                case(Constant.S3):decimal[i]=3;hex1=hex.substring(2);break;
                case(Constant.S4):decimal[i]=4;hex1=hex.substring(2);break;
                case(Constant.S5):decimal[i]=5;hex1=hex.substring(2);break;
                case(Constant.S6):decimal[i]=6;hex1=hex.substring(2);break;
                case(Constant.S7):decimal[i]=7;hex1=hex.substring(2);break;
                case(Constant.S8):decimal[i]=8;hex1=hex.substring(2);break;
                case(Constant.S9):decimal[i]=9;hex1=hex.substring(2);break;
                default:hex1=hex.substring(2);decimal[i]=0;break;
            }hex=hex1;
            if(hex.length()<=1){i++;break;}
            //if(decimal[i]==10) Log.d(TAG, "decimalToVref:no ");return 1;
    }else{hex1=hex.substring(1);hex=hex1;decimal[i]=0;i--;}}
        int data1;
            data1=decimal[0]*((int)Math.pow(10, i-1))+decimal[1]*((int)Math.pow(10, i-2))
                    +decimal[2]*((int)Math.pow(10, i-3))+decimal[3]*((int)Math.pow(10, i-4));
        data=(float)data1;
           // vref=Math.round(data/(2^(12))*(float)5*1000f)/1000f;//保留两位小数
    vref=data/((float)Math.pow(2, 12))*(float)5;//保留两位小数
    Log.e(TAG, "decimalToVref: Href" );
            return vref;//起作用了
}
private float caculateVpp(){
          float[]str=ys2;
    Arrays.sort(str);//从小到大排序
    float []max=new  float[]{str[ys2.length-1],str[ys2.length-2],str[ys2.length-3]};
    float []min=new  float[]{str[0],str[1],str[2],str[3],str[4]};
    float VT=(max[1]-min[3]+max[2]-min[4])/2;
    return VT;

}














//16进制转二进制
public static String hex2bin(String input) {
    StringBuilder sb = new StringBuilder();
    int len = input.length();
    System.out.println("原数据长度：" + (len / 2) + "字节");
    for (int i = 0; i < len; i++){
        //每1个十六进制位转换为4个二进制位
        String temp = input.substring(i, i + 1);
        int tempInt = Integer.parseInt(temp, 16);
        String tempBin = Integer.toBinaryString(tempInt);}
    return sb.toString();
}
        //如果二进制数不足4位，补0
//        if (tempBin.length() < 4){
//            int num = 4 - tempBin.length();
//            for (int j = 0; j < num; j++){
//                sb.append("0");
//            }
        }
        //sb.append(tempBin);





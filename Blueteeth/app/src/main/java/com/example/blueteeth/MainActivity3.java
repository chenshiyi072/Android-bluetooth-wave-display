package com.example.blueteeth;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.blueteeth.Constant.frequecy;
import static com.example.blueteeth.Constant.ys2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

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
import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    private LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        lineChart=findViewById(R.id.FFTpicture);
        Button buttonBack=findViewById(R.id.buttonBack);
        Button buttonFFT=findViewById(R.id.buttonFFT);

        int i=0;
        //取ys2的500个点，
for( i=0;i<15;i++){
    if(Math.pow(2,i)<ys2.length&Math.pow(2,i+1)>ys2.length){
        Toast.makeText(MainActivity3.this,"阶数"+i,Toast.LENGTH_SHORT);
    break;
}}
        Toast.makeText(MainActivity3.this,"阶数"+i,Toast.LENGTH_SHORT);
        float []data=new float[(int)(Math.pow(2,i)+0.4f)];
//
for(i=0;i<data.length;i++){data[i]=ys2[i];}
//        if (ys2.length<500){data=data2;}
//        else{data=data1;}
        Log.d(TAG, "onCreate:62hang ");
        double[] xN=new double[data.length];
        int N=data.length;
        //傅里叶变换计算
        Complex[] input = new Complex[N];//声明复数数组
        Log.d(TAG, "onCreate:67hang ");
        for ( i = 0; i <= N-1; i++) {

            input[i] = new Complex(data[i], 0);}//将实数数据转换为复数数据
        Log.d(TAG, "onCreate:68hang ");
        input = getFFT(input, N);//傅里叶变换
        Log.d(TAG, "onCreate:70hang ");
        Double[] x=Complex.toModArray(input);//计算傅里叶变换得到的复数数组的模值
        for(i=0;i<=N-1;i++) {
            //的模值数组除以N再乘以2
            xN[i]=x[i]/N*2;//个点
        }
        Log.d(TAG, "onCreate:75hang ");
        initLineChart(xN);
        Log.d(TAG, "onCreate:77hang ");
        buttonFFT.setOnClickListener((v)->{
        initLineChart(xN);
            Toast.makeText(MainActivity3.this, "图形绘画中", Toast.LENGTH_SHORT).show();});

        buttonBack.setOnClickListener((v)->{
            Intent intent3 = new Intent();
            intent3.setClass(MainActivity3.this, MainActivity2.class);
            startActivity(intent3);
            Toast.makeText(MainActivity3.this, "返回上一级", Toast.LENGTH_SHORT).show();});

    }

        public  Complex[] getFFT(Complex[] input, int N) {
            if ((N / 2) % 2 == 0) {
                Complex[] even = new Complex[N / 2];// 偶数
                Complex[] odd = new Complex[N / 2];// 奇数
                for (int i = 0; i < N / 2; i++) {
                    even[i] = input[2 * i];
                    odd[i] = input[2 * i + 1];
                }
                even = getFFT(even, N / 2);
                odd = getFFT(odd, N / 2);
                for (int i = 0; i < N / 2; i++) {
                    Complex[] res = Complex.butterfly(even[i], odd[i], Complex.GetW(i, N));
                    input[i] = res[0];
                    input[i + N / 2] = res[1];
                }
                return input;
            } else {// 两点DFT,直接进行碟形运算
                Complex[] res = Complex.butterfly(input[0], input[1], Complex.GetW(0, N));
                return res;
            }
        }
    private void initLineChart(double[] xN){
        lineChart.animateXY(500, 500); // 呈现动画speed
        Description description = new Description();
        description.setText("FFT"); //自定义描述
        lineChart.setDescription(description);
        Legend legend = lineChart.getLegend();
        legend.setTextColor(Color.WHITE);
        setYAxis();
        setXAxis(xN);
        setData(xN);

    }
    /**
     * 设置Y轴数据
     */
    private void setYAxis(){
        YAxis yAxisLeft = lineChart.getAxisLeft();// 左边Y轴
        yAxisLeft.setDrawAxisLine(true); // 绘制Y轴
        yAxisLeft.setDrawLabels(true); // 绘制标签
        yAxisLeft.setAxisMaxValue(5); // 设置Y轴最大值
        yAxisLeft.setAxisMinValue(0); // 设置Y轴最小值
        yAxisLeft.setGranularity(0.001f); // 设置间隔尺寸
        yAxisLeft.setTextColor(Color.WHITE); //设置颜色
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value + "abs";//单位
            }
        });
        // 右侧Y轴
        lineChart.getAxisRight().setEnabled(false); // 不启用
    }

    /**
     * 设置X轴数据
     */
    private void setXAxis(double[] xN){
        // X轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawAxisLine(true); // 绘制X轴
        xAxis.setDrawLabels(true); // 绘制标签
        xAxis.setAxisMaxValue(xN.length); // 设置X轴最大值
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

                return  (value-xN.length/2)*frequecy/xN.length+"kHz";}
        });

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 在底部显示
    }

    /**
     * 填充数据
     */
    @SuppressLint("SuspiciousIndentation")
    private void setData(double[] xN){
        // 模拟数据1
        List<Entry> yVals1 = new ArrayList<>();
        float[] ys1 = new float[xN.length];
        // 模拟数据2
        List<Entry> yVals2 = new ArrayList<>();
        float[] ffty=new float[xN.length];
        for(int i=0;i<xN.length;i++){
        ffty[i] = Float.valueOf(String.valueOf(xN[i]));}

        for (int i = 0; i < ys1.length; i++) {
            yVals1.add(new Entry(i, ys1[i]));

        }
        for (int i = 0; i < ffty.length; i++) {
            //加一个滤波，左右数据进行一个比较
            yVals2.add(new Entry(i, ffty[i]));
        }
        // 2. 分别通过每一组Entry对象集合的数据创建折线数据集
        LineDataSet lineDataSet1 = new LineDataSet(yVals1, "0点基准频谱波形");
        LineDataSet lineDataSet2 = new LineDataSet(yVals2, "信号频谱波形");
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

}
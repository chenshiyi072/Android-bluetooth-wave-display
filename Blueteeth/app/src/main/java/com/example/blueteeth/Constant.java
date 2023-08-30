package com.example.blueteeth;

public class Constant {

    public static final String SA="41";//混合信号
    public static final String SB="42";//噪声信号
    public static final String SC="43";//控制stop信号
    public static final String SE="45";//滤波后的信号
    public static final char SF='F';//标志信号
    public static final String SF2="AF";//标志信号
    public static final char S0='0';
    public static final char S1='1';
    public static final char S2='2';
    public static final char S3='3';
    public static final char S4='4';
    public static final char S5='5';
    public static final char S6='6';
    public static final char S7='7';
    public static final char S8='8';
    public static final char S9='9';
    public  static int sw;//0默认关闭
    public  static int sw1;//0默认关闭
    public  static StringBuilder stringBuilder=new StringBuilder();
    public  static StringBuilder stringBuilder2=new StringBuilder();
    public static float[] ys2=new float[100];//display data
    //只用调一个，就可调坐标
    public static String  buff;//buffer数据缓冲
    public static int q=20;//用来调节进来数据的多少：
    // if (Constant.stringBuilder.length()>=Constant.q*Constant.ys2.length)
    public static int frequecy=4000;//对于采样频率4M-4000；1M-1000；
   public static String info;

//    public static final int MONDAY = 1;
//    public static final int TUESDAY = 2;
//    public static final int WEDNESDAY = 3;
//    public static final int THURSDAY = 4;
//    public static final int FRIDAY = 5;
//    public static final int SATURDAY = 6;
}


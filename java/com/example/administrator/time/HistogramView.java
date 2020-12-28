package com.example.administrator.time;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.ArrayList;

public class HistogramView extends View {
    private int m_imaxValue = 10000;//纵轴最大值
    private int m_ivalueCounts=7;
    private int m_iyStepsDivid=5;
    private String m_unit="分钟";
    private int m_cfontcolor=Color.parseColor("#444444");//纯蓝 #00aaff 淡蓝 6DCAEC 绿色 429a58 反色 d14400
    private Paint xLinePaint;// 坐标轴 轴线 画笔：
    private Paint hLinePaint;// 坐标轴水平内部 虚线画笔
    private Paint titlePaint;// 绘制文本的画笔
    private Paint paint;// 矩形画笔 柱状图的样式信息
    private int[] progress = { 2000, 5000, 6000, 8000, 500, 6000, 9000 };// 7

    // 条，显示各个柱状的数据
    private int[] aniProgress;// 实现动画的值
    private final int TRUE = 1;// 在柱状图上显示数字
    private int[] text;// 设置点击事件，显示哪一条柱状的信息
    private Bitmap bitmap;
    // 坐标轴左侧的数标
    private String[] ySteps;
    // 坐标轴底部的星期数
    private String[] xWeeks;
    private int flag;// 是否使用动画

    private HistogramAnimation ani;

    public HistogramView(Context context) {
        super(context);
        init();
    }

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        ySteps = new String[] { "10k", "7.5k", "5k", "2.5k", "0" };
        xWeeks = new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
        text = new int[7] ;//{ 0, 0, 0, 0, 0, 0, 0 ,0};
        aniProgress = new int[7];// { 1000,};
        ani = new HistogramAnimation();
        ani.setDuration(1000);

        xLinePaint = new Paint();
        hLinePaint = new Paint();
        titlePaint = new Paint();
        paint = new Paint();

        // 给画笔设置颜色
        xLinePaint.setColor(Color.DKGRAY);
        hLinePaint.setColor(Color.LTGRAY);
        titlePaint.setColor(Color.BLACK);

        // 加载画图
        bitmap = BitmapFactory
                .decodeResource(getResources(), R.drawable.column);
    }

    public void start(int flag) {
        this.flag = flag;
        this.startAnimation(ani);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight() - dp2px(50);
        // 绘制底部的线条
        canvas.drawLine(dp2px(30), height + dp2px(3), width - dp2px(30), height
                + dp2px(3), xLinePaint);

        int leftHeight = height - dp2px(5);// 左侧外周的 需要划分的高度：

        int hPerHeight = leftHeight / (m_iyStepsDivid-1);// 分成四部分

        hLinePaint.setTextAlign(Paint.Align.CENTER);
        // 设置四条虚线
        for (int i = 0; i < (m_iyStepsDivid-1); i++) {
            canvas.drawLine(dp2px(30), dp2px(10) + i * hPerHeight, width
                    - dp2px(30), dp2px(10) + i * hPerHeight, hLinePaint);
        }

        // 绘制 Y 周坐标
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTextSize(sp2px(12));
        titlePaint.setAntiAlias(true);
        titlePaint.setStyle(Paint.Style.FILL);
        // 设置左部的数字
        for (int i = 0; i < ySteps.length; i++) {
            canvas.drawText(ySteps[i], dp2px(25), dp2px(13) + i * hPerHeight,
                    titlePaint);
        }

        // 绘制 X 周 做坐标
        int xAxisLength = width - dp2px(30);
        int columCount = xWeeks.length + 1;
        int step = xAxisLength / columCount;

        // 设置底部的数字
        for (int i = 0; i < columCount - 1; i++) {
            // text, baseX, baseY, textPaint
            if(columCount>30){
                canvas.drawText(xWeeks[i], dp2px(16) + step * (i + 1), height
                        + dp2px(16)+(i%4)*dp2px(11), titlePaint);
            }
            else if(columCount>16){
                canvas.drawText(xWeeks[i], dp2px(16) + step * (i + 1), height
                        + dp2px(20)+(i%3)*dp2px(15), titlePaint);
            }
            else if(columCount>7){
                canvas.drawText(xWeeks[i], dp2px(16) + step * (i + 1), height
                    + dp2px(20)+(i%2)*dp2px(20), titlePaint);
            }else{
                canvas.drawText(xWeeks[i], dp2px(16) + step * (i + 1), height
                        + dp2px(20), titlePaint);

            }
        }

        // 绘制矩形
        if (aniProgress != null && aniProgress.length > 0) {
            for (int i = 0; i < aniProgress.length; i++) {// 循环遍历将7条柱状图形画出来
                int value = aniProgress[i];
                paint.setAntiAlias(true);// 抗锯齿效果
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(sp2px(15));// 字体大小
                paint.setTextAlign(Paint.Align.CENTER);
                //paint.setColor(Color.parseColor("#6DCAEC"));// 字体颜色
                paint.setColor(m_cfontcolor);// 字体颜色
                Rect rect = new Rect();// 柱状图的形状

                rect.left = step * (i + 1);
                rect.right = dp2px(30) + step * (i + 1);
                int rh = (int) (leftHeight - leftHeight * (value / (float)m_imaxValue));
                rect.top = rh + dp2px(10);
                rect.bottom = height+ dp2px(2);

                canvas.drawBitmap(bitmap, null, rect, paint);
                // 是否显示柱状图上方的数字
                if (this.text[i] == TRUE) {
                    canvas.drawText(value + m_unit, dp2px(15) + step * (i + 1)
                            - dp2px(0), rh + dp2px(5), paint);
                }

            }
        }

    }

    private int dp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }

    private int sp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (v * value + 0.5f);
    }

    /**
     * 设置点击事件，是否显示数字
     */
    public boolean onTouchEvent(MotionEvent event) {
        int step = (getWidth() - dp2px(30)) / (m_ivalueCounts+1);
        int x = (int) event.getX();
        for (int i = 0; i < m_ivalueCounts; i++) {
            if (x > (dp2px(15) + step * (i + 1) - dp2px(15))
                    && x < (dp2px(15) + step * (i + 1) + dp2px(15))) {
                text[i] = 1;
                for (int j = 0; j < m_ivalueCounts; j++) {
                    if (i != j) {
                        text[j] = 0;
                    }
                }
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    invalidate();
                } else {
                    postInvalidate();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 集成animation的一个动画类
     *
     * @author 李垭超
     */
    private class HistogramAnimation extends Animation {
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f && flag == 2) {
                for (int i = 0; i < aniProgress.length; i++) {
                    aniProgress[i] = (int) (progress[i] * interpolatedTime);
                }
            } else {
                for (int i = 0; i < aniProgress.length; i++) {
                    aniProgress[i] = progress[i];
                }
            }
            invalidate();
        }
    }

    public void notifyDataSetChanged(){
        this.start(2);
    }

    public void setMaxValue (int arg1){
        m_imaxValue=arg1;

        ySteps=new String[m_iyStepsDivid];
        for (int i =0;i<ySteps.length;i++){
            ySteps[i]=String.valueOf(arg1-(arg1/ (ySteps.length-1)*i))+m_unit;
        }
        ySteps[m_iyStepsDivid-1]="0"+m_unit;
    }
    public void setMaxValue (int arg1,int Ydivid){
        m_iyStepsDivid=Ydivid;
        m_imaxValue=arg1;

        ySteps=new String[m_iyStepsDivid];
        for (int i =0;i<ySteps.length;i++){
            ySteps[i]=String.valueOf(arg1-(arg1/ (ySteps.length-1)*i))+m_unit;
        }
        ySteps[m_iyStepsDivid-1]="0"+m_unit;
    }
    public void setValuesOnly(int arg1[]){
        //数目必须严格与X轴的内容对应
        progress=new int[arg1.length];
        progress=(int[]) arg1.clone();
    }
    public void setValuesAll(String arg1[],int arg2[]){
        m_ivalueCounts=arg2.length;
        progress=new int[arg2.length];
        progress=(int[]) arg2.clone();
        xWeeks =new String[arg1.length];
        xWeeks = (String[])arg1.clone();
        if (text.length!=arg2.length)text = new int[arg2.length] ;
        //{ 0, 0, 0, 0, 0, 0, 0 ,0};
        aniProgress = new int[arg2.length];// { 1000,};
    }
    public void setValuesAll(ArrayList<String> arg1, ArrayList<Integer> arg2){
        String[] str_item_name=new  String[arg1.size()];
        int max=0;
        int[] int_item_num=new int[arg1.size()];
        for(int i=0;i<arg1.size();i++){
            str_item_name[i]=arg1.get(i);
            int_item_num[i]=arg2.get(i);
            if (max<arg2.get(i)){
                max=arg2.get(i);
            }
        }
        this.setValuesAll(str_item_name,int_item_num);
        if (max<100)ani.setDuration(max*10);
        else ani.setDuration(1000);
    }

    public void setYStepsDivid(int arg1){
        m_iyStepsDivid=arg1;


        ySteps=new String[m_iyStepsDivid];
        for (int i =0;i<ySteps.length;i++){
            ySteps[i]=String.valueOf(m_imaxValue-(m_imaxValue/ (ySteps.length-1)*i))+m_unit;
        }
        ySteps[m_iyStepsDivid-1]="0"+m_unit;

    }

    public void setUnit(String arg1){
        m_unit=arg1;
    }

    public void clearText(){
        text = new int[progress.length] ;
    }

    public void setTextForTheMost(){
        clearText();
        int max=0;
        int maxi=99999;
        //int[] int_item_num=new int[arg1.size()];
        for(int i=0;i < progress.length;i++){
            if (max<progress[i]){
                max=progress[i];
                maxi=i;
            }
        }
        if (maxi!=99999)text[maxi]=1;
    }
}

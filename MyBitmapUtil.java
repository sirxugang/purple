package com.mobiletrain.qrcode.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by idea on 2016/10/14.
 */
public class MyBitmapUtil {

    public static Bitmap createTextBitmap(String text, float textSize, int textColor, int bold) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//创建画笔
        paint.setTextSize(textSize);//设置文字的大小
//        paint.setTypeface(Typeface.DEFAULT_BOLD);//文字的样式(加粗)
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(bold);
        paint.setColor(textColor);//文字的颜色

        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);

//        float textWidth = paint.measureText(text);
//        float textHeight = textWidth / text.length();
//        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, context.getResources().getDisplayMetrics());
//        Bitmap bitmap = Bitmap.createBitmap((int) textWidth, (int) textHeight, Bitmap.Config.ARGB_8888);//创建一个宽度和高度都是400、32位ARGB图

        int width = rect.width();
        width = width > 0 ? width : 1;
        int height = rect.height();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//创建一个宽度和高度都是400、32位ARGB图

        Canvas canvas = new Canvas(bitmap);//初始化画布绘制的图像到icon上
        canvas.drawColor(Color.TRANSPARENT);//图层的背景色
        canvas.drawText(text, 0, rect.height() - 5, paint);//将文字写入。这里面的（120，130）代表着文字在图层上的初始位置
        canvas.save(canvas.ALL_SAVE_FLAG);//保存所有图层
        canvas.restore();

        return bitmap;
    }

    public static Bitmap drawLogo(Bitmap bgBitmap,Bitmap logo){
        //创造一张与bgBitmap尺寸相同的白纸Bitmap
        Bitmap paper = Bitmap.createBitmap(bgBitmap.getWidth(), bgBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //创造一张画布，将白纸Bitmap固定在画布上
        Canvas canvas = new Canvas(paper);

        //在白纸上绘制bgBitmap
        canvas.drawBitmap(bgBitmap,0,0,null);

        //在白纸上绘制logo
        int left = (bgBitmap.getWidth() - logo.getWidth()) / 2;
        int height = (bgBitmap.getHeight() - logo.getHeight()) / 2;
        Paint paint = new Paint();
        paint.setAlpha(200);
        canvas.drawBitmap(logo, left,height, paint);
        canvas.save();

        return paper;
    }
}

package com.ichangemycity.customui;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

public class RoundedBackgroundSpan extends ReplacementSpan {
    private CharSequence text;
    private int start;
    private int end;
    private int bgColor;

    public RoundedBackgroundSpan(CharSequence text, int start, int end,
                                 int bgColor) {
        this.text = text;
        this.start = start;
        this.end = end;
        this.bgColor = bgColor;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
        text = this.text;
        start = this.start;
        end = this.end;
        return Math.round(MeasureText(paint, text, start, end));
    }

    private float MeasureText(Paint paint, CharSequence text, int start, int end) {
        text = this.text;
        start = this.start;
        end = this.end;
        return paint.measureText(text, start, end);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        text = this.text;
        start = this.start;
        end = this.end;
        paint.setStyle(Style.STROKE);

        RectF rect = new RectF(x, top, MeasureText(paint, text.toString(), start, end),
                bottom);
        paint.setColor(bgColor);
        canvas.drawRect(rect, paint);
        paint.setColor(bgColor);
        canvas.drawText(text, start, end, x, y, paint);
    }

    public int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public CharSequence getText() {
        return text;
    }
}
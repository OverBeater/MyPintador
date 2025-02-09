package com.example.mypaint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimplePaint extends View {
    private static final float DEFAULT_STROKE_WIDTH = 20f;

    private final List<DrawAction> drawActions = new ArrayList<>();
    private Paint currentPaint;
    private Path currentPath;
    private StyleType style = StyleType.desenhoLivre;

    private float startX, startY, endX, endY;
    private boolean isPencilMode = false;

    public SimplePaint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        currentPaint = new Paint();
        currentPath = new Path();

        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        currentPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (DrawAction action : drawActions) {
            canvas.drawPath(action.path, action.paint);
        }
        canvas.drawPath(currentPath, currentPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPencilMode = (style == StyleType.desenhoLivre);
                startX = endX = x;
                startY = endY = y;
                currentPath.moveTo(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                endX = x;
                endY = y;
                if (isPencilMode) {
                    currentPath.lineTo(x, y);
                }
                break;

            case MotionEvent.ACTION_UP:
                if (style == StyleType.linha) {
                    currentPath.lineTo(x, y);
                } else if (style == StyleType.circulo) {
                    float radius = (float) Math.hypot(endX - startX, endY - startY) / 2;
                    currentPath.reset();
                    currentPath.addCircle((startX + endX) / 2, (startY + endY) / 2, radius, Path.Direction.CW);
                } else if (style == StyleType.quadrado) {
                    currentPath.reset();
                    currentPath.addRect(Math.min(startX, endX), Math.min(startY, endY),
                            Math.max(startX, endX), Math.max(startY, endY),
                            Path.Direction.CW);
                }
                drawActions.add(new DrawAction(new Path(currentPath), new Paint(currentPaint)));
                initialize();
                break;
        }
        invalidate();
        return true;
    }

    public void setColor(int color) {
        currentPaint.setColor(color);
    }

    public void backDraw() {
        if (!drawActions.isEmpty()) {
            drawActions.remove(drawActions.size() - 1);
            invalidate();
        }
    }

    public void removeDraw() {
        drawActions.clear();
        invalidate();
    }

    public void setStyleType(StyleType style) {
        this.style = style;
    }

    private static class DrawAction {
        Path path;
        Paint paint;

        DrawAction(Path path, Paint paint) {
            this.path = path;
            this.paint = paint;
        }
    }
}

enum StyleType {
    linha,
    circulo,
    quadrado,
    desenhoLivre,
}

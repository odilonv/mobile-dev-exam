package com.odilonvidal.exam.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.odilonvidal.exam.model.Wind;
import java.util.Map;

public class WindRoseView extends View {
  private Paint circlePaint;
  private Paint trianglePaint;
  private Paint triangleStrokePaint;
  private Paint textPaint;
  private Paint dividerPaint;
  private Paint backgroundPaint;
  private Map<String, Wind.WindDirection> windDirections;
  private double maxCount = 0;

  private final Path trianglePath;
  private final String[] directions = { "N", "NE", "E", "SE", "S", "SW", "W", "NW" };
  private final float[] textCoordinates = new float[16]; // Pour stocker les coordonnées x,y des textes

  public WindRoseView(Context context) {
    super(context);
    trianglePath = new Path();
    init();
  }

  public WindRoseView(Context context, AttributeSet attrs) {
    super(context, attrs);
    trianglePath = new Path();
    init();
  }

  private void init() {
    circlePaint = new Paint();
    circlePaint.setColor(Color.WHITE);
    circlePaint.setStyle(Paint.Style.STROKE);
    circlePaint.setStrokeWidth(2);
    circlePaint.setAntiAlias(true);

    trianglePaint = new Paint();
    trianglePaint.setColor(Color.parseColor("#87CEEB"));
    trianglePaint.setStyle(Paint.Style.FILL);
    trianglePaint.setAntiAlias(true);

    triangleStrokePaint = new Paint();
    triangleStrokePaint.setColor(Color.WHITE);
    triangleStrokePaint.setStyle(Paint.Style.STROKE);
    triangleStrokePaint.setStrokeWidth(2);
    triangleStrokePaint.setAntiAlias(true);

    textPaint = new Paint();
    textPaint.setColor(Color.WHITE);
    textPaint.setTextSize(30);
    textPaint.setTextAlign(Paint.Align.CENTER);
    textPaint.setAntiAlias(true);

    dividerPaint = new Paint();
    dividerPaint.setColor(Color.WHITE);
    dividerPaint.setStyle(Paint.Style.STROKE);
    dividerPaint.setStrokeWidth(1);
    dividerPaint.setAntiAlias(true);

    backgroundPaint = new Paint();
    backgroundPaint.setColor(Color.parseColor("#F5F5F5"));
    backgroundPaint.setStyle(Paint.Style.FILL);
    backgroundPaint.setAlpha(128);
    backgroundPaint.setAntiAlias(true);
  }

  public void setWindData(Map<String, Wind.WindDirection> directions) {
    this.windDirections = directions;
    if (directions != null) {
      maxCount = 0;
      for (Wind.WindDirection direction : directions.values()) {
        maxCount = Math.max(maxCount, direction.getCount());
      }
    }
    invalidate();
  }

  @Override
  protected void onDraw(@NonNull Canvas canvas) {
    super.onDraw(canvas);

    if (windDirections == null)
      return;

    int width = getWidth();
    int height = getHeight();
    int centerX = width / 2;
    int centerY = height / 2;
    int radius = Math.min(width, height) / 2 - 50;

    canvas.drawCircle(centerX, centerY, radius, backgroundPaint);

    canvas.drawLine(centerX - radius, centerY, centerX + radius, centerY, dividerPaint);
    canvas.drawLine(centerX, centerY - radius, centerX, centerY + radius, dividerPaint);

    canvas.drawCircle(centerX, centerY, radius, circlePaint);

    for (int i = 0; i < 8; i++) {
      double angle = Math.toRadians(i * 45 - 90);
      textCoordinates[i * 2] = centerX + (float) (Math.cos(angle) * (radius + 30));
      textCoordinates[i * 2 + 1] = centerY + (float) (Math.sin(angle) * (radius + 30));
    }

    for (int i = 0; i < 8; i++) {
      canvas.drawText(directions[i], textCoordinates[i * 2], textCoordinates[i * 2 + 1], textPaint);
    }

    for (int i = 0; i < 16; i++) {
      Wind.WindDirection direction = windDirections.get(String.valueOf(i));
      if (direction != null) {
        double count = direction.getCount();
        double ratio = count / maxCount;

        canvas.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(i * 22.5f);

        float triangleHeight = (float) (radius * ratio);
        float triangleWidth = (float) (triangleHeight * 0.2);

        trianglePath.reset();
        trianglePath.moveTo(0, 0);
        trianglePath.lineTo(-triangleWidth, -triangleHeight);
        trianglePath.lineTo(triangleWidth, -triangleHeight);
        trianglePath.close();
        canvas.drawPath(trianglePath, trianglePaint);
        canvas.drawPath(trianglePath, triangleStrokePaint);

        canvas.restore();
      }
    }
  }
}
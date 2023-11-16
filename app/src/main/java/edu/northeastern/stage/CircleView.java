package edu.northeastern.stage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Random;

import edu.northeastern.stage.model.Circle;

public class CircleView extends View {

    // Constructor and circle list initialization
    Circle[] circles;
    private Matrix matrix;
    private Paint paint;
    private float scaleFactor = 1f;
    private float lastTouchX;
    private float lastTouchY;
    private boolean isDragging = false;
    int viewWidth;
    int viewHeight;


    // Constructors for XML inflation
    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public CircleView(Context context, AttributeSet attrs, List<Circle> circles, int defStyle) {
        super(context, attrs, defStyle);
        this.circles = circles.toArray(new Circle[0]);
        init();
    }

    public CircleView(Context context, List<Circle> circles) {
        super(context);
        this.circles = circles.toArray(new Circle[0]);
        init();
    }

    private void init() {
        // Get the dimensions of the View
        viewWidth = getWidth();
        viewHeight = getHeight();

        matrix = new Matrix();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {

        super.onDraw(canvas);

//        Paint paint = new Paint();
//        paint.setColor(Color.rgb(67,83,52));
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        // Calculate center x and y
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Get the dimensions of the View
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        // Set the boundary for drawing
        int left = 50;
        int top = 50;
        int right = viewWidth - 50;
        int bottom = viewHeight - 50;

        // Calculate rectangle width and height
        int rectWidth = right - left;
        int rectHeight = bottom - top;

        canvas.save();
        canvas.concat(matrix);
        // Draw a rectangle within the boundary
        canvas.drawRect(left, top, right, bottom, paint);

        canvas.restore();
        drawZoomControls(canvas);

        int circleCount = 0;
        if (circles != null) {
            // Draw each circle on the canvas
            for (Circle c : circles) {
                canvas.save();
                canvas.concat(matrix);
                // Offset circle x and y to center
                c.setX(centerX + c.getX());
                c.setY(centerY + c.getY());

                // Adjust circle position to avoid overlapping
//                adjustCirclePosition(c);

                // Set random text size based on circle radius
                float textSize = c.getRadius() / 3;
                paint.setTextSize(textSize);

                // Generate random text
                String randomText = generateRandomText();
                // Calculate text width and height
                float textWidth = paint.measureText(randomText);
                Paint.FontMetrics metrics = paint.getFontMetrics();
                float textHeight = metrics.descent - metrics.ascent;
                // Calculate centered coordinates for the text
                float textX = c.getX() - (textWidth / 2);
                float textY = c.getY() - (textHeight / 2);

                // Draw circle with black border
                canvas.drawCircle(c.getX(), c.getY(), c.getRadius(), paint);

                // Draw text inside the circle
                canvas.drawText(randomText, textX, textY, paint);

                canvas.restore();
                drawZoomControls(canvas);
            }
        }
    }

        // Function to generate random text
    private String generateRandomText() {
        // Replace this with your own logic to generate random text
        String[] texts = {"Text1", "Text2", "Text3", "Text4", "Text5"};
        int randomIndex = new Random().nextInt(texts.length);
        return texts[randomIndex];
    }

    // Function to calculate distance between two circles
    private float calculateDistance(Circle circle1, Circle circle2) {
        float dx = circle2.getX() - circle1.getX();
        float dy = circle2.getY() - circle1.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    // Function to calculate angle between two circles
    private float calculateAngle(Circle circle1, Circle circle2) {
        float dx = circle2.getX() - circle1.getX();
        float dy = circle2.getY() - circle1.getY();
        return (float) Math.atan2(dy, dx);
    }

    public void setCircles(List<Circle> circles) {
        this.circles = circles.toArray(new Circle[0]);
        invalidate(); // Request a redraw
    }

    private void drawContent(Canvas canvas) {
        // Add your custom drawing code here
        canvas.drawCircle(200, 200, 100, paint);
    }

    private void drawZoomControls(Canvas canvas) {
        Paint controlsPaint = new Paint();
        controlsPaint.setColor(Color.BLACK);
        controlsPaint.setTextSize(50);

        canvas.drawText("+", getWidth() - 80, getHeight() - 80, controlsPaint);
        canvas.drawText("-", getWidth() - 80, getHeight() - 20, controlsPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = touchX;
                lastTouchY = touchY;
                isDragging = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    float dx = touchX - lastTouchX;
                    float dy = touchY - lastTouchY;
                    matrix.postTranslate(dx, dy);
                    invalidate();
                    lastTouchX = touchX;
                    lastTouchY = touchY;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                break;
        }

        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

//    @Override
//    public boolean onSingleTapConfirmed(MotionEvent event) {
//        float touchX = event.getX();
//        float touchY = event.getY();
//
//        if (isZoomInButtonTapped(touchX, touchY)) {
//            scaleFactor *= 1.2f;
//        } else if (isZoomOutButtonTapped(touchX, touchY)) {
//            scaleFactor /= 1.2f;
//        }
//
//        matrix.reset();
//        matrix.postScale(scaleFactor, scaleFactor, getWidth() / 2f, getHeight() / 2f);
//        invalidate();
//        return super.onSingleTapConfirmed(event);
//    }

    private boolean isZoomInButtonTapped(float x, float y) {
        return x > getWidth() - 100 && y > getHeight() - 100 && x < getWidth() && y < getHeight();
    }

    private boolean isZoomOutButtonTapped(float x, float y) {
        return x > getWidth() - 100 && y > getHeight() - 30 && x < getWidth() && y < getHeight() - 10;
    }


}

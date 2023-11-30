package edu.northeastern.stage.ui.explore;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.stage.model.Circle;

public class CircleView extends View {

    // Constructor and circle list initialization
    Circle[] circles;
    private Matrix matrix;
    private Paint paint;
    private float scaleFactor = 1.05f;
    private float lastTouchX;
    private float lastTouchY;
    private boolean isDragging = false;
    Integer countDraw = 0;
    Map<Circle, String> circleTextMap = new HashMap<>();


    ScaleGestureDetector objScaleGestureDetector;


    // Constructors for XML inflation
    public CircleView(Context context) {
        super(context);
        Log.d("CIRCLEVIEW", "circleview context");
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("CIRCLEVIEW", "circleview context + attrs");
        init();
    }


    public CircleView(Context context, AttributeSet attrs, List<Circle> circles, int defStyle) {
        super(context, attrs, defStyle);
        this.circles = circles.toArray(new Circle[0]);
        Log.d("CIRCLEVIEW", "circleview context + attrs + defstyle");
        init();
    }

    public CircleView(Context context, List<Circle> circles) {
        super(context);
        this.circles = circles.toArray(new Circle[0]);
        Log.d("CIRCLEVIEW", "circleview context + list of circles");
        init();
    }

    private void init() {
        Log.d("CIRCLEVIEW", "init");

        objScaleGestureDetector = new ScaleGestureDetector(this.getContext(), new PinchZoomListener());

        matrix = new Matrix();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {

        countDraw ++;

        super.onDraw(canvas);
        Log.d("CIRCLEVIEW", "on draw count " + countDraw);

        canvas.save();
        canvas.concat(matrix);

        if (circles != null) {
            // Draw each circle on the canvas
            for (Circle c : circles) {

                // Set random text size based on circle radius
                float textSize = c.getRadius() / 3;
                paint.setTextSize(textSize);

                // Generate random text
                String randomText = circleTextMap.get(c);
                // Calculate text width and height
                float textWidth = paint.measureText(randomText);
                Paint.FontMetrics metrics = paint.getFontMetrics();
                float textHeight = metrics.descent - metrics.ascent;
                // Calculate centered coordinates for the text
                float textX = c.getX() - (textWidth / 2);
                float textY = c.getY() - (textHeight / 2);

                canvas.save();
                // Draw circle with black border
                canvas.drawCircle(c.getX(), c.getY(), c.getRadius(), paint);
                // Draw text inside the circle
                canvas.drawText(randomText, textX, textY, paint);
            }
        }

        canvas.save();
        canvas.concat(matrix);
        canvas.restore();
//        drawZoomControls(canvas);

        if(countDraw < 8) {
            for (Integer i = 0; i < 3; i++) {
                //best scale factor so far for current configs (phone size, maxAttempts, number of circles)
                scaleFactor /= 1.05f;
            }
            matrix.reset();
            //best division factors for width and height so far for current configs (phone size, maxAttempts, number of circles)
            matrix.postScale(scaleFactor, scaleFactor, getWidth() / 1.25f, getHeight() / 1.25f);
            invalidate();
        }
//        postInvalidate();
    }

    public void setCircles(List<Circle> circles, HashMap<Circle, String> circleTextMap) {
        Log.d("CIRCLEVIEW", "set circles");

        this.circles = circles.toArray(new Circle[0]);
        this.circleTextMap = circleTextMap;

        invalidate(); // Request a redraw
    }

    private void drawZoomControls(Canvas canvas) {
        Paint controlsPaint = new Paint();
        controlsPaint.setColor(Color.BLUE);
        controlsPaint.setTextSize(50);

        canvas.drawText("+", getWidth() - 80, getHeight() - 80, controlsPaint);
        canvas.drawText("-", getWidth() - 70, getHeight() - 20, controlsPaint);
    }


    //include the + & - back again after fixing the canvas touch that makes the circles disappear (rectangle border remains)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        Log.d("CIRCLEVIEW", "Touch event: " + event.getAction());
//        toastmsg("In onTouchEvent ->" + Thread.activeCount());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("CIRCLEVIEW", "ACTION_DOWN");
                lastTouchX = touchX;
                lastTouchY = touchY;
                isDragging = true;
                break;

            case MotionEvent.ACTION_MOVE:

                if (isDragging) {
                    Log.d("CIRCLEVIEW", "isDragging  in ACTION_MOVE");

                    float dx = touchX - lastTouchX;
                    float dy = touchY - lastTouchY;
                    matrix.postTranslate(dx, dy);
                    invalidate();
                    lastTouchX = touchX;
                    lastTouchY = touchY;
                }
                break;

            case MotionEvent.ACTION_UP:
                Log.d("CIRCLEVIEW", "ACTION_UP");
//                checkCircleClick(touchX, touchY);
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.d("CIRCLEVIEW", "ACTION_CANCEL");
                isDragging = false;
                break;
        }

        objScaleGestureDetector.onTouchEvent(event);

        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private boolean isZoomInButtonTapped(float x, float y) {
        return x > getWidth() - 100 && y > getHeight() - 100 && x < getWidth() && y < getHeight();
    }

    private boolean isZoomOutButtonTapped(float x, float y) {
        return x > getWidth() - 100 && y > getHeight() - 30 && x < getWidth() && y < getHeight() - 10;
    }


    //check if we still want to leave the pinch in and out seeing that texts don't get zoomed in
    public class PinchZoomListener extends SimpleOnScaleGestureListener{

        @Override
        public boolean onScale(ScaleGestureDetector detector){
            Log.d("CIRCLEVIEW", "Touch PinchZoomListener");

            float gestureFactor = detector.getScaleFactor();
            // zoom out
            if(gestureFactor > 1){
                Log.d("CIRCLEVIEW", "Touch PinchZoomListener zoom in");
                scaleFactor *= 1.05f;
            } else { //zoom in
                Log.d("CIRCLEVIEW", "Touch PinchZoomListener zoom out");
                scaleFactor /= 1.05f;
            }

            matrix.reset();
            matrix.postScale(scaleFactor, scaleFactor, getWidth() / 2f, getHeight() / 2f);
            invalidate();
            return true;

        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector){
            return true;
        }

    }

    private void toastmsg(String msg){
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void checkCircleClick(float touchX, float touchY) {
        if (circles != null) {
            for (Circle c : circles) {
                // Check if the touch point is within the bounds of the circle
                if (isPointInsideCircle(touchX, touchY, c)) {
                    // Handle the circle click, for example, display a message or perform an action
                    toastmsg("" + c + " text value is " + circleTextMap.get(c));
                    break; // Exit the loop once a circle is clicked
                }
            }
        }
    }

    private boolean isPointInsideCircle(float x, float y, Circle circle) {
        //Create a float array to represent the touch coordinates as a point.
        float[] point = {x, y};
        matrix.invert(matrix); // Invert the matrix to get the original coordinates
        matrix.mapPoints(point); // Map the touch coordinates to the original coordinates

        //Calculate the distance between the mapped touch coordinates and the circle's center using the Pythagorean theorem.
        float distance = (float) Math.sqrt(Math.pow(point[0] - circle.getX(), 2) + Math.pow(point[1] - circle.getY(), 2));
        return distance <= circle.getRadius();
    }


}
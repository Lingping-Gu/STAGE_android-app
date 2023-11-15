package edu.northeastern.stage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Random;

public class CircleView extends View {

    // Constructor and circle list initialization
    Circle[] circles;

    // Constructors for XML inflation
    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public CircleView(Context context, List<Circle> circles) {
        super(context);
        this.circles = circles.toArray(new Circle[0]);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {

        super.onDraw(canvas);

        @SuppressLint("DrawAllocation") Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);

        // Calculate center x and y
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        if(circles != null){
            // Draw each circle on the canvas
            for(Circle c : circles) {
                // Offset circle x and y to center
                c.setX(centerX + c.getX());
                c.setY(centerY + c.getY());

                // Adjust circle position to avoid collisions
                adjustCirclePosition(c);

                // Set random text size based on circle radius
                float textSize = c.getRadius() / 2;
                paint.setTextSize(textSize);

                // Generate random text
                String randomText = generateRandomText();

                // Draw circle with black border
                canvas.drawCircle(c.getX(), c.getY(), c.getRadius(), paint);

                // Draw text inside the circle
                canvas.drawText(randomText, c.getX(), c.getY(), paint);
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

    // Function to adjust circle position to avoid collisions
    private void adjustCirclePosition(Circle currentCircle) {
        for (Circle otherCircle : circles) {
            if (otherCircle != currentCircle) {
                float distance = calculateDistance(currentCircle, otherCircle);
                float minDistance = currentCircle.getRadius() + otherCircle.getRadius();

                // If circles are too close, adjust the position of the current circle
                if (distance < minDistance) {
                    float angle = calculateAngle(currentCircle, otherCircle);
                    float newX = otherCircle.getX() + minDistance * (float) Math.cos(angle);
                    float newY = otherCircle.getY() + minDistance * (float) Math.sin(angle);

                    currentCircle.setX(newX);
                    currentCircle.setY(newY);
                }
            }
        }
    }

//    private void adjustCirclePosition(Circle circle) {
//
//        // Minimum distance between circles
//        int minDistance = circle.getRadius() * 2;
//
//        for (Circle other : circles) {
//
//            if (circle != other) {
//
//                // Calculate distance between circle centers
//                int dx = Math.abs(circle.getX() - other.getX());
//                int dy = Math.abs(circle.getY() - other.getY());
//                double distance = Math.sqrt(dx * dx + dy * dy);
//
//                // If circles too close, adjust current circle position
//                if (distance < minDistance) {
//                    int xDiff = circle.getX() - other.getX();
//                    int yDiff = circle.getY() - other.getY();
//                    if (xDiff > 0) {
//                        circle.setX(circle.getX() + minDistance);
//                    } else {
//                        circle.setX(circle.getX() - minDistance);
//                    }
//                    if (yDiff > 0) {
//                        circle.setY(circle.getY() + minDistance);
//                    } else {
//                        circle.setY(circle.getY() - minDistance);
//                    }
//                }
//
//            }
//        }
//    }

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

}

package edu.northeastern.stage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import java.util.List;
import java.util.Random;

import android.os.Handler;

public class CircleView extends View {
    private Circle draggedCircle = null;
    private float touchOffsetX, touchOffsetY;
    private Circle[] circles;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Handler handler = new Handler();
    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (circles != null) {
                updatePositions(); // Update positions based on velocity
                handleCollisions(); // Handle any collisions
                invalidate(); // Redraw the view
            }
            handler.postDelayed(this, 16); // Schedule the next update (approximately 60 FPS)
        }
    };

    // Constructors as before...

    private void init() {
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        handler.post(updateRunnable); // Start the update loop
        Log.e("CircleView", "Circles inited");
    }

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
        init();
    }

    public void setCircles(List<Circle> circles) {
        this.circles = circles.toArray(new Circle[0]);
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {

        super.onDraw(canvas);

        @SuppressLint("DrawAllocation") Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);

        // Calculate center x and y
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        Log.e("CircleView", "Circles are onDraw.");
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


    private void updatePositions() {
        final float deltaTime = 1f / 60f; // Assuming a 60 FPS update rate
        for (Circle circle : circles) {
            circle.updatePosition(deltaTime);
        }
    }

    private void handleCollisions() {
        for (int i = 0; i < circles.length; i++) {
            for (int j = i + 1; j < circles.length; j++) {
                Circle c1 = circles[i];
                Circle c2 = circles[j];
                if (c1.isColliding(c1, c2)) {
                    // Respond to collision: adjust positions and update velocities
                    collisionResponse(c1, c2);
                }
            }
        }
    }

    private void collisionResponse(Circle c1, Circle c2) {
        // Calculate the vector between the circles' centers
        float dx = c2.getX() - c1.getX();
        float dy = c2.getY() - c1.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // Calculate the minimum translation distance to push circles apart after collision
        float overlap = 0.5f * (distance - c1.getRadius() - c2.getRadius());

        // Displace Current Circle away from Collision
        c1.setX(c1.getX() - overlap * (c1.getX() - c2.getX()) / distance);
        c1.setY(c1.getY() - overlap * (c1.getY() - c2.getY()) / distance);

        // Displace Other Circle away from Collision
        c2.setX(c2.getX() + overlap * (c1.getX() - c2.getX()) / distance);
        c2.setY(c2.getY() + overlap * (c1.getY() - c2.getY()) / distance);

        // Update velocities based on collision response
        // ... your existing collision velocity update logic ...
    }

    // Call this function when circles are created to ensure they do not overlap
    private boolean circlesOverlap(Circle newCircle) {
        for (Circle existingCircle : circles) {
            if (calculateDistance(newCircle, existingCircle) < newCircle.getRadius() + existingCircle.getRadius()) {
                return true; // They overlap
            }
        }
        return false; // No overlap
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Check if the touch event is within any of the circles
                for (Circle c : circles) {
                    if (isPointInsideCircle(x, y, c)) {
                        draggedCircle = c;
                        // Calculate the touch offset from the center of the circle
                        touchOffsetX = x - c.getX();
                        touchOffsetY = y - c.getY();
                        break; // No need to check other circles
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (draggedCircle != null) {
                    // Update the position of the dragged circle based on touch
                    draggedCircle.setX(x - touchOffsetX);
                    draggedCircle.setY(y - touchOffsetY);
                    // Redraw the view
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                // Release the dragged circle
                draggedCircle = null;
                break;
        }

        return true; // Consume the touch event
    }

    // Function to check if a point (x, y) is inside a circle
    private boolean isPointInsideCircle(float x, float y, Circle circle) {
        float dx = x - circle.getX();
        float dy = y - circle.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance <= circle.getRadius();
    }

}

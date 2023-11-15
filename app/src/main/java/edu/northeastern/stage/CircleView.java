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
        paint.setColor(Color.BLUE);

        if(circles != null){
            // Draw each circle on the canvas
            for(Circle c : circles) {
                canvas.drawCircle(c.getX(), c.getY(), c.getRadius(), paint);
            }
        }

    }

}

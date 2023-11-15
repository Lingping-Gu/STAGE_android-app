package edu.northeastern.stage;

public class Circle {

    public float x, y;
    public float vx, vy;
    public float radius;
    public float mass;

    // Constructor

    public Circle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public Circle(float x, float y, float vx, float vy, float radius, float mass) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.mass = mass;
    }

    // Getters

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    // Setters

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    // Method to update position based on velocity
    public void updatePosition(float deltaTime) {
        x += vx * deltaTime;
        y += vy * deltaTime;
    }

    public boolean isColliding(Circle c1, Circle c2) {
        float dx = c1.x - c2.x;
        float dy = c1.y - c2.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (c1.radius + c2.radius);
    }

    public void handleCollision(Circle c1, Circle c2) {
        // Vector from c1 to c2
        float nx = c2.x - c1.x;
        float ny = c2.y - c1.y;

        // Distance squared between circle centers
        float distSquared = nx * nx + ny * ny;
        if (distSquared == 0.0f) {
            // Circles are on the same position, avoid division by zero
            nx = 1.0f;
            ny = 0.0f;
            distSquared = 1.0f;
        }

        // Normalize the collision vector
        float d = (float) Math.sqrt(distSquared);
        nx /= d;
        ny /= d;

        // Relative velocity
        float vx = c1.vx - c2.vx;
        float vy = c1.vy - c2.vy;

        // Velocity along the normal
        float vn = vx * nx + vy * ny;

        // No collision if velocities are separating
        if (vn > 0.0f) return;

        // Calculate restitution (elasticity of the collision)
        float restitution = 0.7f; // Can be any value between 0 and 1

        // Calculate impulse scalar
        float impulse = (-(1 + restitution) * vn) / (c1.mass + c2.mass);

        // Update velocities based on impulse
        c1.vx -= impulse * c1.mass * nx;
        c1.vy -= impulse * c1.mass * ny;
        c2.vx += impulse * c2.mass * nx;
        c2.vy += impulse * c2.mass * ny;
    }
}

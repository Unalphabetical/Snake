package com.example.snake.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;

import com.example.snake.R;

import java.util.List;
import java.util.Locale;

public class SnakeHandler {

    ////
    private Snake snake;

    // How big is each body of the snake?
    private int segmentSize;

    // How big is the entire grid
    private Point moveRange;

    // Where is the centre of the screen
    // horizontally in pixels?
    private int halfWayPoint;

    // For tracking movement Heading
    private enum Heading {
        UP, RIGHT, DOWN, LEFT
    }

    // Start by heading to the right
    private Heading heading = Heading.RIGHT;

    // A bitmap for each direction the head can face
    private Bitmap bitmapHeadRight;
    private Bitmap bitmapHeadLeft;
    private Bitmap bitmapHeadUp;
    private Bitmap bitmapHeadDown;

    // A bitmap for the body
    private Bitmap bitmapBody;

    //// Wrap around variable to enable or disable wrap around
    private boolean wrapAround = false;

    private double leftBorder = 0.22;
    private double rightBorder = 0.75;

    public SnakeHandler(Context context, Point moveRange, int segmentSize) {

        // Initialize our new Snake
        this.snake = new Snake();

        // Initialize the body size and movement
        // range from the passed in parameters
        this.segmentSize = segmentSize;
        this.moveRange = moveRange;

        // Create and scale the bitmaps
        this.bitmapHeadRight = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        // Create 3 more versions of the head for different headings
        this.bitmapHeadLeft = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        this.bitmapHeadUp = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        this.bitmapHeadDown = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        // Modify the bitmaps to face the snake head
        // in the correct direction
        this.bitmapHeadRight = Bitmap
                .createScaledBitmap(this.bitmapHeadRight,
                        segmentSize, segmentSize, false);

        // A matrix for scaling
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);

        this.bitmapHeadLeft = Bitmap
                .createBitmap(this.bitmapHeadRight,
                        0, 0, segmentSize, segmentSize, matrix, true);

        // A matrix for rotating
        matrix.preRotate(-90);
        this.bitmapHeadUp = Bitmap
                .createBitmap(this.bitmapHeadRight,
                        0, 0, segmentSize, segmentSize, matrix, true);

        // Matrix operations are cumulative
        // so rotate by 180 to face down
        matrix.preRotate(180);
        this.bitmapHeadDown = Bitmap
                .createBitmap(this.bitmapHeadRight,
                        0, 0, segmentSize, segmentSize, matrix, true);

        // Create and scale the body
        this.bitmapBody = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.body);

        this.bitmapBody = Bitmap
                .createScaledBitmap(this.bitmapBody,
                        segmentSize, segmentSize, false);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            this.leftBorder = 0.23;
            this.rightBorder = 0.8;
        }

    }

    public Snake getSnake() {
        return this.snake;
    }

    public Heading getHeading() {
        return this.heading;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public void setWrapAround(boolean wrapAround) {
        this.wrapAround = wrapAround;
    }

    public void move(String direction) {
        this.heading = Heading.valueOf(direction.toUpperCase(Locale.ROOT));
    }

    public void move() {
        // Move the body
        // Start at the back and move it
        // to the position of the body in front of it
        List<Segments> segmentLocations = this.snake.getSegments();
        for (int i = segmentLocations.size() - 1; i > 0; i--) {

            // Make it the same value as the next body
            // going forwards towards the head
            segmentLocations.get(i).setX(segmentLocations.get(i - 1).getX());
            segmentLocations.get(i).setY(segmentLocations.get(i - 1).getY());
        }

        // Move the head in the appropriate heading
        // Get the existing head position
        // Move it appropriately
        switch (heading) {
            case UP:
                segmentLocations.get(0).setY(segmentLocations.get(0).getY() - 1);
                break;

            case RIGHT:
                segmentLocations.get(0).setX(segmentLocations.get(0).getX() + 1);
                break;

            case DOWN:
                segmentLocations.get(0).setY(segmentLocations.get(0).getY() + 1);
                break;

            case LEFT:
                segmentLocations.get(0).setX(segmentLocations.get(0).getX() - 1);
                break;
        }

    }

    public void reset(int w, int h) {

        // Reset the heading
        heading = Heading.RIGHT;

        // Delete the old contents of the ArrayList
        this.snake = new Snake();

        // Start with a single snake body
        this.snake.grow(new Point(w / 2, h / 2), Segments.Type.HEAD);
    }

    public boolean detectDeath() {
        // Has the snake died?
        boolean dead = false;

        List<Segments> segmentLocations = this.snake.getSegments();
        // Hit any of the screen edges
        if (segmentLocations.get(0).getX() < moveRange.x * leftBorder ||
                segmentLocations.get(0).getX() > moveRange.x * rightBorder ||
                segmentLocations.get(0).getY() == -1 ||
                segmentLocations.get(0).getY() > moveRange.y + 1) {

            dead = true;
        }

        // Eaten itself?
        for (int i = segmentLocations.size() - 1; i > 0; i--) {
            // Have any of the sections collided with the head
            if (segmentLocations.get(0).getX() == segmentLocations.get(i).getX() &&
                    segmentLocations.get(0).getY() == segmentLocations.get(i).getY()) {

                dead = true;
            }
        }
        return dead;
    }

    public boolean wrapAround() {
        if (wrapAround) {
            List<Segments> segmentLocations = this.snake.getSegments();
            if (segmentLocations.get(0).getX() < moveRange.x * leftBorder) {
                segmentLocations.get(0).setX((int) (moveRange.x * rightBorder));
                this.heading = Heading.LEFT;
            } else if (segmentLocations.get(0).getX() > moveRange.x * rightBorder) {
                segmentLocations.get(0).setX((int) (moveRange.x * leftBorder));
                this.heading = Heading.RIGHT;
            } else if (segmentLocations.get(0).getY() == -1) {
                segmentLocations.get(0).setY(moveRange.y);
                this.heading = Heading.UP;
            } else if (segmentLocations.get(0).getY() > moveRange.y + 1) {
                segmentLocations.get(0).setY(0);
                this.heading = Heading.DOWN;
            }
        }
        return wrapAround;
    }

    public boolean checkDinner(Point l) {

        List<Segments> segmentLocations = this.snake.getSegments();
        //if (snakeXs[0] == l.x && snakeYs[0] == l.y) {
        if (segmentLocations.get(0).getX() == l.x &&
                segmentLocations.get(0).getY() == l.y) {

            // Add a new Point to the list
            // located off-screen.
            // This is OK because on the next call to
            // move it will take the position of
            // the body in front of it

            this.snake.grow(new Point(-10, -10), Segments.Type.BODY);
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas, Paint paint) {

        List<Segments> segmentLocations = this.snake.getSegments();
        // Don't run this code if ArrayList has nothing in it
        if (!segmentLocations.isEmpty()) {
            // All the code from this method goes here
            // Draw the head
            for (int i = 0; i < segmentLocations.size(); i++) {
                if (segmentLocations.get(i).getType() == Segments.Type.HEAD) {

                    switch (heading) {
                        case RIGHT:
                            canvas.drawBitmap(bitmapHeadRight,
                                    segmentLocations.get(i).getX()
                                            * segmentSize,
                                    segmentLocations.get(i).getY()
                                            * segmentSize, paint);
                            break;

                        case LEFT:
                            canvas.drawBitmap(bitmapHeadLeft,
                                    segmentLocations.get(i).getX()
                                            * segmentSize,
                                    segmentLocations.get(i).getY()
                                            * segmentSize, paint);
                            break;

                        case UP:
                            canvas.drawBitmap(bitmapHeadUp,
                                    segmentLocations.get(i).getX()
                                            * segmentSize,
                                    segmentLocations.get(i).getY()
                                            * segmentSize, paint);
                            break;

                        case DOWN:
                            canvas.drawBitmap(bitmapHeadDown,
                                    segmentLocations.get(i).getX()
                                            * segmentSize,
                                    segmentLocations.get(i).getY()
                                            * segmentSize, paint);
                            break;
                    }
                }

                // Draw the snake body one block at a time
                if (segmentLocations.get(i).getType() == Segments.Type.BODY) {
                    canvas.drawBitmap(bitmapBody,
                            segmentLocations.get(i).getX()
                                    * segmentSize,
                            segmentLocations.get(i).getY()
                                    * segmentSize, paint);
                }
            }
        }
    }

}

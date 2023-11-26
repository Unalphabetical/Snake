package com.example.snakio.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.snakio.R;

import java.util.List;
import java.util.Locale;

public class SnakeHandler {

    ////
    private Snake snake;

    // How big is each body of the snake?
    private int mSegmentSize;

    // How big is the entire grid
    private Point mMoveRange;

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
    private Bitmap mBitmapHeadRight;
    private Bitmap mBitmapHeadLeft;
    private Bitmap mBitmapHeadUp;
    private Bitmap mBitmapHeadDown;

    // A bitmap for the body
    private Bitmap mBitmapBody;

    //// Wrap around variable to enable or disable wrap around
    private boolean wrapAround = false;

    public SnakeHandler(Context context, Point mr, int ss) {

        // Initialize our new Snake
        snake = new Snake();

        // Initialize the body size and movement
        // range from the passed in parameters
        mSegmentSize = ss;
        mMoveRange = mr;

        // Create and scale the bitmaps
        mBitmapHeadRight = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        // Create 3 more versions of the head for different headings
        mBitmapHeadLeft = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        mBitmapHeadUp = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        mBitmapHeadDown = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        // Modify the bitmaps to face the snake head
        // in the correct direction
        mBitmapHeadRight = Bitmap
                .createScaledBitmap(mBitmapHeadRight,
                        ss, ss, false);

        // A matrix for scaling
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);

        mBitmapHeadLeft = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss, ss, matrix, true);

        // A matrix for rotating
        matrix.preRotate(-90);
        mBitmapHeadUp = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss, ss, matrix, true);

        // Matrix operations are cumulative
        // so rotate by 180 to face down
        matrix.preRotate(180);
        mBitmapHeadDown = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss, ss, matrix, true);

        // Create and scale the body
        mBitmapBody = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.body);

        mBitmapBody = Bitmap
                .createScaledBitmap(mBitmapBody,
                        ss, ss, false);

    }

    public Snake getSnake() {
        return this.snake;
    }

    public Heading getHeading() {
        return heading;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
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
        this.snake.reset();

        // Start with a single snake body
        this.snake.grow(new Point(w / 2, h / 2), Segments.Type.HEAD);
    }

    public boolean detectDeath() {
        // Has the snake died?
        boolean dead = false;

        List<Segments> segmentLocations = this.snake.getSegments();
        // Hit any of the screen edges
        if (segmentLocations.get(0).getX() < mMoveRange.x * 0.2 ||
                segmentLocations.get(0).getX() > mMoveRange.x * 0.75 ||
                segmentLocations.get(0).getY() == -1 ||
                segmentLocations.get(0).getY() > mMoveRange.y + 1) {

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
            if (segmentLocations.get(0).getX() < mMoveRange.x * 0.2) {
                segmentLocations.get(0).setX((int) (mMoveRange.x * 0.75));
                this.heading = Heading.LEFT;
            } else if (segmentLocations.get(0).getX() > mMoveRange.x * 0.75) {
                segmentLocations.get(0).setX((int) (mMoveRange.x * 0.2));
                this.heading = Heading.RIGHT;
            } else if (segmentLocations.get(0).getY() == -1) {
                segmentLocations.get(0).setY(mMoveRange.y);
                this.heading = Heading.UP;
            } else if (segmentLocations.get(0).getY() > mMoveRange.y + 1) {
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
                            canvas.drawBitmap(mBitmapHeadRight,
                                    segmentLocations.get(i).getX()
                                            * mSegmentSize,
                                    segmentLocations.get(i).getY()
                                            * mSegmentSize, paint);
                            break;

                        case LEFT:
                            canvas.drawBitmap(mBitmapHeadLeft,
                                    segmentLocations.get(i).getX()
                                            * mSegmentSize,
                                    segmentLocations.get(i).getY()
                                            * mSegmentSize, paint);
                            break;

                        case UP:
                            canvas.drawBitmap(mBitmapHeadUp,
                                    segmentLocations.get(i).getX()
                                            * mSegmentSize,
                                    segmentLocations.get(i).getY()
                                            * mSegmentSize, paint);
                            break;

                        case DOWN:
                            canvas.drawBitmap(mBitmapHeadDown,
                                    segmentLocations.get(i).getX()
                                            * mSegmentSize,
                                    segmentLocations.get(i).getY()
                                            * mSegmentSize, paint);
                            break;
                    }
                }

                // Draw the snake body one block at a time
                if (segmentLocations.get(i).getType() == Segments.Type.BODY) {
                    canvas.drawBitmap(mBitmapBody,
                            segmentLocations.get(i).getX()
                                    * mSegmentSize,
                            segmentLocations.get(i).getY()
                                    * mSegmentSize, paint);
                }
            }
        }
    }

    public void move(String direction) {
        heading = Heading.valueOf(direction.toUpperCase(Locale.ROOT));
    }

    public void setWrapAround(boolean wrapAround) {
        this.wrapAround = wrapAround;
    }

}

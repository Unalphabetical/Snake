package com.example.snakio;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.example.snakio.managers.SaveManager;
import com.example.snakio.snake.SnakeHandler;
import com.example.snakio.states.GameState;

public class SnakeGame extends SurfaceView implements Runnable{

    // Objects for the game loop/thread
    private Thread mThread = null;

    // Control pausing between updates
    private long mNextFrameTime;

    //// This is the game state
    GameState gameState;

    // The size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 40;
    private int mNumBlocksHigh;

    // Objects for drawing
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;

    // A snake ssss
    private SnakeHandler snakeHandler;

    // And an apple
    private Apple mApple;
    private SnakeAudio snakeAudio;

    private TextView gameScore;

    private SaveManager saveManager;

    // This is the constructor method that gets called
    // from SnakeActivity
    public SnakeGame(Context context, Point size) {
        super(context);

        //// Initialize the audio
        snakeAudio = new SnakeAudio(context);

        //// Initialize the game state
        gameState = new GameState();

//        gameScore = (TextView) findViewById(R.id.scoreValue);

        // Work out how many pixels each block is
        int blockSize = size.x / NUM_BLOCKS_WIDE;
        // How many blocks of the same size will fit into the height
        mNumBlocksHigh = size.y / blockSize;

        // Initialize the drawing objects
        mSurfaceHolder = getHolder();
        mPaint = new Paint();

        // Call the constructors of our two game objects
        mApple = new Apple(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        snakeHandler = new SnakeHandler(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        //// Initialize the save manager
        saveManager = new SaveManager(context);
    }

    public SnakeGame(SnakeActivity context, Point size, View viewById) {
        this(context, size);
        this.setGameScore((TextView) viewById);
    }

    public void setGameScore(TextView gameScore) {
        this.gameScore = gameScore;
    }

    //// Getter for the Snake Handler
    //// Handles the art, movement, etc. of the snake
    public SnakeHandler getSnakeHandler() {
        return snakeHandler;
    }

    // Called to start a new game
    public void newGame() {

        // reset the snake
        snakeHandler.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);

        // Get the apple ready for dinner
        mApple.spawn();

        // Setup mNextFrameTime so an update can triggered
        mNextFrameTime = System.currentTimeMillis();
    }

    // Handles the game loop
    @Override
    public void run() {
        while (gameState.isPlaying()) {
            if(!gameState.isPaused()) {
                // Update 10 times a second
                if (updateRequired()) {
                    update();
                }
            }

            draw();
        }
    }

    // Check to see if it is time for an update
    public boolean updateRequired() {

        // Run at 10 frames per second
        final long TARGET_FPS = 10;
        // There are 1000 milliseconds in a second
        final long MILLIS_PER_SECOND = 1000;

        // Are we due to update the frame
        if(mNextFrameTime <= System.currentTimeMillis()){
            // Tenth of a second has passed

            // Setup when the next update will be triggered
            mNextFrameTime =System.currentTimeMillis()
                    + MILLIS_PER_SECOND / TARGET_FPS;

            // Return true so that the update and draw
            // methods are executed
            return true;
        }

        return false;
    }

    // Update all the game objects
    public void update() {

        // Move the snake
        snakeHandler.move();

        // Did the head of the snake eat the apple?
        if(snakeHandler.checkDinner(mApple.getLocation())){
            // This reminds me of Edge of Tomorrow.
            // One day the apple will be ready!
            mApple.spawn();

            // Add to the score of the snake
            snakeHandler.getSnake().setScore(snakeHandler.getSnake().getScore() + 1);

            // Play eat sound
            snakeAudio.playEatSound();
        }

        //// This will be turned into a power later on.
        if (snakeHandler.wrapAround()) {
            System.out.println("wrap around debug");
        }

        // Did the snake die?
        if (snakeHandler.detectDeath()) {
            // Pause the game ready to start again, play crash sound
            snakeAudio.playCrashSound();

            gameState.setPaused(true);
            gameState.setDead(true);
        }

    }

    // Do all the drawing
    public void draw() {

        // Get a lock on the mCanvas
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();

            // Fill the screen with a color
            mCanvas.drawColor(Color.argb(255, 26, 128, 182));

            // Set the size and color of the mPaint for the text
            mPaint.setColor(Color.argb(255, 255, 255, 255));
            mPaint.setTextSize(120);

            // Draw the score
            if (gameScore != null) {
                gameScore.setText(String.valueOf(snakeHandler.getSnake().getScore()));
                gameScore.invalidate();
            }

            // Draw the apple and the snake
            mApple.draw(mCanvas, mPaint);
            snakeHandler.draw(mCanvas, mPaint);

            // Draw some text while paused
            if (gameState.isPaused() || gameState.isDead()){

                // Set the size and color of the mPaint for the text
                mPaint.setColor(Color.argb(255, 255, 255, 255));
                mPaint.setTextSize(200);
                mPaint.setTextAlign(Paint.Align.CENTER);

                //// Calculate the middle of the screen
                //// with the font size included
                int xPos = (mCanvas.getWidth() / 2);
                int yPos = (int) ((mCanvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));

                if (gameState.isDead()) {
                    // Draw the message
                    mCanvas.drawText("Game Over!", xPos, yPos, mPaint);
                } else if (gameState.isPaused()) {
                    // Draw the message
                    mCanvas.drawText("Tap To Play!", xPos, yPos, mPaint);
                }


            }

            // Unlock the mCanvas and reveal the graphics for this frame
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (gameState.isPaused() || gameState.isDead()) {
                    gameState.setPaused(false);

                    if (gameState.isDead()) {
                        gameState.setDead(false);
                    }

                    newGame();

                    // Don't want to process snake direction for this tap
                    return true;
                }
                break;

            default:
                break;
        }

        return true;
    }

    // Stop the thread
    public void pause() {
        gameState.setPlaying(false);
        this.saveManager.setSnake(snakeHandler.getSnake())
                .setGameState(gameState)
                .setApple(mApple)
                .save();

        try {
            mThread.join();
            if (!snakeAudio.isBackgroundMusicPaused()) snakeAudio.pauseBackgroundMusic();
        } catch (InterruptedException e) {
            // Error
        }
    }

    // Start the thread
    public void resume() {
        if (this.saveManager.hasData()) {
            this.saveManager.load();
            this.snakeHandler.setSnake(saveManager.getSnake());
            this.gameState = saveManager.getGameState();
            this.mApple = saveManager.getApple().generateBitmap(getContext());
        }
        gameState.setPlaying(true);

        mThread = new Thread(this);
        mThread.start();
        if (snakeAudio.isBackgroundMusicPaused()) snakeAudio.playBackgroundMusic();
    }

}

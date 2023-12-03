package com.example.snakio;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.example.snakio.apples.Apple;
import com.example.snakio.managers.AppleManager;
import com.example.snakio.managers.LeaderboardManager;
import com.example.snakio.managers.SaveManager;
import com.example.snakio.snake.SnakeCanvas;
import com.example.snakio.snake.SnakeHandler;
import com.example.snakio.states.GameState;

public class SnakeGame extends SurfaceView implements Runnable {

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
    private SnakeCanvas snakeCanvas;

    // A snake ssss
    private SnakeHandler snakeHandler;

//    // And an apple
//    private Apple apple;
    private SnakeAudio snakeAudio;

    private TextView gameScore;

    private SaveManager saveManager;

    private AppleManager appleManager;
    private int appleCount = 10;
    
    // Introducing fps speed for snake
    private long TARGET_FPS = 10;

    // Leaderboard manager
    private LeaderboardManager leaderboardManager;

    // This is the constructor method that gets called
    // from SnakeActivity
    public SnakeGame(Context context, Point size) {
        super(context);

        //// Initialize the audio
        snakeAudio = new SnakeAudio(context);

        //// Initialize the game state
        gameState = new GameState();

        // Work out how many pixels each block is
        int blockSize = size.x / NUM_BLOCKS_WIDE;
        // How many blocks of the same size will fit into the height
        mNumBlocksHigh = size.y / blockSize;

        // Initialize the drawing objects
        SurfaceHolder mSurfaceHolder = getHolder();
        Paint mPaint = new Paint();
        snakeCanvas = new SnakeCanvas(mSurfaceHolder, mPaint);

        // Call the constructors of our two game objects
        appleManager = new AppleManager(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize, appleCount);

        snakeHandler = new SnakeHandler(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        //// Initialize the save manager
        saveManager = new SaveManager(context);

        //// Initialize the leaderboard manager
        leaderboardManager = new LeaderboardManager(context);
    }

    //// Constructor for the Game Score text view
    public SnakeGame(SnakeActivity context, Point size, View viewById) {
        this(context, size);
        this.gameScore = (TextView) viewById;
    }

    //// Getter for the Snake Handler
    //// Handles the art, movement, etc. of the snake
    public SnakeHandler getSnakeHandler() {
        return snakeHandler;
    }

    //// Setter for the Game's FPS speed
    //// allowing us to control how fast or slow the snake moves
    public void setSpeed(long TARGET_FPS){
        this.TARGET_FPS = TARGET_FPS;
    }

    // Called to start a new game
    public void newGame() {

        // reset the snake
        snakeHandler.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);

        // Get the apple(s) ready for dinner
        appleManager.spawnApple();

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

        // There are 1000 milliseconds in a second
        final long MILLIS_PER_SECOND = 1000;

        // Are we due to update the frame
        if(mNextFrameTime <= System.currentTimeMillis()){
            // Tenth of a second has passed

            // Setup when the next update will be triggered
            mNextFrameTime = System.currentTimeMillis()
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
        for (Apple apple : appleManager.getAppleList()) {
            if (apple.isExpired()) {
                apple.updateValidity();
                apple.spawn();
            }

            if(snakeHandler.checkDinner(apple.getLocation())){
                // This reminds me of Edge of Tomorrow.
                // One day the apple will be ready!

                //// Call the power up event
                apple.onEaten(getObjects());

                //// Allow a new apple type to be spawned
                appleManager.replaceApple(apple);
                snakeAudio.playSpawnAppleSound();

                // Add to the score of the snake
                snakeHandler.getSnake().setScore(snakeHandler.getSnake().getScore() + 1);

                // Play eat sound
                snakeAudio.playEatSound();
            }
        }

        //// This will be turned into a power later on.
        snakeHandler.wrapAround();

        // Did the snake die?
        if (snakeHandler.detectDeath()) {
            // Pause the game ready to start again, play crash sound
            snakeAudio.playCrashSound();

            // Save the score to the leaderboard
            this.leaderboardManager.addSnake(snakeHandler.getSnake());

            gameState.setPaused(true);
            gameState.setDead(true);
        }

    }

    // Do all the drawing
    public void draw() {

        // Get a lock on the mCanvas
        if (snakeCanvas.lock()) {
            Canvas canvas = snakeCanvas.getCanvas();
            Paint paint = snakeCanvas.getPaint();
            int score = snakeHandler.getSnake().getScore();

            for (Apple apple : appleManager.getAppleList()) {
                apple.refreshBitmap(getContext()).draw(canvas, paint);
            }

            snakeHandler.draw(canvas, paint);

            if (gameState.isDead()) {
                //// Draw game over message
                snakeCanvas.drawGameOver(score);
            } else if (gameState.isPaused()) {
                // Draw the message
                snakeCanvas.drawTapToPlay();
            }

            snakeCanvas.unlock();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked() & MotionEvent.ACTION_MASK) {
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
                .setHeading(String.valueOf(snakeHandler.getHeading()))
                .setAppleList(appleManager.getAppleList())
                .save();
        this.leaderboardManager.saveSnakeList();

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
            this.snakeHandler.move(saveManager.getHeading());
            this.appleManager.setAppleList(saveManager.getAppleList());
            this.leaderboardManager.loadSnakeList();
        }
        gameState.setPlaying(true);

        mThread = new Thread(this);
        mThread.start();
        //if (snakeAudio.isBackgroundMusicPaused()) snakeAudio.playBackgroundMusic();
    }

    //// This returns all the objects needed for the power up events
    //// So we can manipulate the game, snake handler, or snake itself
    public Object[] getObjects() {
        return new Object[]{this, this.snakeHandler, this.snakeHandler.getSnake()};
    }

}

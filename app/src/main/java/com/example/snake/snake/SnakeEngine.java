package com.example.snake.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.example.snake.menus.SnakeControlMenuActivity;
import com.example.snake.apples.abstracts.AbstractApple;
import com.example.snake.audio.SnakeAudio;
import com.example.snake.managers.AppleManager;
import com.example.snake.managers.LeaderboardManager;
import com.example.snake.managers.SaveManager;
import com.example.snake.states.GameState;

public class SnakeEngine extends SurfaceView implements Runnable {

    // Objects for the game loop/thread
    private Thread mThread = null;

    // Control pausing between updates
    private long nextFrameTime;

    //// This is the game state
    GameState gameState;

    // The size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 40;
    private int numBlocksHigh;

    // Objects for drawing
    private SnakeCanvas snakeCanvas;

    // A snake ssss
    private SnakeHandler snakeHandler;

    //// The snake audio
    private SnakeAudio snakeAudio;

    //// The game score text view
    private TextView gameScore;

    //// The save manager
    private SaveManager saveManager;

    //// The apple manager that handles
    //// the apples and spawning them
    private AppleManager appleManager;

    //// The number of apples to spawn
    private int APPLE_COUNT = 10;

    // Introducing fps speed for snake
    private long TARGET_FPS = 10;

    // Leaderboard manager
    private LeaderboardManager leaderboardManager;

    // This is the constructor method that gets called
    // from SnakeMainMenuActivity
    public SnakeEngine(Context context, Point size) {
        super(context);
        initialize(context, size);
    }

    //// Constructor for the Game Score text view
    public SnakeEngine(SnakeControlMenuActivity context, Point size, View gameScoreView) {
        super(context);
        initialize(context, size);
        this.gameScore = (TextView) gameScoreView;
    }

    //// This initializes all the objects needed for the game
    private void initialize(Context context, Point size) {
        this.snakeAudio = new SnakeAudio(context);
        this.gameState = new GameState();

        int blockSize = size.x / this.NUM_BLOCKS_WIDE;
        this.numBlocksHigh = size.y / blockSize;

        SurfaceHolder mSurfaceHolder = getHolder();
        Paint mPaint = new Paint();
        this.snakeCanvas = new SnakeCanvas(mSurfaceHolder, mPaint);

        this.appleManager = new AppleManager(context, new Point(NUM_BLOCKS_WIDE, numBlocksHigh), blockSize, APPLE_COUNT);
        this.snakeHandler = new SnakeHandler(context, new Point(NUM_BLOCKS_WIDE, numBlocksHigh), blockSize);

        this.saveManager = new SaveManager(context);
        this.leaderboardManager = new LeaderboardManager(context);
    }

    //// Getter for the Snake Handler
    //// Handles the art, movement, etc. of the snake
    public SnakeHandler getSnakeHandler() {
        return this.snakeHandler;
    }

    //// This returns all the objects needed for the power up events
    //// So we can manipulate the game, snake handler, or snake itself
    public Object[] getObjects() {
        return new Object[]{this, this.snakeHandler, this.snakeHandler.getSnake()};
    }

    //// Setter for the Game's FPS speed
    //// allowing us to control how fast or slow the snake moves
    public void setSpeed(long TARGET_FPS){
        this.TARGET_FPS = TARGET_FPS;
    }

    // Called to start a new game
    public void newGame() {

        // reset the snake
        snakeHandler.reset(NUM_BLOCKS_WIDE, numBlocksHigh);

        //// Reset the inverted state
        Snake snake = snakeHandler.getSnake();
        snake.setInverted(false);

        //// Reset the FPS speed
        this.TARGET_FPS = 10;

        //// Reset the wrap around
        snakeHandler.setWrapAround(false);

        // Get the apple(s) ready for dinner
        appleManager.spawnApple();

        // Setup nextFrameTime so an update can triggered
        nextFrameTime = System.currentTimeMillis();
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
        if(nextFrameTime <= System.currentTimeMillis()){
            // Tenth of a second has passed

            // Setup when the next update will be triggered
            nextFrameTime = System.currentTimeMillis()
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
        for (AbstractApple apple : appleManager.getAppleList()) {
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
            leaderboardManager.addSnake(snakeHandler.getSnake());
            leaderboardManager.saveSnakeList();

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

            for (AbstractApple apple : appleManager.getAppleList()) {
                apple.refreshBitmap(getContext()).draw(canvas, paint);
            }

            if (gameScore != null) {
                gameScore.setText(String.valueOf(score));
                gameScore.invalidate();
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
    //// And save all the required data for the game
    public void pause() {
        gameState.setPlaying(false);
        saveGameData();
        try {
            mThread.join();
            if (!snakeAudio.isBackgroundMusicPaused()) snakeAudio.pauseBackgroundMusic();
        } catch (InterruptedException e) {
            // Error handling...
        }
    }

    // Start the thread
    //// And load all the required data for the game
    public void resume() {
        if (saveManager.hasData()) {
            loadGameData();
        }
        gameState.setPlaying(true);
        mThread = new Thread(this);
        mThread.start();
        // if (snakeAudio.isBackgroundMusicPaused()) snakeAudio.playBackgroundMusic();
    }

    // Method to save the game data
    private void saveGameData() {
        saveManager.setSnake(snakeHandler.getSnake())
                .setGameState(gameState)
                .setHeading(String.valueOf(snakeHandler.getHeading()))
                .setAppleList(appleManager.getAppleList())
                .save();
        leaderboardManager.saveSnakeList();
    }

    // Method to load the game data
    private void loadGameData() {
        saveManager.load();
        snakeHandler.setSnake(saveManager.getSnake());
        gameState = saveManager.getGameState();
        snakeHandler.move(saveManager.getHeading());
        appleManager.setAppleList(saveManager.getAppleList());
        leaderboardManager.loadSnakeList();
    }

}

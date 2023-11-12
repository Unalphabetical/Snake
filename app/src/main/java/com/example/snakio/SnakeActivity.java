package com.example.snakio;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class SnakeActivity extends Activity {

    // Declare an instance of SnakeGame
    SnakeGame mSnakeGame;

    // Set the game up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the pixel dimensions of the screen
        Display display = getWindowManager().getDefaultDisplay();

        // Initialize the result into a Point object
        Point size = new Point();
        display.getSize(size);

        //// This puts the game in full screen and
        //// hides all the other things such as navigation bar.
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //// This relative layout allows us to put the snake game and control menu together
        RelativeLayout relativeLayout = new RelativeLayout(this);

        //// This loads in the control menu from the layout xml file
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View textView = inflater.inflate(R.layout.snake_control_menu, null);

        // Create a new instance of the SnakeEngine class
        mSnakeGame = new SnakeGame(this, size);

        //// This adds the snake game to the relative layout
        relativeLayout.addView(mSnakeGame);

        //// This adds the control menu to the relative layout
        relativeLayout.addView(textView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        //// This sets the content view to the relative layout
        //// which now has the snake game and control rendered on it
        setContentView(relativeLayout);
    }

    public void moveLeft(View view) {
        this.mSnakeGame.getSnake().move("Left");
    }

    public void moveUp(View view) {
        this.mSnakeGame.getSnake().move("Up");
    }

    public void moveRight(View view) {
        this.mSnakeGame.getSnake().move("Right");
    }

    public void moveDown(View view) {
        this.mSnakeGame.getSnake().move("Down");
    }

    // Start the thread in snakeEngine
    @Override
    protected void onResume() {
        super.onResume();
        mSnakeGame.resume();
    }

    // Stop the thread in snakeEngine
    @Override
    protected void onPause() {
        super.onPause();
        mSnakeGame.pause();
    }
}
package com.example.snake;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.snake.menus.SnakeSettingsActivity;
import com.example.snake.snake.Snake;
import com.example.snake.snake.SnakeEngine;
import com.example.snake.snake.SnakeHandler;

public class SnakeMainMenuActivity extends Activity {

    // Declare an instance of SnakeEngine
    SnakeEngine snakeEngine;

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

        //// This gets the text view from the control menu
        TextView scoreTextView = textView.findViewById(R.id.scoreValue);

        // Create a new instance of the SnakeEngine class
        //// The score text view is passed onto the SnakeEngine class
        //// So we can show and update the score inside there
        this.snakeEngine = new SnakeEngine(this, size, scoreTextView);

        //// This adds the snake game to the relative layout
        relativeLayout.addView(this.snakeEngine);

        //// This adds the control menu to the relative layout
        relativeLayout.addView(textView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        //// This sets the content view to the relative layout
        //// which now has the snake game and control rendered on it
        setContentView(relativeLayout);
    }

    //// The click event for the "Left" button
    //// This method can invert the snake's direction if the snake has eaten a Blue apple
    public void moveLeft(View view) {
        SnakeHandler snakeHandler = snakeEngine.getSnakeHandler();
        Snake snake = snakeHandler.getSnake();
        snakeHandler.move(snake.isInverted() ? "Right" : "Left");
    }

    //// The click event for the "Up" button
    //// This method can invert the snake's direction if the snake has eaten a Blue apple
    public void moveUp(View view) {
        SnakeHandler snakeHandler = snakeEngine.getSnakeHandler();
        Snake snake = snakeHandler.getSnake();
        snakeHandler.move(snake.isInverted() ? "Down" : "Up");
    }

    //// The click event for the "Right" button
    //// This method can invert the snake's direction if the snake has eaten a Blue apple
    public void moveRight(View view) {
        SnakeHandler snakeHandler = snakeEngine.getSnakeHandler();
        Snake snake = snakeHandler.getSnake();
        snakeHandler.move(snake.isInverted() ? "Left" : "Right");
    }

    //// The click event for the "Down" button
    //// This method can invert the snake's direction if the snake has eaten a Blue apple
    public void moveDown(View view) {
        SnakeHandler snakeHandler = snakeEngine.getSnakeHandler();
        Snake snake = snakeHandler.getSnake();
        snakeHandler.move(snake.isInverted() ? "Up" : "Down");
    }

    //// The click event for the "Settings" button
    public void settingsMenu(View v){
        Intent intent = new Intent(this, SnakeSettingsActivity.class);
        startActivity(intent);
    }

    // Start the thread in snakeEngine
    @Override
    protected void onResume() {
        super.onResume();
        snakeEngine.resume();
    }

    // Stop the thread in snakeEngine
    @Override
    protected void onPause() {
        super.onPause();
        snakeEngine.pause();
    }

}

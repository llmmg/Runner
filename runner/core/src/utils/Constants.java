package utils;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Lancelot on 22.08.2016.
 */
public class Constants {
    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 480;
    public static final float SCALE = 32;//WORLD_TO_STAGE_SCALE

    public static final Vector2 WORLD_GRAVITY = new Vector2(0, -10);

    public static final float GROUND_X = 0;
    public static final float GROUND_Y = 0;
    public static final float GROUND_WIDTH = 50f;
    public static final float GROUND_HEIGHT = 2f;
    public static final float GROUND_DENSITY = 0f;

    public static final float RUNNER_X = 2;
    public static final float RUNNER_Y = GROUND_Y + GROUND_HEIGHT;
    public static final float RUNNER_WIDTH = 1f;
    public static final float RUNNER_HEIGHT = 2f;
    public static final float RUNNER_DENSITY = 0.5f;

    public static final  float RUNNER_GRAVITY_SCALE=3f; //gravity is amplified for a best gameplay
    public static final Vector2 RUNNER_JUMPING_LINEAR_IMPULSE= new Vector2(0,13f);

    public static final float RUNNER_DODGE_X =2f;
    public static final float RUNNER_DODGE_Y=1.5f;

    //useful for an infinite runner but not for our platformer
    public static final Vector2 RUNNER_VELOCITY= new Vector2(0,0);

    public static final String BACKGROUND_IMAGE_PATH= "core//assets//background.png";

    public static final String CHARACTER_RUN_PATH = "core//assets//sprites//runSprite.png";
    public static final String CHARACTER_IDLE_PATH = "core//assets//sprites//idle.png";
    public static final String CHARACTER_JUMP_PATH= "core//assets//sprites//jump.png";
}

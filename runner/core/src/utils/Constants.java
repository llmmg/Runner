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

    public static final float RUNNER_GRAVITY_SCALE = 3f; //gravity is amplified for a best gameplay
    public static final Vector2 RUNNER_JUMPING_LINEAR_IMPULSE = new Vector2(0, 13f);

    public static final float RUNNER_DODGE_X = 2f;
    public static final float RUNNER_DODGE_Y = 1.5f;

    //useful for an infinite runner but not for our platformer
    public static final Vector2 RUNNER_VELOCITY = new Vector2(0, 0);

    public static final String BACKGROUND_IMAGE_PATH = "core//assets//background.png";

    public static final String CHARACTER_RUN_PATH = "core//assets//sprites//runSprite.png";
    public static final String CHARACTER_IDLE_PATH = "core//assets//sprites//idle.png";
    public static final String CHARACTER_JUMP_PATH = "core//assets//sprites//jump.png";

    public static final String CAT_ATLAS_PATH = "core//assets//sprites//cat//cat.txt";
    public static final String[] CAT_RUN_REGION_NAMES = new String[]{"Run (1)", "Run (2)", "Run (3)", "Run (4)", "Run (5)", "Run (6)", "Run (7)", "Run (8)"};
    public static final String[] CAT_IDLE_REGION_NAMES = new String[]{"Idle (1)", "Idle (2)", "Idle (3)", "Idle (4)", "Idle (5)", "Idle (6)", "Idle (7)", "Idle (8)", "Idle (9)", "Idle (10)"};
    public static final String[] CAT_JUMP_REGION_NAMES = new String[]{"Jump (1)", "Jump (2)", "Jump (3)", "Jump (4)", "Jump (5)", "Jump (6)", "Jump (7)", "Jump (8)"};
    public static final String[] CAT_FALL_REGION_NAMES = new String[]{"Fall (1)", "Fall (2)", "Fall (3)", "Fall (4)", "Fall (5)", "Fall (6)", "Fall (7)", "Fall (8)"};
    public static final String[] CAT_SLIDE_REGION_NAMES = new String[]{"Slide (1)", "Slide (2)", "Slide (3)", "Slide (4)", "Slide (5)", "Slide (6)", "Slide (7)", "Slide (8)", "Slide (9)", "Slide (10)"};

}

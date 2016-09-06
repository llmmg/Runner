package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import utils.Constants;

/**
 * Created by Lancelot on 26.08.2016.
 */
public class Background extends Actor {

    private final TextureRegion textureRegion;
    private Rectangle textureRegionBounds1;
    private Rectangle textureRegionBounds2;
    private int speed = 0;


    public Background() {
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal(Constants.BACKGROUND_IMAGE_PATH)));
        textureRegionBounds1 = new Rectangle(0 - Constants.APP_WIDTH / 2, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        textureRegionBounds2 = new Rectangle(Constants.APP_WIDTH / 2, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
    }

    /**
     * @param delta time interval
     */
    @Override
    public void act(float delta) {
        if (leftBoundsReached(delta)) {
            resetBounds(false);
        } else if (rightBoundsReached(delta)) {
            resetBounds(true);
        } else {
            updateXBounds(-delta);
        }
    }

    /**
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion, textureRegionBounds1.x, textureRegionBounds1.y, Constants.APP_WIDTH,
                Constants.APP_HEIGHT);
        batch.draw(textureRegion, textureRegionBounds2.x, textureRegionBounds2.y, Constants.APP_WIDTH,
                Constants.APP_HEIGHT);
    }

    /**
     * Return true when the left side of background is reached
     *
     * @param delta
     * @return boolean
     */
    private boolean leftBoundsReached(float delta) {
        return (textureRegionBounds2.x - (delta * speed)) <= 0;
    }

    /**
     * Return true when the right side of background is reached
     *
     * @param delta
     * @return boolean
     */
    private boolean rightBoundsReached(float delta) {
        return (textureRegionBounds2.x - (delta * speed)) > Constants.APP_WIDTH;
    }

    /**
     * Update background bounds positions compared to speed
     *
     * @param delta
     */
    private void updateXBounds(float delta) {
        textureRegionBounds1.x += delta * speed;
        textureRegionBounds2.x += delta * speed;
    }

    /**
     * Setter for variable speed
     *
     * @param speed value to set for speed variable
     */
    public void setSpeed(float speed) {
        this.speed = (int) speed * 13;
    }


    /**
     * Replace background bounds to initial position.
     * Used to make the scrolling effect
     *
     * @param right true if rightBoundReached is true(if background scroll to the right)
     *              false if leftBoundReached is true (if background scroll to the left)
     */
    private void resetBounds(boolean right) {
        if (right) {
            textureRegionBounds2 = textureRegionBounds1;
            textureRegionBounds1 = new Rectangle(-Constants.APP_WIDTH, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        } else {
            textureRegionBounds1 = textureRegionBounds2;
            textureRegionBounds2 = new Rectangle(Constants.APP_WIDTH, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        }

    }
}

package actors.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.RunnerGame;
import utils.Constants;

/**
 * Created by Lancelot on 30.08.2016.
 * <p>
 * Create a label who contains a timer
 * </p>
 */
public class TextScore extends Actor {
    private float totalTime;
    private float minutes;
    private float seconds;
    private Label time;
    private Label.LabelStyle labelStyle;
    private BitmapFont font;
    private TextureRegionDrawable reg;
    private Texture img;
    private RunnerGame game;

    public TextScore(){
        game= RunnerGame.getINSTANCE();
        totalTime=0;
        font = new BitmapFont();
        labelStyle = new Label.LabelStyle();
        img = new Texture(Constants.LABEL_TIMER_IMG);
        labelStyle.font = font;
        reg =new TextureRegionDrawable(new TextureRegion(img));
        labelStyle.background= reg;

        time = new Label("", labelStyle);
        time.setAlignment(Align.center);
        time.setBounds((Constants.APP_WIDTH-img.getWidth())/2, Constants.APP_HEIGHT-img.getHeight(),img.getWidth(),img.getHeight());
    }

    /**
     * Return the label
     * @return
     */
    public Label getTextScore()
    {
        return  time;
    }

    /**
     * Update the label's text with the current time
     */
    public void update(){
        if(!game.getState()) {
            totalTime += Gdx.graphics.getRawDeltaTime();
        }
        else{
            totalTime+=0;
        }
        minutes = (float)Math.floor(totalTime / 60.0f);
        seconds = totalTime - minutes * 60.0f;
        time.setText(String.format("%.0fm%.0fs", minutes, seconds));
    }
    /**
     * @return The time you got when you finished the level
     */
    public String getTime(){
        return String.format("Temps : %.0fm : %.0fs \n Press space to continue ",minutes,seconds);
    }
}

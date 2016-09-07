package actors.HUD;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import utils.Constants;

/**
 * Created by damien.gygi on 05.09.2016.
 * <p>
 * The label which is used when the player finished a level
 * </p>
 */
public class TextEndLevel extends Actor {

    private Label.LabelStyle labelStyle;
    private BitmapFont font;
    private Label time;
    public TextEndLevel() {
        font = new BitmapFont();
        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor= Color.PURPLE;

        time = new Label("Score", labelStyle);
        time.setAlignment(Align.center);
        time.setBounds(Constants.APP_WIDTH / 2, Constants.APP_HEIGHT/2+100, 0, 0);
    }

    /**
     * Set the text with the string in parameter
     * @param text
     */
    public void showTextEndLevel(String text){
        time.setText(text);
    }

    /**
     * Return the label
     * @return
     */
    public Label getTextEndLevel()
    {
        return  time;
    }
}

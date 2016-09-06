package actors.HUD;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.RunnerGame;
import utils.Constants;

/**
 * Created by damien.gygi on 05.09.2016.
 */
public class TextEndLevel extends Actor {

    private Label.LabelStyle labelStyle;
    private BitmapFont font;
    private Label time;
    public TextEndLevel() {
        font = new BitmapFont();
        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor= Color.RED;

        time = new Label("Score", labelStyle);
        time.setAlignment(Align.center);
        time.setBounds(Constants.APP_WIDTH / 2, Constants.APP_HEIGHT/2, 0, 0);
    }
    public void showTextEndLevel(String text){
        time.setText(text);
    }

    public Label getTextEndLevel()
    {
        return  time;
    }
}

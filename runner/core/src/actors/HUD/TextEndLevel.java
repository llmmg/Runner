package actors.HUD;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.RunnerGame;
import utils.Constants;

/**
 * Created by damien.gygi on 05.09.2016.
 */
public class TextEndLevel extends Actor {

    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private TextButton time;
    public TextEndLevel() {
        font = new BitmapFont();
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor= Color.RED;
        time = new TextButton("Score", textButtonStyle);
        time.setBounds(Constants.APP_WIDTH / 2, Constants.APP_HEIGHT/2, 10, 10);
    }
    public void showTextEndLevel(String text){
        time.setText(text);
    }

    public TextButton getTextEndLevel()
    {
        return  time;
    }
}

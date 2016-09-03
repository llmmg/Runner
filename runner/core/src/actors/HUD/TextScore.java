package actors.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import utils.Constants;

/**
 * Created by Lancelot on 30.08.2016.
 */
public class TextScore extends Actor {
    private float totalTime;
    private float minutes;
    private float seconds;
    //private Label time;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private TextButton time;
    public TextScore(){

        totalTime=0;
        font = new BitmapFont();
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        time = new TextButton("Score", textButtonStyle);
        time.setBounds(20, 10,10,10);
    }
    public TextButton getTextScore()
    {
        return  time;
    }
    public void update(){
        totalTime += Gdx.graphics.getDeltaTime();
        minutes = (float)Math.floor(totalTime / 60.0f);
        seconds = totalTime - minutes * 60.0f;
        time.setText(String.format("%.0fm%.0fs", minutes, seconds));
    }
}

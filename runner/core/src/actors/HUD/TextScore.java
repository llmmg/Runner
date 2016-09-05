package actors.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.RunnerGame;
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
    private RunnerGame game;
    public TextScore(){
        game= RunnerGame.getINSTANCE();
        totalTime=0;
        font = new BitmapFont();
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        time = new TextButton("Score", textButtonStyle);
        time.setBounds(Constants.APP_WIDTH/2, Constants.APP_HEIGHT-20,10,10);
    }
    public TextButton getTextScore()
    {
        return  time;
    }
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
    public String getTime(){
        return String.format("Temps : %.0fm : %.0fs \n Appuyez sur espace pour continuer ",minutes,seconds);
    }
}

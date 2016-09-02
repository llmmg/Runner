package actors.HUD;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.RunnerGame;
import utils.Constants;

/**
 * Created by Lancelot on 30.08.2016.
 */
public class ButtonPause extends Button{

    private TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private TextButton button;
    private float buttonHeight=10f;
    private float buttonWidth=10f;
    private RunnerGame Game;

    public ButtonPause() {
        Game= RunnerGame.getINSTANCE();
        createButton();
    }
    public TextButton createButton(){
        font = new BitmapFont();
        textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        button = new TextButton("Pause", textButtonStyle);
        button.setBounds(0+buttonWidth*2, Constants.APP_HEIGHT-buttonHeight,buttonWidth,buttonHeight);
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Game.pause();
                System.out.println("Button Pressed");
            }
        });
        return button;
    }
}

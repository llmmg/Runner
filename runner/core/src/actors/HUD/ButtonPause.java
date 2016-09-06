package actors.HUD;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.RunnerGame;
import utils.Constants;

import javax.swing.text.AbstractDocument;

/**
 * Created by Lancelot on 30.08.2016.
 */
public class ButtonPause extends Button{

    private TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private TextButton button;
    private Skin skin;
    private float buttonHeight=10f;
    private float buttonWidth=10f;
    private RunnerGame game;
    private Texture img;

    public ButtonPause() {
        game= RunnerGame.getINSTANCE();
        font = new BitmapFont();
        img = new Texture(Constants.BUTTON_PAUSE_IMG);

        textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;

        button = new TextButton("Pause", textButtonStyle);
        button.setBounds(0+buttonWidth*2, Constants.APP_HEIGHT-buttonHeight,buttonWidth,buttonHeight);
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                game.setPause();
                System.out.println("Button Pressed");
            }
        });
        //createButton();
    }
    public TextButton getButtonPause(){
        return button;
    }
}

package actors.HUD;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

    public ButtonPause() {
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
                System.out.println("Button Pressed");
            }
        });
        return button;
    }
}

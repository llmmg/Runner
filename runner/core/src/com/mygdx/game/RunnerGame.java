package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sun.glass.ui.EventLoop;
import screens.GameScreen;

//extends Game for multiple screen
public class RunnerGame extends Game {
    private boolean isPause=false;
    private RunnerGame(){}

    private static RunnerGame INSTANCE;

    public static RunnerGame getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new RunnerGame();
        }
        return INSTANCE;
    }

	@Override
	public void create() {
		setScreen(new GameScreen());
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		if (Gdx.input.isKeyPressed(Input.Keys.R))
			reset();
		if(Gdx.input.isKeyPressed(Input.Keys.P)){
            Gdx.graphics.setContinuousRendering(false);
            isPause=true;
            //Gdx.graphics.requestRendering();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.O)){
            //Gdx.graphics.requestRendering();
            Gdx.graphics.setContinuousRendering(true);
            isPause=false;
		}
        super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	public void reset(){
        try {
            setScreen(getScreen().getClass().newInstance());
        } catch(InstantiationException e) {
            e.printStackTrace();
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public boolean getState()
    {
        return isPause;
    }
}

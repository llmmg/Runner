package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import screens.GameScreen;

//extends Game for multiple screen
public class RunnerGame extends Game {
    private boolean isPause=false;
	private int currentLevel =0;
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
		if (Gdx.input.isKeyJustPressed(Input.Keys.R))
			reset();
		if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
			setPause();
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
    public void setPause(){
		if(isPause){
			this.resume();
			isPause=false;
		}
		else {
			this.pause();
			isPause = true;
		}
	}
	public int getCurrentLevel(){
		return currentLevel;
	}
	public int nextLevel(){
		return currentLevel++;
	}
}

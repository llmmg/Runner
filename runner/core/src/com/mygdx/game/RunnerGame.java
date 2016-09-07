package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import screens.GameScreen;

/**
 * extends Game for multiple screen
 */

public class RunnerGame extends Game {
    private boolean isPause=false;
	private int currentLevel =0;
	private boolean isEndLevel=false;

    private RunnerGame(){}

    private static RunnerGame INSTANCE;

	/**
	 * return RunnerGame Instance
	 * @return
	 */
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
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
			if (isEndLevel)
			{
				isEndLevel=false;
				nextLevel();
				setPause();
				this.reset();
			}
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

	/**
	 * Reset the current level
	 */
	public void reset(){
        try {
            if(!isEndLevel) {
                setScreen(getScreen().getClass().newInstance());
            }
        } catch(InstantiationException e) {
            e.printStackTrace();
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
    }

	/**
	 * Return if the game state is paused
	 * @return
	 */
	public boolean getState()
    {
        return isPause;
    }
    public void setPause(){
    	if(!isEndLevel) {
			if (isPause) {
				this.resume();
				isPause = false;
			} else {
				this.pause();
				isPause = true;
			}
		}
	}

	/**
	 * Return the current level
	 * @return
	 */
	public int getCurrentLevel(){
		return currentLevel;
	}

	/**
	 * Increase the current level when you finished a level
	 * @return
	 */
	public int nextLevel(){
		return currentLevel++;
	}

	/**
	 * Called when you reach the end of a level
	 */
	public void setEndLevel(){
		setPause();
		isEndLevel=true;
	}
}

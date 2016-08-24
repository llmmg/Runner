package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import screens.GameScreen;

//extends Game for multiple screen
public class Runner extends Game {
	
	@Override
	public void create () {
		setScreen(new GameScreen());
	}
}

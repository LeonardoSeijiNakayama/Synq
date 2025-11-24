package io.Prototipo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.Prototipo.loader.B2DAssetManager;
import io.Prototipo.views.LevelsScreen;
import io.Prototipo.views.LoadingScreen;
import io.Prototipo.views.LoseScreen;
import io.Prototipo.views.MainScreen;
import io.Prototipo.views.MenuScreen;
import io.Prototipo.views.PreferencesScreen;
import io.Prototipo.views.WinScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Prototipo extends Game {

	private LoadingScreen loadingScreen;
	private PreferencesScreen preferencesScreen;
	private MenuScreen menuScreen;
	private MainScreen mainScreen;
	private AppPreferences preferences;
	private LevelsScreen levelsScreen;
	private WinScreen winScreen;
	private LoseScreen loseScreen;
	public B2DAssetManager assMan = new B2DAssetManager();
	public int currentLevel;
	public SpriteBatch batch;

	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int ENDGAME = 3;
	public final static int LEVELS = 4;
	public final static int WIN = 5;
	public final static int LOSE = 6;

	public final static int PHASE1 = 10;
	public final static int PHASE2 = 11;
	public final static int PHASE3 = 12;
	public final static int PHASE4 = 13;

	@Override
	public void create() {
		Gdx.graphics.setForegroundFPS(120);
		loadingScreen = new LoadingScreen(this);
		batch = new SpriteBatch();
		setScreen(loadingScreen);
		preferences = new AppPreferences();
	}

	public void changeScreen(int screen) {
		switch (screen) {
			case MENU:
				if (menuScreen == null)
					menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
			case PREFERENCES:
				if (preferencesScreen == null)
					preferencesScreen = new PreferencesScreen(this);
				this.setScreen(preferencesScreen);
				break;
			case PHASE1:
				if (mainScreen == null)
					mainScreen = new MainScreen(this, Prototipo.PHASE1);
				else
					mainScreen.resetWorld(Prototipo.PHASE1);
				currentLevel = Prototipo.PHASE1;
				this.setScreen(mainScreen);
				break;
			case LEVELS:
				if (levelsScreen == null) {
					levelsScreen = new LevelsScreen(this);
				}
				this.setScreen(levelsScreen);
				break;
			case WIN:
				if (winScreen == null) {
					winScreen = new WinScreen(this);
				}
				this.setScreen(winScreen);
				break;
			case LOSE:
				if (loseScreen == null) {
					loseScreen = new LoseScreen(this);
				}
				loseScreen.reset();
				this.setScreen(loseScreen);
				break;
			case PHASE2:
				if (mainScreen == null)
					mainScreen = new MainScreen(this, Prototipo.PHASE2);
				else
					mainScreen.resetWorld(Prototipo.PHASE2);
				currentLevel = Prototipo.PHASE2;
				this.setScreen(mainScreen);
				break;
			case PHASE3:
				if (mainScreen == null)
					mainScreen = new MainScreen(this, Prototipo.PHASE3);
				else
					mainScreen.resetWorld(Prototipo.PHASE3);
				currentLevel = Prototipo.PHASE3;
				this.setScreen(mainScreen);
				break;
			case PHASE4:

				if (mainScreen == null)
					mainScreen = new MainScreen(this, Prototipo.PHASE4);
				else
					mainScreen.resetWorld(Prototipo.PHASE4);
				currentLevel = Prototipo.PHASE4;
				this.setScreen(mainScreen);

				break;
		}
	}

	public AppPreferences getPreferences() {
		return this.preferences;
	}

	@Override
	public void dispose() {
		assMan.manager.dispose();
	}

	public void nextLevel(){
		currentLevel +=1;
	}
}

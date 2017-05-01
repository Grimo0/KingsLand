package fr.grimoire.kingsland;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.grimoire.kingsland.domain.Domain;
import fr.grimoire.kingsland.ui.AchievementBox;
import fr.grimoire.kingsland.ui.EcusLabel;
import fr.grimoire.kingsland.ui.Toolbox;

/**
 * @author Radnap
 */
public class GameScreen implements Screen {

	private Stage stage;

	private Domain domain;
	private EcusLabel ecusLabel;
	private Toolbox toolbox;
	private AchievementBox achievementBox;

	private boolean debug;


	public GameScreen(int width, int height) {
		debug = false;
		KingsLand.soundLevel = 0.f;

		FitViewport fitViewport = new FitViewport(width, height);
		stage = new Stage(fitViewport);

		ecusLabel = new EcusLabel(" ");
		ecusLabel.setX(2.f);
		ecusLabel.setY(height - ecusLabel.getPrefHeight());
		stage.addActor(ecusLabel);

		toolbox = new Toolbox(width, 50);
		stage.addActor(toolbox);

		achievementBox = new AchievementBox();
		achievementBox.setX(width - achievementBox.getWidth() - 10);
		achievementBox.setY(height - achievementBox.getHeight());
		stage.addActor(achievementBox);
		Achievement.init(achievementBox);

		domain = new Domain();
		domain.setPosition(8.f, 64.f);
		stage.addActor(domain);

		Gdx.input.setInputProcessor(stage);
		KingsLand.soundLevel = 1.f;
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		if (KingsLand.DEBUG && Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			debug = !debug;
			stage.setDebugAll(debug);
		}
		Gdx.gl.glClearColor(KingsLand.secondary.r, KingsLand.secondary.g, KingsLand.secondary.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();

		ecusLabel.setText(domain.getEcus() + "");

		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}

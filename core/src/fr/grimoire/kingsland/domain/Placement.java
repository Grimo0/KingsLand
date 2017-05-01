package fr.grimoire.kingsland.domain;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fr.grimoire.kingsland.Achievement;
import fr.grimoire.kingsland.KingsLand;
import fr.grimoire.kingsland.domain.building.Building;
import fr.grimoire.kingsland.domain.building.BuildingType;
import fr.grimoire.kingsland.ui.InvertedLabel;
import fr.grimoire.kingsland.ui.LevelLabel;
import fr.grimoire.kingsland.ui.ProgressBar;
import fr.grimoire.kingsland.ui.Toolbox;

/**
 * @author Radnap
 */
public class Placement extends Group {

	private Domain domain;
	private Building building;
	private Group buildGroup;
	private ProgressBar buildProgress;
	private InvertedLabel workLabel;
	private LevelLabel levelLabel;
	private InvertedLabel upgradeGroup;


	public Placement(Domain domain) {
		this.domain = domain;
		building = BuildingType.EMPTY.build();

		// Build
		buildGroup = new Group();
		Image buildButtonBack = new Image(KingsLand.skin, "hammerBack");
		buildGroup.addActor(buildButtonBack);
		Image buildButton = new Image(KingsLand.skin, "hammer");
		buildGroup.addActor(buildButton);
		buildGroup.setSize(buildButtonBack.getWidth(), buildButtonBack.getHeight());
		buildGroup.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				construct(Toolbox.getSelectedBuildingType());
			}
		});
		addActor(buildGroup);

		buildProgress = new ProgressBar();
		buildProgress.setVisible(false);
		addActor(buildProgress);

		// Work
		workLabel = new InvertedLabel("ecuIncrease", false);
		workLabel.setVisible(false);
		addActor(workLabel);

		// Level
		levelLabel = new LevelLabel();
		levelLabel.setVisible(false);
		addActor(levelLabel);

		// Upgrade
		upgradeGroup = new InvertedLabel("upArrow", true);
		upgradeGroup.setVisible(false);
		upgradeGroup.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				upgrade();
			}
		});
		addActor(upgradeGroup);

		updateUI();
	}

	public Building getBuilding() {
		return building;
	}

	@Override
	protected void positionChanged() {
		super.positionChanged();
		updateUI();
	}

	@Override
	protected void sizeChanged() {
		super.sizeChanged();
		updateUI();
	}

	private void updateUI() {
		building.setSize(getWidth(), getWidth());
		building.setPosition(getX(), getY() + getHeight() - getWidth());
		buildGroup.setX((int) (getWidth() - buildGroup.getWidth()) / 2);
		buildGroup.setY((int) (getHeight() - buildGroup.getHeight()) / 2);
		buildProgress.setWidth(getWidth() * 0.8f);
		buildProgress.setX((int) (getWidth() - buildProgress.getWidth()) / 2);
		buildProgress.setY(levelLabel.getY() + (int) (levelLabel.getHeight() - buildProgress.getHeight()) / 2);
		levelLabel.setX((int) (getWidth() - levelLabel.getWidth()) / 2);
		upgradeGroup.setX(levelLabel.getX() + levelLabel.getWidth());
		upgradeGroup.setY((int) (levelLabel.getHeight() - upgradeGroup.getHeight()) / 2 - 1);
	}

	void construct(BuildingType type) {
		if (building.getType() != BuildingType.EMPTY || type == BuildingType.EMPTY || !domain.pay(type.getCost(0)))
			return;
		building = type.build();
		building.setSize(getWidth(), getWidth());
		building.setPosition(getX(), getY() + getHeight() - getWidth());
		buildGroup.setVisible(false);
		buildProgress.setProgress(0.f);
		buildProgress.setVisible(true);
		Achievement.CRAFTSMAN.step();
		Achievement.ARCHITECT.step();
		Sound sound = KingsLand.skin.get("build.wav", Sound.class);
		sound.setPitch(sound.play(0.07f * KingsLand.soundLevel), 0.7f + (float) Math.random() * 0.6f);
	}

	private void destroy() {
		if (building.getType() != BuildingType.EMPTY || !domain.pay(BuildingType.EMPTY.getCost(0)))
			return;
		building = BuildingType.EMPTY.build();
		buildGroup.setVisible(true);
		workLabel.setVisible(false);
		levelLabel.setVisible(false);
		upgradeGroup.setVisible(false);
		Achievement.CRAFTSMAN.unstep();
		Achievement.ARCHITECT.unstep();
		KingsLand.skin.get("build", Sound.class).play();
	}

	private void upgrade() {
		if (building.getLevel() == building.getType().getLevelMax() || !domain.pay(building.getType().getCost(building.getLevel())))
			return;
		building.startBuilding();

		buildProgress.setVisible(true);
		workLabel.setVisible(false);
		upgradeGroup.setVisible(false);
	}

	private void buildFinished() {
		building.upgrade();

		buildProgress.setVisible(false);

		workLabel.setText(building.work() + "");
		workLabel.setX(levelLabel.getX() - workLabel.getWidth());
		workLabel.setY((int) (levelLabel.getHeight() - workLabel.getHeight()) / 2 - 1);
		workLabel.setVisible(true);

		levelLabel.setText(building.getLevel() + "");
		levelLabel.setVisible(true);

		if (building.getLevel() == building.getType().getLevelMax()) {
			upgradeGroup.setText("MAX");

			if (building.getType() == BuildingType.CASTLE)
				Achievement.KING.step();
		} else {
			int cost = building.getType().getCost(building.getLevel());
			upgradeGroup.setText(KingsLand.toReadable(cost));
		}
		upgradeGroup.setVisible(true);

		updateUI();
	}

	public boolean isBuilding() {
		return !building.isBuilt();
	}

	public float work(float delta) {
		if (!building.isBuilt()) {
			float progress = building.getAdvancement();
			if (building.build(delta))
				buildFinished();
			buildProgress.setProgress(building.getAdvancement());
			return building.getAdvancement() - progress;
		}
		float sum = building.work() * delta;
		domain.produce(sum);
		return sum;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		work(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		building.draw(batch);
		super.draw(batch, parentAlpha);
	}
}

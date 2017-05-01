package fr.grimoire.kingsland.ui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import fr.grimoire.kingsland.KingsLand;
import fr.grimoire.kingsland.domain.building.BuildingType;

/**
 * @author Radnap
 */
public class Toolbox extends Group {

	private Group buildingList;
	private int boxWidth;
	private int boxX;

	private int currentBuilding;
	private Label productionLabel;
	private EcusLabel costLabel;


	private static BuildingType selectedBuildingType = null;

	public static BuildingType getSelectedBuildingType() {
		return selectedBuildingType;
	}


	public Toolbox(int width, int height) {
		setSize(width, height);

		Image borders = new Image(KingsLand.skin, "toolbox");
		borders.setSize(width, height);
		addActor(borders);

		final Image arrowRight = new Image(KingsLand.skin, "arrow");
		arrowRight.setPosition(width - arrowRight.getPrefWidth(), 16.f);
		arrowRight.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (currentBuilding == BuildingType.values().length)
					return;
				scrollTo(currentBuilding + 1);
			}
		});
		addActor(arrowRight);
		Image arrowLeft = new Image(KingsLand.skin, "arrow");
		arrowLeft.setScaleX(-1.f);
		arrowLeft.setPosition(arrowLeft.getPrefWidth(), 16.f);
		arrowLeft.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (currentBuilding == 0)
					return;
				scrollTo(currentBuilding - 1);
			}
		});
		addActor(arrowLeft);

		Image box = new Image(KingsLand.skin, "selectBox");
		box.setHeight(height - 4);
		box.setWidth(box.getHeight());
		boxWidth = (int) box.getWidth();
		boxX = (width - boxWidth) / 2;
		box.setPosition(boxX, 2);
		addActor(box);

		buildingList = new Group();
		BuildingType[] buildingTypes = BuildingType.values();
		for (int i = 0; i < buildingTypes.length; i++) {
			Sprite sprite = KingsLand.atlas.createSprite(buildingTypes[i].toString());
			sprite.setColor(KingsLand.primary);
			Image image = new Image(new SpriteDrawable(sprite));
			image.setX(i * boxWidth + (int) (boxWidth - sprite.getWidth()) / 2);
			image.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Actor listenerActor = event.getListenerActor();
					scrollTo(buildingList.getChildren().indexOf(listenerActor, true));
				}
			});
			buildingList.addActor(image);
		}
		buildingList.setY((height - 32) / 2 - 2);
		addActor(buildingList);

		productionLabel = new Label("0", KingsLand.skin, "small");
		productionLabel.setAlignment(Align.left);
		productionLabel.setWidth(boxWidth / 2 - 4);
		productionLabel.setX(boxX + 2);
		productionLabel.setY(box.getY() + box.getHeight() - productionLabel.getPrefHeight());
		addActor(productionLabel);

		costLabel = new EcusLabel("0", true);
		costLabel.setWidth(boxWidth / 2 - 2);
		costLabel.setY(box.getY() + box.getHeight() - costLabel.getPrefHeight());
		addActor(costLabel);

		currentBuilding = 0;
		scrollTo(1);
	}

	private void scrollTo(int buildingN) {
		currentBuilding = buildingN;
		selectedBuildingType = BuildingType.values()[currentBuilding];
		productionLabel.setText("+" + selectedBuildingType.getWorkValue());
		int cost = selectedBuildingType.getCost(0);
		costLabel.setText(KingsLand.toReadable(cost));
		costLabel.setX(boxX + boxWidth - costLabel.getPrefWidth() - 1);
		buildingList.setX(boxX - buildingN * boxWidth);
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		return super.hit(x, y, touchable);
	}
}

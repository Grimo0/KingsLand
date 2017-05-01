package fr.grimoire.kingsland.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import fr.grimoire.kingsland.KingsLand;

/**
 * @author Radnap
 */
public class LevelLabel extends Group {

	private Label label;


	public LevelLabel() {
		Image circleBack = new Image(KingsLand.skin, "levelBack");
		addActor(circleBack);
		setSize(circleBack.getWidth(), circleBack.getHeight());

		Image circle = new Image(KingsLand.skin, "level");
		circle.setPosition(1.f, 1.f);
		addActor(circle);

		label = new Label("0", KingsLand.skin, "small");
		label.setWidth(circleBack.getWidth());
		label.setPosition(0, (int) (circleBack.getHeight() - label.getHeight()) / 2);
		label.setAlignment(Align.center);
		addActor(label);
	}

	public void setText(CharSequence sequence) {
		label.setText(sequence);
	}
}

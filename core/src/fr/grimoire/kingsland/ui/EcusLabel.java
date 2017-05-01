package fr.grimoire.kingsland.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import fr.grimoire.kingsland.KingsLand;

/**
 * @author Radnap
 */
public class EcusLabel extends Group {

	private Label sum;
	private Image ecuImage;


	public EcusLabel(CharSequence s) {
		this(s, false);
	}

	public EcusLabel(CharSequence s, boolean small) {
		if (small) {
			ecuImage = new Image(KingsLand.skin, "ecuSmall");
			sum = new Label(s, KingsLand.skin, "small");
		} else {
			ecuImage = new Image(KingsLand.skin, "ecu");
			sum = new Label(s, KingsLand.skin, "big");
		}

		sum.setAlignment(Align.left);
		sum.setX(ecuImage.getPrefWidth() + 1.f);

		ecuImage.setY((int)(sum.getPrefHeight() - ecuImage.getPrefHeight() + 0.5f) / 2);

		addActor(ecuImage);
		addActor(sum);
	}

	@Override
	public void setWidth(float width) {
		sum.setWidth(width - sum.getX());
	}

	public float getPrefWidth() {
		return sum.getX() + sum.getPrefWidth();
	}

	public float getPrefHeight() {
		return Math.max(ecuImage.getHeight(), sum.getPrefHeight());
	}

	public void setText(String s) {
		sum.setText(s);
	}
}

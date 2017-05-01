package fr.grimoire.kingsland.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import fr.grimoire.kingsland.KingsLand;

/**
 * @author Radnap
 */
public class FramedImage extends Group {

	private Image bg;
	private Image image;

	public FramedImage(String imageName, String bgName) {
		bg = new Image(KingsLand.skin, bgName);
		addActor(bg);

		image = new Image(KingsLand.skin, imageName);
		addActor(image);

		setSize(bg.getDrawable().getMinWidth() + image.getWidth(), bg.getDrawable().getMinHeight() + image.getHeight());
	}

	public void setImage(String name) {
		image.setDrawable(KingsLand.skin.getDrawable(name));
	}

	public void setBackground(String name) {
		bg.setDrawable(KingsLand.skin.getDrawable(name));
	}

	public void setColor(Color color) {
		SpriteDrawable drawable = (SpriteDrawable) image.getDrawable();
		drawable.getSprite().setColor(color);
	}

	@Override
	protected void sizeChanged() {
		super.sizeChanged();

		bg.setSize(getWidth(), getHeight());
		image.setX((int) ((bg.getWidth() - image.getWidth()) / 2.f));
		image.setY((int) ((bg.getHeight() - image.getHeight()) / 2.f));
	}
}

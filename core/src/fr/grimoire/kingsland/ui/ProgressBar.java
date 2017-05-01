package fr.grimoire.kingsland.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import fr.grimoire.kingsland.KingsLand;

/**
 * @author Radnap
 */
public class ProgressBar extends Group {

	private Image back;
	private Image bar;
	private Image barProgress;
	private float progress;

	public ProgressBar() {
		back = new Image(KingsLand.skin, "barBack");
		addActor(back);
		bar = new Image(KingsLand.skin, "bar");
		bar.setY(1);
		addActor(bar);
		barProgress = new Image(KingsLand.skin, "barProgress");
		barProgress.setPosition(bar.getX() + 2, bar.getY() + 2);
		addActor(barProgress);

		setHeight(back.getHeight());
	}

	public void setProgress(float progress) {
		if (progress < 0)
			progress = 0.f;
		else if (progress > 1.f)
			progress = 1.f;
		this.progress = progress;
		barProgress.setWidth(progress * (bar.getWidth() - 4 - barProgress.getDrawable().getMinWidth()) + barProgress.getDrawable().getMinWidth());
	}

	@Override
	protected void sizeChanged() {
		super.sizeChanged();
		back.setSize(getWidth(), getHeight());
		bar.setSize(back.getWidth(), back.getHeight() - 1);
		barProgress.setWidth(progress * (bar.getWidth() - 4 - barProgress.getDrawable().getMinWidth()) + barProgress.getDrawable().getMinWidth());
		barProgress.setHeight(bar.getHeight() - 4);
	}
}

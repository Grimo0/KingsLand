package fr.grimoire.kingsland.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fr.grimoire.kingsland.KingsLand;

/**
 * @author Radnap
 */
public class InvertedLabel extends Group {

	private Image box;
	private Label label;
	private Image icon;
	private boolean clickable;

	public InvertedLabel(String iconDrawableName, boolean clickable) {
		this(clickable);

		icon = new Image(KingsLand.skin, iconDrawableName);
		icon.setPosition(1.f, (int) (box.getHeight() - icon.getHeight()) / 2 + (clickable ? 1 : 0));
		addActor(icon);

		label.setX(icon.getX() + icon.getWidth() + 1);
		setHeight(label.getPrefHeight());
	}

	public InvertedLabel(final boolean clickable) {
		this.clickable = clickable;

		if (clickable)
			box = new Image(KingsLand.skin, "boxButton");
		else
			box = new Image(KingsLand.skin, "box");
		box.setSize(getWidth(), getHeight());
		addActor(box);

		label = new Label(" ", KingsLand.skin, "smallSecondary");
		label.setPosition(2, (int) (box.getHeight() - label.getHeight()) / 2 + (clickable ? 1 : 0));
		addActor(label);

		addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (!clickable)
					return super.touchDown(event, x, y, pointer, button);
				box.setDrawable(KingsLand.skin.getDrawable("box"));
				box.sizeBy(0.f, -2.f);
				box.moveBy(0.f, 1.f);
				label.moveBy(0.f, -1.f);
				icon.moveBy(0.f, -1.f);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (!clickable)
					return;
				box.setDrawable(KingsLand.skin.getDrawable("boxButton"));
				box.sizeBy(0.f, 2.f);
				box.moveBy(0.f, -1.f);
				label.moveBy(0.f, 1.f);
				icon.moveBy(0.f, 1.f);
			}
		});
	}

	public void setText(CharSequence sequence) {
		label.setText(sequence);
		setSize(label.getX() + label.getPrefWidth() + 1, label.getPrefHeight() - (clickable ? 0 : 2));
	}

	@Override
	protected void sizeChanged() {
		super.sizeChanged();
		box.setSize(getWidth(), getHeight());
		label.setHeight(getHeight());
		label.setY((int) (box.getHeight() - label.getHeight()) / 2 + (clickable ? 1 : 0));
		if (icon != null)
			icon.setY((int) (box.getHeight() - icon.getHeight()) / 2 + (clickable ? 1 : 0));
	}
}

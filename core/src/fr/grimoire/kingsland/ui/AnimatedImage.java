package fr.grimoire.kingsland.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

/**
 * @author Radnap
 */
public class AnimatedImage extends Image {

	final Drawable[] keyFrames;
	private float frameDuration;
	private float animationDuration;
	private boolean looping;

	private float stateTime;


	public AnimatedImage(float frameDuration, Array<? extends Drawable> keyFrames) {
		this.frameDuration = frameDuration;
		this.animationDuration = keyFrames.size * frameDuration;
		this.keyFrames = new Drawable[keyFrames.size];
		for (int i = 0, n = keyFrames.size; i < n; i++) {
			this.keyFrames[i] = keyFrames.get(i);
		}
		setSize(this.keyFrames[0].getMinWidth(), this.keyFrames[0].getMinHeight());
		looping = false;
	}

	public AnimatedImage(float frameDuration, Drawable... keyFrames) {
		this.frameDuration = frameDuration;
		this.animationDuration = keyFrames.length * frameDuration;
		this.keyFrames = keyFrames;
		setSize(this.keyFrames[0].getMinWidth(), this.keyFrames[0].getMinHeight());
		looping = false;
	}

	public float getFrameDuration() {
		return frameDuration;
	}

	public void setFrameDuration(float frameDuration) {
		this.frameDuration = frameDuration;
	}

	public boolean isLooping() {
		return looping;
	}

	public void setLooping(boolean looping) {
		this.looping = looping;
	}

	public void restart() {
		stateTime = 0.f;
	}

	public void stop() {
		stateTime = animationDuration;
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		stateTime += delta;
		if (stateTime >= animationDuration) {
			setDrawable(null);
			return;
		}

		int frameNumber = (int) (stateTime / frameDuration);
		if (looping)
			frameNumber = frameNumber % keyFrames.length;
		setDrawable(keyFrames[frameNumber]);
	}
}

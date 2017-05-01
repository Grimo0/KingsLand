package fr.grimoire.kingsland;

import fr.grimoire.kingsland.ui.AchievementBox;

/**
 * @author Radnap
 */
public enum Achievement {
	WORKER("Worker", 150),
	BUILDER("Builder", 50),
	SAVER("Saver", 100),
	KING("King", 1),
	CRAFTSMAN("Craftsman", 3),
	ARCHITECT("Architect", 5);

	private static AchievementBox achievementBox;

	public static void init(AchievementBox achievementBox) {
		Achievement.achievementBox = achievementBox;
	}

	private String description;
	private float threshold;
	private float counter;
	private boolean unlocked = false;


	Achievement(String description, int threshold) {
		this.description = description;
		this.threshold = threshold;
		counter = 0;
	}

	public void step() {
		step(1);
	}

	public void step(float n) {
		if (unlocked)
			return;
		counter += n;
		if (counter >= threshold)
			unlock();
	}

	public void unstep() {
		unstep(1);
	}

	public void unstep(float n) {
		if (unlocked)
			return;
		counter -= n;
	}

	public void unlock() {
		unlocked = true;
		achievementBox.unlock(this);
	}

	public boolean isUnlocked() {
		return unlocked;
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}

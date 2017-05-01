package fr.grimoire.kingsland.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import fr.grimoire.kingsland.Achievement;
import fr.grimoire.kingsland.KingsLand;

/**
 * @author Radnap
 */
public class AchievementBox extends Group {

	private ObjectMap<Achievement, FramedImage> achievementsImages;
	private InvertedLabel description;


	public AchievementBox() {
		achievementsImages = new ObjectMap<Achievement, FramedImage>();

		Achievement[] achievements = Achievement.values();
		float achievementOffset = 2.f;
		for (int i = 0; i < achievements.length; i++) {
			final Achievement achievement = achievements[i];
			final FramedImage image = new FramedImage("unknownAchievement", "achievementBg");
			image.setX(getWidth());
			image.addListener(new ClickListener() {
				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					if (achievement.isUnlocked())
						description.setText(achievement.toString());
					else
						description.setText("???");
					description.setX(image.getX() + (image.getWidth() - description.getWidth()) / 2);
					description.setY(image.getY() - description.getHeight() - 2);
					description.setVisible(true);
				}

				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					description.setVisible(false);
				}
			});

			setHeight(image.getHeight());
			if (i != achievements.length - 1)
				setWidth(image.getX() + image.getWidth() + achievementOffset);
			else
				setWidth(image.getX() + image.getWidth());
			addActor(image);
			achievementsImages.put(achievement, image);
		}

		description = new InvertedLabel(false);
		description.setVisible(false);
		addActor(description);
	}

	public void unlock(Achievement achievement) {
		FramedImage framedImage = achievementsImages.get(achievement);
		framedImage.setImage(achievement.toString());
		framedImage.setBackground("achievementUnlockedBg");
		framedImage.setColor(KingsLand.secondary);
	}
}

package fr.grimoire.kingsland.domain;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import fr.grimoire.kingsland.Achievement;
import fr.grimoire.kingsland.KingsLand;
import fr.grimoire.kingsland.domain.building.BuildingType;
import fr.grimoire.kingsland.ui.AnimatedImage;
import fr.grimoire.kingsland.ui.InvertedLabel;

/**
 * @author Radnap
 */
public class Domain extends Group {

	private float ecus;
	private int nbVillagers;
	private int nbPlacements;
	private Array<Placement> placements;

	private AnimatedImage pressAnim;
	private Array<InvertedLabel> producedAmounts;
	private Array<InvertedLabel> buildAmounts;
	private int lastProducedLabel;
	private int lastBuildLabel;
	private Image selectionMark;
	private int selectPlacement;


	public Domain() {
		ecus = 5900;
		nbVillagers = 20;
		nbPlacements = 8;

		pressAnim = new AnimatedImage(0.02f, KingsLand.skin.get("pressAnim", Drawable[].class));
		pressAnim.stop();
		addActor(pressAnim);

		placements = new Array<Placement>(nbPlacements);
		float offset = 16.f;
		float size = 64.f;
		for (int i = 0; i < nbPlacements; i++) {
			final Placement placement = new Placement(this);
			placement.setSize(size, size + 10.f);
			placement.setX((i % 4) * (size + offset));
			placement.setY((i / 4) * (size + offset + 2.f));
			placement.addListener(new ClickListener() {
				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					int placementN = placements.indexOf((Placement) event.getListenerActor(), true);
					if (placementN != selectPlacement)
						select(placementN);
				}

				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (event.getTarget() == event.getListenerActor())
						press();
				}
			});
			placements.add(placement);
			addActor(placement);
		}
		placements.get(0).construct(BuildingType.CASTLE);
		Achievement.SAVER.step(BuildingType.CASTLE.getCost(0));

		producedAmounts = new Array<InvertedLabel>(5);
		buildAmounts = new Array<InvertedLabel>(5);
		for (int i = 0; i < 5; i++) {
			InvertedLabel value = new InvertedLabel("ecuIncrease", false);
			producedAmounts.add(value);
			value.setVisible(false);
			addActor(value);
			value = new InvertedLabel("buildIncrease", false);
			buildAmounts.add(value);
			value.setVisible(false);
			addActor(value);
		}
		lastProducedLabel = -1;
		lastBuildLabel = -1;

		selectionMark = new Image(KingsLand.skin, "select");
		addActor(selectionMark);

		select(0);
	}

	private void select(int placementN) {
		if (placementN == -1) return;
		selectPlacement = placementN;
		Placement placement = placements.get(selectPlacement);

		pressAnim.setX(placement.getX() + (int) (placement.getWidth() - pressAnim.getWidth()) / 2);
		pressAnim.setY(placement.getY() + 9.f);

		selectionMark.setX(placement.getX() + (int) (placement.getWidth() - selectionMark.getWidth()) / 2);
		selectionMark.setY(placement.getY() - selectionMark.getHeight() + 5);
	}

	private void press() {
		Placement placement = placements.get(selectPlacement);
		if (placement.getBuilding().getType() == BuildingType.EMPTY)
			return;

		boolean isBuilding = placement.isBuilding();
		float progress = placement.work(isBuilding ? 0.5f : 1.f);
		InvertedLabel amount;
		if (isBuilding) {
			pressAnim.restart();
			amount = buildAmounts.get(++lastBuildLabel % buildAmounts.size);
			amount.setText("");
			Achievement.BUILDER.step();
			Sound sound = KingsLand.skin.get("work.wav", Sound.class);
			long id = sound.play();
			sound.setPitch(id, 0.7f + (float) Math.random() * 0.6f);
			sound.setPan(id, (float) Math.random() * 0.6f - 0.3f, 0.08f * KingsLand.soundLevel);
		} else {
			pressAnim.restart();
			amount = producedAmounts.get(++lastProducedLabel % producedAmounts.size);
			amount.setText("" + progress);
			Achievement.WORKER.step();
			Sound sound = KingsLand.skin.get("produce.wav", Sound.class);
			long id = sound.play();
			sound.setPitch(id, 0.7f + (float) Math.random() * 0.6f);
			sound.setPan(id, (float) Math.random() * 0.6f - 0.3f, 0.04f * KingsLand.soundLevel);
		}
		amount.setX(placement.getX() + (int) (Math.random() * (placement.getWidth() - amount.getWidth())));
		amount.setY(placement.getY() + placement.getHeight() - (float) Math.random() * 7.f - 8.f);
		amount.addAction(Actions.sequence(
				Actions.moveTo(amount.getX(), placement.getY() + placement.getHeight() - (float) Math.random() * 5.f, 0.5f),
				Actions.visible(false)));
		amount.setVisible(true);
	}

	public int getEcus() {
		return (int) ecus;
	}

	public int getNbVillagers() {
		return nbVillagers;
	}

	public int getNbPlacements() {
		return nbPlacements;
	}

	public void produce(float sum) {
		Achievement.SAVER.step(sum);
		ecus += sum;
	}

	public boolean pay(int sum) {
		if (ecus < sum)
			return false;
		Achievement.SAVER.unstep(sum);
		ecus -= sum;
		return true;
	}
}

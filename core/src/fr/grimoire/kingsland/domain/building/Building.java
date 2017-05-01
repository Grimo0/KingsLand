package fr.grimoire.kingsland.domain.building;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import fr.grimoire.kingsland.KingsLand;

/**
 * @author Radnap
 */
public class Building {

	private final BuildingType type;
	protected float advancement;
	protected int level;
	protected float workValue;
	protected Sprite sprite;


	public Building(BuildingType type) {
		this.type = type;
		advancement = 0.f;
		level = 0;
		workValue = type.getWorkValue();
		sprite = KingsLand.atlas.createSprite(type.toString());
		sprite.setColor(KingsLand.primary);
	}

	public BuildingType getType() {
		return type;
	}

	public float getAdvancement() {
		return advancement;
	}

	public boolean isBuilt() {
		return advancement >= 1.f;
	}

	public int getLevel() {
		return level;
	}

	public void setPosition(float x, float y) {
		sprite.setPosition(x, y);
	}

	public float getX() {
		return sprite.getX();
	}

	public float getY() {
		return sprite.getY();
	}

	public float getWidth() {
		return sprite.getWidth();
	}

	public float getHeight() {
		return sprite.getHeight();
	}

	public void setSize(float width, float height) {
		if (width / height < sprite.getRegionWidth() / sprite.getRegionHeight())
			sprite.setSize(width, width * sprite.getRegionHeight() / sprite.getRegionWidth());
		else
			sprite.setSize(height * sprite.getRegionWidth() / sprite.getRegionHeight(), height);
	}

	public void startBuilding() {
		advancement = 0.f;
	}

	public boolean build(float delta) {
		advancement += delta * 0.1f / (level + 1);
		return advancement >= 1.f;
	}

	public float work() {
		return workValue;
	}

	public void draw(Batch batch) {
		sprite.draw(batch);
	}

	public void upgrade() {
		level++;
		workValue *= level;
		sprite.setRegion(KingsLand.atlas.findRegion(type.toString(), level));
	}
}

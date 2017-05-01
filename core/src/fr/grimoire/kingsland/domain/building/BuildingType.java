package fr.grimoire.kingsland.domain.building;

/**
 * @author Radnap
 */
public enum BuildingType {
	EMPTY(0.f, 100),
	SMALL_TOWN(2f, 1000, 500, 2500, 10000, 50000, 500000),
	CASTLE(5f, 5000, 1000, 5000, 20000, 100000, 1000000);

	private float workValue;
	private int[] costs;

	BuildingType(float workValue, int... costs) {
		this.workValue = workValue;
		this.costs = costs;
	}

	public float getWorkValue() {
		return workValue;
	}

	public int getLevelMax() {
		return costs.length - 1;
	}

	public int getCost(int level) {
		if (level < 0 || level >= costs.length)
			return -1;
		return costs[level];
	}

	public Building build() {
		if (this != EMPTY)
			return new Building(this);
		Building empty = new Building(EMPTY);
		empty.advancement = 1.f;
		return empty;
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}

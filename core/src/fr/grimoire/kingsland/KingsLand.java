package fr.grimoire.kingsland;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import fr.grimoire.kingsland.domain.building.Building;
import fr.grimoire.kingsland.domain.building.BuildingType;

public class KingsLand extends Game {

	public final static boolean DEBUG = false;

	public static Logger logger;
	public static AssetManager assetManager;

	public static Color primary = new Color(0x9bbc0fff);
	public static Color secondary = new Color(0x0f380fff);

	public static FreeTypeFontGenerator bigFontGenerator;
	public static FreeTypeFontGenerator smallFontGenerator;

	public static TextureAtlas atlas;
	public static Skin skin;

	public static Building emptyBuilding;
	public static float soundLevel = 1.f;

	private float width;
	private float height;

	private SpriteBatch batch;
	private FrameBuffer frameBuffer;

	private GameScreen gameScreen;


	@Override
	public void create() {
		logger = new Logger("King's Land", Logger.DEBUG);

		assetManager = new AssetManager();
		assetManager.setLogger(logger);

		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(new InternalFileHandleResolver()));
		assetManager.load("OldWizard.ttf", FreeTypeFontGenerator.class);
		assetManager.load("434.ttf", FreeTypeFontGenerator.class);
		assetManager.load("mainPack.atlas", TextureAtlas.class);
		assetManager.load("build.wav", Sound.class);
		assetManager.load("produce.wav", Sound.class);
		assetManager.load("work.wav", Sound.class);
		assetManager.finishLoading();

		//---- Populate skin
		skin = new Skin();

		atlas = assetManager.get("mainPack.atlas", TextureAtlas.class);
		add9ToSkin("achievementBg", primary);
		add9ToSkin("achievementUnlockedBg", primary);
		add9ToSkin("bar", primary);
		add9ToSkin("barBack", secondary);
		add9ToSkin("barProgress", primary);
		add9ToSkin("box", primary);
		add9ToSkin("boxButton", primary);
		add9ToSkin("selectBox", primary);
		add9ToSkin("toolbox", primary);

		addToSkin("arrow", primary);
		addToSkin("buildIncrease", secondary);
		addToSkin("ecu", primary);
		addToSkin("ecuSmall", primary);
		addToSkin("ecuIncrease", secondary);
		addToSkin("hammer", primary);
		addToSkin("hammerBack", secondary);
		addToSkin("level", primary);
		addToSkin("levelBack", secondary);
		addToSkin("select", primary);
		addToSkin("upArrow", secondary);
		addToSkin("unknownAchievement", primary);

		for (Achievement achievement : Achievement.values()) {
			addToSkin(achievement.toString(), primary);
		}

		addAnimToSkin("pressAnim", primary);

		addSoundToSkin("build.wav");
		addSoundToSkin("produce.wav");
		addSoundToSkin("work.wav");

		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.mono = true;
		parameter.size = 16;
		parameter.minFilter = Texture.TextureFilter.Nearest;
		parameter.magFilter = Texture.TextureFilter.Nearest;
		bigFontGenerator = assetManager.get("OldWizard.ttf", FreeTypeFontGenerator.class);
		skin.add("big", new Label.LabelStyle(bigFontGenerator.generateFont(parameter), primary));
		parameter.size = 7;
		smallFontGenerator = assetManager.get("434.ttf", FreeTypeFontGenerator.class);
		skin.add("small", new Label.LabelStyle(smallFontGenerator.generateFont(parameter), primary));
		skin.add("smallSecondary", new Label.LabelStyle(smallFontGenerator.generateFont(parameter), secondary));

		emptyBuilding = BuildingType.EMPTY.build();

		batch = new SpriteBatch();
		frameBuffer = new FrameBuffer(Pixmap.Format.RGB888, 320, 240, false);
		frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		gameScreen = new GameScreen(frameBuffer.getWidth(), frameBuffer.getHeight());
		setScreen(gameScreen);

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
	}

	private void addToSkin(String name, Color color) {
		Sprite sprite = atlas.createSprite(name);
		sprite.setColor(color);
		skin.add(name, new SpriteDrawable(sprite), Drawable.class);
	}

	private void addAnimToSkin(String name, Color color) {
		Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(name);
		SpriteDrawable[] drawables = new SpriteDrawable[regions.size];
		for (int i = 0; i < regions.size; i++) {
			Sprite sprite = new Sprite(regions.get(i));
			sprite.setColor(color);
			drawables[i] = new SpriteDrawable(sprite);
		}
		skin.add(name, drawables, Drawable[].class);
	}

	private void add9ToSkin(String name, Color color) {
		NinePatch ninePatch = atlas.createPatch(name);
		ninePatch.setColor(color);
		skin.add(name, new NinePatchDrawable(ninePatch), Drawable.class);
	}

	private void addSoundToSkin(String name) {
		Sound sound = assetManager.get(name, Sound.class);
		skin.add(name, sound, Sound.class);
	}

	@Override
	public void render() {
		frameBuffer.begin();
		super.render();
		frameBuffer.end();

		batch.begin();
		batch.draw(frameBuffer.getColorBufferTexture(), 0.f, height, width, -height);
		batch.end();

		if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.Q)))
			Gdx.app.exit();
		else if (KingsLand.DEBUG && Gdx.input.isKeyJustPressed(Input.Keys.F2))
			Gdx.graphics.setWindowedMode(
					Gdx.graphics.getWidth() == 640 ? 1280 : 640,
					Gdx.graphics.getHeight() == 480 ? 960 : 480);
	}

	@Override
	public void dispose() {
		batch.dispose();
		gameScreen.dispose();
		assetManager.dispose();
	}

	public static String toReadable(int n) {
		if (n >= 1000000)
			return n / 1000000.f + "M";
		else if (n >= 1000)
			return n / 1000.f + "K";
		else
			return n + "";
	}
}

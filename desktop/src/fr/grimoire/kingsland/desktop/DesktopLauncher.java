package fr.grimoire.kingsland.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import fr.grimoire.kingsland.KingsLand;

public class DesktopLauncher {
	public static void main (String[] arg) {

		if (arg.length != 0 && arg[0].equals("-p")) {
			FileHandle fileHandle = new FileHandle("../../_assets");
			if (fileHandle.isDirectory()) {
				TexturePacker.process(arg[1], ".", "mainPack"); // "../../_assets"
			}
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 480;
		new LwjglApplication(new KingsLand(), config);
	}
}

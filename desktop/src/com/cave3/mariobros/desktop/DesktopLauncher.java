package com.cave3.mariobros.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cave3.mariobros.MarioBros;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        //config.width = MarioBros.V_WIDTH;
		config.width = 1200;
        //config.height = MarioBros.V_HEIGHT;
		config.height = 624;
        config.title = MarioBros.TITLE;
		new LwjglApplication(new MarioBros(), config);
	}
}

package es.carlosrolindez.ping.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import es.carlosrolindez.ping.PingGame;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height=540;
		config.width=860;
		config.title = "Ping";

		new LwjglApplication(new PingGame(), config);
	}
}

package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		showSplashScreen();


		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Gameverse Showdown");
		config.setMaximized(true);

		new Lwjgl3Application(new Main(), config);
	}

	private static void showSplashScreen() {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.disableAudio(true);
		config.setDecorated(false);
		config.setResizable(false);
		config.setTransparentFramebuffer(true);

		new Lwjgl3Application(new ApplicationAdapter() {
			private Batch batch;
			private Texture texture;
			private float time;
			@Override
			public void create() {
				texture = new Texture(Gdx.files.classpath("Img/Splash/Gameverse_Showdown_Splash.png"));
				batch = new SpriteBatch();
			}
			@Override
			public void render() {

				time += Gdx.graphics.getDeltaTime();

				Gdx.gl.glClearColor(0, 0, 0, 0);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				batch.getProjectionMatrix().setToOrtho2D(0, 0, 1, 1);
				batch.begin();
				batch.draw(texture, 0, 0, 1, 1);
				batch.end();

				if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || time > 2f){
					Gdx.app.exit();
				}
			}
			@Override
			public void dispose() {
				texture.dispose();
				batch.dispose();
			}
		}, config);
	}
}

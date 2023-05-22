package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
//width 1680 height 913
//width 3008 height 1639
public class MenuScreen extends GameState {

	private static final int START = 0, PREFERENCES = 1, EXIT = 2;//LAUNCHER
	public static final int YES = 0, NO = 1;
	private Boolean checkExit, checkMenu;
	private int positionMenu, positionExit;

	private Stage stage;
	private OrthographicCamera camera;
	public  FitViewport viewPort;
	private ButtonC[] btnMenu, btnExit;
	private Skin skinRed, skinDarkRed, skinGreen, skinDarkGreen;
    private Image backgroundMenu, backgroundExit,titleMenu, titleExit;

	MenuScreen(final Main game){
		super(game);
		setInputsDelay(0.15f);
		//System.out.println(Gdx.graphics.getWidth());
		camera = new OrthographicCamera();
		viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		stage = new Stage(viewPort);

		Gdx.input.setInputProcessor(stage);

		//skin menu screen

		skinRed = new Skin(Gdx.files.internal(GameConstants.SKIN_GLASSY_RED));
		skinDarkRed = new Skin(Gdx.files.internal(GameConstants.SKIN_GLASSY_DARKRED));

		//skin exit screen

		skinGreen = new Skin(Gdx.files.internal(GameConstants.SKIN_GLASSY_GREEN));
		skinDarkGreen = new Skin(Gdx.files.internal(GameConstants.SKIN_GLASSY_DARKGREEN));

		//title

		Texture textureTitleMenu = new Texture(GameConstants.IMAGE_TITLE);
		titleMenu = new Image(textureTitleMenu);

		titleMenu.setPosition((float) (viewPort.getWorldWidth() * 0.03), (float) (viewPort.getWorldHeight() * 0.77));

		//table menu screen

		Table tableMenu = new Table();

		btnMenu = new ButtonC[3];

		btnMenu[START] = new ButtonC("Start", skinRed, skinDarkRed);
		btnMenu[PREFERENCES] = new ButtonC("Preferences", skinRed, skinDarkRed);
		btnMenu[EXIT] = new ButtonC("Exit", skinRed, skinDarkRed);

		tableMenu.defaults().size((float) (GameConstants.screenWidth * 0.267), (float) (GameConstants.screenHeight * 0.142));
		tableMenu.setPosition((float) (GameConstants.screenWidth * 0.242), GameConstants.centerY);

		tableMenu.row().pad((float) (GameConstants.screenHeight * 0.127), 0, 0, 0);
		tableMenu.add(btnMenu[START]).fillX().uniform();
		tableMenu.row().pad((float) (GameConstants.screenHeight * 0.055), 0, (float) (GameConstants.screenHeight * 0.055), 0);
		tableMenu.add(btnMenu[PREFERENCES]).fillX().uniform();
		tableMenu.row();
		tableMenu.add(btnMenu[EXIT]).fillX().uniform();

		//title exit menu

		Texture textureTitleExit = new Texture(GameConstants.IMAGE_TITLE_EXIT);
		titleExit = new Image(textureTitleExit);

		titleExit.setPosition(-GameConstants.screenWidth * 0.830f - titleExit.getWidth()/2, GameConstants.screenHeight / 1.5f);


		//table exit screen

		Table tableExit = new Table();

		btnExit = new ButtonC[2];

		btnExit[YES] = new ButtonC("Yes", skinGreen, skinDarkGreen, "btn-bold");
		btnExit[NO] = new ButtonC("No", skinGreen, skinDarkGreen, "btn-bold");

		tableExit.defaults().size(GameConstants.screenWidth * 0.267f, GameConstants.screenHeight * 0.142f);
		tableExit.setPosition( -GameConstants.screenWidth * 0.831f, GameConstants.screenHeight / 2.9f);

		tableExit.row().pad((float) (GameConstants.screenHeight * 0.127), 0, 0, 0);
		tableExit.add(btnExit[YES]).pad(0,0,0,50);

		tableExit.add(btnExit[NO]).pad(0,0,0,0);
		tableExit.row();

		//background

		Texture textureMenu = new Texture(GameConstants.BACKGROUND_MENU);
		backgroundMenu = new Image(textureMenu);
		backgroundMenu.setFillParent(true);

		Texture textureExit = new Texture(GameConstants.BACKGROUND_EXIT);
		backgroundExit = new Image(textureExit);
		backgroundExit.setFillParent(true);
		backgroundExit.setPosition(-GameConstants.screenWidth, 0f);


		//variable for logic

		checkExit = false;
		checkMenu = true;

		btnMenu[START].isOver = true;
		
		positionMenu = 0;
		positionExit = 0;

		//add component to the stage

		stage.addActor(backgroundMenu);
		stage.addActor(backgroundExit);
		stage.addActor(titleMenu);
		stage.addActor(tableMenu);
		stage.addActor(titleExit);
		stage.addActor(tableExit);
	}


	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		super.render(delta);
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(checkExit && camera.position.x > -GameConstants.screenWidth + (GameConstants.screenWidth * 0.31f)){
			camera.position.set(camera.position.x - 30f, camera.position.y, camera.position.z);
			camera.update();
		} else if (checkMenu && camera.position.x < GameConstants.screenWidth/2) {
			camera.position.set(camera.position.x + 30f, camera.position.y, camera.position.z);
			camera.update();
		}
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewPort.update(width, height);
		backgroundMenu.setSize(viewPort.getWorldWidth(), viewPort.getWorldHeight());
		//camera.position.set(camera.viewportWidth/2, camera.viewportHeight / 2, 0);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}


	@Override
	public void c1_buttonDpadRight() {//d
		if(positionExit + 1 < btnExit.length && checkExit){
			btnExit[positionExit].isOver = false;
			positionExit++;
			btnExit[positionExit].isOver = true;
		}
	}
	public void c1_buttonDpadLeft() {//a
		if(positionExit - 1 >= 0 && checkExit){
			btnExit[positionExit].isOver = false;
			positionExit--;
			btnExit[positionExit].isOver = true;
		}
	}
	public void c1_buttonDpadUp() {//w
		if(positionMenu - 1 >= 0 && checkMenu){
			btnMenu[positionMenu].isOver = false;
			positionMenu--;
			btnMenu[positionMenu].isOver = true;
		}
	}
	public void c1_buttonDpadDown() {//s
		if(positionMenu + 1 < btnMenu.length && checkMenu){
			btnMenu[positionMenu].isOver = false;
			positionMenu++;
			btnMenu[positionMenu].isOver = true;
		}
	}
	public void c1_buttonA() {//k
		//System.out.println("buttonA");
		if(btnMenu[START].isOver){
			changeScreen(GameState.CHOOSE_CHARACTERS);
		}else if(btnMenu[PREFERENCES].isOver){
			changeScreen(GameState.PREFERENCES);
		}else if(btnMenu[EXIT].isOver){
			checkExit = true;
			btnMenu[positionMenu].isOver = false;
			btnExit[positionExit].isOver = true;
			checkMenu = false;
		}else if(btnExit[YES].isOver){
			Gdx.app.exit();
		}else if(btnExit[NO].isOver){
			checkExit = false;
			checkMenu = true;
			btnMenu[positionMenu].isOver = true;
			btnExit[positionExit].isOver = false;
		}
	}
	public void c1_buttonB() {//L == o
		//System.out.println("buttonB");
		if(checkExit){
			checkExit = false;
			btnMenu[positionMenu].isOver = true;
			btnExit[positionExit].isOver = false;
			checkMenu = true;
		}
	}
	public void c1_buttonBack() {
		//System.out.println("buttonBack");
	}
	public void c1_buttonY() {
		//System.out.println("buttonY");
	}
	public void c1_buttonX() {//J
		//System.out.println("buttonX");
	}
	public void c1_buttonStart() {
		//System.out.println("buttonStart");
	}
	public void c1_buttonL1() {
		//System.out.println("buttonL1");
	}
	public void c1_buttonR1() {
		//System.out.println("buttonR1");
	}
	public void c1_buttonLeftStick() {
		//System.out.println("buttonLStick");
	}
	public void c1_buttonRightStick() {
		//System.out.println("buttonRStick");
	}
	public void c1_axisLeftX(float val) {
		if (val > 0) {
			c1_buttonDpadRight();
		} else {
			c1_buttonDpadLeft();
		}

	}
	public void c1_axisRightX(float val) {
		//System.out.println("axisRightX");
	}
	public void c1_axisLeftY(float val) {
		if (val > 0) {
			c1_buttonDpadDown();
		} else {
			c1_buttonDpadUp();
		}

	}
	public void c1_axisRightY(float val) {
		//System.out.println("axisRightY");
	}


	public void c2_buttonDpadRight() {

	}
	public void c2_buttonDpadLeft() {

	}
	public void c2_buttonDpadUp() {

	}
	public void c2_buttonDpadDown() {

	}
	public void c2_buttonA() {

	}
	public void c2_buttonB() {

	}
	public void c2_buttonBack() {

	}
	public void c2_buttonY() {

	}
	public void c2_buttonX() {

	}
	public void c2_buttonStart() {

	}
	public void c2_buttonL1() {

	}
	public void c2_buttonR1() {

	}
	public void c2_buttonLeftStick() {

	}
	public void c2_buttonRightStick() {

	}
	public void c2_axisLeftX(float val) {

	}
	public void c2_axisRightX(float val) {

	}
	public void c2_axisLeftY(float val) {

	}
	public void c2_axisRightY(float val) {

	}
}















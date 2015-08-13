package com.tnk.clone1010;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class MyGdxGame extends ApplicationAdapter {

	public static final int VIEW_WIDTH = 480;
	public static final int VIEW_HEIGHT = 800;

	public static final int MARGIN_LEFT = 30;
	public static final int MARGIN_UPPER = 150;

    public static final int SMALL_BLOCK = 28;
    public static final int BLOCK_SIZE = 42;
    public static final int NEXT_CHUNK_NUMBER = 3;
    public static final int HEIGHT_MENU_BAR = 200;
    public static final int SIZE_RESUME_BUTTON = 100;

	private OrthographicCamera camera; // upside down
	private OrthographicCamera uiCamera;
	private OrthographicCamera menuCamera;

	private SpriteBatch batch;
    private int score;

	private Chunk next[];
	private Chunk heldChunk;
    private Texture[] blockImages;
    private Texture resumeButton;
    private GameText text;

    private Board board;
    private Vector3 touchPosition;
    private int heldChunkPosition;

    private GameState gameState;
    private Sound slidingSound;
    private Sound deleteSound;


    @Override
	public void create () {
        initResources();
        resetGame();
    }

    private void initResources(){
        batch = new SpriteBatch();

        blockImages = new Texture[Block.VARIATION_COLOR + 1];
        blockImages[Block.COLOR_NONE] = new Texture("grey.png");
        blockImages[Block.COLOR_RED] = new Texture("red.png");
        blockImages[Block.COLOR_BLUE] = new Texture("blue.png");
        blockImages[Block.COLOR_PURPLE] = new Texture("purple.png");
        blockImages[Block.COLOR_YELLOW] = new Texture("yellow.png");
        blockImages[Block.COLOR_L_GREEN] = new Texture("l_green.png");
        blockImages[Block.COLOR_D_GREEN] = new Texture("d_green.png");
        blockImages[Block.COLOR_ORANGE] = new Texture("orange.png");
        blockImages[Block.COLOR_PINK] = new Texture("pink.png");
        blockImages[Block.COLOR_TEAL] = new Texture("teal.png");
        resumeButton = new Texture("reboot.png");

        slidingSound = Gdx.audio.newSound(Gdx.files.internal("Hole_Punch-Simon_Craggs-1910998415.mp3"));
        deleteSound = Gdx.audio.newSound(Gdx.files.internal("Pew_Pew-DKnight556-1379997159.mp3"));

        camera = new OrthographicCamera();
        camera.setToOrtho(true, VIEW_WIDTH, VIEW_HEIGHT);
        camera.position.set(VIEW_WIDTH / 2, VIEW_HEIGHT / 2, 0);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, VIEW_WIDTH, VIEW_HEIGHT);
        uiCamera.position.set(VIEW_WIDTH / 2, VIEW_HEIGHT / 2, 0);

        menuCamera = new OrthographicCamera();
        menuCamera.setToOrtho(false, VIEW_WIDTH, VIEW_HEIGHT);
        menuCamera.position.set(VIEW_WIDTH / 2, VIEW_HEIGHT / 2, 0);

        text = new GameText();
    }

    private void resetGame(){
        board = new Board(MARGIN_LEFT, MARGIN_UPPER);
        next = new Chunk[NEXT_CHUNK_NUMBER];
        score = 0;
        fillNext();
        gameState = GameState.READY;
    }

	@Override
	public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(gameState == GameState.PLAY || gameState == GameState.READY) {
            update();
            draw();
        }else if(gameState == GameState.OVER){
            // no update
            draw();
            updateMenu();
            drawMenu();
        }
	}

    private void updateMenu() {
        // user action
        if(Gdx.input.justTouched()){
            updatePosition();
            // Gdx.app.log("@@@@@", "[debug] x:" + touchPosition.x + ", y:" + touchPosition.y);
            if(VIEW_WIDTH / 2 - SIZE_RESUME_BUTTON / 2 <= touchPosition.x
                    && touchPosition.x <= VIEW_WIDTH / 2 + SIZE_RESUME_BUTTON / 2
                    && VIEW_HEIGHT / 2 - SIZE_RESUME_BUTTON / 2 <= touchPosition.y
                    && touchPosition.y <= VIEW_HEIGHT / 2 + SIZE_RESUME_BUTTON / 2
                    ){
                // within resume button
                resetGame();
                gameState = GameState.PLAY;
            }
        }
    }

    private void drawMenu() {
        menuCamera.update();

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(menuCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.valueOf("FFFFFF"));
        shapeRenderer.rect(0, (VIEW_HEIGHT - HEIGHT_MENU_BAR) / 2, VIEW_WIDTH, HEIGHT_MENU_BAR);
        shapeRenderer.end();

        batch.setProjectionMatrix(menuCamera.combined);
        batch.begin();
        batch.draw(
                resumeButton,
                VIEW_WIDTH / 2 - SIZE_RESUME_BUTTON/2,
                VIEW_HEIGHT / 2 - SIZE_RESUME_BUTTON/2,
                SIZE_RESUME_BUTTON,
                SIZE_RESUME_BUTTON);
        batch.end();
    }

    private void update(){
        // user action
        if(Gdx.input.justTouched()){
            updatePosition();

            //Gdx.app.log("@@@", "x: " + touchPosition.x + ", y:" + touchPosition.y);
            for(int i = 0; i < next.length; i++){
                if(next[i] != null && next[i].isContain(touchPosition)) {
                    heldChunk = next[i];
                    heldChunkPosition = i;
                    heldChunk.onTouch();
                    break;
                }
            }
        }

        if(heldChunk != null){
            if(Gdx.input.isTouched()) {
                // move
                updatePosition();
                heldChunk.setX((int)touchPosition.x);
                heldChunk.setY((int)touchPosition.y - heldChunk.getMarginFromFinger());
            }else{
                // release
                if(board.place(heldChunk)){

                    score += heldChunk.blockNumber();

                    if(board.hasLine()){
                        deleteSound.play();
                        score += board.eraseLine();
                    }

                    next[heldChunkPosition] = null;
                    heldChunk = null;
                    fillNextIfNecessary();
                }else{
                    heldChunk.released();
                    heldChunk.setX(nextXcoordAt(heldChunkPosition));
                    heldChunk.setY(nextYcoordAt(heldChunkPosition));
                    heldChunk = null;
                }
            }
        }

        if(!canPlaceAny()){
            gameState = GameState.OVER;
        }
    }

    private boolean canPlaceAny(){
        //Gdx.app.log("@@@ debug", "can place any");
        for(int i = 0; i < next.length; i++){
            //Gdx.app.log("@@@ debug", "chunk:" + next[i].toString() +", is placeable: " + board.isPlaceable(next[i]));
            if(next[i] != null && board.isPlaceable(next[i])){
                return true;
            }
        }

        return false;
    }

    private void draw(){

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        board.draw(batch, this);

        // next
        for(int i = 0; i < next.length; i++){
            if(next[i] != null) {
                next[i].draw(this, batch);
            }

        }
        //Gdx.app.log("@@@", "[mygdxgame] bottomY:" + board.bottomY());

        batch.end();

        uiCamera.update();
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        text.drawScore(batch, score);

        batch.end();
    }


    private void fillNextIfNecessary() {
        for(int i = 0; i < next.length; i++){
            if(next[i] != null) {
                return;
            }
        }

        fillNext();
    }

    private void fillNext(){
        slidingSound.play();
        for(int i = 0; i < NEXT_CHUNK_NUMBER; i++) {
            next[i] = new Chunk(nextXcoordAt(i), nextYcoordAt(i), SMALL_BLOCK);
        }
    }

    private int nextXcoordAt(int i){
        int division = 5;
        int x;
        if(i == 0){
            x = VIEW_WIDTH / division * (i + 1);
        }else if(i == 1){
            x = VIEW_WIDTH / division * division / 2;
        }else if(i == 2){
            x = VIEW_WIDTH / division * (division - 1);
        }else{
            x = VIEW_WIDTH / division * (i + 1);
        }
//        Gdx.app.log("@@@", "[nextXcoordAt] i:" + i +", x:" + x);

        return x;
    }

    private int nextYcoordAt(int i){
        return (VIEW_HEIGHT - board.bottomY()) / 2 + board.bottomY();
    }

    private void updatePosition(){
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        touchPosition = camera.unproject(new Vector3(x, y, 0));
    }

    public Texture getImage(int color){
        return blockImages[color];
    }
}

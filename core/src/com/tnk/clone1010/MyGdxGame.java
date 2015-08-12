package com.tnk.clone1010;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {

	public static final int VIEW_WIDTH = 480;
	public static final int VIEW_HEIGHT = 800;

	public static final int BLOCK_SIZE = 42;
	public static final int MARGIN_LEFT = 30;
	public static final int MARGIN_UPPER = 200;

	private OrthographicCamera camera; // upside down
	private OrthographicCamera uiCamera;

	private SpriteBatch batch;

	private Chunk next[];
    private Texture[] blockImages;
    private GameText text;


    @Override
	public void create () {
        batch = new SpriteBatch();

        blockImages = new Texture[Block.VARIATION_COLOR + 1];
        blockImages[Block.COLOR_NONE] = new Texture("grey.png");
        blockImages[Block.COLOR_RED] = new Texture("red.png");
        blockImages[Block.COLOR_BLUE] = new Texture("blue.png");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEW_WIDTH, VIEW_HEIGHT);
        camera.position.set(VIEW_WIDTH / 2, VIEW_HEIGHT / 2, 0);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, VIEW_WIDTH, VIEW_HEIGHT);
        uiCamera.position.set(VIEW_WIDTH / 2, VIEW_HEIGHT / 2, 0);

        next = Chunk.generate(3);
        text = new GameText();
    }


	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		// 10x10
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++) {
				batch.draw(blockImages[Block.COLOR_NONE], MARGIN_LEFT + BLOCK_SIZE * j, VIEW_HEIGHT - MARGIN_UPPER - BLOCK_SIZE * i, BLOCK_SIZE, BLOCK_SIZE);
			}
		}

		// next
		for(int i = 0; i < next.length; i++){
			next[i].draw(this, batch, VIEW_WIDTH / 4 * (i + 1), 100, 28, 28);
		}


		batch.end();


        uiCamera.update();
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        text.draw(batch);

        batch.end();

	}

    public Texture getImage(int color){
        return blockImages[color];
    }
}

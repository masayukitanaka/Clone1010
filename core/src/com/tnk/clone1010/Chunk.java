package com.tnk.clone1010;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by tnk on 8/11/15.
 */
public class Chunk {

    /* setting, common values */


    /* variables */
    private Block[] blocks;
    private int color;
    private ChunkType type;
    private Texture blockTexture;

    public Chunk(ChunkType type){
        this.type = type;
        this.blocks = new Block[type.getBlockNumber()];
        this.color = Block.getRandomColor();
        Gdx.app.log("@@@", "random color:" + color);

        for(int i = 0; i < blocks.length; i++){
            blocks[i] = new Block(color);
        }
    }

    public static Chunk[] generate(int num) {
        Chunk chunks[] = new Chunk[num];
        for(int i = 0; i < num; i++){
            Chunk chunk = new Chunk(ChunkType.getRandom());
            chunks[i] = chunk;
        }

        return chunks;
    }

    public void draw(MyGdxGame game, SpriteBatch batch, int centerX, int centerY, int blockWidth, int blockHeight) {
        Texture texture = game.getImage(color);
        int[][] map = type.getDrawMap();
        Gdx.app.log("@@@", "chunk type:" + type);
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++) {
                if(map[i][j] == 1){
                    batch.draw(texture, centerX + j * blockWidth, centerY + i * blockHeight, blockWidth, blockHeight);
                }
            }
        }
    }

}

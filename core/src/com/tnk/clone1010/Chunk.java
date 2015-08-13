package com.tnk.clone1010;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by tnk on 8/11/15.
 */
public class Chunk {

    /* variables */
    private Block[] blocks;
    private int color;
    private ChunkType type;
    private int blockWidth;
    private int blockHeight;
    private int x;
    private int y;
    int[][] map;

    public Chunk(int x, int y, int blockSize) {
        this.x = x;
        this.y = y;
        this.blockWidth = blockSize;
        this.blockHeight = blockSize;

        this.type = ChunkType.getRandom();
        this.map = type.getDrawMap();
        this.color = type.getBlockColor();
        this.blocks = new Block[type.getBlockNumber()];

        for(int i = 0; i < blocks.length; i++){
            blocks[i] = new Block(color);
        }
    }

    public Block getBlockAt(int index){
        return blocks[index];
    }

    public int getLeft(){
        return x - blockWidth * map[0].length / 2;
    }

    public int getTop(){
        return y - blockHeight * map.length / 2;
    }

    public void draw(MyGdxGame game, SpriteBatch batch){
        Texture texture = game.getImage(color);

        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++) {
                if(map[i][j] == 1){
                    batch.draw(texture, getLeft() + j * blockWidth, getTop() + i * blockHeight, blockWidth, blockHeight);
                }
            }
        }
    }

    public void onTouch() {
        blockWidth = MyGdxGame.BLOCK_SIZE;
        blockHeight = MyGdxGame.BLOCK_SIZE;
    }

    public void released() {
        blockWidth = MyGdxGame.SMALL_BLOCK;
        blockHeight = MyGdxGame.SMALL_BLOCK;
    }

    public boolean isContain(Vector3 coord){
        int margin = MyGdxGame.TOUCH_MARGIN;
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++) {
                if(map[i][j] == 1){
                    int leftX = getLeft() + j * blockWidth;
                    int topY = getTop() + i * blockHeight;
                    if((leftX - margin <= coord.x && coord.x <= leftX + blockWidth + margin)
                            &&
                            (topY - margin <= coord.y && coord.y <= topY + blockHeight + margin)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int[][] getDrawMap() {
        return map;
    }

    public int blockNumber() {
        return blocks.length;
    }

    @Override
    public String toString(){
        return "{chunkType:"+ type +", color:" + color +"}";
    }

    public int getHeight() {
        return MyGdxGame.BLOCK_SIZE * map.length / 2;
    }

    public int getMarginFromFinger() {
        return marginFunc(map.length);
    }

    private int marginFunc(int t){
        return 8 * t * (10 - t);
    }
}

package com.tnk.clone1010;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by tnk on 8/11/15.
 */
public class Board {

    /* setting, common values */
    public static final int SIZE = 10;

    /* variables */
    private Block blocks[][] = new Block[SIZE][SIZE];
    private int x;
    private int y;
    private Array<Integer> removeRows;
    private Array<Integer> removeCols;
    private Array<Block> animatingBlock;

    public Board(int x, int y){
        this.x = x;
        this.y = y;

        // comment out when debugging
//        for(int i = 0; i < 7; i++) {
//            for (int j = 0; j < 9; j++) {
//                blocks[i][j] = new Block(Block.COLOR_BLUE);
//            }
//        }
    }

    public void draw(SpriteBatch batch, MyGdxGame game) {
        Texture defaultTexture = game.getImage(Block.COLOR_NONE);

        for(int i = 0; i < SIZE; i++){
            for(int j = 0; j < SIZE; j++) {
                if(blocks[i][j] == null){
                    batch.draw(defaultTexture, x + MyGdxGame.BLOCK_SIZE * j, y + MyGdxGame.BLOCK_SIZE * i, MyGdxGame.BLOCK_SIZE, MyGdxGame.BLOCK_SIZE);
                }else{
                    // block for each color
                    Texture texture = game.getImage(blocks[i][j].getColor());
                    batch.draw(texture, x + MyGdxGame.BLOCK_SIZE * j, y + MyGdxGame.BLOCK_SIZE * i, MyGdxGame.BLOCK_SIZE, MyGdxGame.BLOCK_SIZE);
                }
            }
        }

        if(animatingBlock != null && animatingBlock.size > 0) {
            Array<Block> deadBlocks = new Array<Block>();
            for (Block b : animatingBlock) {
                b.animateFading(batch, game);
                if (b.isDead()) {
                    deadBlocks.add(b);
                }
            }
            for (Block b : deadBlocks) {
                animatingBlock.removeValue(b, true);
            }
        }
    }

    public int bottomY(){
        return y + MyGdxGame.BLOCK_SIZE * SIZE;
    }

    public boolean place(Chunk chunk) {
        // fix position to the closest block in the board
        int position[] = adjustPosition(chunk);

        if(position == null){
            return false;
        }

        // check if the rectangle is in the board
        if(!isContain(chunk, position)){
            return false;
        }

        // check if each block is occupied
        if(isPreoccupied(chunk, position)){
            return false;
        }

        // absorb it
        absorb(chunk, position);

        return true;
    }

    public boolean isPlaceable(Chunk chunk){
        int map[][] = chunk.getDrawMap();
        for(int i = 0; i <= SIZE - map.length; i++) {
            for (int j = 0; j <= SIZE - map[0].length; j++) {
                int position[] = new int[2];
                /**
                 * position[0] = Board.getXcoordAt(j);
                 * position[1] = Board.getYcoordAt(i);
                 */
                position[0] = i;
                position[1] = j;
                if(!isPreoccupied(chunk, position)){
                    return true;
                }
            }
        }

        return false;
    }

    public static int getXcoordAt(int colIndex) {
        return MyGdxGame.MARGIN_LEFT + MyGdxGame.BLOCK_SIZE * colIndex;
    }

    public static int getYcoordAt(int rowIndex) {
        return MyGdxGame.MARGIN_UPPER + MyGdxGame.BLOCK_SIZE * rowIndex;
    }

    private void absorb(Chunk chunk, int[] leftTopPosition) {
        int map[][] = chunk.getDrawMap();
        int currentIndex = 0;

        for(int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if(map[i][j] == 1) {
                    int currentRow = leftTopPosition[0] + i;
                    int currentCol = leftTopPosition[1] + j;
                    blocks[currentRow][currentCol] = chunk.getBlockAt(currentIndex);
                    currentIndex++;
                }
            }
        }
    }

    /**
     *
     * leftTopPosition[0]: row position
     * leftTopPosition[1]: column position
     * @param chunk
     * @param leftTopPosition
     * @return
     */
    private boolean isPreoccupied(Chunk chunk, int[] leftTopPosition) {
        int map[][] = chunk.getDrawMap();

        for(int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                int currentRow = leftTopPosition[0] + i;
                int currentCol = leftTopPosition[1] + j;
                if(map[i][j] == 1 && blocks[currentRow][currentCol] != null){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     *
     * leftTopPosition[0]: row position
     * leftTopPosition[1]: column position
     * @param chunk
     * @param leftTopPosition
     * @return
     */
    private boolean isContain(Chunk chunk, int[] leftTopPosition) {
        int map[][] = chunk.getDrawMap();
        // map.length: row number
        // map[0].length: column number
        return leftTopPosition[0] + map.length <= SIZE && leftTopPosition[1] + map[0].length <= SIZE;
    }

    private int[] adjustPosition(Chunk chunk) {
        int leftX = chunk.getLeft();
        int topY = chunk.getTop();

        for(int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int eachX = x + j * MyGdxGame.BLOCK_SIZE;
                int eachY = y + i * MyGdxGame.BLOCK_SIZE;

                if(eachX - MyGdxGame.BLOCK_SIZE / 2 < leftX
                        && leftX < eachX + MyGdxGame.BLOCK_SIZE / 2
                        && eachY - MyGdxGame.BLOCK_SIZE / 2 < topY
                        && topY < eachY + MyGdxGame.BLOCK_SIZE / 2
                        ){
                    int differenceX = eachX - leftX;
                    int differenceY = eachY - topY;

                    chunk.setX(chunk.getX() + differenceX);
                    chunk.setY(chunk.getY() + differenceY);

                    return new int[]{i, j};
                }
            }
        }

        return null;
    }

    public boolean hasLine() {
        removeRows = new Array<Integer>();
        removeCols = new Array<Integer>();

        ROWLABEL:
        for(int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if(blocks[i][j] == null) {
                    continue ROWLABEL;
                }
            }

            // if everything is null, this line is executed.
            removeRows.add(i);
        }

        COLLABEL:
        for(int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++) {
                if(blocks[i][j] == null) {
                    continue COLLABEL;
                }
            }

            // if everything is null, this line is executed.
            removeCols.add(j);
        }

        return removeRows.size > 0 || removeCols.size > 0;
    }

    public int eraseLine() {
        //Gdx.app.log("@@@@", "[remove] rows:[" + removeRows.toString(",") +"], cols:[" + removeCols.toString(",") + "]" );

        animatingBlock = new Array<Block>();

        int removed = 0;
        for(Integer row : removeRows){
            for (int j = 0; j < SIZE; j++) {
                if(blocks[row][j] != null){
                    removed++;
                }
                blocks[row][j].setRow(row);
                blocks[row][j].setCol(j);
                animatingBlock.add(blocks[row][j]);

                blocks[row][j] = null;
            }
        }

        for(Integer col : removeCols){
            for (int i = 0; i < SIZE; i++) {
                if(blocks[i][col] != null){
                    removed++;
                }
                blocks[i][col].setRow(i);
                blocks[i][col].setCol(col);
                animatingBlock.add(blocks[i][col]);

                blocks[i][col] = null;
            }
        }

        return removed;
    }
}

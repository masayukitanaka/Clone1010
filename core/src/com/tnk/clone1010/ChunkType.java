package com.tnk.clone1010;

import com.badlogic.gdx.Gdx;

import java.util.Random;

/**
 * Created by tnk on 8/11/15.
 */
public enum ChunkType {

    /**
     * @
     */
    SINGLE,

    /**
     * @
     * @
     */
    VERTICAL2,

    /**
     * @@
     */
    HORIZONTAL2,

    /**
     * @
     * @
     * @
     */
    VERTICAL3,

    /**
     * @@@
     */
    HORIZONTAL3,

    /**
     *  @
     * @@
     */
    RIGHT_UP3,

    /**
     * @
     * @@
     */
    LEFT_UP3,

    /**
     * @@
     *  @
     */
    RIGHT_DOWN3,

    /**
     * @@
     * @
     */
    LEFT_DOWN3,

    /**
     * @@
     * @@
     */
    SQUARE4,

    /**
     * @
     * @
     * @
     * @
     */
    VERTICAL4,

    /**
     * @@@@
     */
    HORIZONTAL4,

    /**
     * @
     * @
     * @
     * @
     * @
     */
    VERTICAL5,

    /**
     * @@@@@
     */
    HORIZONTAL5,

    /**
     *   @
     *   @
     * @@@
     */
    RIGHT_UP5,

    /**
     * @
     * @
     * @@@
     */
    LEFT_UP5,

    /**
     * @@@
     *   @
     *   @
     */
    RIGHT_DOWN5,

    /**
     * @@@
     * @
     * @
     */
    LEFT_DOWN5,

    /**
     * @@@
     * @@@
     * @@@
     */
    SQUARE9;

    public int getBlockNumber(){
        switch(this){
            case SINGLE:
                return 1;
            case VERTICAL2:
            case HORIZONTAL2:
                return 2;
            case VERTICAL3:
            case HORIZONTAL3:
            case LEFT_UP3:
            case RIGHT_UP3:
            case RIGHT_DOWN3:
            case LEFT_DOWN3:
                return 3;
            case VERTICAL4:
            case HORIZONTAL4:
            case SQUARE4:
                return 4;
            case VERTICAL5:
            case HORIZONTAL5:
            case RIGHT_UP5:
            case LEFT_UP5:
            case RIGHT_DOWN5:
            case LEFT_DOWN5:
                return 5;
            case SQUARE9:
                return 9;
        }
        Gdx.app.log(getClass().getSimpleName(), "SHOULD NOT BE HERE.");
        return 0;
    }

    private static final ChunkType[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random random = new Random();

    public static ChunkType getRandom()  {
        return VALUES[random.nextInt(SIZE)];
    }

    public int[][] getDrawMap(){
        switch(this){
            case SINGLE:
                int num1[][] = {{1}};
                return num1;
            case VERTICAL2:
                int num2[][] = {{1},{1}};
                return num2;
            case HORIZONTAL2:
                int num3[][] = {{1,1}};
                return num3;
            case VERTICAL3:
                int num4[][] = {{1},{1},{1}};
                return num4;
            case HORIZONTAL3:
                int num5[][] = {{1,1,1}};
                return num5;
            case LEFT_UP3:
                int num6[][] = {{1,0},{1,1}};
                return num6;
            case RIGHT_UP3:
                int num7[][] = {{0,1},{1,1}};
                return num7;
            case RIGHT_DOWN3:
                int num8[][] = {{1,1},{0,1}};
                return num8;
            case LEFT_DOWN3:
                int num9[][] = {{1,1},{1,0}};
                return num9;
            case VERTICAL4:
                int num10[][] = {{1},{1},{1},{1}};
                return num10;
            case HORIZONTAL4:
                int num11[][] = {{1,1,1,1}};
                return num11;
            case SQUARE4:
                int num12[][] = {{1,1},{1,1}};
                return num12;
            case VERTICAL5:
                int num13[][] = {{1},{1},{1},{1},{1}};
                return num13;
            case HORIZONTAL5:
                int num14[][] = {{1,1,1,1,1}};
                return num14;
            case RIGHT_UP5:
                int num15[][] = {{0,0,1},{0,0,1},{1,1,1}};
                return num15;
            case LEFT_UP5:
                int num16[][] = {{1,0,0},{1,0,0},{1,1,1}};
                return num16;
            case RIGHT_DOWN5:
                int num17[][] = {{1,1,1},{0,0,1},{0,0,1}};
                return num17;
            case LEFT_DOWN5:
                int num18[][] = {{1,1,1},{1,0,0},{1,0,0}};
                return num18;
            case SQUARE9:
                int num19[][] = {{1,1,1},{1,1,1},{1,1,1}};
                return num19;
        }

        return null;
    }

}

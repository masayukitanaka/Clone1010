package com.tnk.clone1010;

import java.util.Random;

/**
 * Created by tnk on 8/11/15.
 */
public class Block {

    /* setting, common values */

    public static final int COLOR_NONE = 0;
    public static final int COLOR_RED = 1;
    public static final int COLOR_BLUE = 2;
    public static final int COLOR_PURPLE = 3;
    public static final int COLOR_YELLOW = 4;
    public static final int COLOR_L_GREEN = 5;
    public static final int COLOR_D_GREEN = 6;
    public static final int VARIATION_COLOR = 2; // 6;

    private static Random r = new Random();

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /* variables */
    private int color;
    private int x;
    private int y;

    public Block(int color){
        this.color = color;
    }

    /**
     * @return int color (min: 1)
     */
    public static int getRandomColor() {
        int ret =  r.nextInt(VARIATION_COLOR) + 1;
        //Gdx.app.log("@@@", "[test] random color:" + ret);

        return ret;
    }

    public int getColor() {
        return color;
    }
}

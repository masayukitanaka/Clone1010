package com.tnk.clone1010;

import com.badlogic.gdx.Gdx;

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

    /* variables */
    private int color;

    public Block(int color){
        this.color = color;
    }

    /**
     * @return int color (min: 1)
     */
    public static int getRandomColor() {
        //int nextInt = r.nextInt();
        //int ret = 1 + ((nextInt % VARIATION_COLOR + VARIATION_COLOR) % VARIATION_COLOR);

        int ret =  r.nextInt(VARIATION_COLOR) + 1;
        Gdx.app.log("@@@", "[test] random color:" + ret);

        return ret;
    }
}

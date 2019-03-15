package frc.systems;

import java.util.ArrayList;

public class LevelManager {

    private int level = 0;

    private ArrayList<Double> heights = new ArrayList<Double>();

    public LevelManager(double... heights) {
        for (double levelHeight : heights) {
            this.heights.add(levelHeight);
        }
    }

    public void moveUp() {
        if (!atTop())
            level++;
    }

    public void moveDown() {
        if (!atBottom())
            level--;
    }

    public void gotoBottom() {
        level = 0; // arraylist are 0 based
    }

    public void setLevelNearButNotOver(double height) {
        // find height closet to this without going over and set level to that level
        int lastHeight = 0;
        for (int i = 0; i < heights.size(); i++) {
            double cur = heights.get(i);
            if (cur > height) {
                level = lastHeight;
                return;
            }
            lastHeight = i;
        }
        level = lastHeight; // this should set it to the top
    }

    public double getHeightForCurLevel() {
        return heights.get(level);
    }

    private boolean atTop() {
        return heights.size() == level;
    }

    private boolean atBottom() {
        return 0 == level; // arrayList are 0 based
    }
}
package nl.gremmee.mazegenerator;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cell {

    boolean visited = false;

    boolean[] walls;

    int i;
    int j;

    private ID id;

    private int index;

    private Random random = new Random();

    public Cell(int aIndex, int aI, int aJ, ID aId) {
        this.index = aIndex;
        this.i = aI;
        this.j = aJ;
        this.walls = new boolean[] { true, true, true, true };
    }

    public Cell checkNeighbors() {
        List<Cell> neighbors = new ArrayList<>();

        Cell top = MazeGenerator.handler.getGameObject(getIndex(i, j - 1));
        Cell right = MazeGenerator.handler.getGameObject(getIndex(i + 1, j));
        Cell bottom = MazeGenerator.handler.getGameObject(getIndex(i, j + 1));
        Cell left = MazeGenerator.handler.getGameObject(getIndex(i - 1, j));

        if ((top != null) && !top.visited) {
            neighbors.add(top);
        }
        if ((right != null) && !right.visited) {
            neighbors.add(right);
        }
        if ((bottom != null) && !bottom.visited) {
            neighbors.add(bottom);
        }
        if ((left != null) && !left.visited) {
            neighbors.add(left);
        }

        if (neighbors.size() > 0) {
            int r = random.nextInt(neighbors.size());
            return neighbors.get(r);
        }
        return null;
    }

    public void doRender(Graphics aGraphics) {
        int w = MazeGenerator.w;
        int x = this.i * w;
        int y = this.j * w;

        if (visited) {
            aGraphics.setColor(new Color(255, 255, 255, 15));
            aGraphics.fillRect(x, y, w, w);
        }

        aGraphics.setColor(Color.black);
        if (this.walls[0]) {
            // top
            aGraphics.drawLine(x, y, x + w, y);
        }
        if (this.walls[1]) {
            // right
            aGraphics.drawLine(x + w, y, x + w, y + w);
        }
        if (this.walls[2]) {
            // bottom
            aGraphics.drawLine(x + w, y + w, x, y + w);
        }
        if (this.walls[3]) {
            // left
            aGraphics.drawLine(x, y + w, x, y);
        }
    }

    public void doUpdate() {
    }

    public ID getID() {
        return this.id;
    }

    public int getIndex(int aI, int aJ) {
        if ((aI < 0) || (aJ < 0) || (aI > (MazeGenerator.cols - 1)) || (aJ > (MazeGenerator.rows - 1))) {
            return -1;
        }

        return aI + (aJ * MazeGenerator.cols);
    }

    public Random getRandom() {
        return this.random;
    }

    public void highlight(Graphics aGraphics) {
        int w = MazeGenerator.w;
        int x = this.i * w;
        int y = this.j * w;

        aGraphics.setColor(new Color(0, 255, 0, 127));
        aGraphics.fillRect(x, y, w, w);
    }

    public void render(Graphics aGraphics) {
        doRender(aGraphics);
    }

    public void setId(ID aId) {
        this.id = aId;
    }

    public void update() {
        doUpdate();
    }
}

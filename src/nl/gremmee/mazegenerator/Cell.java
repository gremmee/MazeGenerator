package nl.gremmee.mazegenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cell {

    public boolean visited = false;
    private int printWidth = MazeGenerator.defaultCell;
    private int printHeight = MazeGenerator.defaultCell;
    public boolean[] walls;

    public int i;
    public int j;

    private ID id;

    private boolean startCell;
    private boolean endCell;

    private Random random = new Random();

    public Cell(int aIndex, int aI, int aJ, boolean aStartCell, boolean aEndCell) {
        this.setStartCell(aStartCell);
        this.setEndCell(aEndCell);
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
        int w = this.printWidth;
        int h = this.printHeight;
        int x = this.i * w;
        int y = this.j * h;

        if (visited) {
            aGraphics.setColor(new Color(255, 255, 255, 15));
            aGraphics.fillRect(x, y, w, h);
        }

        aGraphics.setColor(Color.black);
        Font font = new Font("Arial", 0, h / 3);
        if (this.endCell) {
            drawCenteredString(aGraphics, "Eind", new Rectangle(x, y, w, h), font);
        } else if (this.startCell) {
            drawCenteredString(aGraphics, "Start", new Rectangle(x, y, w, h), font);
        }
        if (this.walls[0]) {
            // top
            aGraphics.drawLine(x, y, x + w, y);
        }
        if (this.walls[1]) {
            // right
            aGraphics.drawLine(x + w, y, x + w, y + h);
        }
        if (this.walls[2]) {
            // bottom
            aGraphics.drawLine(x + w, y + h, x, y + h);
        }
        if (this.walls[3]) {
            // left
            aGraphics.drawLine(x, y + h, x, y);
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
        int w = this.printWidth;
        int h = this.printHeight;
        int x = this.i * w;
        int y = this.j * h;

        aGraphics.setColor(new Color(255, 0, 0, 127));
        aGraphics.fillRect(x, y, w, h);
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

    public void setPrintWidth(int pWidth) {
        this.printWidth = pWidth;

    }

    public void setPrintHeight(int pHeight) {
        this.printHeight = pHeight;

    }

    public boolean isStartCell() {
        return startCell;
    }

    public void setStartCell(boolean startCell) {
        this.startCell = startCell;
    }

    public boolean isEndCell() {
        return endCell;
    }

    public void setEndCell(boolean endCell) {
        this.endCell = endCell;
    }

    protected void drawCenteredString(Graphics aGraphics, String aText, Rectangle aRectangle, Font aFont) {
        // Get the FontMetrics
        FontMetrics metrics = aGraphics.getFontMetrics(aFont);
        // Determine the X coordinate for the text
        int x = (aRectangle.width - metrics.stringWidth(aText)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as
        // in java 2d 0 is top of the screen)
        int y = ((aRectangle.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        aGraphics.setFont(aFont);
        // Draw the String
        aGraphics.drawString(aText, x + aRectangle.x, y + aRectangle.y);
    }

}

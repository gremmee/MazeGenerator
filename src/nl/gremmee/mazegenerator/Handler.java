package nl.gremmee.mazegenerator;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.LinkedList;

public class Handler implements Printable {
    LinkedList<Cell> object = new LinkedList<>();

    public void addObject(Cell aObject) {
        this.object.add(aObject);
    }

    public Cell getGameObject(Cell aObject) {
        int index = this.object.indexOf(aObject);
        if (index != -1) {
            return getGameObject(index);
        }
        return null;
    }

    public Cell getGameObject(int aIndex) {
        if (aIndex < 0) {
            return null;
        }
        return this.object.get(aIndex);
    }

    public int getStars() {
        int result = 0;
        for (Cell fireObject : object) {
            if (fireObject instanceof Cell) {
                result++;
            }
        }
        return result;
    }

    public void removeObject(Cell aObject) {
        this.object.remove(aObject);
    }

    public void render(Graphics aGraphics) {
        for (int i = 0; i < object.size(); i++) {
            Cell tempObject = object.get(i);
            tempObject.render(aGraphics);
        }
    }

    public void update() {
        for (int i = 0; i < object.size(); i++) {
            Cell tempObject = object.get(i);
            tempObject.update();
        }
    }

    @Override
    public int print(Graphics aGraphics, PageFormat aPageFormat, int aPageIndex) throws PrinterException {
        if (aPageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        int pWidth = (int) Math.round(aPageFormat.getImageableWidth() / MazeGenerator.cols);
        int pHeight = (int) Math.round(aPageFormat.getImageableHeight() / MazeGenerator.rows);
        for (int i = 0; i < object.size(); i++) {
            Cell tempObject = object.get(i);
            tempObject.setPrintWidth(pWidth);
            tempObject.setPrintHeight(pHeight);
            tempObject.render(aGraphics);
        }
        return PAGE_EXISTS;
    }
}

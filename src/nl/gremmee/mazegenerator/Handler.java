package nl.gremmee.mazegenerator;

import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
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
}

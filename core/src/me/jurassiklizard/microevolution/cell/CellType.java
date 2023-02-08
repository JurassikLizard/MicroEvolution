package me.jurassiklizard.microevolution.cell;

import com.badlogic.gdx.graphics.Color;

public enum CellType {
    VOID(Color.BLACK),
    FOOD(new Color(Color.OLIVE)),
    PLANTER(Color.TEAL),
    WALL(Color.GRAY),
    MOUTH(Color.ORANGE),
    MOVER(Color.BLUE);

    private Color color;

    CellType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}

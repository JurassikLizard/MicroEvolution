package me.jurassiklizard.microevolution.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import me.jurassiklizard.microevolution.MicroEvolution;
import me.jurassiklizard.microevolution.cell.Cell;
import me.jurassiklizard.microevolution.cell.CellType;
import me.jurassiklizard.microevolution.organism.Organism;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Renderer {
    SpriteBatch batch;
    Camera camera;

    public Renderer(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // TODO: Figure out why the heck the y coordinate is 560 pixels lower (higher on the screen) than it should be
        camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2 + 560, 0);
    }

    public void render() {
        ScreenUtils.clear(Color.PINK);

        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(),Gdx.graphics.getWidth(), Pixmap.Format.RGBA8888);

        drawCells(pixmap);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        batch.draw(texture, 0, 0);

        batch.end();
        texture.dispose();

        //clearCellLocks();
    }

    public void drawCells(Pixmap pixmap) {
        Cell.timer++;

        for (int x = 0; x < Cell.cells.length; x++)
        {
            for (int y = 0; y < Cell.cells[x].length; y++)
            {
                Cell cell = Cell.cells[x][y];
                if(cell.getOrganism() != null) cell.getOrganism().executeActions();
                else cell.executeAction();

                pixmap.setColor(cell.getType().getColor());
                pixmap.fillRectangle(cell.getCellPosition().x * Cell.CELL_SIZE, cell.getCellPosition().y * Cell.CELL_SIZE, Cell.CELL_SIZE, Cell.CELL_SIZE);
            }
        }

        for(Organism organism : Organism.organisms) organism.resetActions();
    }

    public void dispose(){
        batch.dispose();
    }
}

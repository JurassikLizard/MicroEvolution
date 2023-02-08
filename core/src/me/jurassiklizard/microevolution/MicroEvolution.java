package me.jurassiklizard.microevolution;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import me.jurassiklizard.microevolution.cell.Cell;
import me.jurassiklizard.microevolution.cell.CellType;
import me.jurassiklizard.microevolution.math.Vector2;
import me.jurassiklizard.microevolution.organism.Organism;
import me.jurassiklizard.microevolution.render.Renderer;
import me.jurassiklizard.microevolution.utils.Utils;

public class MicroEvolution extends ApplicationAdapter {
	Renderer renderer;
	Organism organism;
	public static Organism mover;

	@Override
	public void create () {
		Cell.cells = new Cell[Gdx.graphics.getWidth() / Cell.CELL_SIZE][Gdx.graphics.getHeight() / Cell.CELL_SIZE];

		for (int x = 0; x < Cell.cells.length; x++)
		{
			for (int y = 0; y < Cell.cells[x].length; y++)
			{
				new Cell(CellType.VOID, new Vector2(x, y));
			}
		}

		renderer = new Renderer(); // Renderer should probably be last because it might reference lots of stuff

		CellType[][] cellTypes = new CellType[3][3];
		Utils.fill(cellTypes, CellType.VOID);
		cellTypes[1][1] = CellType.MOUTH;
		cellTypes[2][2] = CellType.PLANTER;
		cellTypes[0][0] = CellType.PLANTER;

		CellType[][] moverTypes = new CellType[3][3];
		Utils.fill(moverTypes, CellType.VOID);
		moverTypes[1][1] = CellType.MOUTH;
		moverTypes[2][2] = CellType.MOVER;

		//organism = new Organism(new Vector2(Gdx.graphics.getWidth() / 2 / Cell.CELL_SIZE, Gdx.graphics.getHeight() / 2 / Cell.CELL_SIZE), Utils.generateGradientForSquareVectorArray(3), cellTypes);
		mover = new Organism(new Vector2(15, 30), moverTypes, false);
	}

	@Override
	public void render () {
		renderer.render();
	}
	
	@Override
	public void dispose () {
		renderer.dispose();
	}
}

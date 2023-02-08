package me.jurassiklizard.microevolution.cell;

import me.jurassiklizard.microevolution.math.Vector2;
import me.jurassiklizard.microevolution.organism.Organism;
import me.jurassiklizard.microevolution.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Cell {
    public final static int CELL_SIZE = 8;

    public static Cell[][] cells;

    private CellType type;

    private Vector2 cellPosition = new Vector2(0, 0);

    public static int timer;

    private Organism organism;

    public Cell(CellType type){
        new Cell(type, Vector2.Zero);
    }

    public static CellType[] reproductionInhibitorCellTypes = new CellType[]{
            CellType.FOOD, CellType.WALL, CellType.PLANTER, CellType.MOUTH, CellType.MOVER
    };
    public static CellType[] movementInhibitorCellTypes = new CellType[]{
            CellType.WALL, CellType.PLANTER, CellType.MOUTH, CellType.MOVER
    };
    public static CellType[] aliveCellTypes = new CellType[]{CellType.PLANTER, CellType.MOUTH, CellType.MOVER};

    public Cell(CellType type, Vector2 cellPosition){
        setCellPosition(cellPosition);
        this.type = type;
    }

    public void executeAction(){
        switch (type) {
            case VOID: {
                break;
            }
            case PLANTER: {
                if(timer % 10 != 0 || organism == null) break;
                if(organism.getFood() == 0) break;

                Cell[] cellArray = Utils.getNeighbors(this);
                Random random = new Random();
                double plantChance = 0.2d;

                for(Cell cell : cellArray){
                    if(cell != null && random.nextDouble() < plantChance){
                        cell.setType(CellType.FOOD, null);
                    }
                }

                //organism.addFood(-0.5f);

                break;
            }

            case MOUTH: {
                if(timer % 10 != 0 || organism == null) break;

                Cell[] cellArray = Utils.getNeighbors(this);
                Random random = new Random();
                double eatChance = 0.75d;

                for(Cell cell : cellArray){
                    if(cell != null && random.nextDouble() < eatChance){
                        cell.setType(CellType.VOID, null);
                    }
                }

                //organism.addFood(3);
                break;
            }
            case MOVER: {
                if(timer % 50 != 0 || organism == null) break;
                if(organism.getFood() == 0) break;
                int moverAmount = 0;
                for(Cell cell : organism.aliveCells){
                    if(cell.getType() == CellType.MOVER){
                        // make sure only the first cell found is the one moving, to avoid double movements
                        if(cell != this) break;
                        moverAmount++;
                    }
                }
                if(organism.aliveCells.size() / moverAmount > 2) break;
                Random random = new Random();

                int actionChance = 50;
                int rotateChance = 0; //If doesn't move, rotate

                //if(random.nextInt(100) >= actionChance) break;

//                if(random.nextInt(100) < rotateChance){
//                    //organism.rotate();
//                }
//                else{
//                    organism.move();
//                }
                organism.move();

                if(organism == null) break;

                break;
            }
            case FOOD:
                break;
            case WALL:
                break;
            default:
                break;
        }
    }
    // Position in cells array
    public Vector2 getCellPosition(){
        return cellPosition;
    }

    public void setCellPosition(Vector2 position){
        this.cellPosition = position;
        cells[position.x][position.y] = this;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type, Organism cause) {
        if(!Arrays.asList(aliveCellTypes).contains(type)){
            if(organism != null) {
                organism.aliveCells.remove(this);
            }
            this.setOrganism(null);
        }
        else{
            this.setOrganism(cause);
            if(organism != null) {
                organism.aliveCells.add(this);
            }
        }
        this.type = type;
    }

    public Organism getOrganism() {
        return organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
    }
}

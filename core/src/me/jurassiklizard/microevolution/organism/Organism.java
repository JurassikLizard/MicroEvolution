package me.jurassiklizard.microevolution.organism;

import com.badlogic.gdx.Gdx;
import me.jurassiklizard.microevolution.cell.Cell;
import me.jurassiklizard.microevolution.cell.CellType;
import me.jurassiklizard.microevolution.math.Vector2;
import me.jurassiklizard.microevolution.render.Renderer;
import me.jurassiklizard.microevolution.utils.Utils;

import java.util.*;

// 90 degree rotation clockwise = newx = y, newy = -x
// 90 degree rotation counterclockwise newy = x, newx = -y

// THERE IS A MASSIVE PROBLEM WHERE AN ORGANISM CAN MOVE FORWARD AND SOMETIMES THE RENDERER RENDER THE CELL TWICE AND MOVES IT TWICE
public class Organism {
    public static ArrayList<Organism> organisms = new ArrayList<>();

    private Vector2 location;

    public ArrayList<Cell> aliveCells = new ArrayList<>();

    private CellType[][] types;
    private boolean[][] executionLocks;
    private Vector2 executionLockSearchIndex = Vector2.Zero; // Vector2 because we use 2d arrays

    private float food = 3;
    private final float foodMax = 8;

    public int reproductionAttempts = 0;
    private int reproductionAttemptLimit = 10;

    //public int reproductionSuccesses = 0;
    //private int successfullReproductionLimit = 5;

    //public int lifeTime = 0;
    //private int maxLifeTime = Integer.MAX_VALUE;

    public Vector2 forward = new Vector2(1, 0);

    private int centerCoord;

    public Organism(Vector2 location, CellType[][] types, boolean randomRotation){
        this.location = location;
        this.types = types;
        this.executionLocks = new boolean[types.length][types.length];
        this.centerCoord = (types.length - 1) / 2;
        for(int x = 0; x < types.length; x++){
            for(int y = 0; y < types[x].length; y++){
                executionLocks[x][y] = false; // nothing starts off as locked

                // Subtract center coord because top left (0, 0) is -3, -3, for example
                Cell cell = Cell.cells[location.x + (x - centerCoord)][location.y + (y - centerCoord)];
                CellType type = types[x][y];

                if(cell == null) return;

                // If we are trying to reproduce somewhere we can't, die
                if(Arrays.asList(Cell.reproductionInhibitorCellTypes).contains(cell.getType())) {
                    die();
                    return;
                }
                // Check if what we are trying to place is alive
                if(Arrays.asList(Cell.aliveCellTypes).contains(type)) {
                    cell.setType(type, this);
                }
            }
        }

        organisms.add(this);

        if(randomRotation && new Random().nextBoolean()) rotate();
    }

    public void reproduce(){
        // ||
        //                (reproductionSuccesses >= successfullReproductionLimit) || (lifeTime > maxLifeTime)
        if((reproductionAttempts >= reproductionAttemptLimit)){
            die();
            return;
        }

        Random random = new Random();

        int upperDistance = 4; // has to be positive
        int lowerDistance = -4; // has to be negative

        Vector2 offset = new Vector2(random.nextInt(upperDistance - lowerDistance) - upperDistance,
                random.nextInt(upperDistance - lowerDistance) - upperDistance);

        boolean isValidSpot = checkSpot(offset, Cell.reproductionInhibitorCellTypes);

        if(!isValidSpot){
            reproductionAttempts++;
            return;
        }

        reproductionAttempts = 0;

        new Organism(location.add(offset), types, true);
    }

    public void die(){
        setAliveCellsToType(CellType.FOOD);

        organisms.remove(this);
    }

    //Uses relative cell positions because rotation might need to check before moving
    public boolean checkSpot(Vector2 offset, CellType[] bannedCellTypes){
        for(int x = 0; x < types.length; x++){
            for(int y = 0; y < types[x].length; y++){
                Vector2 oldCellPosition = location.add(new Vector2(x - centerCoord, y - centerCoord)); // realign relative coords
                Vector2 newCellPosition = oldCellPosition.add(offset);

                if(newCellPosition.x < 0 || newCellPosition.x >= (Gdx.graphics.getWidth() / Cell.CELL_SIZE) ||
                        newCellPosition.y < 0 || newCellPosition.y >= (Gdx.graphics.getHeight() / Cell.CELL_SIZE)) return false;
                // If a new cell is trying to be put where there is an un-passable other type and the attempted tile isn't a void tile
                if (Arrays.asList(bannedCellTypes).contains(Cell.cells[newCellPosition.x][newCellPosition.y].getType())){
                    return false;
                }
            }
        }
        return true;
    }

    // Resets and re-draws our cells on the grid based on types array
    public void updateGlobalCellTypes(Vector2 offset){
        setAliveCellsToType(CellType.VOID); // Could be a redundancy, but just in-case will be kept here

        for(int x = 0; x < types.length; x++){
            for(int y = 0; y < types[x].length; y++){
                Vector2 oldCellPosition = location.add(new Vector2(x - centerCoord, y - centerCoord)); // realign relative coords
                Vector2 newCellPosition = oldCellPosition.add(offset);
                if(Arrays.asList(Cell.aliveCellTypes).contains(types[x][y])){
                    Cell.cells[newCellPosition.x][newCellPosition.y].setType(types[x][y], this);
                }
            }
        }
    }

    public void resetActions(){
        for (int x = 0; x < executionLocks.length; x++)
        {
            for (int y = 0; y < executionLocks.length; y++)
            {
                executionLocks[x][y] = false;
            }
        }
        executionLockSearchIndex = Vector2.Zero;
    }

    public void executeActions(){
        for(executionLockSearchIndex.x = 0; executionLockSearchIndex.x < types.length; executionLockSearchIndex.x++){
            for(executionLockSearchIndex.y = 0; executionLockSearchIndex.y < types.length; executionLockSearchIndex.y++){
                int x = executionLockSearchIndex.x;
                int y = executionLockSearchIndex.y;
                if(executionLocks[x][y]){ // If we should lock and not execute
                    continue;
                }
                Vector2 cellPosition = location.add(new Vector2(x - centerCoord, y - centerCoord)); // realign relative coords
                Cell.cells[cellPosition.x][cellPosition.y].executeAction();
                executionLocks[x][y] = true;
            }
        }
    }

    // TODO: Add same 2 vector randomization as reproduce(), and fix CellType checks
    public void move(){
        executionLockSearchIndex = Vector2.Zero; //Reset search index so we don't accidentally miss anything MIGHT BE A PROBLEM HERE

        Vector2 offset = forward;

        setAliveCellsToType(CellType.VOID);

        if(!checkSpot(offset, Cell.movementInhibitorCellTypes)) return;
        updateGlobalCellTypes(offset);

        location = location.add(offset);
    }

    // TODO: ADD ANOTHER 2D ARRAY THAT ROTATES WITH TYPES SO THAT YOU CAN SEE WHICH CELLS HAVE ALREADY EXECUTED
    // Also add an index of where to start scanning so that if it's rotated, you can reset to make sure you don't skip anything
    public void rotate(){
        CellType[][] cellTypesCopy = types.clone();

        Utils.rotate90Clockwise(types);

        Utils.rotate90Clockwise(executionLocks); //Rotate our locks as well
        executionLockSearchIndex = Vector2.Zero; //Reset search index so we don't accidentally miss anything

        setAliveCellsToType(CellType.VOID);

        if(!checkSpot(Vector2.Zero, Cell.movementInhibitorCellTypes)){
            types = cellTypesCopy;
            return;
        }

        updateGlobalCellTypes(Vector2.Zero);

        Vector2 forwardTemp = new Vector2(forward.x, forward.y);
        //newx = -y, newy = x
        forwardTemp.x = -forward.y;
        forwardTemp.y = forward.x;
        forward = forwardTemp;
    }

    public void setAliveCellsToType(CellType type){
        Cell[] antiConcurrentModExceptionArray = aliveCells.toArray(new Cell[0]);
        for(Cell cell : antiConcurrentModExceptionArray) cell.setType(type, this);
    }

    public float getFood() {
        return food;
    }

    public void addFood(float amount) {
        this.food += amount;
        if(food > aliveCells.size()){
            food = 1;
            reproduce();
        }
        this.food = Math.min(foodMax, Math.max(0, this.food));
    }
}

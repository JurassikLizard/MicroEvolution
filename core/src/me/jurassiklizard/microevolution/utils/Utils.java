package me.jurassiklizard.microevolution.utils;

import com.badlogic.gdx.Gdx;
import me.jurassiklizard.microevolution.cell.Cell;
import me.jurassiklizard.microevolution.cell.CellType;
import me.jurassiklizard.microevolution.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;

public class Utils {
    public static Cell[] getNeighbors(Cell cell){
        Vector2 vector = cell.getCellPosition().straightMul(new Vector2(Cell.CELL_SIZE, Cell.CELL_SIZE));
        Cell[] cellArray = new Cell[4];

        //Left
        if(vector.x <= 0) cellArray[0] = null;
        else cellArray[0] = Cell.cells[cell.getCellPosition().x - 1][cell.getCellPosition().y];
        //Top
        if(vector.y <= 0) cellArray[1] = null;
        else cellArray[1] = Cell.cells[cell.getCellPosition().x][cell.getCellPosition().y - 1];
        //Right
        if(vector.x + Cell.CELL_SIZE >= Gdx.graphics.getWidth()) cellArray[2] = null;
        else cellArray[2] = Cell.cells[cell.getCellPosition().x + 1][cell.getCellPosition().y];
        //Bottom
        if(vector.y + Cell.CELL_SIZE >= Gdx.graphics.getHeight()) cellArray[3] = null;
        else cellArray[3] = Cell.cells[cell.getCellPosition().x][cell.getCellPosition().y + 1];

        return cellArray;
    }

    public static void fill(Object[][] array, Object filler){
        for (Object[] objects : array) {
            Arrays.fill(objects, filler);
        }
    }

    // Try to prevent even sided arrays
    public static Vector2[][] generateGradientForSquareVectorArray(int size){
        int subtractAmount = size % 2 == 0 ? size / 2 : (size - 1) / 2;
        Vector2[][] array = new Vector2[size][size];
        for(int x=0; x < size; x++){
            for(int y=0; y < size; y++){
                array[x][y] = new Vector2(x - subtractAmount, y - subtractAmount); // 0-2 becomes -1 to 1
            }
        }
        return array;
    }

//    public static <T> boolean contains(T[] arr, T o){
//        return new ArrayList<>(Arrays.asList(arr)).contains(o);
//    }

    public static void rotate90Clockwise(CellType[][] a)
    {
        int N = a.length;

        // Traverse each cycle
        for (int i = 0; i < N / 2; i++)
        {
            for (int j = i; j < N - i - 1; j++)
            {

                // Swap elements of each cycle
                // in clockwise direction
                CellType temp = a[i][j];
                a[i][j] = a[N - 1 - j][i];
                a[N - 1 - j][i] = a[N - 1 - i][N - 1 - j];
                a[N - 1 - i][N - 1 - j] = a[j][N - 1 - i];
                a[j][N - 1 - i] = temp;
            }
        }
    }

    public static void rotate90Clockwise(boolean[][] a)
    {
        int N = a.length;

        // Traverse each cycle
        for (int i = 0; i < N / 2; i++)
        {
            for (int j = i; j < N - i - 1; j++)
            {

                // Swap elements of each cycle
                // in clockwise direction
                boolean temp = a[i][j];
                a[i][j] = a[N - 1 - j][i];
                a[N - 1 - j][i] = a[N - 1 - i][N - 1 - j];
                a[N - 1 - i][N - 1 - j] = a[j][N - 1 - i];
                a[j][N - 1 - i] = temp;
            }
        }
    }
}

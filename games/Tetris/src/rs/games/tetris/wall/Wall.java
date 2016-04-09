package rs.games.tetris.wall;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import rs.games.tetris.Configurations;
import rs.games.tetris.Statistics;
import rs.games.tetris.exceptions.CollisionWithWalException;
import rs.games.tetris.figures.Brick;
import rs.games.tetris.figures.Figure;

/**
 * Wall is object which take cares of bricks from figures which reached the
 * bottom and maintains all functionalities related to these bricks
 * 
 * @author Zeljko
 * 
 */
public class Wall {

    // Properties

    protected final Logger log = Logger.getLogger(getClass());
    private final Map<Integer, List<Brick>> rows;
    private final RowsCache rowsCache;
    private final int boxSize;
    private final Statistics stats;

    // Constructor

    public Wall(Statistics stats) {
        this.boxSize = Configurations.BRICK_SIZE;
        int numOfRows = (Configurations.PLAYGROUND_HEIGHT / boxSize);
        int fullRowWidth = Configurations.PLAYGROUND_WIDTH;

        this.stats = stats;
        this.rows = new HashMap<Integer, List<Brick>>();
        this.rowsCache = new RowsCache(fullRowWidth, boxSize);
        initDefaultValues(numOfRows);
    }

    // Methods

    /**
     * Initialize default values to rows and rowsCache model data
     */
    private void initDefaultValues(int numOfRows) {
        for (int i = 0; i < numOfRows; i++) {
            int row = i * boxSize;
            rows.put(row, new ArrayList<Brick>());
            rowsCache.setRow(row, 0);
        }
    }

    /**
     * Puts the figure into wall and trigger all appropriate actions to check if
     * there are need for distrouing gull rows and etc.
     * 
     * @param figure
     */
    public void putIntoWall(Figure figure) {
        addToWall(figure);
        destroyFullRows();
    }

    /**
     * Adds bricks to the wall
     * 
     * @param figure
     */
    protected void addToWall(Figure figure) {
        for (Brick brick : figure.getBricks()) {
            int row = brick.y;
            addBrickToRow(row, brick);
        }
    }

    /**
     * Destroys all full rows and triggers corresponding actions
     */
    protected void destroyFullRows() {
        while (rowsCache.hasFullRow()) {
            Integer fullRow = rowsCache.getLowestFullRow();
            log.debug("Going to destroy full row " + fullRow);
            destroyRow(fullRow);
            stats.increaseScore();
        }
    }

    /**
     * Destroy the wall row and move down all rows above the row
     * 
     * @param fullRow
     *            is the row to be destroyed
     */
    protected void destroyRow(int fullRow) {
        int lowestRow = rowsCache.getLowestNotNullRow();
        for (int row = fullRow; row > lowestRow; row = rowBefore(row)) {
            setBricksToRow(row, fallDownRow(rowBefore(row)));
        }
        clearRow(lowestRow);
    }

    /**
     * Fall down the row
     * 
     * @param row
     *            to be fall down
     * @return raw previous fall down one level
     */
    private List<Brick> fallDownRow(Integer row) {
        List<Brick> rowBricks = getBricksInRow(row);
        for (Brick brick : rowBricks) {
            brick.y = brick.y + brick.getBoxSize();
        }
        return rowBricks;
    }

    /**
     * Checks for the collision of figure with wall
     * 
     * @param figure
     *            to be checked for collision
     * @throws CollisionWithWalException
     *             is thrown when figure is in collision with wall
     */
    public void checkCollision(Figure figure) throws CollisionWithWalException {
        for (Brick brickFigure : figure.getBricks()) {
            for (Brick brickInTheWall : getAllBricks()) {
                if (brickInTheWall.intersects(brickFigure)) {
                    log.debug("Figure is in collision with wall... CollisionWithWalException");
                    throw new CollisionWithWalException();
                }
            }
        }
    }

    /**
     * Returns bricks in the row
     * 
     * @return bricks in the row
     */
    private List<Brick> getBricksInRow(int row) {
        return rows.get(row);
    }

    /**
     * Returns number of bricks in the row
     * 
     * @return number of bricks in the row
     */
    public int getNumberOfBricksInRow(int row) {
        return rows.get(row).size();
    }

    /**
     * Returns all bricks in the wall
     * 
     * @return all bricks in the wall
     */
    private List<Brick> getAllBricks() {
        List<Brick> result = new ArrayList<Brick>();
        for (int row : rows.keySet()) {
            result.addAll(rows.get(row));
        }
        return result;
    }

    /**
     * Returns number of all bricks in the wall
     * 
     * @return number of all bricks in the wall
     */
    public int getNumberOfAllBricks() {
        return getAllBricks().size();
    }

    /**
     * Paints the wall on the screen
     * 
     * @param g
     *            is offscreen graphics
     */
    public void paint(Graphics g) {
        for (Brick brick : getAllBricks())
            brick.paint(g);
    }

    /**
     * Returns row before the row
     * 
     * @param row
     * @return row before the row
     */
    private int rowBefore(int row) {
        return row - boxSize;
    }

    /**
     * Sets the bricks for the row
     * 
     * @param row
     *            for which we set the bricks
     * @param bricks
     *            to be set for row
     */
    private void setBricksToRow(int row, List<Brick> bricks) {
        rows.put(row, bricks);
        rowsCache.setRow(row, bricks.size() * boxSize);
    }

    /**
     * Adds brick to the row
     * 
     * @param row
     *            for which we add the brick
     * @param brick
     *            to be added to row
     */
    private void addBrickToRow(int row, Brick brick) {
        try {
            rows.get(row).add(brick);
            rowsCache.increaseRow(row);
        } catch (Exception e) {
            throw new RuntimeException("Null Pointer exception. Probably row value is out of playground");
        }
    }

    /**
     * Clear the bricks in the precise row
     * 
     * @param row
     *            for which we want to clear the bricks
     */
    private void clearRow(int row) {
        rows.put(row, new ArrayList<Brick>());
        rowsCache.clearRow(row);
        log.debug("Row " + row + " cleared");
    }
}
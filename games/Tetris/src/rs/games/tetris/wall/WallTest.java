package rs.games.tetris.wall;

import java.awt.Point;

import junit.framework.TestCase;
import rs.games.tetris.Tetris;
import rs.games.tetris.figures.BFigure;
import rs.games.tetris.figures.GFigure;
import rs.games.tetris.figures.IFigure;

/**
 * Tests all public methods from Wall object
 * 
 * @author Zeljko
 * 
 */
public class WallTest extends TestCase {
    private Tetris tetris;
    private Wall wall;

    @Override
    protected void setUp() throws Exception {
        this.tetris = new Tetris();
        tetris.initSwing();
        this.wall = tetris.getWall();
    }

    public void testAddInTheWall() {
        prepareAlmostTwoFullLines();

        wall.addToWall(new IFigure(null, new Point(190, 460)));

        assertEquals(20, wall.getNumberOfBricksInRow(490));
        assertEquals(20, wall.getNumberOfBricksInRow(480));
        assertEquals(2, wall.getNumberOfBricksInRow(470));
        assertEquals(2, wall.getNumberOfBricksInRow(460));
        assertEquals(44, wall.getNumberOfAllBricks());

        wall.destroyRow(480);

        assertEquals(20, wall.getNumberOfBricksInRow(490));
        assertEquals(2, wall.getNumberOfBricksInRow(480));
        assertEquals(2, wall.getNumberOfBricksInRow(470));
        assertEquals(0, wall.getNumberOfBricksInRow(460));
        assertEquals(24, wall.getNumberOfAllBricks());

        wall.destroyRow(490);

        assertEquals(2, wall.getNumberOfBricksInRow(490));
        assertEquals(2, wall.getNumberOfBricksInRow(480));
        assertEquals(0, wall.getNumberOfBricksInRow(470));
        assertEquals(0, wall.getNumberOfBricksInRow(460));
        assertEquals(4, wall.getNumberOfAllBricks());
    }

    public void testPutIntoWallTwoFullRows() {
        prepareAlmostTwoFullLines();

        wall.putIntoWall(new IFigure(null, new Point(190, 460)));

        assertEquals(2, wall.getNumberOfBricksInRow(490));
        assertEquals(2, wall.getNumberOfBricksInRow(480));
        assertEquals(0, wall.getNumberOfBricksInRow(470));
        assertEquals(0, wall.getNumberOfBricksInRow(460));
        assertEquals(4, wall.getNumberOfAllBricks());
    }

    public void testPutIntoWallOneFullRow() {
        prepareAlmostOneFullLine();
        
        wall.putIntoWall(new IFigure(null, new Point(190, 460)));

        assertEquals(11, tetris.getWall().getNumberOfBricksInRow(490));
        assertEquals(11, tetris.getWall().getNumberOfBricksInRow(480));
        assertEquals(2, tetris.getWall().getNumberOfBricksInRow(470));
        assertEquals(0, tetris.getWall().getNumberOfBricksInRow(460));
        assertEquals(24, tetris.getWall().getNumberOfAllBricks());
    }

    private void prepareAlmostOneFullLine() {
        // create initial wall, last column is empty...
        wall.putIntoWall(new GFigure(null, new Point(0, 470)));
        wall.putIntoWall(new GFigure(null, new Point(20, 470)));
        wall.putIntoWall(new GFigure(null, new Point(40, 470)));
        wall.putIntoWall(new GFigure(null, new Point(60, 470)));
        wall.putIntoWall(new GFigure(null, new Point(80, 470)));
        wall.putIntoWall(new GFigure(null, new Point(100, 470)));
        wall.putIntoWall(new GFigure(null, new Point(120, 470)));
        wall.putIntoWall(new GFigure(null, new Point(140, 470)));
        wall.putIntoWall(new GFigure(null, new Point(160, 470)));
        wall.putIntoWall(new IFigure(null, new Point(180, 460)));

        // test if everything is as we expect
        assertEquals(19, wall.getNumberOfBricksInRow(490));
        assertEquals(10, wall.getNumberOfBricksInRow(480));
        assertEquals(10, wall.getNumberOfBricksInRow(470));
        assertEquals(1, wall.getNumberOfBricksInRow(460));
        assertEquals(40, wall.getNumberOfAllBricks());
    }

    private void prepareAlmostTwoFullLines() {
        // create initial wall, last column is empty...
        wall.putIntoWall(new BFigure(null, new Point(0, 480)));
        wall.putIntoWall(new BFigure(null, new Point(20, 480)));
        wall.putIntoWall(new BFigure(null, new Point(40, 480)));
        wall.putIntoWall(new BFigure(null, new Point(60, 480)));
        wall.putIntoWall(new BFigure(null, new Point(80, 480)));
        wall.putIntoWall(new BFigure(null, new Point(100, 480)));
        wall.putIntoWall(new BFigure(null, new Point(120, 480)));
        wall.putIntoWall(new BFigure(null, new Point(140, 480)));
        wall.putIntoWall(new BFigure(null, new Point(160, 480)));
        wall.putIntoWall(new IFigure(null, new Point(180, 460)));

        // test if everything is as we expect
        assertEquals(19, wall.getNumberOfBricksInRow(490));
        assertEquals(19, wall.getNumberOfBricksInRow(480));
        assertEquals(1, wall.getNumberOfBricksInRow(470));
        assertEquals(1, wall.getNumberOfBricksInRow(460));
        assertEquals(40, wall.getNumberOfAllBricks());
    }
}
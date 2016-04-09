package rs.games.tetris.wall;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This class is responsible for tracking activities on wall data model. This
 * class speeds up finding lowest non null row, findings full rows and etc.
 * 
 * @author Zeljko
 * 
 */
class RowsCache {

	// Properties

	protected final Logger log = Logger.getLogger(getClass());
	private final Map<Integer, Integer> rowsCache = new HashMap<Integer, Integer>();
	private final int fullRowWidth;
	private final int boxSize;

	// Constructor

	public RowsCache(int fullRowWidth, int boxSize) {
		this.fullRowWidth = fullRowWidth;
		this.boxSize = boxSize;
	}

	/**
	 * Checks if there are full rows
	 * 
	 * @return true if there are one or more full rows
	 */
	public boolean hasFullRow() {
		return rowsCache.containsValue(fullRowWidth);
	}

	/**
	 * Finds lowest full row in the wall
	 * 
	 * @return lowest row or null if there are no full rows in the wall
	 */
	public Integer getLowestFullRow() {
		Object[] rows = (Object[]) rowsCache.keySet().toArray();
		Arrays.sort(rows);
		for (Object rowO : rows) {
			Integer row = (Integer) rowO;
			int width = rowsCache.get(row);
			if (width == fullRowWidth) {
				return row;
			}
		}
		return null;

	}

	/**
	 * Finds lowest row in the wall with at least one brick
	 * 
	 * @return lowest not null row or null if there are no bricks in the wall
	 */
	public Integer getLowestNotNullRow() {
		Object[] rows = (Object[]) rowsCache.keySet().toArray();
		Arrays.sort(rows);
		for (Object rowO : rows) {
			Integer row = (Integer) rowO;
			int width = rowsCache.get(row);
			if (width > 0) {
				return row;
			}
		}
		return null;
	}

	/**
	 * Set the absolute width for row
	 * 
	 * @param row
	 *            for which we set the width
	 * @param width
	 *            to be set for row
	 */
	public void setRow(int row, int width) {
		rowsCache.put(row, width);
	}

	/**
	 * Increase width for row
	 * 
	 * @param row
	 *            for which we set the width
	 */
	public void increaseRow(int row) {
		int oldValue = rowsCache.get(row);
		rowsCache.put(row, oldValue + boxSize);
	}

	public void clearRow(int row) {
		rowsCache.put(row, 0);
	}
}
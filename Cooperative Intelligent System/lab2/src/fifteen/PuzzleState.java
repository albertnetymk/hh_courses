package fifteen;
import java.util.*;

/**
 * <p>Title: 15-puzzle</p>
 * <p>Description: Game for studying search techniques</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class PuzzleState {
  public int[][] state;
  public Vector history; // the moves which took us here

  /** identify which position (row) that is empty on the map
   *
   */
  int emptyGetRow() {
    for (int r=0; r<state.length; r++) {
      for (int c=0; c<state[r].length; c++) {
        if (state[r][c]==0)
          return r;
      }
    }
    return 0; // actually one should throw an exception...
  }

  public boolean equals(Object o) {
	  if(this == o) {
		  return true;
	  }
	  if(o instanceof PuzzleState) {
		  if ( getClass().getName().equals(o.getClass().getName()) ) {
			  for(int i=0; i<state.length; ++i) {
				  for(int j=0; j<state[0].length; ++j) {
					  if( state[i][j] != ((PuzzleState)o).state[i][j] ) {
						  return false;
					  }
				  }
			  }
			  return true;
		  }
	  }
	  return false;
  }


  /** identify which position (column) that is empty on the map
   *
   */
  int emptyGetCol() {
    for (int r=0; r<state.length; r++) {
      for (int c=0; c<state[r].length; c++) {
        if (state[r][c]==0)
          return c;
      }
    }
    return 0; // actually one should throw an exception...
  }

  /** create a new state based on a source state and a move
   *
   */
  PuzzleState nextState(Direction move) {
    PuzzleState next=null;
    int rowEmpty=emptyGetRow();
    int colEmpty=emptyGetCol();
    if (move==Direction.UP && rowEmpty!=0) {
      next=new PuzzleState(this, move); // make a copy of the current tile configuration
      next.state[rowEmpty][colEmpty]=next.state[rowEmpty-1][colEmpty]; // fill empty spot with tile to the NORTH
      next.state[rowEmpty-1][colEmpty]=0; // new empty spot
    } else if (move==Direction.DOWN && rowEmpty!=(state.length-1)) {
      next=new PuzzleState(this, move);
      next.state[rowEmpty][colEmpty]=next.state[rowEmpty+1][colEmpty];
      next.state[rowEmpty+1][colEmpty]=0;
    } else if (move==Direction.LEFT && colEmpty!=0) {
      next=new PuzzleState(this, move);
      next.state[rowEmpty][colEmpty]=next.state[rowEmpty][colEmpty-1];
      next.state[rowEmpty][colEmpty-1]=0;
    } else if (move==Direction.RIGHT && colEmpty!=(state[0].length-1)) {
      next=new PuzzleState(this, move);
      next.state[rowEmpty][colEmpty]=next.state[rowEmpty][colEmpty+1];
      next.state[rowEmpty][colEmpty+1]=0;
    }
    return next;
  }

  /** Identify and return possible "next states".
   *  Note that possible states get ordered...
   */
  PuzzleState[] expandState() {
	ArrayList<PuzzleState> possible = new ArrayList<PuzzleState>();
	for( Direction d : Direction.values()) {
      if (nextState(d)!=null &&(history.isEmpty() || !isPreviousState(d))) {
      // if (nextState(d)!=null) {
		  possible.add(nextState(d));
	  }
	}
	return possible.toArray(new PuzzleState[possible.size()]);
  }

  private boolean isPreviousState(Direction d) {
	  switch(d) {
		  case UP: 
			  return history.lastElement() == Direction.DOWN;
		  case DOWN: 
			  return history.lastElement() == Direction.UP;
		  case RIGHT: 
			  return history.lastElement() == Direction.LEFT;
		  case LEFT: 
			  return history.lastElement() == Direction.RIGHT;
		default:
			  throw new RuntimeException("Other value for Direction");
	  }
  }

  public String toString() {
	  StringBuilder sb = new StringBuilder();
	  for(int i=0; i<state.length; ++i) {
		  for(int j=0; j<state[i].length; ++j) {
			  sb.append(state[i][j] + "\t");
		  }
		  sb.append("\n");
	  }
	  return sb.toString();
  }

  /** checks if goal is fulfilled
   *
   */
  public boolean goal(int[][] map) {
    for (int r=0; r<state.length; r++) {
      for (int c=0; c<state[r].length; c++)
        if (state[r][c]!=map[r][c])
          return false;
    }
    return true;
  }

  /** reset state history
   *
   */
  public void clear() {
    history=new Vector();
  }

  /** creates an instance of a state with a specified tile configuration
   *  and a clean trace.
   */
  public PuzzleState(int[][] orig) {
    state=new int[4][4];
    for (int r=0; r<orig.length; r++) {
      for (int c=0; c<orig[r].length; c++)
        state[r][c]=orig[r][c];
    }
    history=new Vector();
  }

  /** creates an instance of a state from an ascending state and a specified move.
   *  The trace is updated.
   */
  public PuzzleState(PuzzleState ascendant, Direction move) {
    state=new int[ascendant.state.length][ascendant.state[0].length];
    for (int r=0; r<state.length; r++) {
      for (int c=0; c<state[r].length; c++)
        state[r][c]=ascendant.state[r][c];
    }
    history=new Vector(ascendant.history);
    history.add(move); // add the current move to trace
  }

}

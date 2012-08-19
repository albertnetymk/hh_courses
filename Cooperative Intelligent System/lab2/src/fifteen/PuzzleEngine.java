package fifteen;
import java.util.*;
import java.awt.*;

/**
 * <p>Title: 15-puzzle</p>
 * <p>Description: Game for studying search techniques</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

/** The PuzzleEngine class manages the search process.
 *  It has a current "state".
 *  If a search has been completed and there is a solution, it knows it and how to approach a goal state.
 *
 */
public class PuzzleEngine {
	private final static int EVALMAX = 10000;
	private final static int MAXLEVEL = 30;
	PuzzleState state;        // the current state
	public int evalCount=0;   // the number of nodes searched, re-init when search is re-started
	Vector solution=null;     // the steps to a goal state (in temporal order)

	public Label label=null;  // if not null, we update this GUI label with search status

	/** returns the current configuration of tiles
	 *
	 */
	public int[][] getState() {
		return state.state;
	}

	/** take a step towards a solution
	 *  @return the configuration of tiles resulting
	 */
	public int[][] step() {
		state=state.nextState((Direction)solution.firstElement());
		solution.removeElementAt(0);
		return getState();
	}

	/** start search
	 *  @param  goal the configuration of tiles to search for
	 *  @param  labelSearched the GUI label to update with search status
	 *  @return the trace of steps towards the solution (also stored in "solution").
	 */
	public Vector search(int[][] goal, Label labelSearched) {
		evalCount=0;
		solution=null;
		label=labelSearched;
		Vector queue=new Vector();
		state.clear();
		queue.add(state);

		Vector tmpSolution;

		tmpSolution = searchWithHeuristicFunction(goal, queue, new Displaced(goal));
		if(tmpSolution != null) {
			System.out.println("Displaced: Evaluation time: " + evalCount + " Level: " + tmpSolution.size());
		} else {
			System.out.println("Displaced: Evaluation time: " + evalCount + " Aborted");
		}
		evalCount = 0;

		tmpSolution = searchWithHeuristicFunction(goal, queue, new DisplacedPlusTaxicabDistance(goal) );
		if(tmpSolution != null) {
			System.out.println("Improved: Evaluation time: " + evalCount + " Level: " + tmpSolution.size());
		} else {
			System.out.println("Improved: Evaluation time: " + evalCount + " Aborted");
		}
		evalCount = 0;

		return tmpSolution;
		// return searchActively(goal, queue);
		// return searchDepthFirst(goal, queue, 15);
		// return search(goal, queue);
	}

	private static class Displaced implements Comparator<PuzzleState> {
		protected int[][] goal;
		public Displaced(int[][] goal) {
			this.goal = goal;
		}
		public int compare(PuzzleState o1, PuzzleState o2) {
			// h(x) heuristic function
			int unmatched_1=0, unmatched_2=0;
			for(int i=0; i<o1.state.length; ++i) {
				for(int j=0; j<o1.state[0].length; ++j) {
					if(o1.state[i][j] != goal[i][j]) {
						unmatched_1++;
					}
					if(o2.state[i][j] != goal[i][j]) {
						unmatched_2++;
					}
				}
			}
			// f(x) actual cost to come to this state.
			unmatched_1 += o1.history.size();
			unmatched_2 += o2.history.size();
			return unmatched_1 - unmatched_2;
		}
	}

	private static class DisplacedPlusTaxicabDistance implements Comparator<PuzzleState> {
		private int[][] goal;
		private int distance_1=0;
		private int distance_2=0;
		DisplacedPlusTaxicabDistance(int[][] goal) {
			this.goal = goal;
		}

		public int getDistance_1() {
			return distance_1;
		}
		@Override
		public int compare(PuzzleState o1, PuzzleState o2) {
			distance_1=0; distance_2=0;
			for(int i=0; i<o1.state.length; ++i) {
				for(int j=0; j<o1.state[0].length; ++j) {
					if(o1.state[i][j] != goal[i][j]) {
						for(int i_i=0; i_i<goal.length; ++i_i) {
							for(int i_j=0; i_j<goal[0].length; ++i_j) {
								if(o1.state[i_i][i_j] == goal[i][j]) {
									distance_1 += (Math.abs(i_i - i) + Math.abs(i_j - j));
									i_i = goal.length;
									break;
								}
							}
						}
					}
					if(o2.state[i][j] != goal[i][j]) {
						for(int i_i=0; i_i<goal.length; ++i_i) {
							for(int i_j=0; i_j<goal[0].length; ++i_j) {
								if(o2.state[i_i][i_j] == goal[i][j]) {
									distance_2 += (Math.abs(i_i - i) + Math.abs(i_j - j));
									i_i = goal.length;
									break;
								}
							}
						}
					}
				}
			}
			// return super.compare(o1, o2) + distance_1 - distance_2;
			distance_1 += o1.history.size();
			distance_2 += o2.history.size();
			return distance_1 - distance_2;
		}
	}

	private static void printPriorityQueue(PriorityQueue<PuzzleState> queue) {
		Queue<PuzzleState> q = new PriorityQueue<PuzzleState>(queue);
		while(!q.isEmpty()) {
			System.out.println(q.poll());
		}
	}

	private Vector searchWithHeuristicFunction(int[][] goal, Vector queue, Comparator<PuzzleState> comparator) {
		PriorityQueue<PuzzleState> priorityQueue = new PriorityQueue<PuzzleState>(10, comparator);
		priorityQueue.add((PuzzleState)queue.firstElement());
		while(!priorityQueue.isEmpty()) {
			evalCount++;
			if(evalCount > EVALMAX) {
				/*
				int i = 1;
				PuzzleState previous = priorityQueue.poll();
				while(!priorityQueue.isEmpty()) {
					PuzzleState s = priorityQueue.poll();
					System.out.println("Comparison between " + i + " " + (i+1) + " " +
							priorityQueue.comparator().compare(previous, s));
					System.out.println(previous);
					System.out.println(s);
					previous = s;
					i++;
				}
				*/
				return null;
			}
			PuzzleState head = priorityQueue.peek();
			if(head.goal(goal)) {
				/*
				int i = 1;
				PuzzleState previous = priorityQueue.poll();
				while(!priorityQueue.isEmpty()) {
					PuzzleState s = priorityQueue.poll();
					System.out.println(previous);
					DisplacedPlusTaxicabDistance tmp = (DisplacedPlusTaxicabDistance)priorityQueue.comparator();
					tmp.compare(previous, s);
					System.out.println("Distance is " + tmp.getDistance_1());
					// System.out.println(previous);
					// System.out.println(s);
					previous = s;
					i++;
				}
				*/
				return solution = head.history;
			}
			/*
			// if(priorityQueue.size() <= 10) {
			if(priorityQueue.size() == 3) {
				System.out.println("head is\n" + head);
				System.out.println("new nodes are " + head.expandState().length);
				printPriorityQueue(priorityQueue);
			}
			*/
			priorityQueue.poll();
			if(evalCount % 100 == 0) {
				label.setText("#states: " + evalCount + " level: " + head.history.size());
			}
			// System.out.println("eval " + evalCount);
			for(PuzzleState s : head.expandState()) {
			// 	System.out.println(s);
				PuzzleState selected = null;
				if(priorityQueue.contains(s)) {
					for(PuzzleState sInQueue : priorityQueue) {
						if(s.equals(sInQueue)) {
							selected = sInQueue;
							break;
						}
					}
				}
				// if(selected != null && s.history.size() < selected.history.size()) {
				if(selected != null && priorityQueue.comparator().compare(s, selected) < 0) {
					// The same state has existed in this queue and the new value is better.
					// Doing this check using f(x).
					priorityQueue.remove(selected);
					priorityQueue.add(s);
					selected = null;
				} else {
					priorityQueue.add(s);
				}
			}
			// System.out.println("Size of queue :" + priorityQueue.size());
		}
		return null;
	}

	public Vector searchActively(int[][] goal, Vector queue) {
		Vector result;
		for(int i=1; i<=MAXLEVEL; ++i) {
			result = searchDepthFirst(goal, new Vector(queue), i);
			if(result != null) {
				return result;
			}
		}
		return null;
	}

	public Vector searchDepthFirst(int[][] goal, Vector queue, int maxLevel) {
		while(!queue.isEmpty()) {
			evalCount++;
			if(evalCount > EVALMAX) {
				return null;
			}
			PuzzleState head = (PuzzleState)queue.firstElement();
			if(head.goal(goal)) {
				return solution = head.history;
			}
			if(evalCount % 100 == 0) {
				label.setText("#states: " + evalCount + " level: " + head.history.size());
			}
			if(head.history.size() >= maxLevel) {
				queue.removeElementAt(0);
			} else {
				PuzzleState[] newNodes = head.expandState();
				queue.removeElementAt(0);
				for(int i=0; i<newNodes.length; ++i) {
					queue.add(0, newNodes[i]);
				}
			}
		}
		return null;
	}

	/** start search with a initialized queue,
	 *  @see search(int[][] goal, Label labelSearched)
	 *  @param  goal  what to search for
	 *  @param  queue which states to start with
	 *  @return the trace of steps towards the solution (also stored in "solution").
	 */
	public Vector search(int[][] goal, Vector queue) {
		while (!queue.isEmpty()) {                    // if queue is empty, all options are exhausted
			evalCount++;                                // keep track of how many states we've looked at
			if (evalCount> EVALMAX)                       // too many evaluations are required...
				return null;                              // abort!
			PuzzleState head=(PuzzleState)queue.firstElement(); // first state in queue...
			System.out.println(head);
			if (head.goal(goal))                        // ...was a success!
				return (solution=head.history);           //    hence return it's trace

			if (label!=null && evalCount%100==0) {      // update search status
				label.setText("#states:"+evalCount+" level:"+head.history.size());
			}
			PuzzleState[] newNodes=head.expandState();  // expand the current state
			queue.removeElementAt(0);                   // remove the expanded state
			// below: breadth-first strategy (new states are put at the end of the queue).
			for (int n=0; n<newNodes.length; n++) {
				queue.add(newNodes[n]);                   // adds one node at a time
			}
		}
		return null;                                  // failure!
	}

	/** shuffle the board
	 *  @param  seed  seeding the randomizer
	 *  @param  nStep number of attempted random moves
	 */
	public void randomize(int seed, int nStep) {
		Random rand=new Random(seed);
		for (int i=0; i<nStep; i++) {
			int move;
			PuzzleState tmp;
			while(true) {
				move=rand.nextInt(Direction.values().length); // choose one randomly
				tmp=state.nextState(Direction.values()[move]);
				if (tmp!=null) { // illegal move
					break;
				}
			}
			state=tmp;
		}
	}

	/** init engine using start state
	 *
	 */
	public PuzzleEngine(int[][] start) {
		state=new PuzzleState(start);
	}
}

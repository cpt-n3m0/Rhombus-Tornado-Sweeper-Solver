import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

import net.sf.tweety.commons.ParserException;

public class SATX {

	private KnowledgeBase KB;
	private int[][] variableTable;
	private int[] clauseVariables;
	private Board board;
	private Game game;

	// private ArrayList<Location> uncovered;
	public SATX(Board board) {
		this.board = board;
		this.game = new Game(this.board);
		this.KB = new KnowledgeBase(this.game, this.board);
	}

	public void play() throws ParserException, IOException , TimeoutException {
		game.updateBoardState();
		
		int previouslyCovered= 0;
		
		while (!game.isOver()) {
			
			KB.buildKnowledgeBase();
			ArrayList<Location> covered= game.getCoveredCells();
			if(covered.size() == previouslyCovered)
			{
				Location rl = board.getRandomLocation();
				if(game.isUncovered(rl) || game.isMarked(rl))
					continue;
				System.out.println("Agent stuck, uncovering random location " + rl.toString() );
				game.probe(rl);
				game.uncover(rl);
				game.updateBoardState();
				if(board.getContent(rl) == 't')
				{
					System.out.println("Game Over");
					continue;
				}
					
				
			}
			previouslyCovered = covered.size();
			
			for(Location l: covered)
			{
				boolean hasMine = KB.ask(l, true);
				boolean notMine  = KB.ask(l, false);
				if( notMine == false)
				{
					System.out.println("Infered that location " + l.toString() + " has mine");
					game.mark(l);
					game.updateBoardState();
				}
				else if( hasMine == false)
				{
					
					System.out.println("Infered that location " + l.toString() + " is safe");
					game.uncover(l);
					game.updateBoardState();
				}
			}
						
			
		}
		if(game.won())
		{
			this.board.printBoard();
			System.out.println("won !");
			System.out.println("Marked cells : " + game.getMarked().toString() );
		}

	}


}

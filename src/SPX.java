import java.util.ArrayList;

public class SPX {

	private Board board;

	public SPX(Board board) {
		this.board = board;
	}


	public void play() {

		Game game = new Game(this.board);
		char clue = '?';
		int previouslyCvrd = 0;
		while (!game.isOver()) {
			if(game.getCoveredCells().size() == previouslyCvrd)
			{
				Location rand = board.getRandomLocation();

				if (game.isProbed(rand) || game.isMarked(rand))
					continue;

				game.uncover(rand);
				System.out.println("Agent stuck. probing random cell at " + rand.toString());

			}
			previouslyCvrd = game.getCoveredCells().size();
			ArrayList<Location> covered;
			for(int i = 0 ; i < game.getUncovered().size(); i++) {
				Location current = game.getUncovered().get(i);
			//	Location current = game.getNext();

				clue = game.probe(current);

				if (clue == 't') {
					System.out.println("Game Over");
					break;
				}
				covered = game.getCoveredNeighbours(current);
				if(covered.size() == 0)
					continue;
				if (game.isAFN(current)) {
					System.out.println(current.toString() + " has AFN adding " + covered.toString() + "to S");
					game.uncover(covered);
					game.updateBoardState();
				} else if (game.isAMN(current)) {
					System.out.println("AMN case found at : " + current.toString());
					game.mark(covered);
					game.updateBoardState();
				}

			}

		}

		if (game.won()) {

			System.out.println("Win !");
			System.out.println("Marked tornado locations : " + game.getMarked().toString());

		}

	}

}

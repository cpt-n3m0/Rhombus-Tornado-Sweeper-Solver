import java.util.ArrayList;

public class RPX {

    private Board board;

    public RPX(Board board)
    {
        this.board = board;
    }


    public void play()
    {
    	Game game = new Game(this.board);

        while(!game.isOver())
        {
            Location nextLoc = board.getRandomLocation();
            if(game.isProbed(nextLoc))
                continue;
            char clue = game.probe(nextLoc);
            game.uncover(nextLoc);
            game.updateBoardState();
            if(clue == 't')
            {
                System.out.println("Game Over.");
                break;
            }
            
           
        }

        if(game.won())
            System.out.println("Agent has won the game");
    }


}

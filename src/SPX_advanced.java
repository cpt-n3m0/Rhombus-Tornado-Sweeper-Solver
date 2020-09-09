import java.util.ArrayList;
import java.util.Collection;

public class SPX_advanced {
     
	Board board;
	//ArrayList<Location> uncovered;
	
	public SPX_advanced(Board board)
	{
		this.board = board;
	}
	
     public void play()
     {

    	 Game game = new Game(this.board);
    	 char clue = '?';
          while(!game.isOver())
          {
        	  if(game.getUncovered().isEmpty())
        	  {
				  do
				  {
					  Location rand = board.getRandomLocation();
	 
					  if(game.isProbed(rand) || game.isMarked(rand))
						  continue;
					  
					  game.uncover(rand);  
					  System.out.println("Agent stuck. probing random cell at " + rand.toString());
					 
				  }
				  while(game.getUncovered().isEmpty());
        	  }        	   
        	  
		       ArrayList<Location> covered; 
               while(!game.getUncovered().isEmpty())
               {
            	    game.updateBoardState();
                    Location current = game.getNext();
                    
                   clue  = game.probe(current);

                    if(clue == 't')
                    {
                         System.out.println("Game Over");
                         break;
                    }
                    covered = game.getCoveredNeighbours(current);
                    if(game.isAFN(current))
                    {
                    	System.out.println(current.toString() + " has AFN adding " + covered.toString() + "to S"); 
                       game.uncover(covered);
                       game.updateBoardState();
        
                    }
                     else
                     {
                    	System.out.println(current.toString() + " is uncertain.");
                        game.addUncertain(current);
						
                     }
               }
               if (clue == 't') continue;
               
               ArrayList<Location> uncertain = game.getUncertain();
               for (int q = 0 ; q < uncertain.size() ; q++)
               {
                    covered = game.getCoveredNeighbours(uncertain.get(q));
                    if(game.isAMN(uncertain.get(q))) {
                    	 System.out.println("AMN case found at : " + uncertain.get(q).toString());
                         game.mark(covered);
                         uncertain.remove(q);
                         q--;
			
						  game.updateBoardState();
                    }
               }
               
               for (int r = 0 ; r <uncertain.size() ; r++) {
                    covered = game.getCoveredNeighbours(uncertain.get(r));
                    if (game.isAFN(uncertain.get(r))) {
                    	System.out.println("AFN case found at : " + uncertain.get(r).toString());
                        game.uncover(covered);
                        uncertain.remove(r);
                         r--;

						  game.updateBoardState();
                    }
               }
               game.setUncertain(uncertain);
          }

          if(game.won())
          {
          
               System.out.println("Win !");
               System.out.println("Marked tornado locations : " + game.getMarked().toString());

          }



     }

}

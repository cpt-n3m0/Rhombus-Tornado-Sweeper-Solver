import java.util.ArrayList;
import java.util.Collection;

public class Game {
	private ArrayList<Location> probed;
    private ArrayList<Location> marked;
    private ArrayList<Location> uncovered;
    private ArrayList<Location> uncertain;
    private boolean probedTornado;
    
    Board board;
    Board gameBoard;
    
    
    public Game(Board board)
    {
    	 this.probedTornado = false;
         this.board = board;
         this.probed = new ArrayList<Location>();
         this.marked = new ArrayList<Location>();
         this.uncovered = new ArrayList<Location>();
         this.uncertain = new ArrayList<Location>();
         this.gameBoard = board.makePlayBoard();
         this.uncover(new Location(0,0));
         this.uncover(this.board.getCenterCell());
    }
    
   
    public boolean isMarked(Location loc)
    {
    	return isInCollection(marked, loc);
    }
    public boolean isProbed(Location loc)

    {
    	return isInCollection(probed, loc);
    }
    public boolean isUncertain(Location loc)
    {
    	return isInCollection(uncertain, loc);
    }
    public boolean isUncovered(Location loc)
    {
    	return isInCollection(uncovered, loc);
    }
    
    
    public void uncover(Location loc)
    {
    	if(this.isUncovered(loc))
    		return;
    	this.uncovered.add(loc);
    }
    public void uncover(Collection<Location> loc)
    {
    	for(Location l: loc)
    		this.uncover(l);
    }
    public void mark(Location loc)
    {
    	this.marked.add(loc);
    }
    public void mark(Collection<Location> loc)
    {
    	this.marked.addAll(loc);
    }
    public void addUncertain(Location loc)
    {
    	this.uncertain.add(loc);
    }
    public void addUncertain(Collection<Location> loc)
    {
    	this.uncertain.addAll(loc);
    }
    public void setUncertain(ArrayList<Location> uc)
    {
    	this.uncertain = uc;
    }

   
    public boolean isOver()
    {
    	return this.marked.size() == board.getT() || probedTornado;
    }
    public boolean won()
    {
    	return !probedTornado;
    }

	public ArrayList<Location> getProbed() {
		return probed;
	}



	public ArrayList<Location> getMarked() {
		return marked;
	}
	
	public void updateBoardState()
	{
		for(Location uc: uncovered)
			this.gameBoard.setValue(uc, this.board.getContent(uc));

		for(Location mrkd: marked)
			this.gameBoard.setValue(mrkd, '*');
		
		this.gameBoard.printBoard();
	}

	public Location getNext()
	{
		return this.uncovered.remove(0);
	}

	public ArrayList<Location> getUncovered() {
		return uncovered;
	}



	public ArrayList<Location> getUncertain() {
		return uncertain;
	}



	public Board getBoard() {
		return board;
	}



	public boolean isAFN (Location loc)
    {
         int marked = 0;
         int clue;

         clue = board.getContent(loc) - 48;
         marked = getMarkedNeighbours(loc).size();

         if(clue == 0 || clue == marked)
              return true;

         return false;

    }
    public boolean isAMN(Location loc)
    {
         int marked = 0;
         int clue;

         clue = board.getContent(loc) - 48;
         marked = getMarkedNeighbours(loc).size();
         int covered = getCoveredNeighbours(loc).size();

         if(covered == (clue - marked))
              return true;

         return false;
    }
    private boolean isInCollection(Collection<Location> col, Location loc)
    {
         for (Location l: col)
         {
              if (l.equals(loc)) {
                   return true;
              }
         }
         return false;
    }


    public  ArrayList<Location> getMarkedNeighbours(Location loc)
    {
         ArrayList<Location> mrkd = new ArrayList<Location>() ;
         for (Location n : board.getAdjacent(loc))
         {
              if(isInCollection(marked, n))
                   mrkd.add(n);

         }

         return mrkd;
    }
    public ArrayList<Location> getCoveredNeighbours(Location loc){
         ArrayList<Location> covered = new ArrayList<Location>() ;
         for (Location n : board.getAdjacent(loc))
         {
              if(isInCollection(marked, n))
                   continue;

              if(!isInCollection(this.uncovered, n) && !isInCollection(probed, n))
                   covered.add(n);
         }

         return covered;
    }
    
    public Board getGameBoard()
    {
    	return this.gameBoard;
    }
    

    public ArrayList<Location> getCoveredCells()
    {
    	ArrayList<Location> covered = new ArrayList<Location>(); 
    	for(int i = 0 ; i < board.getBoardSizeY(); i++)
    	{
    		for(int j = 0 ; j < board.getBoardSizeX(); j ++)
    		{
    			Location loc = new Location(i, j);
    			if(this.isProbed(loc) || this.isMarked(loc) || this.isUncovered(loc))
    				continue;
    			covered.add(loc);
    		}
    	}
    	return covered;
    }
    public char probe(Location loc)
    {
         System.out.println("probe " + loc.toString());
         if(board.isValidLoc(loc)) {

             if(board.getContent(loc) == 't')
              	this.probedTornado = true;
        	  
	     this.probed.add(loc);
	     return board.getContent(loc);
         }
         System.out.println("Invalid location");
		return 0;
         

    }

	
}

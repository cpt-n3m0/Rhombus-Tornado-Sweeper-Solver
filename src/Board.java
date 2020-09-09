/*
 * CS5011 A2main Starter code
 * This class holds the board to be played
 * An example on how to print the board is given
 * 
 * author Alice Toniolo
 * 
 * From another class you can access, set and print a world map as
 * World world = World.valueOf("TEST0");
 * Board board = new Board(world.map);
 * board.printBoard();
 * 
 */


import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Board {

	char[][] board;
	int T;

	public Board(char[][] map) {
		board = map;
		this.T = calculateT();
	}


	public Board makePlayBoard()
	{
		char[][] playBoard = Arrays.copyOf(this.board, this.board.length);
		for(int i = 0; i < playBoard.length; i++)
		{
			playBoard[i] = Arrays.copyOf(this.board[i], this.board[i].length);
		}
		
		for (int i = 0; i < playBoard.length; i++)
			for(int j= 0; j < playBoard[0].length ; j++)
				playBoard[i][j] = '?';
		
		return new Board(playBoard);
	}
	
	
	public void setValue(Location loc, char val)
	{
		if(this.isValidLoc(loc))
			this.board[loc.getY()][loc.getX()] = val;
		else
			System.out.println("Invalid location");
	}
    
	public boolean isValidLoc(Location loc)
    {
        return (loc.getY() < board.length && loc.getY() >= 0 && loc.getX() < board[0].length && loc.getX() >= 0 );
    }

	public Location getCenterCell()
	{
		return new Location(this.board.length/2, this.board[0].length/2);
	}
    public char getContent(Location loc)
    {
        return board[loc.getY()][loc.getX()];
    }

	public Location getRandomLocation()
	{
		Random randGen = new Random();
		
		int y =  (int)(randGen.nextInt(board.length));
		int x =  (int)(randGen.nextInt(board[0].length));
		return new Location(x, y);
	}
	
	public int getBoardSizeY()
	{
		return this.board.length;
	}
	public int getBoardSizeX()
	{
		return this.board[0].length;
	}

	public ArrayList<Location> getAdjacent(Location loc)
	{
	    Location[] nonHexAdjacent = {new Location(loc.getX() - 1, loc.getY() + 1), new Location(loc.getX() + 1, loc.getY() - 1)};
	    ArrayList<Location> adjacents= new ArrayList<Location>();

		for (int  i = loc.getY() - 1; i <= loc.getY() + 1; i++)
		{
			for (int j = loc.getX() -1 ; j <= loc.getX() + 1; j++) {
				Location adj = new Location(j, i);
				if(adj.equals(loc) || adj.equals(nonHexAdjacent[0]) || adj.equals(nonHexAdjacent[1]) || !isValidLoc(adj))
					continue;
				adjacents.add(adj);

			}
		}

		return adjacents;

	}
	public int calculateT()
	{
		int T = 0;
		for (int i = 0; i < this.board.length; i ++)
			for(int j = 0; j < this.board[0].length; j++)
				if(board[i][j] == 't')
					T++;
		return T;
	}

	public int getT() {
		return T;
	}

	public int getBoardSize()
	{
		return board.length * board[0].length;
	}

	public void printBoard()
	{
		printBoard(this.board);
	}
	// method to print the board
	public void printBoard(char[][] board) {
		System.out.println();
		// first line
		for (int l = 0; l < board.length + 5; l++) {
			System.out.print(" ");// shift to start
		}
		for (int j = 0; j < board[0].length; j++) {
			System.out.print(j);// x indexes
			if (j < 10) {
				System.out.print(" ");
			}
		}
		System.out.println();
		// second line
		for (int l = 0; l < board.length + 3; l++) {
			System.out.print(" ");
		}
		for (int j = 0; j < board[0].length; j++) {
			System.out.print(" -");// separator
		}
		System.out.println();
		// the board
		for (int i = 0; i < board.length; i++) {
			for (int l = i; l < board.length - 1; l++) {
				System.out.print(" ");// fill with left-hand spaces
			}
			if (i < 10) {
				System.out.print(" ");
			}

			System.out.print(i + "/ ");// index+separator
			for (int j = 0; j < board[0].length; j++) {
				System.out.print(board[i][j] + " ");// value in the board
			}
			System.out.println();
		}
		System.out.println();
	}
}

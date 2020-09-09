import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import org.sat4j.*;
import org.sat4j.core.VecInt;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;

//import org.logicng.*;
import net.sf.tweety.commons.ParserException;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.PlBeliefSet;
import net.sf.tweety.logics.pl.syntax.PlFormula;




public class KnowledgeBase {
	private int[][] variableTable;
	private Game game;
	private ArrayList<String> knowledge;
	private Board board;
	private int[][] permutations;
	private int pc;
	private ArrayList<Location> toProbe;
	private ISolver solver;
	
	
	public KnowledgeBase(Game game, Board board)
	{
		this.game = game;
		this.board = board;
		this.toProbe = new ArrayList<Location>();
		createVarTable();
		for(int i = 0 ; i < variableTable.length ; i++)
		{
			for(int j = 0 ; j < variableTable.length ; j++)
				System.out.print(variableTable[i][j] + " ");
			System.out.println("");
		}
	}
	
	public void buildKnowledgeBase()
	{

		this.knowledge = new ArrayList<String>();
		int uncovered = game.getUncovered().size();
		for( int i = 0; i < uncovered; i++)
		{
			tell(game.getUncovered().get(i), false);
			uncovered = game.getUncovered().size();
		}
		/*int marked = game.getMarked().size();
		for( int i = 0; i < marked; i++)
		{
			tell(game.getMarked().get(i), true);
			marked = game.getMarked().size();
		}*/
		
	}
	public void createVarTable()
	{
		int[][] vars = new int[board.getBoardSizeY()][board.getBoardSizeX()];
		int varCount = 0;
		for(int i = 0; i < vars.length ; i++)
			for(int j = 0 ; j < vars[0].length; j++)
				vars[i][j] = ++varCount;
		this.variableTable = vars;
	}
	
	private void updateToProbe()
	{
		for(int i = 0 ; i < this.toProbe.size() ; i++)
		{
			if(game.isUncovered(this.toProbe.get(i)));
			{
				this.toProbe.remove(i);
				i--;
			}
		}
	}
	public ArrayList<Location> getToProbe()
	{
		updateToProbe();
		return this.toProbe;
	}
	public void tell(Location loc, boolean hasMine)
	{
		int clue = board.getContent(loc) - 48;
		int var = variableTable[loc.getY()][loc.getX()];
		int marked = game.getMarkedNeighbours(loc).size();
		int rem = clue - marked;
		String formula = "";
		
		
		this.knowledge.add(hasMine?(String.valueOf(var)):("!" + String.valueOf(var)));
		
		ArrayList<Location> covered = game.getCoveredNeighbours(loc);
		
		if(rem == 0)
		{
			if(covered.size() > 0)
			{
				System.out.println("AFN case detected at " + loc.toString());
			       	System.out.println(" Clearning neighbours " + covered.toString());
			}
			for(Location safeNeighbour: covered)
			{
				game.uncover(safeNeighbour);
				game.updateBoardState();
				this.knowledge.add("!" + String.valueOf(variableTable[loc.getY()][loc.getX()]));
			}
			return;
		}
		
		int[] vars = new int[6];
		int counter = 0;
		for(Location c: covered)
		{
			vars[counter++] = variableTable[c.getY()][c.getX()];
			
		}
		int [] data = new int[rem];	
		permutations = new int[10000][rem];
		pc = 0;
		combinationUtil(vars, counter, rem, 0, data, 0);
		for (int i = 0; i< pc; i++)
		{
			formula += "(";
			for (int j = 0; j< rem; j++)
				formula += String.valueOf(permutations[i][j]) + " && ";
			if(rem > 0)
				formula = formula.substring(0, formula.length() - 4); 
			formula += ") || ";
		}

		formula = formula.substring(0, formula.length() - 4); 
		this.knowledge.add(formula);
		
	}
	private String getKBFormula()
	{
		String formula = "";
		for(String clause: this.knowledge)
		{
			formula += "(" + clause + ") && ";
			
		}
	 
		return formula.substring(0, formula.length() - 4);
	}
	public String makeQueryClause(Location loc, boolean hasMine)
	{
		String var = String.valueOf(this.variableTable[loc.getY()][loc.getX()]);
		var =  (hasMine == true)?var:("!" + var);
		
		return " && (" + var + ")";
	}
	public boolean ask(Location loc, boolean hasMine) throws ParserException, IOException , TimeoutException
	{
		
		String formula = getKBFormula() +  makeQueryClause(loc, hasMine);
	
		int MAXVAR = variableTable.length * variableTable[0].length + 1;
		int NBCLAUSES = 500000;
		
		
		this.solver  = SolverFactory.newDefault();
		solver.newVar(MAXVAR);
		solver.setExpectedNumberOfClauses(NBCLAUSES);

		
	//	System.out.println("Formula is : "  + formula);
		PlBeliefSet kb = new PlBeliefSet();
		PlParser parser = new PlParser();
		
		kb.add((PlFormula)parser.parseFormula(formula));
		
		Conjunction conj = kb.toCnf();
	
		// prepare solver
		ListIterator<PlFormula> iter = conj.listIterator();
		
		while(iter.hasNext())
		{
			PlFormula clause = iter.next();
			ArrayList<Integer> c = new ArrayList<Integer>();
			for(PlFormula l: clause.getLiterals())
			{
				String ls = l.toString();
				if(ls.startsWith("!"))
					c.add(-Integer.valueOf(ls.substring(1, ls.length())));
				else
					c.add(Integer.valueOf(ls));
				
			}
			int[] sclause = new int[c.size()];
			for(int i = 0 ; i < c.size() ; i++)
			{
				sclause[i] = c.get(i);
			}
			//System.out.println("adding clause : " + c.toString());
			
			try {
				solver.addClause(new VecInt(sclause));
			} catch (ContradictionException e) {
				return false;
			}
			
		}
		
		IProblem problem = solver;
		return problem.isSatisfiable();
		
		
	}
	
	
	/* CombinationUtil's code has been adapted from "https://www.geeksforgeeks.org/print-subsets-given-size-set/" */
	 private void combinationUtil(int arr[], int n, int r, 
             int index, int data[], int i) 
	{
// Current combination is ready to be printed,  
// print it 
		if (index == r) {
			permutations[pc++] = Arrays.copyOf(data, data.length);
		
			return;
		}

		if (i >= n)
			return;
 
		data[index] = arr[i];
		combinationUtil(arr, n, r, index + 1, data, i + 1);

		combinationUtil(arr, n, r, index, data, i + 1);
	}


}

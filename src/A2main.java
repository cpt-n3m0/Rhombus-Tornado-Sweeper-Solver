import java.io.IOException;
import java.util.ArrayList;

import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

import net.sf.tweety.commons.ParserException;

import java.util.Date;


public class A2main {



    private static void evaluate()
    {
		System.out.println("Testing SPX");

    }

    public static void main(String[] args) throws ParserException, IOException, ContradictionException, TimeoutException
    {
        World w ;
         
        
       	System.out.println(args.length);

	if(args[0].equals("evaluate"))
	{
		evaluate();
	}

        if(args.length != 2)
        {
        	System.out.println(args.length);
        	System.out.println("Invalid number of argumetns");
        	return;
        }

        switch (args[1])
        {
			case "TEST0":
        		w = World.TEST0;
        		break;
			case "TEST1":
				w = World.TEST1;
				break;
			case "TEST2":
				w = World.TEST2;
				break;
			case "S1":
        		w = World.S1;
        		break;
			case "S2":
				w = World.S2;
				break;
			case "S3":
				w = World.S3;
				break;
			case "M1":
				w = World.M1;
				break;
			case "M2":
				w = World.M2;
				break;
			case "M3":
				w = World.M3;
				break;
			case "L1":
				w = World.L1;
				break;
			case "L2":
				w = World.L2;
				break;
			case "L3":
				w = World.L3;
				break;	
			default :
				w = World.TEST0;
				
			
        }
        Board b = new Board(w.map);      
        
        switch(args[0])
        {
        	case "SPX":
        		(new SPX(b)).play();
        		break;
        	case "SPXadv":
        		( new SPX_advanced(b)).play();
        		break;
        	case "RPX":
        		(new RPX(b)).play();
        		break;
        	case "SATX":
        		(new SATX(b)).play();
        		
        }

    }
}





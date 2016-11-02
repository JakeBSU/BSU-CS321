import java.io.File;
import java.io.FileNotFoundException;

public class GeneBankCreateBTree {
    public static void main (String[] args){


            if (args.length() < 4 || args.length() > 6 ){
            //print usage
                System.out.println("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
				System.out.println("[<cache size>] should only be specified if you are using a cache.");
            	exit(1);
            }
     		else{
				if (args[0] == 0){
					cachelessBTree();
				}
				else if (args[0] == 1){
					cachedBTree();
				}
				else{
					System.out.println("First argument must be a 0 or 1 to determine if cache is used");
					System.out.println("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
					System.out.println("[<cache size>] should only be specified if you are using a cache.");
					exit(1);
				}

            }
	}

		
	public static <T> void cachelessBTree(){ // generic? static necessary?
		
		int degree = Integer.parseInt(args[1]);
		String gbName = args[2];
		int sequence = Integer.parseInt(args[3]);
		int debug = 0;
		
		if (args.length == 5){ 
			 int temp = Integer.parseInt[4];
            if (temp == 1){
                debug = 1;
            }

		}

		if (degree == 0){
		//generate opitmal degree
		system.out.println("Generating optimal degree based on disc size"); 
		degree = OptimalDegreeGenerator.generate();		
		}
		
		if (!sequence >= 1){
			System.out.println("specified sequence is too short, or is negative");
			System.out.println("Please specifiy a sequence length such that 1 <= sequence <= 31")
			System.out.println("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
			exit(1);					
		}

		if (!sequence <= 31){
			System.out.println("specified sequence length is too long.")
			 System.out.println("Please specifiy a sequence length such that 1 <= sequence <= 31")
            System.out.println("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
            exit(1);
		}

		//create BTree here
		BTree tree = new BTree(degree, sequence);

		try {
			File gbFile = new File(gbName); // TODO double check that this is working. 
			//here is where parsing takes place 
			//Each sequence will be passed to BTree as a new BTree Object, and handled there
						
		
		}
		catch(FileNotFoundException fnfe){
			system.out.println("Genebank File Not Found: Program Terminated.");
			exit(1);
		}	
	
	}




	public static <T> void cachedBTree(){
	
	//This will, more or less, be the same as the above. Will update once we've discussed cache implementation.	
	 	int degree = Integer.parseInt(args[1]);
        String gbName = args[2];
        int sequence = Integer.parseInt(args[3]);
        int cacheSize = Integer.parseInt(args[4]); 
		int debug = 0;

        if (args.length == 6){
        	int temp = Integer.parseInt[5];
			if (temp == 1){
				debug = 1;
			}
		}

        if (degree == 0){
        //generate opitmal degree
        system.out.println("Generating optimal degree based on disc size");
        degree = OptimalDegreeGenerator.generate();
        }

        if (!sequence >= 1){
            System.out.println("specified sequence is too short, or is negative");
            System.out.println("Please specifiy a sequence length such that 1 <= sequence <= 31")
            System.out.println("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
            exit(1);
        }

        if (!sequence <= 31){
            System.out.println("specified sequence length is too long.")
             System.out.println("Please specifiy a sequence length such that 1 <= sequence <= 31")
            System.out.println("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
            exit(1);
        }

        //create BTree here
        BTree tree = new BTree(degree, sequence);

        try {
            File gbFile = new File(gbName); // TODO double check that this is working.
            //here is where parsing takes place
            //Each sequence will be passed to BTree as a new BTree Object, and handled there	
	}
						
}

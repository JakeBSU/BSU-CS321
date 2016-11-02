public class GeneBankSearch {
	
	public static void main (String[] args){
	 	
		if (args.length() < 4 || args.length() > 5 ){
            //print usage
                System.out.println("Usage: java GeneBankSearch <0/1(no/with Cache)> <btree file name> <query file name> [<cache size>] [<debug level>]");
                System.out.println("[<cache size>] should only be specified if you are using a cache.");
            	exit(1);
            }
            	else{
                if (args[0] == 0){
                    cachelessSearch();
                }
                else if (args[0] == 1){
                    cachedSearch();
                }
                else{
             	   System.out.println("First argument must be a 0 or 1 to determine if cache is used");
                	System.out.println("Usage: java GeneBankSearch <0/1(no/with Cache)> <btree file name> <query file name> [<cache size>] [<debug level>]");
                	System.out.println("[<cache size>] should only be specified if you are using a cache.");
					exit(1);
                }

            }
    }

	public static <T> void cachelessSearch(){
	
	}

	public static <T> void cachedSearch(){
	//TODO figure out cached usage, and how it differs from above
	}
	
}

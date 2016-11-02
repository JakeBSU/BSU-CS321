import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * @author Jayden Weaver
 * Driver class. See usage. 
 */
public class HashTest {
	private static double load;
	public static void main(String[] args){
		try{
			load = Double.parseDouble(args[1]);
			if (load > 1 || load < 0){
				usage();
			}
			if (args.length == 2 || Integer.parseInt(args[2]) == 0){
				if(Integer.parseInt(args[0]) == 1){				
					randomDebug0();
					System.exit(1);
				}else if(Integer.parseInt(args[0]) ==2){
					currentDebug0();
					System.exit(1);
				}else if(Integer.parseInt(args[0]) ==3){
					fileDebug0();
					System.exit(1);
				}
			}
			if (args.length == 3){
				int debug = Integer.parseInt(args[2]);
				if(Integer.parseInt(args[0]) == 1){
					if(debug == 1){
						randomDebug1();
						System.exit(1);
					}
					if(debug == 2){
						randomDebug2();
						System.exit(1);
					}
				}else if(Integer.parseInt(args[0]) == 2){
					if(debug == 1){
						currentDebug1();
						System.exit(1);
					}
					if (debug == 2){
						currentDebug2();
						System.exit(1);
					}
				}else if(Integer.parseInt(args[0]) == 3){
					if(debug == 1){
						fileDebug1();
						System.exit(1);
					}
					if (debug == 2){
						fileDebug2();
						System.exit(1);
					}
				}
			}if(Integer.parseInt(args[0]) > 3 || Integer.parseInt(args[0]) < 1){
				usage();
			}
			else{
				usage();
			}
		}catch(Exception e){
			usage();
		}
	}
	
	public static void randomDebug0(){
		HashTable<Integer> linear = new HashTable<Integer>(load, 0);
		DoubleHash<Integer> trial = new DoubleHash<Integer>(load,0);
		System.out.println("A good table size is: " + linear.getSize());
		System.out.println("Data Source Type: Random Number Generator\n");
		System.out.println("Using Linear Hashing...");
		while(linear.getLoad() < load){
			Random rand = new Random();
			HashObject<Integer> hash1 = new HashObject<Integer>(Math.abs(rand.nextInt()));
			linear.linearProbing(hash1);
			trial.hashIt(hash1);
		}
		System.out.println("Inserted: " + linear.getNumberOfItemsLin() + " elements, of which " + linear.getDuplicates1() + " were duplicates");
		System.out.println("Load Factor = "+load+" Avg. Probes: " + linear.avgProbes1()+ "\n");
		System.out.println("Using Double Hashing...");
		System.out.println("Inserted: " + trial.getItems() + " elements, of which " + linear.getDuplicates1() + " were duplicates");
		System.out.println("Load Factor = "+ load +" Avg. Probes: " + trial.getAvg()+ "\n");
	}

	public static void randomDebug1(){
		HashTable<Integer> linear = new HashTable<Integer>(load, 0);
		DoubleHash<Integer> trial = new DoubleHash<Integer>(load,0);
		while(linear.getLoad() < load){
			Random rand = new Random();
			HashObject<Integer> hash1 = new HashObject<Integer>(Math.abs(rand.nextInt()));
			linear.linearProbing(hash1);
			trial.hashIt(hash1);
		}
		System.out.println("Linear Hash Table");
		linear.viewTable();
		System.out.println("Double Hash Table");
		trial.viewTable();
	}

	public static void randomDebug2(){
		HashTable<Integer> linear = new HashTable<Integer>(load, 0);
		DoubleHash<Integer> trial = new DoubleHash<Integer>(load,0);
		while(linear.getLoad() < load){
			Random rand = new Random();
			HashObject<Integer> hash1 = new HashObject<Integer>(Math.abs(rand.nextInt()));
			linear.linearProbing(hash1);
			trial.hashIt(hash1);
		}
		System.out.println("Linear Hash Table");
		linear.viewTable1();
		System.out.println("Double Hash Table");
		trial.viewTable1();
	}

	public static void currentDebug0(){
		HashTable<Long> linear = new HashTable<Long>(load, 0);
		DoubleHash<Long> trial = new DoubleHash<Long>(load,0);
		System.out.println("A good table size is: " + linear.getSize());
		System.out.println("Data Source Type: System Time\n");
		System.out.println("Using Linear Hashing...");
		while(linear.getLoad() < load){
			HashObject<Long> hashIt = new HashObject<Long>(System.currentTimeMillis());
			linear.linearProbing(hashIt);
			trial.hashIt(hashIt);
		}
		System.out.println("Inserted: " + linear.getNumberOfItemsLin() + " elements, of which " + linear.getDuplicates1() + " were duplicates");
		System.out.println("Load Factor = "+load+" Avg. Probes: " + (double)linear.numProbes1()/linear.getHashReq()+ "\n");
		System.out.println("Using Double Hashing...");
		System.out.println("Inserted: " + trial.getItems() + " elements, of which " + linear.getDuplicates1() + " were duplicates");
		System.out.println("Load Factor = " +load +" Avg. Probes: " + trial.getAvg()+ "\n");
	}

	public static void currentDebug1(){
		HashTable<Long> linear = new HashTable<Long>(load, 0);
		DoubleHash<Long> trial = new DoubleHash<Long>(load,0);
		while(linear.getLoad() < load){
			HashObject<Long> hashIt = new HashObject<Long>(System.currentTimeMillis());
			linear.linearProbing(hashIt);
			trial.hashIt(hashIt);
		}
		System.out.println("Linear Hash Table");
		linear.viewTable();
		System.out.println("Double Hash Table");
		trial.viewTable();
	}

	public static void currentDebug2(){
		HashTable<Long> linear = new HashTable<Long>(load, 0);
		DoubleHash<Long> trial = new DoubleHash<Long>(load,0);
		while(linear.getLoad() < load){
			HashObject<Long> hashIt = new HashObject<Long>(System.currentTimeMillis());
			linear.linearProbing(hashIt);
			trial.hashIt(hashIt);
		}
		System.out.println("Linear Hash Table");
		linear.viewTable1();
		System.out.println("Double Hash Table");
		trial.viewTable1();
	}

	public static void fileDebug0() throws FileNotFoundException{
		HashTable<String> linear = new HashTable<String>(load, 0);
		DoubleHash<String> trial = new DoubleHash<String>(load,0);
		System.out.println("A good table size is: " + linear.getSize());
		System.out.println("Data Source Type: word-list\n");
		System.out.println("Using Linear Hashing...");
		File file = new File("word-list");
		Scanner scan = new Scanner(file);
		while( linear.getLoad() < load && scan.hasNextLine()){
			HashObject<String> hashIt = new HashObject<String>(scan.nextLine());
			linear.linearProbing(hashIt);
			trial.hashIt(hashIt);
		}
		System.out.println("Inserted: " + linear.getNumberOfItemsLin() + " elements, of which " + linear.getDuplicates1() + " were duplicates");
		System.out.println("Load Factor = " + load +  " Avg. Probes: " + (double)linear.numProbes1()/linear.getHashReq()+ "\n");
		System.out.println("Using Double Hashing...");
		System.out.println("Inserted: " + trial.getItems() + " elements, of which " + linear.getDuplicates1() + " were duplicates");
		System.out.println("Load Factor = " +load +" Avg. Probes: " + trial.getAvg()+ "\n");
		scan.close();
	}

	public static void fileDebug1() throws FileNotFoundException{
		HashTable<String> linear = new HashTable<String>(load, 0);
		DoubleHash<String> trial = new DoubleHash<String>(load,0);
		File file = new File("word-list");
		Scanner scan = new Scanner(file);
		while(scan.hasNextLine()){
			HashObject<String> hashIt = new HashObject<String>(scan.nextLine());
			linear.linearProbing(hashIt);
			trial.hashIt(hashIt);
		}
		System.out.println("Linear Hash Table");
		linear.viewTable();
		System.out.println("Double Hash Table");
		trial.viewTable();
		scan.close();
	}

	public static void fileDebug2() throws FileNotFoundException{
		HashTable<String> linear = new HashTable<String>(load, 0);
		DoubleHash<String> trial = new DoubleHash<String>(load,0);
		File file = new File("word-list");
		Scanner scan = new Scanner(file);
		while(scan.hasNextLine()){
			HashObject<String> hashIt = new HashObject<String>(scan.nextLine());
			linear.linearProbing(hashIt);
			trial.hashIt(hashIt);
		}
		System.out.println("Linear Hash Table");
		linear.viewTable1();
		System.out.println("Double Hash Table");
		trial.viewTable1();
		scan.close();
	}

	public static void usage(){
		System.err.println("Java HashTest <input type> <load factor> [<debug level>]");
	}
}

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class GeneBankSearch
{
    private static BTree btree;
    private static int t;
    private static int k;
    private static int debug = 0;
    private static int cacheSize;
    private static String btreeFile;
    private static String queryFile;

    public static void main(String[] args)
    {
	/* If the argument count is not 3 or 4 exit the program */
        if(args.length < 3 || args.length > 5)
        {
            printUsage();
            System.exit(1);
        }

        if((Integer.parseInt(args[0]) == 0)){
            btreeFile = args[1];
            queryFile = args[2];

            StringTokenizer st = new StringTokenizer(btreeFile, ".");
            int count = st.countTokens();
            String tokens[]= new String[count];
            int j = 0;
            while(j <= count){
                while(st.hasMoreElements()) {
                    tokens[j] = st.nextToken();
                    j++;
                }
                j++;
            }

            k = Integer.parseInt(tokens[count - 2]);
            t = Integer.parseInt(tokens[count - 1]);

            if((args.length == 4)){
                debug = Integer.parseInt(args[3]);
            }
        }else if((Integer.parseInt(args[0]) == 1)){
            btreeFile = args[1];
            queryFile = args[2];

            StringTokenizer st = new StringTokenizer(btreeFile, ".");
            int count = st.countTokens();
            String tokens[]= new String[count];
            int j = 0;
            while(j <= count) {
                while(st.hasMoreElements()) {
                    tokens[j] = st.nextToken();
                    j++;
                }
                j++;
            }

            k = Integer.parseInt(tokens[count - 2]);
            t = Integer.parseInt(tokens[count - 1]);
            if(args.length == 4){
                cacheSize = Integer.parseInt(args[3]);
            }
            if(args.length == 5){
                cacheSize = Integer.parseInt(args[3]);
                debug = Integer.parseInt(args[4]);
            }

        }else{
            printUsage();
        }

        String queryInput;
        Scanner scan;

        try
        {
            btree = new BTree(new File(btreeFile), t, k, debug,cacheSize);
            scan = new Scanner(new FileReader(queryFile));

            System.out.println("Sequence | Frequency\n--------------------");
            while (scan.hasNextLine())
            {
                queryInput = scan.nextLine();
                TreeObject t = new TreeObject(ConvertDNAToLong.convertToLong(queryInput));
                System.out.println(queryInput + " --> " + btree.search(t));
            }
        }
        catch (IllegalArgumentException e)
        {
            System.err.println("The degree in the btree file doesn't not match the one being passed int");
            System.exit(1);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("BTree file not found");
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("File error");
            System.exit(1);
        }
    }

    private static void printUsage()
    {
        System.err.println("Usage:\n\t" +
                "<0/1(no/with Cache)> <btree file> <query file> [<cache size>] " +
                "[<debug level>]");
    }
}

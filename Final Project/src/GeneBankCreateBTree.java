import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static java.lang.System.exit;

public class GeneBankCreateBTree {
    public static void main (String[] args){
        if (args.length < 4 || args.length > 6 ){
            //print usage
            System.err.println("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
            System.err.println("[<cache size>] should only be specified if you are using a cache.");
            exit(1);
        }
        else{
            if (Integer.parseInt(args[0]) == 0){
                cachelessBTree(args);
            }
            else if (Integer.parseInt(args[0]) == 1){
                cachedBTree(args);
            }
            else {
                System.err.println("First argument must be a 0 or 1 to determine if cache is used");
                System.err.println("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
                System.err.println("[<cache size>] should only be specified if you are using a cache.");
                exit(1);
            }

        }
    }

    public static void cachelessBTree(String[] args) {

        int degree = Integer.parseInt(args[1]);

        String gbName = args[2];
        int sequence = Integer.parseInt(args[3]);
        int debug = 0;

        String genFileName = args[2];
        if(degree == 0)
            genFileName += ".btree.data." + sequence + "." + BTree.optimalDegree();

        if (args.length == 5){
            int temp = Integer.parseInt(args[4]);
            if (temp == 1){
                debug = 1;
            }

        }

        if (sequence < 1){
            System.err.println("specified sequence is too short, or is negative");
            System.err.println("Please specifiy a sequence length such that 1 <= sequence <= 31");
            System.err.println("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
            exit(1);
        }

        if (sequence > 31){
            System.err.println("specified sequence length is too long.");
            System.err.println("Please specifiy a sequence length such that 1 <= sequence <= 31");
            System.err.println("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
            exit(1);
        }

        try {
            Parser p = new Parser();
            File gbFile = new File(gbName);
            ArrayList<Long> dna = p.parse(gbFile, sequence);

            BTree tree = new BTree(degree, sequence, genFileName, debug, 0);


            //Each sequence will be passed to BTree as a new BTree Object, and handled there
            int noSeqs = dna.size();
            for(int i = 0; i < noSeqs; i++){
                TreeObject obj = new TreeObject(dna.get(i));
                tree.insert(obj);
            }
            tree.writeTree();
            if (debug == 1)
                tree.writeDumpFile();
        } catch(IOException e) {
            System.err.println("Genebank File Not Found: Program Terminated.");
            exit(1);
        }

    }

    public static void cachedBTree(String[] args) {
        int cacheSize = 0;
        int debug = 0;

        if (Integer.parseInt(args[0]) == 1)
            cacheSize = Integer.parseInt(args[4]);
        if(Integer.parseInt(args[5]) == 1)
            debug = 1;

        int degree = Integer.parseInt(args[1]);
        String gbkFile = args[2];
        int sequenceLength = Integer.parseInt(args[3]);
        String genFileName = gbkFile+ ".btree.data." + sequenceLength + "." + degree;


        try {
            Parser p = new Parser();
            File gbFile = new File(gbkFile);
            ArrayList<Long> dna = p.parse(gbFile, sequenceLength);

            BTree tree = new BTree(degree, sequenceLength, genFileName, debug, cacheSize);


            //Each sequence will be passed to BTree as a new BTree Object, and handled there
            int noSeqs = dna.size();
            for(int i = 0; i < noSeqs; i++) {
                TreeObject obj = new TreeObject(dna.get(i));
                tree.insert(obj);
            }
            tree.writeTree();
            if (debug == 1)
                tree.writeDumpFile();
        } catch(IOException e) {
            System.err.println("Genebank File Not Found: Program Terminated.");
            exit(1);
        }
    }

}

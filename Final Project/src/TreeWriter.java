import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

/**
 * Created by pkannah on 4/22/16.
 * Working project: P4_Bioinfomatics.
 */
public class TreeWriter {
    int seqLength;
    private RandomAccessFile fOut;
    private File file;
    int[] array = new int[400];
    //Creates the file based on the specified name and length
    public TreeWriter(String filename) {
        Random rand = new Random();

        for (int i = 0; i <array.length ; i++) {
            array[i] = rand.nextInt(100) + 1;
        }
        file = new File(filename);
        try {
            fOut = new RandomAccessFile(filename, "rwd"); //everyupdate to the file is also written to disk
            //TODO write metadata

            for(int i = 0; i < array.length; i++) {
               fOut.write(array[i]);
            }
//            fOut.write(10);fOut.write(140);fOut.write(120);fOut.write(2222);fOut.write(500);
            fOut.close();
        }
        catch (IOException e) {
            System.out.println("something up");
        }
    }

    public TreeWriter(File file) {
        try {
//            System.out.println("insdie of construct 2212312");
            fOut = new RandomAccessFile(file, "rwd");
            //TODO write metadata
            fOut.write(7); //sequence length
            fOut.write(4); //number of nodes, including root
            //TODO write root
            fOut.close();
        }
        catch (IOException e) {
            System.out.println("something is up");
        }
    }

    //TODO parameter should be a node not a TreeObject
    public void writeToDisk(Object object) {

    }

    //TODO should return a BTreeNode (index?) instead of void
    public void readFromDisk() throws IOException {
        fOut = new RandomAccessFile(file, "rwd");
        fOut.seek(350);
        for(long i = fOut.getFilePointer();i < fOut.length(); i++) {
            System.out.print(fOut.read() + " ");
        }
    }


    public static void main(String[] args) throws Exception {
        TreeWriter writer = new TreeWriter("TREE_WRITER_TEST");
//        TreeWriter writer1 = new TreeWriter("test1.gbk.btree.data.1");

        writer.readFromDisk();
    }
}

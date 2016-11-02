import java.io.*;
import java.util.Stack;

@SuppressWarnings ("WeakerAccess")
public class BTree {
    private BTreeNode root, current, next;
    private TreeWriter writer;
    private int degree, numNodes, seqLength;
    private static int debug;
    private String fName;

    public BTree(int degree, int length, String fileName, int debug, int cacheSize)
    {
        if (degree == 0)
            this.degree = optimalDegree();
        else
            this.degree = degree;

        seqLength = length;
        this.debug = debug;
        fName = fileName.substring(0, fileName.indexOf('k')+1);
        File f = new File(fileName);
        //noinspection ResultOfMethodCallIgnored
        f.delete();

        try {
            writer = new TreeWriter(fileName, this.degree, this.seqLength, cacheSize); //setup file to write to
        }catch (IOException ignored){}

        root = new BTreeNode(this.degree, true, true, 0);
        numNodes = 1;
    }

    public BTree(File bTreeFile, int degree, int seqLength, int debug, int cacheSize) {
        try {
            writer = new TreeWriter(bTreeFile.getName(), degree, seqLength, cacheSize);
        }catch (IOException ignored){}

        this.debug = debug;
        fName = bTreeFile.getName().substring(0,bTreeFile.getName().indexOf('k')+1);

        try
        {
            this.degree = writer.readDegree();
            int rootIndex = writer.readRootIndex();
            this.seqLength = writer.readSeqLength();

            if (this.degree != degree && seqLength != this.seqLength)
                throw new IllegalStateException("The specified degree & sequence length doesn't match the ones in the file");

            root = writer.diskRead(rootIndex,degree);
            root.setRoot(true);
        } catch (IOException e) {System.out.println("can't open bTreeFile");}
    }



    private void splitChild(BTreeNode parent, BTreeNode nodeToSplit, int index) {
        if(parent.isFull() || !nodeToSplit.isFull())
            throw new IllegalStateException();

        if(debug == 0)
            System.err.println("Child split with parent node " + parent.getNodeIndex() + ", split indexes: " +
                    nodeToSplit.getNodeIndex() +" & " + index);

        BTreeNode newChild = new BTreeNode(degree, false, nodeToSplit.isLeaf(), numNodes);
        numNodes++;

        if(debug == 0)
            System.err.println("New child node: " + newChild.getNodeIndex() + " -> Parent: " +parent.getNodeIndex());

        for (int p = nodeToSplit.keys.size() - 1; p > degree - 1; p--) {
            newChild.keys.add(0, nodeToSplit.keys.remove(p));
        }

        if (!nodeToSplit.isLeaf()) {
            for (int j = nodeToSplit.children.size() - 1; j >= degree; j--) {
                newChild.children.add(0, nodeToSplit.children.remove(j));
            }
        }

        parent.children.add(index + 1, newChild.getNodeIndex());
        newChild.setParent(parent.getNodeIndex());
        parent.keys.add(index, nodeToSplit.keys.remove(degree - 1));

        try {
            writer.diskWrite(parent, false);
            writer.diskWrite(nodeToSplit, false);
            writer.diskWrite(newChild, false);
        } catch (IOException e){e.printStackTrace();}
    }

    private void insertNonfull(TreeObject o) throws IOException
    {
        current = root;
        Long treeKey = o.getKey();

        while (true)
        {
            int index = current.keys.size() - 1;
            if (current.isLeaf())
            {
                while (index >= 0 && treeKey.compareTo(current.keys.get(index).getKey()) <= 0)
                {
                    if (treeKey.compareTo(current.keys.get(index).getKey()) == 0)
                    {
                        current.keys.get(index).increaseFrequency();
                        writer.diskWrite(current, false);
                        if(debug == 0)
                            System.err.println("Seq: " +
                                    ConvertDNAToLong.convertFromLong(treeKey) + ", Freq: "
                                    + current.keys.get(index).getFrequency()
                                    + ", Node: " + current.getNodeIndex());
                        return;
                    }
                    index--;
                }
                current.keys.add(index + 1, o);
                if(debug == 0)
                    System.err.println("Inserted " + ConvertDNAToLong.convertFromLong(treeKey) +
                            " at node " + current.getNodeIndex() );

                writer.diskWrite(current, false);
                break;
            } //END OF current.isLef()
            else
            {
                while (index >= 0 && treeKey.compareTo(current.keys.get(index).getKey()) <= 0)
                {
                    if (treeKey.compareTo(current.keys.get(index).getKey()) == 0)
                    {
                        current.keys.get(index).increaseFrequency();
                        writer.diskWrite(current, false);

                        if(debug == 0)
                            System.err.println("Seq: " +
                                    ConvertDNAToLong.convertFromLong(treeKey) + ", Freq: " +
                                    current.keys.get(index).getFrequency()
                                    + ", Node: " + current.getNodeIndex());

                        return;
                    }
                    index--;
                }
                index++;
                next = writer.diskRead(current.children.get(index), degree);
                if (next.isFull())
                {
                    splitChild(current, next, index);
                    if (treeKey.compareTo(current.keys.get(index).getKey()) == 0)
                    {
                        current.keys.get(index).increaseFrequency();
                        writer.diskWrite(current, false);
                        if(debug == 0)
                            System.err.println("Seq: " +
                                    ConvertDNAToLong.convertFromLong(treeKey) + ", Freq: " +
                                    current.keys.get(index).getFrequency()
                                    + ", Node: " + current.getNodeIndex());

                        return;
                    } else if (treeKey.compareTo(current.keys.get(index).getKey()) > 0)
                        next = writer.diskRead(current.children.get(index + 1), degree);
                }
                current = next;
            } //END of else block
        }
    }

    public void insert(TreeObject o) throws IOException {
        if (root.isFull()) {
            next = root;

            root = new BTreeNode(degree, true, false, numNodes++);
            root.children.add(0, next.getNodeIndex());
            next.setParent(root.getNodeIndex());
            next.setRoot(false);

            if(debug == 0)
                System.err.println("New root node: " + root.getNodeIndex());

            splitChild(root, next, 0);
            insertNonfull(o);
        } else
            insertNonfull(o);
    }

    private int nodeSearch(BTreeNode node, TreeObject searchKey)
            throws IOException {
        current = node;
        Long key = searchKey.getKey();
        while (true) {
            int i = 0;
            while (i < current.keys.size()
                    && key.compareTo(current.keys.get(i).getKey()) > 0){
                i++;
            }

            if (i < current.keys.size()
                    && key.compareTo(current.keys.get(i).getKey()) == 0)
            {
                return current.keys.get(i).getFrequency();
            }

            if (current.isLeaf()) {
                return 0; // case where sequence is not found
            } else {
                current = writer.diskRead(current.children.get(i), degree);
            }
        }
    }

    public void writeTree() {
        try{
            writer.flushTree(current,next,root);
        } catch (IOException e){e.printStackTrace();}
    }

    public int search(TreeObject o) throws IOException {
        return nodeSearch(root, o);
    }

    public void writeDumpFile() throws IOException
    {
        boolean traversing = true;
        current = root;
        Stack<Integer> children = new Stack<>();
        Stack<Integer> treeNodes = new Stack<>();
        FileOutputStream outFileStream = new FileOutputStream(new File("dump.txt"));
        PrintWriter outStream = new PrintWriter(outFileStream);
        int childIndex = 0;

        outStream.write("BTree Info:\n----------\nDegree: " + degree + "\nSequence length: " + seqLength
                + "\nGenebank file: " + fName + "\n\n");

        while (traversing)
        {
            if (childIndex == current.children.size() && !current.isLeaf())
            {
                if (treeNodes.isEmpty() && children.isEmpty())
                {
                    traversing = false;
                    continue;
                }
                else
                {
                    current = this.writer.diskRead(treeNodes.pop(), degree);
                    childIndex = children.pop();

                    if (childIndex < current.keys.size())
                    {
                        outStream.write(current.keys.get(childIndex).toString());
                        outStream.println();
                    }
                    childIndex++;
                    continue;
                }
            }

            if (current.isLeaf())
            {
                for (int i = 0; i < current.keys.size() ; i++)
                {
                    outStream.write(current.keys.get(i).toString());
                    outStream.println();
                }

                if (current == root) break;
                current = this.writer.diskRead(treeNodes.pop(), degree);
                childIndex = children.pop();

                if (childIndex < current.keys.size())
                {
                    outStream.write(current.keys.get(childIndex).toString());
                    outStream.println();
                }
                childIndex++;
            }
            else
            {
                treeNodes.push(current.getNodeIndex());
                children.push(childIndex);
                current = writer.diskRead(current.children.get(childIndex),degree);
                childIndex = 0;
            }
        }
        outStream.close();
    }

    public static int optimalDegree() {
        int dbSize = 4096;
        int fineD = 0;
        int nodeSize = 0;

        while (nodeSize <= dbSize) {
            nodeSize = TreeWriter.getNodeSize(++fineD);
        }

        fineD--;
        if (debug == 0)
            System.out.println("An optimal degree of " + fineD + " has been found");
        return fineD;
    }
}

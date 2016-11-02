import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;

@SuppressWarnings ("WeakerAccess")
public class TreeWriter{
    private static final int INT = 4, BOOLEAN = 4;
    private static final int META_DATA = INT * 3;
    private RandomAccessFile outfile;
    private static int degree, length;
    private Cache<BTreeNode> cache;

    public TreeWriter(String fileName, int degree, int seqLength, int cacheSize) throws FileNotFoundException{
        TreeWriter.degree = degree;
        length = seqLength;
        outfile = new RandomAccessFile(fileName, "rw");

        cache = new Cache<>(cacheSize);
    }

    public BTreeNode diskRead(int index, int degree) throws IOException {
        int numKeys;
        BTreeNode temp = new BTreeNode(degree, false, false, index);
        temp = cache.remove(temp);

        if (temp != null) {
            cache.add(temp);
            return temp;
        }

        temp = new BTreeNode(degree, false, false, index);

        ByteBuffer buff = ByteBuffer.allocate(4 + 4 + 4 + temp.maxKeyCount
                * (8 + 4) + (temp.maxKeyCount + 1) * (4));

        outfile.seek(getNodeOffSet(index));
        outfile.read(buff.array());

        // Read meta data
        temp.setParent(buff.getInt());
        temp.maxKeyCount = 2 * degree - 1;

        if (buff.getInt() == 1){
            temp.setIsLeaf(true);
        } else
            temp.setIsLeaf(false);

        numKeys = buff.getInt();

		/* Read keys */
        temp.keys = new ArrayList<>(numKeys);

        for (int k = 0; k < temp.maxKeyCount; k++){
            //read each key into the node
            if (k < numKeys){
                temp.keys.add(k, new TreeObject(buff));
            }
            //Skip past each empty key
            else{
                buff.getLong();
                buff.getInt();
            }
        }
        temp.children = new ArrayList<>(numKeys + 1);

        if (!temp.isLeaf()){
            for (int c = 0; c < temp.getMaxKeyCount() + 1; c++){
                //read each child index into the node
                if (c < numKeys + 1){
                    temp.children.add(c, buff.getInt());
                } else
                    buff.getInt();
            }
        }
        BTreeNode returnNode = temp;
        temp = cache.add(temp);

        if (temp != null)
            diskWrite(temp, false);
        return returnNode;
    }

    public void diskWrite(BTreeNode treeNode, boolean writeRoot) throws IOException{

        if (treeNode.isRoot() && !writeRoot) return;

        ByteBuffer buff = ByteBuffer.allocate(4 + 4 + 4 + treeNode.getMaxKeyCount()
                * (8 + 4) + (treeNode.getMaxKeyCount() + 1) * (4));


        outfile.seek(getNodeOffSet(treeNode.getNodeIndex()));

        buff.putInt(treeNode.getParentIndex());

        if (treeNode.isLeaf())
            buff.putInt(1);
        else
            buff.putInt(0);

        buff.putInt(treeNode.keys.size());

        //writing all keys in the node
        for (int i = 0; i < treeNode.maxKeyCount; i++){
            if (i < treeNode.keys.size()){
                buff.putLong(treeNode.keys.get(i).getKey());
                buff.putInt(treeNode.keys.get(i).getFrequency());
            } else{
                buff.putLong(-1);
                buff.putInt(-1);
            }
        }

        //writing children
        for (int i = 0; i < treeNode.getMaxKeyCount() + 1; i++){
            if (i < treeNode.children.size()){
                buff.putInt(treeNode.children.get(i));
            } else
                buff.putInt(-1);
        }
        outfile.write(buff.array());

    }

    private static int getNodeSize(){
        int numMaxKeys = 2 * degree - 1;
        int sizeOfMetaData = (INT * 2) + BOOLEAN;
        int sizeOfKey = 12 * (numMaxKeys);
        int childSize = INT * (numMaxKeys + 1);
        return (sizeOfMetaData + sizeOfKey + childSize);
    }

    public static int getNodeSize(int degree){
        int numMaxKeys = 2 * degree - 1;
        int sizeOfMetaData = (INT * 2) + BOOLEAN;
        int sizeOfKey = 12 * (numMaxKeys);
        int childSize = INT * (numMaxKeys + 1);
        return (sizeOfMetaData + sizeOfKey + childSize);
    }

    private int getNodeOffSet(int index){
        return META_DATA + (getNodeSize() * index);
    }

    private void writeMetaData(int degree, int rootIndex, int seqLength){
        try{
            outfile.seek(0L);
            outfile.writeInt(degree);
            outfile.writeInt(rootIndex);
            outfile.writeInt(seqLength);
        } catch (IOException ignore){
        }
    }

    public void flushTree(BTreeNode current, BTreeNode next, BTreeNode root) throws IOException{
        int rootIndex = root.getNodeIndex();
        if (current != null){
            diskWrite(current, true);
        }

        if (next != null)
            diskWrite(next, true);

        diskWrite(root, true);
        writeMetaData(degree, rootIndex, length);
    }

    public int readDegree() throws IOException {
        outfile.seek(0L);
        return outfile.readInt();
    }

    public int readRootIndex() throws IOException{
        outfile.seek(4L);
        return outfile.readInt();
    }

    public int readSeqLength() throws IOException{
        outfile.seek(8L);
        return outfile.readInt();
    }
}

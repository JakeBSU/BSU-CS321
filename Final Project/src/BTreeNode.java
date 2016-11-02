import java.util.ArrayList;

public class BTreeNode implements Comparable<BTreeNode>{
    ArrayList<TreeObject> keys;
    ArrayList<Integer> children;
    int nodeIndex, parentIndex;
    int maxKeyCount, degree;
    boolean isRoot, isLeaf; //use to indicate if  a node is either a leaf and  root


    public BTreeNode(int degree, boolean isRoot, boolean isLeaf, int nodeIndex) {
        this.nodeIndex = nodeIndex;
        this.isLeaf = isLeaf;
        this.isRoot = isRoot;
        this.degree  = degree;

        if (isRoot()) {
            parentIndex = -1;
        }

        maxKeyCount = (2 * degree) - 1;
        keys = new ArrayList<>(maxKeyCount);
        children = new ArrayList<>(maxKeyCount + 1);
    }

    public boolean isFull() {
        return keys.size() == maxKeyCount;
    }

    public void setIndex(int index){nodeIndex = index;}

    public boolean isRoot() {
        return isRoot;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public int getMaxKeyCount() {
        return maxKeyCount;
    }


    public void setParent(int parentIndex) {
        this.parentIndex = parentIndex;
    }

    public void setRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public int getParentIndex() {
        return parentIndex;
    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("KEYS:\n");
        int count = 0;
        for (int i = 0; i < keys.size() ; i++) {
            if(count % 4 == 0)
                builder.append("\n");
            builder.append(keys.get(i));
        }
        return builder.toString();
    }

    @Override
    public int compareTo(BTreeNode o){
        return nodeIndex - o.nodeIndex;
    }
}

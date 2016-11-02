import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BTree2 {

    final private int degree;
    private RandomAccessFile file;
    final private int maxKeys;
    private BTreeNode root;

    private class BTreeNode {
        private int numKeys;
        private TreeObject[] keys;
        private BTreeNode[] children;
        private boolean isLeaf;

        public BTreeNode(int numKeys, boolean isLeaf)
        {
            this.numKeys = numKeys;
            this.isLeaf = isLeaf;
            keys = new TreeObject[maxKeys];
            if (isLeaf)
                children = null;
            else
                children = new BTreeNode[maxKeys+1];
        }

        //TODO write me!
        private void diskRead()
        {
        }

        //TODO WRITE ME!
        public void diskWrite() throws IOException
        {
            for (int i = 0; i < keys.length ; i++) {
                if(keys[i] != null)
                    BTree2.this.file.writeLong(keys[i].getKey());
            }
        }

        public TreeObject BTreeSearch(TreeObject k)
        {
            int i = 0;
            Long treeKey = k.getKey();

            while (i < numKeys && keys[i].getKey().compareTo(treeKey) < 0) i++;

            if (i < numKeys && keys[i].getKey().compareTo(treeKey) == 0){
                 keys[i].increaseFrequency();
                return keys[i];
//                return new BTreeHandle(this, i); // found it
            }

            if (isLeaf)
                return null;    // no child to search
            else {      // search child index
                //TODO implement me
                children[i].diskRead();
                return children[i].BTreeSearch(k);
            }
        }

        private void splitChild(BTreeNode x, int i)
        {
            BTreeNode tempNode = new BTreeNode(degree -1, isLeaf);

            // Copy the degree-1 keys in positions degree to 2t-2 into tempNode.
            for (int j = 0; j < degree -1; j++) {
                tempNode.keys[j] = keys[j+ degree];
                keys[j+ degree] = null; // remove the reference
            }

            // If this BTreeNode is not a isLeaf, copy the degree children in
            // positions degree to 2t-1, too.
            if (!isLeaf)
                for (int j = 0; j < degree; j++) {
                    tempNode.children[j] = children[j+ degree];
                    children[j+ degree] = null; // remove the reference
                }

            numKeys = degree -1;

            // Move the children in x that are to the right of y by
            // one position to the right.
            for (int j = x.numKeys; j >= i+1; j--)
                x.children[j+1] = x.children[j];

            // Drop tempNode into x's child index+1.
            x.children[i+1] = tempNode;

            // Move the keys in x that are to the right of y by one
            // position to the right.
            for (int j = x.numKeys -1; j >= i; j--)
                x.keys[j+1] = x.keys[j];

            // Move this BTreeNode's median keys into x, and remove the
            // reference to the keys in this BTreeNode.
            x.keys[i] = keys[degree -1];
            keys[degree -1] = null;

            x.numKeys++;      // one more keys/child in x

            // All done.  Write out the nodes.
            //TODO COMPLETE ME!

            try{
                diskWrite();
                tempNode.diskWrite();
                x.diskWrite();
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
        }


        private void InsertNonfull(TreeObject k)
        {
            if(search(k) != null) {
                return;
            }

            int i = numKeys -1;
            Long treeKey = k.getKey();

            if (isLeaf) {
                // Move all keys greater than k's by one position to
                // the right.
                while (i >= 0 && keys[i].getKey().compareTo(treeKey) > 0) {
                    keys[i+1] = keys[i];
                    i--;
                }

                // Either index is -1 or keys[index] is the rightmost keys <=
                // k's keys.  In either case, drop k into position index+1.
                keys[i+1] = k;

                numKeys++;
                try {
                    diskWrite();
                } catch (IOException e) {
                    System.out.println("this should never heppane!");
                }

                // Return the handle saying where we dropped k.
//                return new BTreeHandle(this, i+1);
            }
            else {
                // Find which child we descend into.
                while (i >= 0 && keys[i].getKey().compareTo(treeKey) > 0) i--;

                // Either index is -1 or keys[index] is the rightmost keys <=
                // k's keys.  In either case, descend into position
                // index+1.
                i++;
                //TODO COMPLETE ME
                children[i].diskRead();

                if (children[i].numKeys == maxKeys) {
                    // That child is full, so split it, and possibly
                    // update index to descend into the new child.
                    children[i].splitChild(this, i);
                    if (keys[i].getKey().compareTo(treeKey) < 0)
                        i++;
                    else if(keys[i].getKey().compareTo(treeKey) == 0)
                        keys[i].increaseFrequency();
                }
                children[i].InsertNonfull(k);
            }
        }

        public String walk(int depth)
        {
            String result = "";

            for (int i = 0; i < numKeys; i++) {
                if (!isLeaf)
                    result += children[i].walk(depth+1);
                for (int j = 0; j < depth; j++)
                    result += "  ";
                result += "BTreeNode at " + this + ", keys " + i + ": " +
                        keys[i] + "\n";
            }

            if (!isLeaf)
                result += children[numKeys].walk(depth+1);

            return result;
        }
    }

    public BTree2(int degree) throws FileNotFoundException
    {
        file = new RandomAccessFile("TEST","rwd");
        this.degree = degree;
        maxKeys = 2 * degree - 1;
        root = new BTreeNode(0, true); // root is a isLeaf

        // write the root to disk
        try {
            root.diskWrite();
        } catch (IOException e) {
            System.out.println("this should never happen");
        }
    }

    public Object search(TreeObject k)
    {
        return root.BTreeSearch(k);
    }

    public void insert(TreeObject o)
    {
        BTreeNode r = root;

        if (r.numKeys == maxKeys)
        {
            BTreeNode s = new BTreeNode(0, false);
            root = s;
            s.children[0] = r;
            r.splitChild(s, 0);
            s.InsertNonfull(o);
        }
        else
            r.InsertNonfull(o);
    }

    public String toString()
    {
        return root.walk(0);
    }
}

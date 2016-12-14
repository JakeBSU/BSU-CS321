import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.LinkedList;

/**
 *  #CS321: lab4 (Group Project)
 *  
 *  The BTree is the data structure to hold the DNA substrings.
 *  Contains inner class, BTreeNode. 
 * @author Marshall Eddy, Jayden Weaver, Benjamin Hetz
 *
 */
public class BTree {
	
	private int degree;
	private BTreeNode root;
	private int BTreeOffset;
	private int nodeSize;
	private int insertionPoint;
	private BTreeCacheNode.BTreeCache cache;
	private BTreeCacheNode BTCN;
	private File File;
	private RandomAccessFile RAF;
	
	class BTreeNode {
		
		 private int Parent;
		 private LinkedList<TreeObject> keys;
		 private LinkedList<Integer> children;
		 private int numKeys;
		 private int offset;
		 private boolean isLeaf;
		 
		 
		 public BTreeNode() {
			 keys = new LinkedList<TreeObject>();
			 children = new LinkedList<Integer>();
			 numKeys = 0;
			 Parent = -1;
		 }
		 
		 public int getParent() {
			 return Parent;
		 }
		 public void setParent(int Parent) {
			 this.Parent = Parent;
		 }
		 public int getNumKeys() {
			 return numKeys;
		 }
		 public void setNumKeys(int numKeys) {
			 this.numKeys = numKeys;
		 }
		 public int getOffset() {
			 return offset;
		 }
		 public void setOffset(int offset) {
			 this.offset = offset;
		 }
		 public TreeObject getKey(int i) {
			 TreeObject obj = keys.get(i);
			 return obj;
		 }
		 public void addKey(TreeObject obj) {
			 keys.add(obj);
		 }
		 public void addKey(TreeObject obj, int i) {
			 keys.add(i, obj);
		 }
		 public TreeObject removeKey(int i) {
			 return keys.remove(i);
		 }
		 public LinkedList<TreeObject> getKeys() {
			 return keys;
		 }
		 public int getChild(int i) {
			 return children.get(i).intValue();
		 }
		 public void addChild(int i) {
			 children.add(i);
		 }
		 public void addChild(Integer c, int i) {
			 children.add(i, c);
		 }
		 public int removeChild(int i) {
			 return children.remove(i);
		 }
		 public LinkedList<Integer> getChildren() {
			 return children;
		 }
		 public boolean isLeaf() {
			 return isLeaf;
		 }
		 public void setIsLeaf(boolean isLeaf) {
			 this.isLeaf = isLeaf;
		 }
		 public String toString() {
			 String s = new String();
			 s += "Keys: ";
			 for (int i = 0; i < keys.size(); i++) {
				 s += (keys.get(i) + " ");
			 }
			 s += "\nchildren: ";
			 for (int i = 0; i < children.size(); i++) {
				 s += (children.get(i) + " ");
			 }
			 return s;
		 }
	}
	
	public BTree (int deg, String FileName, boolean usingCache, int cacheSize) {
		nodeSize = (32 * deg - 3);
		BTreeOffset = 12;
		insertionPoint = (BTreeOffset + nodeSize);
		degree = deg;
		if (usingCache) {
			BTCN = new BTreeCacheNode(root, 0 ,cacheSize);
			cache = BTCN.BTC;
		}
		 BTreeNode x = new BTreeNode();
	        root = x;
	        root.setOffset(BTreeOffset);
	        x.setIsLeaf(true);
	        x.setNumKeys(0);
	        try {
	            File = new File(FileName);
	            File.delete();
	            File.createNewFile();    
	            RAF = new RandomAccessFile(File, "rw");
	        }
	        catch (FileNotFoundException fnfe){
	            System.err.println("file is corrupt or missing!");
	            System.exit(-1);
	        }
	        catch (IOException ioe){
	            System.err.println("IO Exception occurred!");
	            System.exit(-1);
	        }
	    writeTreeMetadata();
	}
	
	 public BTree(int degree, File fileName, boolean usingCache, int cacheSize){
	        
	        try {
	            RAF = new RandomAccessFile(fileName, "r");
	        }
	        catch (FileNotFoundException fnfe){
	            System.err.println("file is corrupt or missing!");
	            System.exit(-1);
	        }
		    readTreeMetadata();
	        root = readNode(BTreeOffset);
	    }
	
	 public BTree() {super();}
	
	public BTreeNode getRoot() {
		return root;
	}
	
	/**
	 * Inserts into the BTree
	 */
	public void BTreeInsert(long key) {
		BTreeNode r = root;
		int i = r.getNumKeys();
		if (i == 2 * degree - 1) {
			TreeObject Tobj = new TreeObject(key);
			while (i > 0 && Tobj.compareTo(r.getKey(i-1)) < 0) {
				i--;
	        } if (i < r.getNumKeys()){ }
	        if (i > 0 && Tobj.compareTo(r.getKey(i-1)) == 0)
	        	r.getKey(i-1).increaseFreq();
	        else {
	        	BTreeNode s = new BTreeNode();
	        	s.setOffset(r.getOffset());
	       		root = s;
	       		r.setOffset(insertionPoint);
	       		r.setParent(s.getOffset());
	       		s.setIsLeaf(false);
	       		s.addChild(r.getOffset());
	       		splitChild(s, 0, r);
	       		BTreeInsertNonFull(s,key);
	       	}
        } else
           BTreeInsertNonFull(r,key);
	}
	
	public void BTreeInsertNonFull(BTreeNode x, long key) {
		int i = x.getNumKeys();
		
		TreeObject Tobj = new TreeObject(key);
		if (x.isLeaf()) {
			if (x.getNumKeys() != 0) {
                while (i > 0 && Tobj.compareTo(x.getKey(i-1)) < 0) {
                    i--;
                }
            } if (i > 0 && Tobj.compareTo(x.getKey(i-1)) == 0) {
                x.getKey(i-1).increaseFreq();
            } else {
                x.addKey(Tobj,i);
                x.setNumKeys(x.getNumKeys()+1);
            }
            writeNode(x,x.getOffset());
        } else {
            while (i > 0 && (Tobj.compareTo(x.getKey(i-1)) < 0)) {
                i--;
            } if (i > 0 && Tobj.compareTo(x.getKey(i-1)) == 0) {
                x.getKey(i-1).increaseFreq();
                writeNode(x,x.getOffset());
                return;
            }
            int offset = x.getChild(i);
            BTreeNode y = readNode(offset);
            if (y.getNumKeys() == 2 * degree - 1) {
                int j = y.getNumKeys();
                while (j > 0 && Tobj.compareTo(y.getKey(j-1)) < 0) {
                    j--;
                } if (j > 0 && Tobj.compareTo(y.getKey(j-1)) == 0) {
                    y.getKey(j-1).increaseFreq();
                    writeNode(y,y.getOffset());
                    return;
                } else {
                    splitChild(x, i, y);
                        if (Tobj.compareTo(x.getKey(i)) > 0) {
                            i++;
                        }
                }
            }
            offset = x.getChild(i);
            BTreeNode child = readNode(offset);
            BTreeInsertNonFull(child,key);
        }
	}
	
	public void splitChild(BTreeNode x, int i, BTreeNode y) {
        BTreeNode z = new BTreeNode();
        z.setIsLeaf(y.isLeaf());
        z.setParent(y.getParent());
        for (int j = 0; j < degree - 1; j++) {
            z.addKey(y.removeKey(degree));
            z.setNumKeys(z.getNumKeys()+1);
            y.setNumKeys(y.getNumKeys()-1);

        } if (!y.isLeaf()) {
            for (int j = 0; j < degree; j++) {
                z.addChild(y.removeChild(degree));
            }
        }
        x.addKey(y.removeKey(degree - 1), i);
        x.setNumKeys(x.getNumKeys()+1);
        y.setNumKeys(y.getNumKeys()-1);
        if (x == root && x.getNumKeys() == 1) {
            writeNode(y,insertionPoint);
            insertionPoint += nodeSize;
            z.setOffset(insertionPoint);
            x.addChild(z.getOffset(),i+1);
           writeNode(z,insertionPoint);
           writeNode(x,BTreeOffset);
            insertionPoint += nodeSize;
        } else {
            writeNode(y,y.getOffset());
            z.setOffset(insertionPoint);
            writeNode(z,insertionPoint);
            x.addChild(z.getOffset(),i+1);
            writeNode(x,x.getOffset());
            insertionPoint += nodeSize;
        }
    }
	
	public TreeObject search(BTreeNode x, long key) {
        int i = 0;
        TreeObject obj = new TreeObject(key);
        while (i < x.getNumKeys() && (obj.compareTo(x.getKey(i)) > 0)) {
            i++;
        } if (i < x.getNumKeys() && obj.compareTo(x.getKey(i)) == 0) {
            return x.getKey(i);
        } if (x.isLeaf()) {
            return null;
        } else {
            int offset = x.getChild(i);
            BTreeNode y = readNode(offset);
            return search(y,key);
        }
    }
	
	public void inOrderPrint(BTreeNode node) {
        System.out.println(node);
        if (node.isLeaf() == true){
            for (int i = 0; i < node.getNumKeys(); i++) {
                System.out.println(node.getKey(i));
            } return;
        }
        for (int i = 0; i < node.getNumKeys() + 1; ++i) {
            int offset = node.getChild(i);
            BTreeNode y = readNode(offset);
            inOrderPrint(y);
            if (i < node.getNumKeys())
                System.out.println(node.getKey(i));
        }
    }
	
	public void inOrderPrintToWriter(BTreeNode node, PrintWriter PWriter, int sequenceLength) throws IOException {
        GeneBankConvert gbc = new GeneBankConvert();
        for (int i = 0; i < node.getNumKeys(); i++){
            PWriter.print(node.getKey(i).getFreq()+ " ");
            PWriter.println(gbc.convertLongToString(node.getKey(i).getData(),sequenceLength));
        } if (!node.isLeaf()) {
	        for (int i = 0; i < node.getNumKeys() + 1; ++i) {
	            int offset = node.getChild(i);
	            BTreeNode y = readNode(offset);
	            inOrderPrintToWriter(y,PWriter,sequenceLength);
	            if (i < node.getNumKeys()) {
	                PWriter.print(node.getKey(i).getFreq() + " ");
                    PWriter.println(gbc.convertLongToString(node.getKey(i).getData(),sequenceLength));
	            }
	        }
        }
    }
	
	public void writeNode(BTreeNode n, int offset){
        if (cache != null) {
        	BTreeNode cnode = cache.add(n, offset);
        	// if a node was pushed off, write it
        	if (cnode != null) writeNodeToFile(cnode,cnode.getOffset());
        } else {
        	writeNodeToFile(n, offset);
        }
    }
	
	   private void writeNodeToFile(BTreeNode n, int offset) {
	        int i = 0;
	        try {
	            writeNodeMetadata(n,n.getOffset());
	            RAF.writeInt(n.getParent());
	            for (i = 0; i < 2 * degree - 1; i++){
	                if (i < n.getNumKeys() + 1 && !n.isLeaf()) {
	                    RAF.writeInt(n.getChild(i));
	                } else if (i >= n.getNumKeys() + 1 || n.isLeaf()) {
	                    RAF.writeInt(0);
	                } if (i < n.getNumKeys()){
	                    long data = n.getKey(i).getData();
	                    RAF.writeLong(data);
	                    int frequency = n.getKey(i).getFreq();
	                    RAF.writeInt(frequency);
	                } else if (i >= n.getNumKeys() || n.isLeaf()) {
	                    RAF.writeLong(0);
	                }
	            } if (i == n.getNumKeys() && !n.isLeaf()) {
	                RAF.writeInt(n.getChild(i));
	            }
	        } catch (IOException ioe) {
	            System.err.println("IO Exception occurred!");
	            System.exit(-1);
	        }
	    }
	
	    public BTreeNode readNode(int offset){
	    	
	    	BTreeNode y = null;
	    	
	    	// if node is cached, we can just read it from there
	        if (cache != null) y = cache.readNode(offset);
	        if (y != null) return y;
	        
	        y = new BTreeNode();
	        TreeObject obj = null;
	        y.setOffset(offset);
	        int k = 0;
	        try {
	            RAF.seek(offset);
	            boolean isLeaf = RAF.readBoolean();
	            y.setIsLeaf(isLeaf);
	            int n = RAF.readInt();
	            y.setNumKeys(n);
	            int parent = RAF.readInt();
	            y.setParent(parent);
	            for (k = 0; k < 2 * degree - 1; k++) {
	                if (k < y.getNumKeys() + 1 && !y.isLeaf()) {
	                    int child = RAF.readInt();
	                    y.addChild(child);
	                } else if (k >= y.getNumKeys() + 1 || y.isLeaf()) {
	                    RAF.seek(RAF.getFilePointer() + 4);
	                } if (k < y.getNumKeys()){
	                    long value = RAF.readLong();
	                    int frequency = RAF.readInt();
	                    obj = new TreeObject(value,frequency);
	                    y.addKey(obj);
	                }
	            } if (k == y.getNumKeys() && !y.isLeaf()) {
	                int child = RAF.readInt();
	                y.addChild(child);
	            }
	        } catch (IOException ioe) {
	            System.err.println(ioe.getMessage());
	            System.exit(-1);
	        }
	        
	        return y;
	    }
	   
	    public void writeTreeMetadata() {
	        try {
	            RAF.seek(0);
	            RAF.writeInt(degree);
	            RAF.writeInt(32 * degree - 3);
	            RAF.writeInt(12);
	        } catch (IOException ioe) {
	            System.err.println("IO Exception occurred!");
	            System.exit(-1);
	        }
	    }
	    public void readTreeMetadata() {
	        try {
	            RAF.seek(0);
	            degree = RAF.readInt();
	            nodeSize = RAF.readInt();
	            BTreeOffset = RAF.readInt();
	        } catch (IOException ioe) {
	            System.err.println("IO Exception occurred!");
	            System.exit(-1);
	        }
	    }
	    public void writeNodeMetadata(BTreeNode x, int offset) {
	        try {
	            RAF.seek(offset);
	            RAF.writeBoolean(x.isLeaf());
	            RAF.writeInt(x.getNumKeys());
	        } catch (IOException ioe){
	            System.err.println("IO Exception occurred!");
	            System.exit(-1);
	        }
	    }
	    
	    public void flushCache() {
	    	if (cache != null) {
	    		for (BTreeNode cnode : cache) writeNodeToFile(cnode,cnode.getOffset());
	    	}
	    }

	public String Convert(long key) {
		String result = "";
		if (key == -1) {
			return result;
		}
		String temp = "";
		String temp2 = "";
		temp = Long.toBinaryString(key);
		for (int i = insertionPoint * 2; i > 1; i -= 2) {
			try{
				temp2 = temp.substring(i - 1, i + 1);
				if (temp2.equals("00")) result = result + "A";
				else if (temp2.equals("11")) result = result + "T";
				else if (temp2.equals("01")) result = result + "C";
				else if (temp2.equals("10")) result = result + "G";	
			}
			catch(StringIndexOutOfBoundsException ex) {
				
			}
		}
		return result;
	}
	
}
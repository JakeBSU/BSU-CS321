/**
 * #CS321: lab4 (Group Project)
 * 
 * Represents the objects.
 * 
 * @author Marshall Eddy, Jayden Weaver, Benjamin Hetz
 *
 */
public class TreeObject implements Comparable<TreeObject>{

		private int freq;
		private long data;
		
		public TreeObject(long d, int freq) {
			this.freq = freq;
			data = d;
		}
		
		public TreeObject(long d) {
			freq = 1;
			data = d;
		}
		
		public void increaseFreq() {
			freq++;
		}
		
		public long getData() {
			return data;
		}
		
		public void setData(long data) {
			this.data = data;
		}
		
		public int compareTo(TreeObject Tobj) {
			if (data < Tobj.data)
				return -1;
			if (data > Tobj.data)
				return 1;
			else
				return 0;
		}
		
		public int getFreq() {
			return freq;
		}
		
		public String toString() {
			return "Key: " + data + "Frequency: " + freq;
		}
}
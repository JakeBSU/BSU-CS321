
/**
 * Converts Long and Strings
 * 
 * @author MEddy
 *
 */
public class GeneBankConvert {
	private long key = 0;
	
	public long getKey() {
		return key;
	}
	
	public String convertLongToString(long seq, int sequenceLength) {
		String s = "";
		long temp;
		
		for (int i = 1; i <= sequenceLength; i++) {
			temp = (seq & 3L << (sequenceLength-i)*2);
			temp = temp >> (sequenceLength-i)*2;
			
			if (temp == 0L) {
				s += "a";
			} else if (temp == 1L) {
				s += "c";
			} else if (temp == 2L) {
				s += "g";
			} else if (temp == 3L) {
				s += "t";
			}
		} return s;
	}
	
	public long convertStringToLong(String seq) {
		seq = seq.toLowerCase();
		
		for (int i = 0; i < seq.length(); i++) {
			if (seq.charAt(i) == 'a') { // 00
				if (i == 0) {
					key = 0;
				} else {
						key = key << 2;
						key = key | 0;
				}
			} if (seq.charAt(i) == 'c') { // 01
				if(i == 0) {
					key = 1	;
				} else {
						key = key << 2;
						key = key | 1;
				}
			} if (seq.charAt(i) == 'g') { // 10
				if(i == 0) {
					key = 2;
				} else {
						key = key << 2;
						key = key | 2;
				}
			} if (seq.charAt(i) == 't') { // 11
				if(i == 0) {
					key = 3;
				} else {
						key = key << 2;
						key = key | 3;
				}
			}
		} return key;
	}
}
public class TreeObject {

	private long key;
	private int frequency;
	
	public TreeObject(){
		this.key = 0;
		this.frequency = 0;
	}
	
	public TreeObject(long key){
		this.key = key;
		this.frequency = 1;
	}
	
	public long getKey(){
		return this.key;
	}
	
	public int getFrequency(){
		return this.frequency;
	}
	
	public void increaseFrequency(){
		this.frequency++;
	}

}

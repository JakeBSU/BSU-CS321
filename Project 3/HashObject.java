/**
 * 
 * @author Jayden Weaver
 * Hash Object class.
 * @param <T>
 */
public class HashObject <T>{
	private T hashOb;
	private int key, frequency, probes2insert;

	public HashObject(T object){
		this.frequency = 0;
		this.key = Math.abs(object.hashCode());
		this.hashOb = object;
	}

	public int getKey(){
		return this.key;
	}
	public String toString(){
		return this.hashOb.toString();
	}
	public void incFreq(){
		this.frequency++;
	}
	public int getFreq(){
		return this.frequency;
	}
	public int getProbe2Insert(){
		return this.probes2insert;
	}
	public void incProbes2In(){
		this.probes2insert++;
	}

	public boolean isEqual(HashObject<T> value){
		if(value.getObject().equals(hashOb)){
			return true;
		}else{
			return false;
		}
	}
	
	public T getObject(){
		return this.hashOb;
	}
}

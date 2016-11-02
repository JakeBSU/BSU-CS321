/**
 * 
 * @author Jayden Weaver
 * 
 * @param <T>
 */
public class DoubleHash <T>{
	private int duplicate, numItems = 0;
	private int debugLevel;
	private double load;
	private int hashR, probes;
	private int size;
	private PrimeGenerator pr = new PrimeGenerator();
	private HashObject<T> dubbed[];
	
	@SuppressWarnings("unchecked")
	public DoubleHash(double load, int debugLevel){
		load = this.load;
		debugLevel = this.debugLevel;
		size = pr.getPrimes(95500, 96000);
		dubbed = new HashObject[size];
	}
	
	public int getHash(){
		return this.hashR;
	}
	public int getSize(){
		return this.size;
	}
	public int getItems(){
		return this.numItems;
	}
	public int getDup(){
		return this.duplicate;
	}
	public double getLoad(){
		return (double) numItems/size;
	}
	public int getProbe(){
		return this.probes;
	}
	public double getAvg(){
		return (double) probes/numItems;
	}
	
	public int doubleHashFun(int key){
		return 1 + (key % (size-2));
	}
	
	public void hashIt(HashObject <T> val){
		int i = 0;
		int key = val.getKey();
		int index = key % size;
		int step = 1 + (key%(size-2)); 
		hashR++;
		int tempProbe = probes;
		probes++;
		while(i < size){
			i++;
			if(dubbed[index] != null && dubbed[index].isEqual(val)){
				dubbed[index].incFreq();
				hashR--;
				probes = tempProbe;
				duplicate++;
				return;
			}
			else if(dubbed[index] == null){
				dubbed[index] = val;
				numItems++;
				return;
			}
			probes++;
			index = (index + step)%size;
		}
		return;
	}
	public void viewTable(){
		for(int i = 0; i < dubbed.length; i++){
			if(dubbed[i] != null){
				System.out.println("table["+ i + "] = " + dubbed[i].toString());
			}
		}
	}
	
	public void viewTable1(){
		for(int i = 0; i < dubbed.length; i++){
			HashObject<?> value = dubbed[i];
			if(dubbed[i] != null){
				System.out.println("table["+ i + "] = " + dubbed[i].toString() + " " + value.getProbe2Insert());
			}
		}
	}
}

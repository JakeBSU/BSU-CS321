/**
 * 
 * @author Jayden Weaver
 * Hash Table Object.
 * @param <T>
 */
public class HashTable <T>{
	private HashObject <T> table[];
	private double load;
	private int debugLevel,size;
	private int duplicates1, duplicates2, numItemsLin, numItemsDub, probes1, probes2, hashReq = 0;
	static PrimeGenerator pr = new PrimeGenerator();

	@SuppressWarnings("unchecked")
	public HashTable(double load, int debugLevel){
		load = this.load;
		debugLevel = this.debugLevel;
		size = pr.getPrimes(95500, 96000);
		table = new HashObject[size];
	}
	public int getHashReq(){
		return this.hashReq;
	}
	
	public int getSize(){
		return this.size;
	}
	public int numProbes1(){
		return this.probes1;
	}
	public int numProbes2(){
		return this.probes2;
	}
	public int getDuplicates1(){
		return this.duplicates1;
	}
	public int getDuplicates2(){
		return this.duplicates2;
	}
	public int getNumberOfItemsDub(){
		return this.numItemsDub;
	}
	public int getNumberOfItemsLin(){
		return this.numItemsLin;
	}

	public void linearInsert(HashObject<T> value){
		int i = 0;
		int key = value.getKey();
		int index = linHashFunction(key);
		System.out.println(this.size);
		while (index < this.size){
			int j = linHashFunction(index+i);
			if(table[j] == null){
				table[j] = value;
			}else{
				i++;
				index %= this.size;
			}
		}
	}
	
	public double getLoad(){
		return (double) numItemsLin/size;
	}
	
	public int linearProbing(HashObject <T> val){
		int i = 0;
		int key = val.getKey();
		int index = (linHashFunction(key) + i) % this.size;
		hashReq++;
		int tempProbe = probes1;
		probes1++;
		while(i < this.size){
			i++;
			if(table[index] != null && table[index].isEqual(val)){
				table[index].incFreq();
				table[index].incProbes2In();
				hashReq--;
				probes1 = tempProbe;
				duplicates1++;
				return index;
			}
			else if(table[index] == null){
				table[index] = val;
				numItemsLin++;
				return index;
			}
			probes1++;
			index = (linHashFunction(key) + i) % this.size;	
		}
		return index;
	}
	public int linHashFunction(int key){
		return key % this.size;
	}
	public int dubHashFunction(int key){
		return 1+(key%(size-2));
	}	
	public void doubleHashing(HashObject <T> val){
		int key = val.getKey();
		int step = 1 + (key % (size - 2));
		int index = key % size;

		while(table[index] != null){
			if(table[index] == val){
				duplicates2++;
				return;
			}else{
				probes2++;
				index += step;
				index %= size;
			}
		}
		if(table[index] == null){
			table[index] = val;
			numItemsDub++;
		}
	}

	public double avgProbes2(){
		return (double) probes2/numItemsDub;

	}
	public double avgProbes1(){
		return (double) probes1/numItemsLin;
	}

	public void viewTable(){
		for(int i = 0; i < table.length; i++){
			if(table[i] != null){
				System.out.println("table["+ i + "] = " + table[i].toString());
			}
		}
	}
	public void viewTable1(){
		for(int i = 0; i < table.length; i++){
			HashObject<?> value = table[i];
			if(table[i] != null){
				System.out.println("table["+ i + "] = " + table[i].toString() + " " + value.getProbe2Insert());
			}
		}
	}
}

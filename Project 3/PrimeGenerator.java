import java.util.Random;
/**
 * 
 * @author Jayden Weaver
 * Generates a prime number to use. 
 */
public class PrimeGenerator {
	public boolean testPrime(int testIt){
		Random randy = new Random();
		int number = 0;
		int base = Math.abs(randy.nextInt(500));
		String binary = Integer.toBinaryString(testIt-1);
		while(base < testIt){
			number = base;
			for(int i = 1; i < binary.length(); i++){
				if (binary.charAt(i) == '0'){
					number = (int) ((Math.pow(number, 2))% testIt);
				}else if (binary.charAt(i) == '1'){
					number = (int) ((((Math.pow(number,2))% testIt) * base) % testIt);
				} 
			}
			if (number != 1){
				return false;
			}
			base ++;
		}
		return true;
	}
	public int getPrimes(int min, int max){
		int prime2 = 0;
		while(prime2 == 0){
			for (int i = min; i <= max; i++){
				if (testPrime(i) == true){
					if(testPrime(i+2) == true){
						prime2 = i + 2;
						return prime2;
					}
				}
			}
		}
		return 0;
	}
}
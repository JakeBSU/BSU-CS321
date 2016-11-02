public class OptimalDegreeGenerator{

	//TODO constructor?
	
	public static int generate() {
		
		int dbSize = 4096;
        int nodeSize = 32; 		//TODO find a more accurate size
        int pointerSize = 4; 	//TODO find a more accurate size
        int metadata = nodeSize;
        
		//value for degree will be truncated, which is okay, as optimal degree must be <= actual value

		int degree = (dbSize - metadata) / (2*nodeSize + 2*pointerSize);	
		return degree;
	}
}

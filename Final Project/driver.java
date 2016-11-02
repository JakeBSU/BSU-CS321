
import java.io.File;
import java.util.ArrayList;

public class driver {
	public static void main(String[] args){
		Parser parse = new Parser();
		File file = new File(args[0]);
		ArrayList longList = parse.parse(file, 3);
	}
}
import java.util.Random;

/*
 * randomly generates processes with a given probability. At beginning
of each time unit, whether a process will arrive is decided by the given probability. In addition,
while generating a new process, both its priority and the required time units to finish the
process are randomly generated within given ranges
 */
public class ProcessGenerator {

	private double ProcessGenerator;
	private Random rand = new Random();
	private double probability;

	public ProcessGenerator(double probability) {
		this.probability = probability;
	}

	public Process getNewProcess(int currentTime, int maxProcessTime, int maxLevel) {
		int level = rand.nextInt(maxLevel) + 1;
		int timeToProcess = rand.nextInt(maxProcessTime) + 1;
		Process process = new Process(currentTime, timeToProcess, level);
		return process;
	}

	public boolean query() {

		// checks to see if it needs to make a process
		if (probability <= rand.nextDouble()) {
			return true;
		} else {
			return false;
		}
	}

}

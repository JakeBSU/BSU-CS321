import java.util.Random;

/*
 * defines a process. You need to implement the compareTo method in this class.
Each process has a priority level, time remaining to finish, and arrival time.
 */


public class Process implements Comparable <Process>{
	private int timeLeft, timeNotProcessed;
	private int priority, processTime, arrivalTime;
	private Random rand = new Random();
	
	public Process (int currentTime, int maxProcessTime, int maxLevel){
		this.priority = rand.nextInt(maxLevel);
		this.processTime = rand.nextInt(maxProcessTime);
		this.arrivalTime = currentTime;
		timeNotProcessed = 0;
	}
	
	public int getTimeRemaining() {
		return timeLeft;
	}

	public void reduceTimeRemaining() {
		this.timeLeft--;
	}

	public boolean finish() {
		if(timeLeft == 0){
			return true;
		}
		else{
			return false;
		}
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void resetTimeNotProcessed() {
		timeNotProcessed = 0;
	}
	public int getTimeNotProcessed(){
		return timeNotProcessed;
	}
	
	public void incrementTimeNotProcessed(){
		timeNotProcessed++;
	}

	public int getPriority() {
		return priority;
	}
	public void incPriority(){
		priority++;
	}

	@Override
	public int compareTo(Process p) {
		if(p.getPriority() < priority){
			return -1;
		}
		if(p.getPriority() > priority){
			return 1;
		}
		else{
			return 0;
		}
	}

	public void addWaitTime() {
		timeLeft++;
	}

	public void setPriority(int i) {
		this.priority = i;
	}

}

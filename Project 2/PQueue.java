import java.util.ArrayList;

/*
 * defines a priority queue using a max-heap
 */
public class PQueue {

	private MaxHeap heaps = null;

	public PQueue() {
		heaps = new MaxHeap();

	}

	public void enPQueue(Process p) {
		heaps.rebuild(p);
	}

	public Process dePQueue() {
		return heaps.extractMax();
	}

	public void update(int timeToIncrementPriority, int maxLevel) {

		for (int i = 1; i < heaps.getSize(); i++) {
			Process p = heaps.getProcess(i);
//			p.incrementTimeNotProcessed();
			if (p.getTimeNotProcessed() > timeToIncrementPriority) {
//				p.incPriority();
				if (p.getPriority() > maxLevel) {
//					p.setPriority(maxLevel);
					p.resetTimeNotProcessed();
				}
			}
//			heaps.buildMaxHeap();
		}
	}



	public boolean isEmpty() {
		return heaps.getSize() == 0;
	}
}

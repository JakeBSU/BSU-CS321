import java.util.ArrayList;

@SuppressWarnings("hiding")
public class MaxHeap extends ArrayList{

		private ArrayList<Process> heap;
		private Process left, parent, child;
		private int i;

		public MaxHeap(){
				heap = new ArrayList<Process>();
		}

		public int getSize(){
				return heap.size();
		}

		public void buildMaxHeap(){
				for (int i = (getSize()-1)/2; i > 0; i--){
					heapDown(i);
				}
		}
		public void rebuild(Process p){
			heap.add(p);
			buildMaxHeap();
		}

//		public void heapUp(Process process){
//				heap.add(process);
//				while(process.compareTo(heap.get(parentIndex(process)))== 1){
//						swap(heap.indexOf(process),parentIndex(process));	
//				}
//		}
		public Process extractMax(){
				int last = heap.size()-1;
				Process max = heap.get(0);
				swap (0,last);
//				heap.remove(last);
				return max;

		}

		public void heapDown(int i){
				//swap first and last
				// heap down time
				Process first = heap.get(i);
				int left = (2 * i)+1;
				int right = (2 * i)+2;
				int bigKid = i;
				if(left < getSize() && heap.get(left).compareTo(heap.get(bigKid)) ==1){
					bigKid = left;
				}
			
				if(right < getSize() && heap.get(right).compareTo(heap.get(bigKid)) ==1){
					bigKid = right;
				}
				if(bigKid != i){
					swap(i,bigKid);
					heapDown(bigKid);
				}
		}


		public void remove(){
				heap.remove(heap.indexOf(heap.size()-1));
		}

		public void swap(int one, int two){
				Process temp = heap.get(one);
				heap.set(one, heap.get(two));
				heap.set(two, temp);
		}

		public boolean isEmpty(){
				return heap.isEmpty();
}

		public Process getProcess(int index){
				return heap.get(index);
		}
}

import java.util.Iterator;
import java.util.LinkedList;

public class Cache<T extends Comparable<T>> implements Iterable<T> {
    int capacity;
    LinkedList<T> data;

    public Cache(int CacheSize) {
        data = new LinkedList<>();
        capacity = CacheSize;
    }
    
    public T remove(T obj) {
        T out = null;
        T curr;
        for (Iterator<T> i = data.iterator(); i.hasNext();) {
            curr = i.next();
            if (curr.compareTo(obj) == 0) {
                out = curr;
                i.remove();
                break;
            }
        }
        return out;
    }

    public boolean check(T checkCache) {
        return data.contains(checkCache);
    }

    public T add(T addObj) {
        data.push(addObj);
        if (data.size() > capacity)
            return data.removeLast();
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }

    public String toString(){
        return data.toString();
    }
}

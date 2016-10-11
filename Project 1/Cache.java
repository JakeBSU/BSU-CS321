import java.util.LinkedList;


public class Cache<T>{
    private LinkedList<T> wordCache;
    private int cacheLimit;

    public Cache(int sizeLimit){
        cacheLimit = sizeLimit;
        wordCache = new LinkedList<>();
    }

    public void add(T object){
        if (wordCache.size() < cacheLimit) 
        {
            if (wordCache.contains(object)){
                wordCache.remove(object);
            }
            wordCache.addFirst(object);
        } else{
            if (wordCache.contains(object)){
                wordCache.remove(object);
                wordCache.addFirst(object);
            } else{
                wordCache.removeLast();
                wordCache.addFirst(object);
            }
        }
    }

    public void removeObject(T object){
        wordCache.remove(object);
    }


    public boolean hit(T object){
        return wordCache.contains(object);
    }

    public void clear(){
        wordCache.clear();
    }

    public T getObject(T Object){
        T target = wordCache.get(wordCache.indexOf(Object));
        add(target);
        return target;
    }

    public int getSize(){
        return wordCache.size();
    }

    public int getCacheLimit(){
        return cacheLimit;
    }

    public String toString(){
        return wordCache.toString();
    }
}
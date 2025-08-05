package Chapter7_v1;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class IteratorAdapter implements Enumeration<Integer> {
    Iterator<Integer> it;
    IteratorAdapter(List<Integer> arr){
        this.it = arr.iterator();
    }


    @Override
    public boolean hasMoreElements() {
        return it.hasNext();
    }

    @Override
    public Integer nextElement() {
        return it.next();
    }

    public void delete(){
        this.it.remove();
    }
}

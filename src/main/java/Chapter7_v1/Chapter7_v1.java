package Chapter7_v1;

import java.util.Arrays;
import java.util.List;

public class Chapter7_v1 {
    public static void main(String[] args){

        List<Integer> arr = Arrays.asList(0, 1, 2, 3);

        IteratorAdapter iteratorAdapter = new IteratorAdapter(arr);

        while(iteratorAdapter.hasMoreElements()){
            System.out.println(iteratorAdapter.nextElement());
        }

    }
}

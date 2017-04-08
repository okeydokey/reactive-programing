package toby;

import java.util.Iterator;

/**
 * Created by yhkim on 2017-02-23.
 */
public class IteratorTest {
    // Iterator <---> observable
    // Pull <---> Push
    public static void main(String[] args) {

//        Iterable<Integer> iter = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

        Iterable<Integer> iter = () ->
            new Iterator<Integer>() {
                int i = 0;
                final static int MAX = 10;

                @Override
                public boolean hasNext() {
                    return i < MAX;
                }

                @Override
                public Integer next() {
                    return ++i;
                }
            };

        for(Integer i : iter) {
            System.out.println(i);
        }

        for(Iterator<Integer> i = iter.iterator(); i.hasNext();) {
            System.out.println(i.next());
        }
    }
}

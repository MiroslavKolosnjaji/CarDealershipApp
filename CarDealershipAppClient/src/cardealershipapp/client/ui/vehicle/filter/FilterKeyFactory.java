package cardealershipapp.client.ui.vehicle.filter;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * @author Miroslav Kološnjaji
 */
public class FilterKeyFactory {

    public static Queue<Key> prepareAndGetKeys() {

        /**
         * There are all combobox indexes from integer array, and we will use that indexes to get specific filter state for vehicle list
         *  TODO -- 1
         *  TODO -- 1,2
         *  TODO -- 1,3
         *  TODO -- 1,4
         *  TODO -- 1,5
         *  TODO -- 1,6
         *  TODO -- 1,7
         *  TODO -- 1,6,7
         *  TODO -- 1,2,3
         *  TODO -- 1,2,6
         *  TODO -- 1,2,7
         *  TODO -- 1,2,6,7
         *  TODO -- 1,2,3,4
         *  TODO -- 1,2,3,4,5
         *  TODO -- 1,2,3,4,5,6
         *  TODO -- 1,2,3,4,5,6,7
         *  TODO -- 3
         *  TODO -- 3,4
         *  TODO -- 3,5
         *  TODO -- 3,6
         *  TODO -- 3,7
         *  TODO -- 4
         *  TODO -- 4,5
         *  TODO -- 4,6
         *  TODO -- 4,7
         *  TODO -- 5
         *  TODO -- 5,6
         *  TODO -- 5,7
         *  TODO -- 6
         *  TODO -- 6,7
         *  TODO -- 7
         */

        Queue<Key> keyQueue = new ArrayDeque<>();

        keyQueue.add(new Key(new Integer[]{0, 0, 0, 0, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 0, 0, 0, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 2, 0, 0, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 0, 3, 0, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 0, 0, 4, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 0, 0, 0, 5, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 0, 0, 0, 0, 6, 0}));
        keyQueue.add(new Key(new Integer[]{1, 0, 0, 0, 0, 0, 7}));
        keyQueue.add(new Key(new Integer[]{1, 0, 0, 0, 0, 6, 7}));
        keyQueue.add(new Key(new Integer[]{1, 2, 3, 0, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 2, 0, 0, 0, 6, 0}));
        keyQueue.add(new Key(new Integer[]{1, 2, 0, 0, 0, 0, 7}));
        keyQueue.add(new Key(new Integer[]{1, 2, 0, 0, 0, 6, 7}));
        keyQueue.add(new Key(new Integer[]{1, 2, 3, 4, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 2, 3, 4, 5, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 2, 3, 4, 5, 6, 0}));
        keyQueue.add(new Key(new Integer[]{1, 2, 3, 4, 5, 6, 7}));
        keyQueue.add(new Key(new Integer[]{0, 0, 3, 0, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 3, 4, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 3, 0, 5, 0, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 3, 0, 0, 6, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 3, 0, 0, 0, 7}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 4, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 4, 5, 0, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 4, 0, 6, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 4, 0, 0, 7}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 0, 5, 0, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 0, 5, 6, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 0, 5, 0, 7}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 0, 0, 6, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 0, 0, 6, 7}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 0, 0, 0, 7}));

        return keyQueue;
    }

    public static Key generatekey(Integer[] comboIndexes) {
        Integer[] generatedKey = new Integer[7];

        for (int i = 0; i < comboIndexes.length; i++) {
            if (comboIndexes[i] > 0)
                generatedKey[i] = i + 1;
            else
                generatedKey[i] = 0;

        }
        return new Key(generatedKey);
    }

}

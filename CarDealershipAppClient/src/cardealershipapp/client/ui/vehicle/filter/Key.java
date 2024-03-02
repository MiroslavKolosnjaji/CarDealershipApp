package cardealershipapp.client.ui.vehicle.filter;

import java.util.Arrays;

/**
 * @author Miroslav Kološnjaji
 */
public class Key {

    private final Integer[] key;

    public Key(Integer[] key) {
        this.key = key;
    }


    @Override
    public String toString() {
        return  Arrays.toString(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key key1 = (Key) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(key, key1.key);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(key);
    }
}

import edu.yu.introtoalgs.WordLayout;
import edu.yu.introtoalgs.WordLayoutBase;
import org.junit.Test;
import org.junit.Assert;
import java.util.*;
import static edu.yu.introtoalgs.WordLayoutBase.Grid ;
import static edu.yu.introtoalgs.WordLayoutBase.LocationBase ;

import static org.junit.Assert.assertThrows; // can i import this

public class GridTest {
    @Test
    // test checks if IAEs are working properly when requirements aren't met
    public void notInRequirements(){
        List<String> words = new ArrayList<>();
        words.add("hello");
        // check if negative rows throws IAE
        assertThrows(IllegalArgumentException.class, () -> { WordLayout wl = new WordLayout(-1, 10, words);});
        // check if negative columns throws IAE
        assertThrows(IllegalArgumentException.class, () -> { WordLayout wl = new WordLayout(10, -1, words);});
        // check if empty list throws IAE
        words.remove("hello");
        assertThrows(IllegalArgumentException.class, () -> { WordLayout wl = new WordLayout(10, 10, words);});
        // check if null list throws IAE
        words.add(null);
        assertThrows(IllegalArgumentException.class, () -> { WordLayout wl = new WordLayout(10, 10, words);});
        // check if null word in valid list throws IAE
        words.remove(null);
        words.add("HEY");
        words.add(null);
        assertThrows(IllegalArgumentException.class, () -> { WordLayout wl = new WordLayout(10, 10, words);});
        // check if empty word in valid list throws IAE
        words.remove(null);
        words.add("");
        assertThrows(IllegalArgumentException.class, () -> { WordLayout wl = new WordLayout(10, 10, words);});
    }

    @ Test
    public void simpleWordSearch(){
        List<String> words = new ArrayList<>();
        words.add("TO");
        words.add("SEE");
        words.add("OF");
        words.add("ME");
        WordLayout wl = new WordLayout(3, 3, words);
        System.out.println(wl.getGrid());
        System.out.println(wl.locations("ME"));
    }

    @Test
    public void wordsWontFit(){
        List<String> words = new ArrayList<>();
        words.add("TO");
        words.add("SEE");
        words.add("OF");
        words.add("ME");
        words.add("I");
        assertThrows(IllegalArgumentException.class, () -> { WordLayout wl = new WordLayout(3, 3, words);});
    }

    @Test
    public void reallyBigWordSearch(){
        List<String> words = new ArrayList<>();
        words.add("HELLO");
        words.add("RAANAN");
        words.add("THEY");
        words.add("FOUR");
        words.add("NICE");
        words.add("COOL");
        words.add("ME");
        words.add("TO");
        words.add("GRAY");
        words.add("HI");
        words.add("DONE");
        words.add("I");
        WordLayout wl = new WordLayout(6, 7, words);
        System.out.println(wl.getGrid());
        System.out.println(wl.locations("HELLO"));
        System.out.println(wl.locations("RAANAN"));
    }
}


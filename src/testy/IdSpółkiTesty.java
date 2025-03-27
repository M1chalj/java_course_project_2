package testy;

import org.junit.jupiter.api.Test;
import systemtransakcyjny.IdSpółki;
import static org.junit.jupiter.api.Assertions.*;

public class IdSpółkiTesty {

    @Test
    public void niepoprawneID() {
        assertThrows(IllegalArgumentException.class, () -> new IdSpółki(""));
        assertThrows(IllegalArgumentException.class, () -> new IdSpółki("123"));
        assertThrows(IllegalArgumentException.class, () -> new IdSpółki("ALAMAKOTA"));
        assertThrows(IllegalArgumentException.class, () -> new IdSpółki("Euro"));
        assertThrows(IllegalArgumentException.class, () -> new IdSpółki("RL09"));
    }

    @Test
    public void poprawneID() {
        assertDoesNotThrow(() -> new IdSpółki("GOGL"));
    }

    @Test
    public void równość() {
        IdSpółki id1 = new IdSpółki("ABC");
        IdSpółki id2 = new IdSpółki("ABC");
        IdSpółki id3 = new IdSpółki("ABBA");
        assertTrue(id1.equals(id2));
        assertFalse(id1.equals(this));
        assertFalse(id1.equals(id3));
    }
}

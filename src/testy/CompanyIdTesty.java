package testy;

import org.junit.jupiter.api.Test;
import tradingsystem.CompanyId;
import static org.junit.jupiter.api.Assertions.*;

public class CompanyIdTesty {

    @Test
    public void niepoprawneID() {
        assertThrows(IllegalArgumentException.class, () -> new CompanyId(""));
        assertThrows(IllegalArgumentException.class, () -> new CompanyId("123"));
        assertThrows(IllegalArgumentException.class, () -> new CompanyId("ALAMAKOTA"));
        assertThrows(IllegalArgumentException.class, () -> new CompanyId("Euro"));
        assertThrows(IllegalArgumentException.class, () -> new CompanyId("RL09"));
    }

    @Test
    public void poprawneID() {
        assertDoesNotThrow(() -> new CompanyId("GOGL"));
    }

    @Test
    public void równość() {
        CompanyId id1 = new CompanyId("ABC");
        CompanyId id2 = new CompanyId("ABC");
        CompanyId id3 = new CompanyId("ABBA");
        assertTrue(id1.equals(id2));
        assertFalse(id1.equals(this));
        assertFalse(id1.equals(id3));
    }
}

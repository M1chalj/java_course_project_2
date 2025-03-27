package testy;

import org.junit.jupiter.api.Test;
import systemtransakcyjny.SMA;

import static org.junit.jupiter.api.Assertions.*;

public class SMATesty {

    @Test
    public void poprawneUżycie() {
        SMA średnia = new SMA(5);
        for (int i = 0; i < 10; i++) {
            średnia.następnaWartość(i);
        }
        assertEquals(średnia.średnia(), 7);
        średnia.następnaWartość(10);
        assertEquals(średnia.średnia(), 8);
    }

    @Test
    public void niepoprawneUżycie() {
        SMA średnia = new SMA(100);
        assertThrows(UnsupportedOperationException.class, średnia::średnia);
        for (int i = 0; i < 99; i++) {
            średnia.następnaWartość(5);
        }
        assertThrows(UnsupportedOperationException.class, średnia::średnia);
        średnia.następnaWartość(5);
        assertEquals(średnia.średnia(), 5);
    }

    @Test
    public void niepoprawneTworzenie() {
        assertThrows(IllegalArgumentException.class, () -> new SMA(-5));
        assertThrows(IllegalArgumentException.class, () -> new SMA(0));
    }
}

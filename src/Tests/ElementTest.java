package Tests;

import game.bin.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ElementTest {

    private Element element;

    @BeforeEach
    void setUp() {
        this.element = new Element(1, 2);
    }

    @AfterEach
    void tearDown() {
        this.element = null;
    }

    @Test
    void setPosition() {
        assertEquals(1, this.element.getLineIndex());
        assertEquals(2, this.element.getColumnIndex());
        this.element.setPosition(4, 0);
        assertEquals(4, this.element.getLineIndex());
        assertEquals(0, this.element.getColumnIndex());
        assertThrows(IllegalArgumentException.class, () -> this.element.setPosition(-1, 0));
    }

    @Test
    void setX() {
        this.element.setX(5);
        assertEquals(5, this.element.getRectangle().x);
        this.element.setX(0);
        assertEquals(0, this.element.getRectangle().x);
        assertThrows(IllegalArgumentException.class, () -> this.element.setX(-2));
    }

    @Test
    void setY() {
        this.element.setY(5);
        assertEquals(5, this.element.getRectangle().y);
        this.element.setY(0);
        assertEquals(0, this.element.getRectangle().y);
        assertThrows(IllegalArgumentException.class, () -> this.element.setY(-1));
    }

    @Test
    void getRectangle() {
        Rectangle r = new Rectangle(15, 10, 5, 5);
        Element.setSizeUnity(5);
        Element e = new Element(1, 2);
        assertEquals(r, e.getRectangle());
    }

    @Test
    void setSizeUnity() {
        Element.setSizeUnity(50);
        assertEquals(50, Element.getSizeUnity());
        Element.setSizeUnity(42);
        assertEquals(42, Element.getSizeUnity());
        assertThrows(IllegalArgumentException.class, () -> Element.setSizeUnity(-1));
    }
}
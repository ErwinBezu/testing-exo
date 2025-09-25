package org.exemple;

import org.example.Calculator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    private final Calculator calculator = new Calculator();

    @Test
    void testAddition() {
        assertEquals(5, calculator.add(2, 3));
        assertEquals(0, calculator.add(-2, 2));
        assertEquals(-7, calculator.add(-5, -2));
    }

    @Test
    void testSubtraction() {
        assertEquals(1, calculator.sub(3, 2));
        assertEquals(-4, calculator.sub(-2, 2));
        assertEquals(-3, calculator.sub(-5, -2));
    }

    @Test
    void testMultiplication() {
        assertEquals(6, calculator.mul(2, 3));
        assertEquals(-4, calculator.mul(-2, 2));
        assertEquals(10, calculator.mul(-5, -2));
        assertEquals(0, calculator.mul(0, 100));
    }

    @Test
    void testDivision() {
        assertEquals(2, calculator.div(6, 3));
        assertEquals(-2, calculator.div(-6, 3));
        assertEquals(3, calculator.div(-9, -3));
    }

    @Test
    void testDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> calculator.div(5, 0));
    }
}

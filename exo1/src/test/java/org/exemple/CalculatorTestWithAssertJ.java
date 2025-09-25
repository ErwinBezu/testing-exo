package org.exemple;

import org.example.Calculator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CalculatorTestWithAssertJ {

    private final Calculator calc = new Calculator();

    @Test
    void addition() {
        assertThat(calc.add(2, 3)).isEqualTo(5);
        assertThat(calc.add(-2, 2)).isZero();
        assertThat(calc.add(-5, -2)).isEqualTo(-7);
    }

    @Test
    void subtraction() {
        assertThat(calc.sub(3, 2)).isOne();
        assertThat(calc.sub(-2, 2)).isEqualTo(-4);
        assertThat(calc.sub(-5, -2)).isEqualTo(-3);
    }

    @Test
    void multiplication() {
        assertThat(calc.mul(2, 3)).isEqualTo(6);
        assertThat(calc.mul(-2, 2)).isNegative();
        assertThat(calc.mul(-5, -2)).isEqualTo(10);
        assertThat(calc.mul(0, 100)).isZero();
    }

    @Test
    void division() {
        assertThat(calc.div(6, 3)).isEqualTo(2);
        assertThat(calc.div(-6, 3)).isEqualTo(-2);
        assertThat(calc.div(-9, -3)).isEqualTo(3);
    }

    @Test
    void divisionByZero() {
        // Variante 1 : vérifier le type d’exception
        assertThatThrownBy(() -> calc.div(5, 0))
                .isInstanceOf(ArithmeticException.class);

    }
}
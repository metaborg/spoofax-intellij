package org.metaborg.core;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.metaborg.core.IntRange;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

@RunWith(Enclosed.class)
public final class IntRangeTests {

    public static class ConstructorTests {

        @Test
        public void emptyRangeStartsAndEndsAtZero() {
            IntRange sut = IntRange.EMPTY;

            assertEquals(0, sut.start);
            assertEquals(0, sut.end);
            assertEquals(0, sut.length());
            assertTrue(sut.isEmpty());
        }

        @Test
        public void createdEmptyRangeHasCorrectStartAndEnd() {
            IntRange sut = IntRange.at(5);

            assertEquals(5, sut.start);
            assertEquals(5, sut.end);
            assertEquals(0, sut.length());
            assertTrue(sut.isEmpty());
        }

        @Test
        public void createdSingletonRangeHasCorrectStartAndEnd() {
            IntRange sut = IntRange.is(5);

            assertEquals(5, sut.start);
            assertEquals(6, sut.end);
            assertEquals(1, sut.length());
            assertFalse(sut.isEmpty());
        }

        @Test
        public void createdRangeHasCorrectStartAndEnd() {
            IntRange sut = IntRange.between(4, 7);

            assertEquals(4, sut.start);
            assertEquals(7, sut.end);
            assertEquals(3, sut.length());
            assertFalse(sut.isEmpty());
        }

    }

    public static class ContainsTests {

        @RunWith(Theories.class)
        public static class ContainsTheory {
            @DataPoints
            public static IntRange[] data() {
                return new IntRange[] {
                        IntRange.at(2),
                        IntRange.at(3),
                        IntRange.at(4),
                        IntRange.at(5),
                        IntRange.at(6),
                        IntRange.at(7),
                        IntRange.at(8),
                        IntRange.at(9),
                        IntRange.at(10),
                        
                        IntRange.is(2),
                        IntRange.is(3),
                        IntRange.is(4),
                        IntRange.is(5),
                        IntRange.is(6),
                        IntRange.is(7),
                        IntRange.is(8),
                        IntRange.is(9),
                        IntRange.is(10),

                        IntRange.between(2, 4),
                        IntRange.between(3, 5),
                        IntRange.between(4, 6),
                        IntRange.between(5, 7),
                        IntRange.between(6, 8),
                        IntRange.between(7, 9),
                        IntRange.between(8, 10),
                        IntRange.between(9, 11),
                        IntRange.between(10, 12),
                };
            }

            @Theory
            public void iffContainsInSingletonRangeThenOverlaps(IntRange value) {
                IntRange sut = IntRange.is(5);
                assertEquals(1, sut.length());

                assumeTrue(sut.containsRange(value));
                assertTrue(sut.overlapsRange(value));
            }

            @Theory
            public void iffContainsInRangeThenOverlaps(IntRange value) {
                IntRange sut = IntRange.between(5, 7);
                assertEquals(2, sut.length());

                assumeTrue(sut.containsRange(value));
                assertTrue(sut.overlapsRange(value));
            }

            @Theory
            public void containsValueAndContainsSingleton(IntRange sut) {
                int value = 6;

                assertEquals(sut.contains(value), sut.containsRange(IntRange.is(value)));
            }
        }

        @Test
        public void containsInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.contains(4));
            assertFalse(sut.contains(5));
            assertFalse(sut.contains(6));
        }

        @Test
        public void containsInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.contains(4));
            assertTrue(sut.contains(5));
            assertFalse(sut.contains(6));
        }

        @Test
        public void containsInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.contains(4));
            assertTrue(sut.contains(5));
            assertTrue(sut.contains(6));
            assertFalse(sut.contains(7));
        }

        @Test
        public void containsEmptyRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.containsRange(IntRange.at(4)));
            assertFalse(sut.containsRange(IntRange.at(5)));
            assertFalse(sut.containsRange(IntRange.at(6)));
        }

        @Test
        public void containsEmptyRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.containsRange(IntRange.at(4)));
            assertFalse(sut.containsRange(IntRange.at(5)));
            assertFalse(sut.containsRange(IntRange.at(6)));
            assertFalse(sut.containsRange(IntRange.at(7)));
        }

        @Test
        public void containsEmptyRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.containsRange(IntRange.at(4)));
            assertFalse(sut.containsRange(IntRange.at(5)));
            assertTrue(sut.containsRange(IntRange.at(6)));
            assertFalse(sut.containsRange(IntRange.at(7)));
            assertFalse(sut.containsRange(IntRange.at(8)));
        }

        @Test
        public void containsSingletonRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.containsRange(IntRange.is(4)));
            assertFalse(sut.containsRange(IntRange.is(5)));
            assertFalse(sut.containsRange(IntRange.is(6)));
        }

        @Test
        public void containsSingletonRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.containsRange(IntRange.is(4)));
            assertTrue(sut.containsRange(IntRange.is(5)));
            assertFalse(sut.containsRange(IntRange.is(6)));
            assertFalse(sut.containsRange(IntRange.is(7)));
        }

        @Test
        public void containsSingletonRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.containsRange(IntRange.is(4)));
            assertTrue(sut.containsRange(IntRange.is(5)));
            assertTrue(sut.containsRange(IntRange.is(6)));
            assertFalse(sut.containsRange(IntRange.is(7)));
            assertFalse(sut.containsRange(IntRange.is(8)));
        }

        @Test
        public void containsRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.containsRange(IntRange.between(4, 6)));
            assertFalse(sut.containsRange(IntRange.between(5, 7)));
            assertFalse(sut.containsRange(IntRange.between(6, 8)));
        }

        @Test
        public void containsRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.containsRange(IntRange.between(4, 6)));
            assertFalse(sut.containsRange(IntRange.between(5, 7)));
            assertFalse(sut.containsRange(IntRange.between(6, 8)));
            assertFalse(sut.containsRange(IntRange.between(7, 9)));
        }

        @Test
        public void containsRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.containsRange(IntRange.between(4, 6)));
            assertTrue(sut.containsRange(IntRange.between(5, 7)));
            assertFalse(sut.containsRange(IntRange.between(6, 8)));
            assertFalse(sut.containsRange(IntRange.between(7, 9)));
            assertFalse(sut.containsRange(IntRange.between(8, 10)));
        }

    }

    public static class OverlapsTests {

        @RunWith(Theories.class)
        public static class OverlapsTheory {
            @DataPoints
            public static IntRange[] data() {
                return new IntRange[] {
                        IntRange.at(2),
                        IntRange.at(3),
                        IntRange.at(4),
                        IntRange.at(5),
                        IntRange.at(6),
                        IntRange.at(7),
                        IntRange.at(8),
                        IntRange.at(9),
                        IntRange.at(10),

                        IntRange.is(2),
                        IntRange.is(3),
                        IntRange.is(4),
                        IntRange.is(5),
                        IntRange.is(6),
                        IntRange.is(7),
                        IntRange.is(8),
                        IntRange.is(9),
                        IntRange.is(10),

                        IntRange.between(2, 4),
                        IntRange.between(3, 5),
                        IntRange.between(4, 6),
                        IntRange.between(5, 7),
                        IntRange.between(6, 8),
                        IntRange.between(7, 9),
                        IntRange.between(8, 10),
                        IntRange.between(9, 11),
                        IntRange.between(10, 12),
                };
            }

            @Theory
            public void iffOverlapsThenInverseOverlaps(IntRange value) {
                IntRange sut = IntRange.at(5);
                assertEquals(0, sut.length());

                assumeTrue(sut.overlapsRange(value));
                assertTrue(value.overlapsRange(sut));
            }

            @Theory
            public void iffOverlapsThenNotBeforeNotAfter(IntRange value) {
                IntRange sut = IntRange.at(5);
                assertEquals(0, sut.length());

                assumeTrue(sut.overlapsRange(value));
                assertFalse(value.isBeforeRange(sut));
                assertFalse(value.isAfterRange(sut));
                assertFalse(value.isStartedByRange(sut));
                assertFalse(value.isEndedByRange(sut));
            }

        }

        @Test
        public void overlapsEmptyRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.overlapsRange(IntRange.at(4)));
            assertFalse(sut.overlapsRange(IntRange.at(5)));
            assertFalse(sut.overlapsRange(IntRange.at(6)));
        }

        @Test
        public void overlapsEmptyRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.overlapsRange(IntRange.at(4)));
            assertFalse(sut.overlapsRange(IntRange.at(5)));
            assertFalse(sut.overlapsRange(IntRange.at(6)));
            assertFalse(sut.overlapsRange(IntRange.at(7)));
        }

        @Test
        public void overlapsEmptyRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.overlapsRange(IntRange.at(4)));
            assertFalse(sut.overlapsRange(IntRange.at(5)));
            assertTrue(sut.overlapsRange(IntRange.at(6)));
            assertFalse(sut.overlapsRange(IntRange.at(7)));
            assertFalse(sut.overlapsRange(IntRange.at(8)));
        }

        @Test
        public void overlapsSingletonRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.overlapsRange(IntRange.is(4)));
            assertFalse(sut.overlapsRange(IntRange.is(5)));
            assertFalse(sut.overlapsRange(IntRange.is(6)));
        }

        @Test
        public void overlapsSingletonRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.overlapsRange(IntRange.is(4)));
            assertTrue(sut.overlapsRange(IntRange.is(5)));
            assertFalse(sut.overlapsRange(IntRange.is(6)));
            assertFalse(sut.overlapsRange(IntRange.is(7)));
        }

        @Test
        public void overlapsSingletonRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.overlapsRange(IntRange.is(4)));
            assertTrue(sut.overlapsRange(IntRange.is(5)));
            assertTrue(sut.overlapsRange(IntRange.is(6)));
            assertFalse(sut.overlapsRange(IntRange.is(7)));
            assertFalse(sut.overlapsRange(IntRange.is(8)));
        }

        @Test
        public void overlapsRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.overlapsRange(IntRange.between(2, 4)));
            assertFalse(sut.overlapsRange(IntRange.between(3, 5)));
            assertTrue(sut.overlapsRange(IntRange.between(4, 6)));
            assertFalse(sut.overlapsRange(IntRange.between(5, 7)));
            assertFalse(sut.overlapsRange(IntRange.between(6, 8)));
        }

        @Test
        public void overlapsRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.overlapsRange(IntRange.between(2, 4)));
            assertFalse(sut.overlapsRange(IntRange.between(3, 5)));
            assertTrue(sut.overlapsRange(IntRange.between(4, 6)));
            assertTrue(sut.overlapsRange(IntRange.between(5, 7)));
            assertFalse(sut.overlapsRange(IntRange.between(6, 8)));
            assertFalse(sut.overlapsRange(IntRange.between(7, 9)));
        }

        @Test
        public void overlapsRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.overlapsRange(IntRange.between(2, 4)));
            assertFalse(sut.overlapsRange(IntRange.between(3, 5)));
            assertTrue(sut.overlapsRange(IntRange.between(4, 6)));
            assertTrue(sut.overlapsRange(IntRange.between(5, 7)));
            assertTrue(sut.overlapsRange(IntRange.between(6, 8)));
            assertFalse(sut.overlapsRange(IntRange.between(7, 9)));
            assertFalse(sut.overlapsRange(IntRange.between(8, 10)));
        }

    }

    public static class BeforeTests {

        @RunWith(Theories.class)
        public static class BeforeTheory {
            @DataPoints
            public static IntRange[] data() {
                return new IntRange[] {
                        IntRange.at(2),
                        IntRange.at(3),
                        IntRange.at(4),
                        IntRange.at(5),
                        IntRange.at(6),
                        IntRange.at(7),
                        IntRange.at(8),
                        IntRange.at(9),
                        IntRange.at(10),

                        IntRange.is(2),
                        IntRange.is(3),
                        IntRange.is(4),
                        IntRange.is(5),
                        IntRange.is(6),
                        IntRange.is(7),
                        IntRange.is(8),
                        IntRange.is(9),
                        IntRange.is(10),

                        IntRange.between(2, 4),
                        IntRange.between(3, 5),
                        IntRange.between(4, 6),
                        IntRange.between(5, 7),
                        IntRange.between(6, 8),
                        IntRange.between(7, 9),
                        IntRange.between(8, 10),
                        IntRange.between(9, 11),
                        IntRange.between(10, 12),
                };
            }

            @Theory
            public void beforeEmptyRange(IntRange value) {
                IntRange sut = IntRange.at(5);
                assertEquals(0, sut.length());

                assumeTrue(value.isBeforeRange(sut));
                assertEquals(value.isBeforeRange(sut), sut.isAfterRange(value));
                assertEquals(sut.isBeforeRange(value), value.isAfterRange(sut));
            }

            @Theory
            public void beforeSingletonRange(IntRange value) {
                IntRange sut = IntRange.is(5);
                assertEquals(1, sut.length());

                assumeTrue(value.isBeforeRange(sut));
                assertEquals(value.isBeforeRange(sut), !value.isAfterRange(sut));
                assertEquals(value.isBeforeRange(sut), sut.isAfterRange(value));
                assertEquals(sut.isBeforeRange(value), value.isAfterRange(sut));
            }


            @Theory
            public void beforeRange(IntRange value) {
                IntRange sut = IntRange.between(5, 7);
                assertEquals(2, sut.length());

                assumeTrue(value.isBeforeRange(sut));
                assertEquals(value.isBeforeRange(sut), !value.isAfterRange(sut));
                assertEquals(value.isBeforeRange(sut), sut.isAfterRange(value));
                assertEquals(sut.isBeforeRange(value), value.isAfterRange(sut));
            }

        }

        @Test
        public void isBeforeEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.isBefore(4));
            assertTrue(sut.isBefore(5));
            assertTrue(sut.isBefore(6));
        }

        @Test
        public void isBeforeSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.isBefore(4));
            assertFalse(sut.isBefore(5));
            assertTrue(sut.isBefore(6));
        }

        @Test
        public void isBeforeRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.isBefore(4));
            assertFalse(sut.isBefore(5));
            assertFalse(sut.isBefore(6));
            assertTrue(sut.isBefore(7));
        }

        @Test
        public void isBeforeEmptyRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.isBeforeRange(IntRange.at(4)));
            assertTrue(sut.isBeforeRange(IntRange.at(5)));
            assertTrue(sut.isBeforeRange(IntRange.at(6)));
        }

        @Test
        public void isBeforeEmptyRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.isBeforeRange(IntRange.at(4)));
            assertFalse(sut.isBeforeRange(IntRange.at(5)));
            assertTrue(sut.isBeforeRange(IntRange.at(6)));
            assertTrue(sut.isBeforeRange(IntRange.at(7)));
        }

        @Test
        public void isBeforeEmptyRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.isBeforeRange(IntRange.at(4)));
            assertFalse(sut.isBeforeRange(IntRange.at(5)));
            assertFalse(sut.isBeforeRange(IntRange.at(6)));
            assertTrue(sut.isBeforeRange(IntRange.at(7)));
            assertTrue(sut.isBeforeRange(IntRange.at(8)));
        }

        @Test
        public void isBeforeSingletonRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.isBeforeRange(IntRange.is(4)));
            assertTrue(sut.isBeforeRange(IntRange.is(5)));
            assertTrue(sut.isBeforeRange(IntRange.is(6)));
        }

        @Test
        public void isBeforeSingletonRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.isBeforeRange(IntRange.is(4)));
            assertFalse(sut.isBeforeRange(IntRange.is(5)));
            assertTrue(sut.isBeforeRange(IntRange.is(6)));
            assertTrue(sut.isBeforeRange(IntRange.is(7)));
        }

        @Test
        public void isBeforeSingletonRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.isBeforeRange(IntRange.is(4)));
            assertFalse(sut.isBeforeRange(IntRange.is(5)));
            assertFalse(sut.isBeforeRange(IntRange.is(6)));
            assertTrue(sut.isBeforeRange(IntRange.is(7)));
            assertTrue(sut.isBeforeRange(IntRange.is(8)));
        }

        @Test
        public void isBeforeRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.isBeforeRange(IntRange.between(3, 5)));
            assertFalse(sut.isBeforeRange(IntRange.between(4, 6)));
            assertTrue(sut.isBeforeRange(IntRange.between(5, 7)));
            assertTrue(sut.isBeforeRange(IntRange.between(6, 8)));
        }

        @Test
        public void isBeforeRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.isBeforeRange(IntRange.between(3, 5)));
            assertFalse(sut.isBeforeRange(IntRange.between(4, 6)));
            assertFalse(sut.isBeforeRange(IntRange.between(5, 7)));
            assertTrue(sut.isBeforeRange(IntRange.between(6, 8)));
            assertTrue(sut.isBeforeRange(IntRange.between(7, 9)));
        }

        @Test
        public void isBeforeRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.isBeforeRange(IntRange.between(3, 5)));
            assertFalse(sut.isBeforeRange(IntRange.between(4, 6)));
            assertFalse(sut.isBeforeRange(IntRange.between(5, 7)));
            assertFalse(sut.isBeforeRange(IntRange.between(6, 8)));
            assertTrue(sut.isBeforeRange(IntRange.between(7, 9)));
            assertTrue(sut.isBeforeRange(IntRange.between(8, 10)));
        }

    }

    public static class AfterTests {

        @Test
        public void isAfterEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertTrue(sut.isAfter(4));
            assertFalse(sut.isAfter(5));
            assertFalse(sut.isAfter(6));
        }

        @Test
        public void isAfterSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertTrue(sut.isAfter(4));
            assertFalse(sut.isAfter(5));
            assertFalse(sut.isAfter(6));
        }

        @Test
        public void isAfterRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertTrue(sut.isAfter(4));
            assertFalse(sut.isAfter(5));
            assertFalse(sut.isAfter(6));
            assertFalse(sut.isAfter(7));
        }

        @Test
        public void isAfterEmptyRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertTrue(sut.isAfterRange(IntRange.at(4)));
            assertTrue(sut.isAfterRange(IntRange.at(5)));
            assertFalse(sut.isAfterRange(IntRange.at(6)));
        }

        @Test
        public void isAfterEmptyRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertTrue(sut.isAfterRange(IntRange.at(4)));
            assertTrue(sut.isAfterRange(IntRange.at(5)));
            assertFalse(sut.isAfterRange(IntRange.at(6)));
            assertFalse(sut.isAfterRange(IntRange.at(7)));
        }

        @Test
        public void isAfterEmptyRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertTrue(sut.isAfterRange(IntRange.at(4)));
            assertTrue(sut.isAfterRange(IntRange.at(5)));
            assertFalse(sut.isAfterRange(IntRange.at(6)));
            assertFalse(sut.isAfterRange(IntRange.at(7)));
            assertFalse(sut.isAfterRange(IntRange.at(8)));
        }

        @Test
        public void isAfterSingletonRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertTrue(sut.isAfterRange(IntRange.is(4)));
            assertFalse(sut.isAfterRange(IntRange.is(5)));
            assertFalse(sut.isAfterRange(IntRange.is(6)));
        }

        @Test
        public void isAfterSingletonRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertTrue(sut.isAfterRange(IntRange.is(4)));
            assertFalse(sut.isAfterRange(IntRange.is(5)));
            assertFalse(sut.isAfterRange(IntRange.is(6)));
            assertFalse(sut.isAfterRange(IntRange.is(7)));
        }

        @Test
        public void isAfterSingletonRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertTrue(sut.isAfterRange(IntRange.is(4)));
            assertFalse(sut.isAfterRange(IntRange.is(5)));
            assertFalse(sut.isAfterRange(IntRange.is(6)));
            assertFalse(sut.isAfterRange(IntRange.is(7)));
            assertFalse(sut.isAfterRange(IntRange.is(8)));
        }

        @Test
        public void isAfterRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertTrue(sut.isAfterRange(IntRange.between(3, 5)));
            assertFalse(sut.isAfterRange(IntRange.between(4, 6)));
            assertFalse(sut.isAfterRange(IntRange.between(5, 7)));
            assertFalse(sut.isAfterRange(IntRange.between(6, 8)));
        }

        @Test
        public void isAfterRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertTrue(sut.isAfterRange(IntRange.between(3, 5)));
            assertFalse(sut.isAfterRange(IntRange.between(4, 6)));
            assertFalse(sut.isAfterRange(IntRange.between(5, 7)));
            assertFalse(sut.isAfterRange(IntRange.between(6, 8)));
            assertFalse(sut.isAfterRange(IntRange.between(7, 9)));
        }

        @Test
        public void isAfterRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertTrue(sut.isAfterRange(IntRange.between(3, 5)));
            assertFalse(sut.isAfterRange(IntRange.between(4, 6)));
            assertFalse(sut.isAfterRange(IntRange.between(5, 7)));
            assertFalse(sut.isAfterRange(IntRange.between(6, 8)));
            assertFalse(sut.isAfterRange(IntRange.between(7, 9)));
            assertFalse(sut.isAfterRange(IntRange.between(8, 10)));
        }

    }

    public static class StartsTests {

        @Test
        public void isStartedByEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertTrue(sut.isStartedBy(4));
            assertFalse(sut.isStartedBy(5));
            assertFalse(sut.isStartedBy(6));
        }

        @Test
        public void isStartedBySingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertTrue(sut.isStartedBy(4));
            assertFalse(sut.isStartedBy(5));
            assertFalse(sut.isStartedBy(6));
        }

        @Test
        public void isStartedByRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertTrue(sut.isStartedBy(4));
            assertFalse(sut.isStartedBy(5));
            assertFalse(sut.isStartedBy(6));
            assertFalse(sut.isStartedBy(7));
        }

        @Test
        public void isStartedByEmptyRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.isStartedByRange(IntRange.at(4)));
            assertTrue(sut.isStartedByRange(IntRange.at(5)));
            assertFalse(sut.isStartedByRange(IntRange.at(6)));
        }

        @Test
        public void isStartedByEmptyRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.isStartedByRange(IntRange.at(4)));
            assertTrue(sut.isStartedByRange(IntRange.at(5)));
            assertFalse(sut.isStartedByRange(IntRange.at(6)));
            assertFalse(sut.isStartedByRange(IntRange.at(7)));
        }

        @Test
        public void isStartedByEmptyRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.isStartedByRange(IntRange.at(4)));
            assertTrue(sut.isStartedByRange(IntRange.at(5)));
            assertFalse(sut.isStartedByRange(IntRange.at(6)));
            assertFalse(sut.isStartedByRange(IntRange.at(7)));
            assertFalse(sut.isStartedByRange(IntRange.at(8)));
        }

        @Test
        public void isStartedBySingletonRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertTrue(sut.isStartedByRange(IntRange.is(4)));
            assertFalse(sut.isStartedByRange(IntRange.is(5)));
            assertFalse(sut.isStartedByRange(IntRange.is(6)));
        }

        @Test
        public void isStartedBySingletonRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertTrue(sut.isStartedByRange(IntRange.is(4)));
            assertFalse(sut.isStartedByRange(IntRange.is(5)));
            assertFalse(sut.isStartedByRange(IntRange.is(6)));
            assertFalse(sut.isStartedByRange(IntRange.is(7)));
        }

        @Test
        public void isStartedBySingletonRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertTrue(sut.isStartedByRange(IntRange.is(4)));
            assertFalse(sut.isStartedByRange(IntRange.is(5)));
            assertFalse(sut.isStartedByRange(IntRange.is(6)));
            assertFalse(sut.isStartedByRange(IntRange.is(7)));
            assertFalse(sut.isStartedByRange(IntRange.is(8)));
        }

        @Test
        public void isStartedByRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.isStartedByRange(IntRange.between(2, 4)));
            assertTrue(sut.isStartedByRange(IntRange.between(3, 5)));
            assertFalse(sut.isStartedByRange(IntRange.between(4, 6)));
            assertFalse(sut.isStartedByRange(IntRange.between(5, 7)));
            assertFalse(sut.isStartedByRange(IntRange.between(6, 8)));
        }

        @Test
        public void isStartedByRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.isStartedByRange(IntRange.between(2, 4)));
            assertTrue(sut.isStartedByRange(IntRange.between(3, 5)));
            assertFalse(sut.isStartedByRange(IntRange.between(4, 6)));
            assertFalse(sut.isStartedByRange(IntRange.between(5, 7)));
            assertFalse(sut.isStartedByRange(IntRange.between(6, 8)));
            assertFalse(sut.isStartedByRange(IntRange.between(7, 9)));
        }

        @Test
        public void isStartedByRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.isStartedByRange(IntRange.between(2, 4)));
            assertTrue(sut.isStartedByRange(IntRange.between(3, 5)));
            assertFalse(sut.isStartedByRange(IntRange.between(4, 6)));
            assertFalse(sut.isStartedByRange(IntRange.between(5, 7)));
            assertFalse(sut.isStartedByRange(IntRange.between(6, 8)));
            assertFalse(sut.isStartedByRange(IntRange.between(7, 9)));
            assertFalse(sut.isStartedByRange(IntRange.between(8, 10)));
        }

    }

    public static class EndsTests {

        @Test
        public void isEndedByEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.isEndedBy(4));
            assertTrue(sut.isEndedBy(5));
            assertFalse(sut.isEndedBy(6));
        }

        @Test
        public void isEndedBySingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.isEndedBy(4));
            assertFalse(sut.isEndedBy(5));
            assertTrue(sut.isEndedBy(6));
        }

        @Test
        public void isEndedByRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.isEndedBy(4));
            assertFalse(sut.isEndedBy(5));
            assertFalse(sut.isEndedBy(6));
            assertTrue(sut.isEndedBy(7));
            assertFalse(sut.isEndedBy(8));
        }

        @Test
        public void isEndedByEmptyRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.isEndedByRange(IntRange.at(4)));
            assertTrue(sut.isEndedByRange(IntRange.at(5)));
            assertFalse(sut.isEndedByRange(IntRange.at(6)));
        }

        @Test
        public void isEndedByEmptyRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.isEndedByRange(IntRange.at(4)));
            assertFalse(sut.isEndedByRange(IntRange.at(5)));
            assertTrue(sut.isEndedByRange(IntRange.at(6)));
            assertFalse(sut.isEndedByRange(IntRange.at(7)));
        }

        @Test
        public void isEndedByEmptyRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.isEndedByRange(IntRange.at(4)));
            assertFalse(sut.isEndedByRange(IntRange.at(5)));
            assertFalse(sut.isEndedByRange(IntRange.at(6)));
            assertTrue(sut.isEndedByRange(IntRange.at(7)));
            assertFalse(sut.isEndedByRange(IntRange.at(8)));
        }

        @Test
        public void isEndedBySingletonRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.isEndedByRange(IntRange.is(4)));
            assertTrue(sut.isEndedByRange(IntRange.is(5)));
            assertFalse(sut.isEndedByRange(IntRange.is(6)));
        }

        @Test
        public void isEndedBySingletonRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.isEndedByRange(IntRange.is(4)));
            assertFalse(sut.isEndedByRange(IntRange.is(5)));
            assertTrue(sut.isEndedByRange(IntRange.is(6)));
            assertFalse(sut.isEndedByRange(IntRange.is(7)));
        }

        @Test
        public void isEndedBySingletonRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.isEndedByRange(IntRange.is(4)));
            assertFalse(sut.isEndedByRange(IntRange.is(5)));
            assertFalse(sut.isEndedByRange(IntRange.is(6)));
            assertTrue(sut.isEndedByRange(IntRange.is(7)));
            assertFalse(sut.isEndedByRange(IntRange.is(8)));
        }

        @Test
        public void isEndedByRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.isEndedByRange(IntRange.between(2, 4)));
            assertFalse(sut.isEndedByRange(IntRange.between(3, 5)));
            assertFalse(sut.isEndedByRange(IntRange.between(4, 6)));
            assertTrue(sut.isEndedByRange(IntRange.between(5, 7)));
            assertFalse(sut.isEndedByRange(IntRange.between(6, 8)));
        }

        @Test
        public void isEndedByRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.isEndedByRange(IntRange.between(2, 4)));
            assertFalse(sut.isEndedByRange(IntRange.between(3, 5)));
            assertFalse(sut.isEndedByRange(IntRange.between(4, 6)));
            assertFalse(sut.isEndedByRange(IntRange.between(5, 7)));
            assertTrue(sut.isEndedByRange(IntRange.between(6, 8)));
            assertFalse(sut.isEndedByRange(IntRange.between(7, 9)));
        }

        @Test
        public void isEndedByRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.isEndedByRange(IntRange.between(2, 4)));
            assertFalse(sut.isEndedByRange(IntRange.between(3, 5)));
            assertFalse(sut.isEndedByRange(IntRange.between(4, 6)));
            assertFalse(sut.isEndedByRange(IntRange.between(5, 7)));
            assertFalse(sut.isEndedByRange(IntRange.between(6, 8)));
            assertTrue(sut.isEndedByRange(IntRange.between(7, 9)));
            assertFalse(sut.isEndedByRange(IntRange.between(8, 10)));
        }

    }

    public static class TouchedTests {

        @Test
        public void isTouchedByEmptyRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.isTouchedByRange(IntRange.at(4)));
            assertTrue(sut.isTouchedByRange(IntRange.at(5)));
            assertFalse(sut.isTouchedByRange(IntRange.at(6)));
        }

        @Test
        public void isTouchedByEmptyRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.isTouchedByRange(IntRange.at(4)));
            assertTrue(sut.isTouchedByRange(IntRange.at(5)));
            assertTrue(sut.isTouchedByRange(IntRange.at(6)));
            assertFalse(sut.isTouchedByRange(IntRange.at(7)));
        }

        @Test
        public void isTouchedByEmptyRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.isTouchedByRange(IntRange.at(4)));
            assertTrue(sut.isTouchedByRange(IntRange.at(5)));
            assertTrue(sut.isTouchedByRange(IntRange.at(6)));
            assertTrue(sut.isTouchedByRange(IntRange.at(7)));
            assertFalse(sut.isTouchedByRange(IntRange.at(8)));
        }

        @Test
        public void isTouchedBySingletonRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.isTouchedByRange(IntRange.is(3)));
            assertTrue(sut.isTouchedByRange(IntRange.is(4)));
            assertTrue(sut.isTouchedByRange(IntRange.is(5)));
            assertFalse(sut.isTouchedByRange(IntRange.is(6)));
            assertFalse(sut.isTouchedByRange(IntRange.is(7)));
        }

        @Test
        public void isTouchedBySingletonRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.isTouchedByRange(IntRange.is(3)));
            assertTrue(sut.isTouchedByRange(IntRange.is(4)));
            assertTrue(sut.isTouchedByRange(IntRange.is(5)));
            assertTrue(sut.isTouchedByRange(IntRange.is(6)));
            assertFalse(sut.isTouchedByRange(IntRange.is(7)));
        }

        @Test
        public void isTouchedBySingletonRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.isTouchedByRange(IntRange.is(3)));
            assertTrue(sut.isTouchedByRange(IntRange.is(4)));
            assertTrue(sut.isTouchedByRange(IntRange.is(5)));
            assertTrue(sut.isTouchedByRange(IntRange.is(6)));
            assertTrue(sut.isTouchedByRange(IntRange.is(7)));
            assertFalse(sut.isTouchedByRange(IntRange.is(8)));
            assertFalse(sut.isTouchedByRange(IntRange.is(9)));
        }

        @Test
        public void isTouchedByRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertFalse(sut.isTouchedByRange(IntRange.between(2, 4)));
            assertTrue(sut.isTouchedByRange(IntRange.between(3, 5)));
            assertTrue(sut.isTouchedByRange(IntRange.between(4, 6)));
            assertTrue(sut.isTouchedByRange(IntRange.between(5, 7)));
            assertFalse(sut.isTouchedByRange(IntRange.between(6, 8)));
        }

        @Test
        public void isTouchedByRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertFalse(sut.isTouchedByRange(IntRange.between(2, 4)));
            assertTrue(sut.isTouchedByRange(IntRange.between(3, 5)));
            assertTrue(sut.isTouchedByRange(IntRange.between(4, 6)));
            assertTrue(sut.isTouchedByRange(IntRange.between(5, 7)));
            assertTrue(sut.isTouchedByRange(IntRange.between(6, 8)));
            assertFalse(sut.isTouchedByRange(IntRange.between(7, 9)));
        }

        @Test
        public void isTouchedByRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertFalse(sut.isTouchedByRange(IntRange.between(2, 4)));
            assertTrue(sut.isTouchedByRange(IntRange.between(3, 5)));
            assertTrue(sut.isTouchedByRange(IntRange.between(4, 6)));
            assertTrue(sut.isTouchedByRange(IntRange.between(5, 7)));
            assertTrue(sut.isTouchedByRange(IntRange.between(6, 8)));
            assertTrue(sut.isTouchedByRange(IntRange.between(7, 9)));
            assertFalse(sut.isTouchedByRange(IntRange.between(8, 10)));
            assertFalse(sut.isTouchedByRange(IntRange.between(9, 11)));
        }

    }

    public static class IntersectionTests {

        @Test
        public void intersectionWithEmptyRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertEquals(IntRange.at(5), sut.intersectionWith(IntRange.at(5)));
        }

        @Test
        public void intersectionWithEmptyRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertEquals(IntRange.at(5), sut.intersectionWith(IntRange.at(5)));
            assertEquals(IntRange.at(6), sut.intersectionWith(IntRange.at(6)));
        }

        @Test
        public void intersectionWithEmptyRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertEquals(IntRange.at(5), sut.intersectionWith(IntRange.at(5)));
            assertEquals(IntRange.at(6), sut.intersectionWith(IntRange.at(6)));
            assertEquals(IntRange.at(7), sut.intersectionWith(IntRange.at(7)));
        }

        @Test
        public void intersectionWithSingletonRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertEquals(IntRange.at(5), sut.intersectionWith(IntRange.is(4)));
            assertEquals(IntRange.at(5), sut.intersectionWith(IntRange.is(5)));
        }

        @Test
        public void intersectionWithSingletonRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertEquals(IntRange.at(5), sut.intersectionWith(IntRange.is(4)));
            assertEquals(IntRange.is(5), sut.intersectionWith(IntRange.is(5)));
            assertEquals(IntRange.at(6), sut.intersectionWith(IntRange.is(6)));
        }

        @Test
        public void intersectionWithSingletonRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertEquals(IntRange.at(5), sut.intersectionWith(IntRange.is(4)));
            assertEquals(IntRange.is(5), sut.intersectionWith(IntRange.is(5)));
            assertEquals(IntRange.is(6), sut.intersectionWith(IntRange.is(6)));
            assertEquals(IntRange.at(7), sut.intersectionWith(IntRange.is(7)));
        }

        @Test
        public void intersectionWithInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertEquals(IntRange.at(5), sut.intersectionWith(IntRange.between(3, 5)));
            assertEquals(IntRange.at(5), sut.intersectionWith(IntRange.between(4, 6)));
            assertEquals(IntRange.at(5), sut.intersectionWith(IntRange.between(5, 7)));
        }

        @Test
        public void intersectionWithInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertEquals(IntRange.at(5), sut.intersectionWith(IntRange.between(3, 5)));
            assertEquals(IntRange.is(5), sut.intersectionWith(IntRange.between(4, 6)));
            assertEquals(IntRange.is(5), sut.intersectionWith(IntRange.between(5, 7)));
            assertEquals(IntRange.at(6), sut.intersectionWith(IntRange.between(6, 8)));
        }

        @Test
        public void intersectionWithInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertEquals(IntRange.at(5), sut.intersectionWith(IntRange.between(3, 5)));
            assertEquals(IntRange.is(5), sut.intersectionWith(IntRange.between(4, 6)));
            assertEquals(IntRange.between(5, 7), sut.intersectionWith(IntRange.between(5, 7)));
            assertEquals(IntRange.is(6), sut.intersectionWith(IntRange.between(6, 8)));
            assertEquals(IntRange.at(7), sut.intersectionWith(IntRange.between(7, 9)));
        }

    }

    public static class UnionTests {

        @Test
        public void unionWithEmptyRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertEquals(IntRange.at(5), sut.unionWith(IntRange.at(5)));
        }

        @Test
        public void unionWithEmptyRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertEquals(IntRange.is(5), sut.unionWith(IntRange.at(5)));
            assertEquals(IntRange.is(5), sut.unionWith(IntRange.at(6)));
        }

        @Test
        public void unionWithEmptyRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertEquals(IntRange.between(5, 7), sut.unionWith(IntRange.at(5)));
            assertEquals(IntRange.between(5, 7), sut.unionWith(IntRange.at(6)));
            assertEquals(IntRange.between(5, 7), sut.unionWith(IntRange.at(7)));
        }

        @Test
        public void unionWithSingletonRangeInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertEquals(IntRange.is(4), sut.unionWith(IntRange.is(4)));
            assertEquals(IntRange.is(5), sut.unionWith(IntRange.is(5)));
        }

        @Test
        public void unionWithSingletonRangeInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertEquals(IntRange.between(4, 6), sut.unionWith(IntRange.is(4)));
            assertEquals(IntRange.is(5), sut.unionWith(IntRange.is(5)));
            assertEquals(IntRange.between(5, 7), sut.unionWith(IntRange.is(6)));
        }

        @Test
        public void unionWithSingletonRangeInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertEquals(IntRange.between(4, 7), sut.unionWith(IntRange.is(4)));
            assertEquals(IntRange.between(5, 7), sut.unionWith(IntRange.is(5)));
            assertEquals(IntRange.between(5, 7), sut.unionWith(IntRange.is(6)));
            assertEquals(IntRange.between(5, 8), sut.unionWith(IntRange.is(7)));
        }

        @Test
        public void unionWithInEmptyRange() {
            IntRange sut = IntRange.at(5);
            assertEquals(0, sut.length());

            assertEquals(IntRange.between(3, 5), sut.unionWith(IntRange.between(3, 5)));
            assertEquals(IntRange.between(4, 6), sut.unionWith(IntRange.between(4, 6)));
            assertEquals(IntRange.between(5, 7), sut.unionWith(IntRange.between(5, 7)));
        }

        @Test
        public void unionWithInSingletonRange() {
            IntRange sut = IntRange.is(5);
            assertEquals(1, sut.length());

            assertEquals(IntRange.between(3, 6), sut.unionWith(IntRange.between(3, 5)));
            assertEquals(IntRange.between(4, 6), sut.unionWith(IntRange.between(4, 6)));
            assertEquals(IntRange.between(5, 7), sut.unionWith(IntRange.between(5, 7)));
            assertEquals(IntRange.between(5, 8), sut.unionWith(IntRange.between(6, 8)));
        }

        @Test
        public void unionWithInRange() {
            IntRange sut = IntRange.between(5, 7);
            assertEquals(2, sut.length());

            assertEquals(IntRange.between(3, 7), sut.unionWith(IntRange.between(3, 5)));
            assertEquals(IntRange.between(4, 7), sut.unionWith(IntRange.between(4, 6)));
            assertEquals(IntRange.between(5, 7), sut.unionWith(IntRange.between(5, 7)));
            assertEquals(IntRange.between(5, 8), sut.unionWith(IntRange.between(6, 8)));
            assertEquals(IntRange.between(5, 9), sut.unionWith(IntRange.between(7, 9)));
        }

    }
}

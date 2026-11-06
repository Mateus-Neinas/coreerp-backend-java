package com.coreerp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class SummaryServiceTest {
    @Test
    void mapsCountsIntoSummary() {
        var summary = new SummaryService().summarize(4, 12, 7);
        assertEquals(4, summary.customers());
        assertEquals(12, summary.products());
        assertEquals(7, summary.orders());
    }
}

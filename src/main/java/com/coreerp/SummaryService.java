package com.coreerp;

public final class SummaryService {
    public Summary summarize(int customers, int products, int orders) {
        return new Summary(customers, products, orders);
    }

    public record Summary(int customers, int products, int orders) {}
}

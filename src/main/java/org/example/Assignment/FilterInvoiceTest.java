package org.example.Assignment;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

// added this class to stub our database
class StubQueryInvoicesDAO extends QueryInvoicesDAO {
    public StubQueryInvoicesDAO() {
        super(null); // Pass null since we don't need a real database
    }

    // Mock the database with customers and values
    @Override
    public List<Invoice> all() {
        return Arrays.asList(
                new Invoice("1", 50),
                new Invoice("2", 200),
                new Invoice("3", 75),
                new Invoice("4", 400)
        );
    }
}


public class FilterInvoiceTest {
    private FilterInvoice filterInvoice;

    @Before
    public void setUp() {
        filterInvoice = new FilterInvoice(new StubQueryInvoicesDAO());
    }

    // this test ensures the mocked database results in the correct size and values
    @Test
    public void filterInvoiceTest() {
        List<Invoice> filtered = filterInvoice.lowValueInvoices();

        assertNotNull("Filtered list should not be null", filtered);
        assertEquals("Filtered list should contain exactly 2 invoices", 2, filtered.size());
        assertTrue("All invoices should have a value less than 100",
                filtered.stream().allMatch(invoice -> invoice.getValue() < 100));
    }

    // this test is essentially the same as the previous test,
    // the database is mocked outside the test class
    @Test
    public void filterInvoiceStubbedTest() {
        List<Invoice> filtered = filterInvoice.lowValueInvoices();

        assertNotNull("Filtered list should not be null", filtered);
        assertEquals("Filtered list should contain exactly 2 invoices", 2, filtered.size());
        assertTrue("All invoices should have a value less than 100",
                filtered.stream().allMatch(invoice -> invoice.getValue() < 100));
    }
}

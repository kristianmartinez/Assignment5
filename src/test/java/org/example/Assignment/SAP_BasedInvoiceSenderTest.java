package org.example.Assignment;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class SAP_BasedInvoiceSenderTest {
    private FilterInvoice mockFilter;
    private SAP mockSap;
    private SAP_BasedInvoiceSender sender;

    @Before
    public void setUp() {
        mockFilter = mock(FilterInvoice.class);
        mockSap = mock(SAP.class);
        sender = new SAP_BasedInvoiceSender(mockFilter, mockSap);
    }

    @Test
    public void testWhenLowInvoicesSent() {
        // Stub filter to return low-value invoices
        List<Invoice> lowInvoices = Arrays.asList(new Invoice("1", 50), new Invoice("2",75));
        when(mockFilter.lowValueInvoices()).thenReturn(lowInvoices);

        // Call method under test
        sender.sendLowValuedInvoices();

        // Verify SAP send() was called for each invoice
        verify(mockSap, times(1)).send(new Invoice("1", 50));
        verify(mockSap, times(1)).send(new Invoice("2", 75));
        verifyNoMoreInteractions(mockSap);  // Ensure no extra calls
    }

    @Test
    public void testWhenNoInvoices() {
        // Stub filter to return an empty list
        when(mockFilter.lowValueInvoices()).thenReturn(Collections.emptyList());

        // Call method under test
        sender.sendLowValuedInvoices();

        // Verify SAP send() was never called
        verify(mockSap, never()).send(any(Invoice.class));
    }
}

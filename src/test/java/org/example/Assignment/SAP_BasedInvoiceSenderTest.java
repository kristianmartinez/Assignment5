package org.example.Assignment;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void testWhenLowInvoicesSent() throws FailToSendSAPInvoiceException {
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
    public void testWhenNoInvoices() throws FailToSendSAPInvoiceException {
        // Stub filter to return an empty list
        when(mockFilter.lowValueInvoices()).thenReturn(Collections.emptyList());

        // Call method under test
        sender.sendLowValuedInvoices();

        // Verify SAP send() was never called
        verify(mockSap, never()).send(any(Invoice.class));
    }

    @Test
    public void testThrowExceptionWhenBadInvoice() throws FailToSendSAPInvoiceException {
        Invoice badInvoice = new Invoice("42", 30); // Invoice ID 42, value 30 (example)

        // Stubbing: When sap.send(badInvoice) is called, it throws the custom exception
        doThrow(new FailToSendSAPInvoiceException("SAP Invoice Failed")).when(mockSap).send(badInvoice);

        // Mock the filter to return a list containing the bad invoice
        when(mockFilter.lowValueInvoices()).thenReturn(Collections.singletonList(badInvoice));

        // Act: Call sendLowValuedInvoices and capture failed invoices
        List<Invoice> failedInvoices = sender.sendLowValuedInvoices();

        // Assert: Verify that the exception was handled and the invoice is in the failed list
        assertEquals(1, failedInvoices.size());  // Only one invoice should fail
        assertEquals(badInvoice, failedInvoices.get(0)); // The failed invoice should be the bad one

        // Verify that sap.send() was called for the invoice
        verify(mockSap).send(badInvoice);
    }
}

package com.example.bankingapp.util;

import junit.framework.TestCase;

import org.junit.Test;

public class ValidateTextTest extends TestCase {

    @Test
    public void testTrimZero_NullInput() {
        assertEquals(null, ValidateText.trimZero(null));
    }

    @Test
    public void testTrimZero_LeadingZeros() {
        assertEquals("123", ValidateText.trimZero("000123"));
    }

    @Test
    public void testTrimZero_NoLeadingZeros() {
        assertEquals("123", ValidateText.trimZero("123"));
    }

}
/*
 * Copyright (c) ${year} Unravelling Technologies
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package io.unravellingtechnologies.excalibur;import org.junit.AfterClass;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Integration tests for the Excalibur project.
 * @author Tiago Veiga
 * @version 1.0.0
 */
public class ExcaliburIntegrationTests {

    /**
     * Class logger.
     */
    public static Logger LOGGER = LoggerFactory.getLogger(ExcaliburIntegrationTests.class.getSimpleName());

    /**
     * Name of the file that should be read to prepare the tests.
     */
    private static String FILENAME = "test-book.xlsx";

    /**
     * Instance of excalibur that will used to run the tests.
     */
    private static Excalibur excaliburInst;

    /**
     * Sets up the needed resources for the tests to run.
     * @throws IOException Exception while opening the target file.
     */
    @BeforeClass
    public static void setup() throws IOException {
        final ClassLoader classLoader = ExcaliburIntegrationTests.class.getClassLoader();
        final URL resourceURL = classLoader.getResource(FILENAME);

        if (resourceURL == null) {
            throw new FileNotFoundException("Test book was not found!");
        }

        final String filePath = resourceURL.getPath();



        excaliburInst = new Excalibur(filePath);
    }

    /**
     * Cleans the used resources.
     */
    @AfterClass
    public static void tearDown() {
        excaliburInst.close();
    }

    /**
     * Opens the first sheet and checks if all is as expected.
     */
    @Test
    public void testAccessOneSheet() {
        final Sheet sheetA = excaliburInst.getSheet("Sheet1");

        assertNotNull("Sheet is null!", sheetA);
        assertFalse("The header is empty!", sheetA.getHeader().isEmpty());
        assertEquals("The number of read columns does not match expected!", 4, sheetA.getHeader().size());
        assertEquals("The number of read rows does not match expected!", 10, sheetA.getRows().size());
    }

    /**
     * Tests the access to the second sheet.
     */
    @Test
    public void testAccessSecondSheet() {
        final Sheet sheetB = excaliburInst.getSheet("Sheet2");

        assertNotNull("Sheet is null!", sheetB);
        assertFalse("The header is empty!", sheetB.getHeader().isEmpty());
        assertEquals("The number of read columns does not match expected!", 4, sheetB.getHeader().size());
        assertEquals("The number of read rows does not match expected!", 10, sheetB.getRows().size());
    }

    /**
     * Tests filtering a column by one value.
     */
    @Test
    public void testFilterColumnOneValue() {
        final Sheet sheetA = excaliburInst.getSheet("Sheet1");

        assertEquals("Number of filtered rows do not match expected!",
            1, sheetA.getRowsWhere("ColumnB","bazinga").size());
    }

    /**
     * Tests filtering a column by one value.
     */
    @Test
    public void testFilterColumnMultipleValues() {
        final Sheet sheetB = excaliburInst.getSheet("Sheet2");

        List<String> filter = new LinkedList<>();
        filter.add("bazinga");
        filter.add("bingo");

        assertEquals("Number of filtered rows do not match expected!",
            3, sheetB.getRowsWhere("ColumnD", filter).size());
    }

    /**
     * Tests filtering a column by one value.
     */
    @Test
    public void testFilterColumnMultipleFilters() {
        final Sheet sheetB = excaliburInst.getSheet("Sheet2");

        Map<String, List<String>> filters = new HashMap<>();

        List<String> filter = new LinkedList<>();
        filter.add("bazinga");
        filter.add("bingo");

        filters.put("ColumnD", filter);

        filter = new LinkedList<>();
        filter.add("oje");

        filters.put("ColumnB", filter);

        assertEquals("Number of filtered rows do not match expected!",
            1, sheetB.getRowsWhere(filters).size());
    }
}

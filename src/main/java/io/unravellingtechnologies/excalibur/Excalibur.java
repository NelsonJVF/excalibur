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
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *  IN THE SOFTWARE.
 */

package io.unravellingtechnologies.excalibur;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Object to read Microsoft Excel files.
 * 
 * @author Tiago Veiga
 * @version 1.0.0
 *
 */
public class Excalibur {

  /**
   * Default logger for the class.
   */
  private static final Logger logger = LoggerFactory.getLogger("Excalibur");

  /**
   * Instance of the book to be read.
   */
  private static XSSFWorkbook excelBook;
  
  /**
   * Holds the different sheets that were opened.
   */
  private Map<String,Sheet> sheets;

  /**
   * Creates an Excalibur instance over a given file.
   * 
   * @param filename Path to the Excel spreadsheet to load.
   */
  public Excalibur(final String filename) throws IOException {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Opening book " + filename + "...");
      }

      excelBook = new XSSFWorkbook(new FileInputStream(filename));
      
      if (excelBook == null) {
        throw new IOException("Could not open " + filename);
      }

      initialiseBook();

      if (logger.isDebugEnabled()) {
        logger.debug("Book successfuly opened.");
      }

    } catch (final FileNotFoundException fileNotFound) {

      logger.error("File " + filename + " could not be found.");
      throw fileNotFound;

    } catch (final IOException ioException) {
      logger.error("File " + filename + " could not be opened.");
      throw ioException;
    }
  }

  /**
   * Initialises the book needed items, like the list of sheets.
   */
  private void initialiseBook() {
    sheets = new HashMap<String, Sheet>();
  }

  /**
   * Closes the open resources.
   */
  public void close() {
    try {
      excelBook.close();
    } catch (final IOException ioException) {
      logger.error("Error while closing the sheet.");
    }
  }
  
  /**
   * Loads a sheet into the instance.
   * @param sheetName Loads the provided sheet data.
   */
  private void loadSheet(String sheetName) {
    if (logger.isDebugEnabled()) {
      logger.debug("Loading sheet " + sheetName + "...");
    }
    
    final Sheet newSheet = new Sheet(excelBook, sheetName);

    if (!newSheet.getRows().isEmpty()) {
      sheets.put(sheetName, newSheet);

      if (logger.isDebugEnabled()) {
        logger.debug("Sheet " + sheetName + " successfuly loaded.");
      }

      return;
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Specified sheet is empty or was not found.");
    }
  }
  
  /**
   * Gets a sheet from the book.
   * @param sheetName Name of the sheet to fetch.
   * @return Sheet requested.
   */
  public Sheet getSheet(final String sheetName) {
    if (!sheets.containsKey(sheetName)) {
      loadSheet(sheetName);
    }
    
    return sheets.get(sheetName);
  }

}

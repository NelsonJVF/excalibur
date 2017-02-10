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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Implements an interface to hold an Excel based configuration sheet.
 * 
 * @author Tiago Veiga
 * @version 1.0.0
 *
 */
class Sheet {

  /**
   * Logger for the class.
   */
  private static final Logger logger = LoggerFactory.getLogger("Excalibur");

  /**
   * The sheetHeader consists of a map between the name of the field and its index in the columns.
   * Key: column index, value: name of the column.
   */
  private Map<Integer, String> sheetHeader;

  /**
   * List of rows existing in this sheet.
   */
  private List<Row> rows;

  /**
   * Default constructor. Loads the sheet into the object, with all the data in it.
   * 
   * @param excelBook Excel book object where the sheet will be read from.
   * @param sheetName Name of the sheet to read.
   */
  Sheet(final XSSFWorkbook excelBook, final String sheetName) {

    final XSSFSheet sheet = excelBook.getSheet(sheetName);

    /*
     * If it has rows proceed. Note: to be considered valid at least one row must exist (header).
     */
    if (sheet.getPhysicalNumberOfRows() > 1) {
      sheetHeader = new HashMap<Integer, String>();
      rows = new ArrayList<Row>();

      setSheetHeader(sheet);
      loadRows(sheet);
    }
  }

  /**
   * Initializes the sheet header structure.
   * 
   * @param sheet Sheet POI object used to initialize the header of this sheet.
   */
  private void setSheetHeader(XSSFSheet sheet) {
    if (logger.isDebugEnabled()) {
      logger.debug("Setting sheet header...");
    }

    org.apache.poi.ss.usermodel.Row firstRow = sheet.getRow(sheet.getFirstRowNum());

    if (firstRow.getPhysicalNumberOfCells() == 0) {
      return;
    }

    for (Iterator<Cell> it = firstRow.cellIterator(); it.hasNext();) {
      Cell cell = it.next();

      sheetHeader.put(cell.getColumnIndex(), cell.getStringCellValue());
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Finished setting the sheet header.");
    }
  }

  /**
   * Loads all the rows that have content into the Sheet structure.
   */
  private void loadRows(XSSFSheet sheet) {
    if (logger.isDebugEnabled()) {
      logger.debug("Loading sheet rows...");
    }

    if (sheet.getPhysicalNumberOfRows() < 2) {
      return;
    }

    for (Iterator<org.apache.poi.ss.usermodel.Row> rowIt = sheet.rowIterator(); rowIt.hasNext();) {
      org.apache.poi.ss.usermodel.Row tableRow = rowIt.next();

      if (tableRow.getRowNum() != sheet.getFirstRowNum()) {
        Row row = new Row(new HashMap<String, String>());

        for (Iterator<Cell> cellIt = tableRow.cellIterator(); cellIt.hasNext();) {
          Cell cell = cellIt.next();

          row.addCell(getColumnName(cell.getColumnIndex()), cell.getStringCellValue());
        }

        rows.add(row);
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Completed loading " + rows.size() + " rows.");
    }
  }

  /**
   * Gets the sheet header.
   * 
   * @return Sheet header. First position is the column index, second it's value (name).
   */
  public Map<Integer, String> getHeader() {
    return sheetHeader;
  }

  /**
   * Gets the rows of the sheet.
   * 
   * @return Rows existing in this sheet.
   */
  public List<Row> getRows() {
    return rows;
  }

  /**
   * Returns the column name of a given index.
   * 
   * @param columnIndex Index of the column in the sheet.
   * @return Name of the column.
   */
  public String getColumnName(int columnIndex) {
    return sheetHeader.get(columnIndex);
  }

  /**
   * Gets the list of rows filtered by a column name.
   * 
   * @param filterColumnName Column to be filtered.
   * @param filterColumnValue Value to search for.
   * @return List of matched rows.
   */
  public List<Row> getRowsWhere(String filterColumnName, String filterColumnValue) {
    List<Row> filteredRows = new ArrayList<Row>();

    for (Iterator<Row> it = rows.iterator(); it.hasNext();) {
      Row row = it.next();

      if (row.getCell(filterColumnName).compareTo(filterColumnValue) == 0) {
        filteredRows.add(row);
      }
    }

    return filteredRows;
  }

  /**
   * Gets a list of rows where the specified filtering was matched. We search for
   * the values in the list in the column indicated.
   * @param filterColumnName Name of the column where to look for the values.
   * @param filterColumnValues Values to search for.
   * @return List with the rows.
   */
  public List<Row> getRowsWhere(String filterColumnName, List<String> filterColumnValues) {
    List<Row> filteredRows = new ArrayList<>();

    for (Iterator<Row> it = rows.iterator(); it.hasNext();) {
      Row row = it.next();

      if (filterColumnValues.contains(row.getCell(filterColumnName))) {
        filteredRows.add(row);
      }
    }

    return filteredRows;
  }

  /**
   * Gets a list of rows where the specified filter was matched. Multiple
   * column names can be specified (key of the Map), and for each a list
   * of values to match can be supplied (value of the Map).
   * @param filters Map with the filter to be matched.
   * @param operator Type of operator to Where clause 'AND' or 'OR'
   * @return List of matched rows.
   */
  public List<Row> getRowsWhere(Map<String, List<String>> filters, String operator) {
    if (filters == null || filters.isEmpty()) {
      return rows;
    }
    
    List<Row> filteredRows = new ArrayList<Row>();
    List<Boolean> controlVar = new ArrayList<Boolean>();

    for (Row row: this.rows) {
      for (Entry<String, List<String>> entry : filters.entrySet()) {
        if (entry.getValue().contains(row.getCell(entry.getKey())))
          controlVar.add(true);
        else
          controlVar.add(false);
      }

      if(operator == "AND" && !controlVar.contains(false))
        filteredRows.add(row);
      else if(operator == "OR" && controlVar.contains(true))
        filteredRows.add(row);
      else if(operator != "AND" && operator != "OR")
        logger.error("Invalid operator ! The possible values are 'AND' or 'OR'.");

      controlVar.clear();
    }

    return filteredRows;
  }
}

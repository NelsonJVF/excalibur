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

import java.util.Map;

/**
 * Represents a row of data read from a sheet.
 * @author Tiago Veiga
 * @version 1.0.0
 */
public class Row {
  
  /**
   * Existing cells in this row.
   */
  private Map<String,String> cells;
  
  /**
   * Default constructor.
   * @param cells Map with the cells that compose this row.
   */
  public Row(Map<String,String> cells) {
    this.cells = cells;
  }
  
  /**
   * Gets a cell by column name
   * @param columnName Name of the column to fetch.
   * @return String value of the cell.
   */
  public String getCell(String columnName) {
    return cells.get(columnName);
  }
  
  /**
   * Puts a cell into the row.
   * @param columnName Name of the column.
   * @param cellValue Data of the cell.
   */
  public void addCell(String columnName, String cellValue) {
    this.cells.put(columnName, cellValue);
  }
}

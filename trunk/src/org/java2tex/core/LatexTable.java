/*
 *                       Java2TeX 
 * Professional Document Preparation with Java and LaTeX
 * 
 * Copyright 2008, Emptoris, Inc. and individual contributors
 * as indicated by the @author tags.  
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org. 
 */
package org.java2tex.core;

import java.util.ArrayList;

import org.apache.log4j.Logger;


/**
 * Instances of this class encapsulate a table inside a LaTeX document.
 * This is a bare bones implementation. Our goal is to (eventually) 
 * expose all the versatility and power of LaTeX through our APIs. 
 * Hence, naturally, early adopters of Java2TeX should expect this 
 * class to change significantly.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * 
 * @since   <tt>0.1</tt> 
 * @version <tt>1.0</tt>
 */
public class LatexTable {
	
	private static final Logger log = Logger.getLogger(LatexTable.class);

	private static volatile int maxRowsPerPage=32;
	
	/** 
	 * This ID is set by the <CODE>LatexDocument</CODE>, it can be used for
	 * reference of the table from anywhere else inside the document.
	 */ 
	private String id=null;
	
	// GENERAL
	private int nRows;
	private int nCols;
	private String caption=null;
	
	// COLUMNS
	private StringBuilder cAlignment=new StringBuilder();
	private ArrayList<ColumnMeta> columnMeta=null;
	
	// HEADERS
	private String[] headers;
	
	// BODY
	private String[][] tableArray;

	private StringBuilder latex = new StringBuilder(); 
	
	private boolean isLandscape = false;
	private boolean hasHorizontalLines=false;

	/**
	 * Elementary constructor. 
	 * @param caption
	 * @param rows
	 */
	public LatexTable(String caption, int rows, int cols) {
		
		this.caption = caption;
		this.nRows = rows;
		this.nCols = cols;
		
		tableArray = new String[nRows][nCols];
	}

	public void alignColumns(char cAlign) throws Java2TeXException {

		if (tableArray != null) {
			
			if (cAlignment!=null && cAlignment.length() > 0) {
				cAlignment.delete(0, cAlignment.length());
			}
			
			for (int j=0; j<nCols; j++) {
				cAlignment.append("|").append(cAlign);
			}
			
			cAlignment.append("|");
			
		} else {
			throw new Java2TeXException("The text array is NULL. Did you load your data?");
		}
	}
	
	public void alignColumns(char[] colAlign) throws Java2TeXException {

		if (tableArray != null) {
			
			if (colAlign.length >= nCols) {
				log.error("You passed an array whose size exceeds the specified number of columns!");
				log.warn("Only the first "+nCols+" columns will be aligned according to your input.");	
			}
			
			if (cAlignment!=null && cAlignment.length() > 0) {
				cAlignment.delete(0, cAlignment.length());
			}
			
			for (int j=0; j<nCols; j++) {
				if (j<colAlign.length) {
					cAlignment.append("|").append(colAlign[j]);
				} else {
					log.error("You passed an array whose size is less than the specified number of columns!");
					log.warn("Only the first "+colAlign.length+" columns will be aligned according to your input.");
					log.warn("The rest of the columns will be centered");
					cAlignment.append("|").append('c');
				}
			}
			
			cAlignment.append("|");
			
		} else {
			throw new Java2TeXException("The text array is NULL. Did you load your data?");
		}
	}
	
	/**
	 * You can create a LaTeX table manually or <i>in bulk</i> (by building the <tt>tableArray</tt>).
	 *  
	 * This auxiliary method provides the common initialization of the LaTeX source for the table.
	 * 
	 */
	public void initLatex() {
		if ( nRows > getMaxRowsPerPage() ) {
			insert("\\begin{longtable}");
		} else {
			insert("\\begin{tabular}");
		}
		add(getColumnAlignment());		
	}

	/**
	 * This method contains a number of logical steps for building the LaTeX source.
	 * The following is a partial list:
	 * <UL>
	 *
	 *   <LI>It checks whether we should use the <tt>tabular</tt> or the <tt>longtable</tt> environment 
	 * by comparing the number of table rows (as defined by <tt>nRows</tt>) with the maximum number 
	 * of rows per page parameter (as defined by <tt>maxRowsPerPage</tt>).</LI>
	 * 
	 *   <LI>It prints the headers if they are not <tt>NULL</tt> <B>and</B> the length of the associated
	 *   array is greater than zero</LI>
	 *   
	 *   <LI>It prints horizontal lines, if <tt>horizontalLines</tt> is <tt>true</tt></LI>
	 * </UL>
	 *    
	 * @return the LaTeX source representation of this table. 
	 * @throws Java2TeXException 
	 */
	public String getLatex() throws Java2TeXException {
	
		//If there is anything in the buffer, erase it
		if (latex.length() > 0) {
			
			// Nothing to build -- the table has been built manually

			// We already called initLatex()
			
		} else {
			
			if ( tableArray != null ) {
				initLatex();
				
				addHorizontalLine();
	
				if ( headers != null && headers.length > 0) {
					printHeaders();
				}
				
				if ( hasHorizontalLines()) {
					addHorizontalLine();
				}
				
				int dummyColumnCount=0;
				
				for (String[] rows : tableArray) {
					
					dummyColumnCount=0;
					
					for (String cell : rows) {
						
						if (dummyColumnCount > 0) {
							insert(" & "+cell);
						} else {
							insert(cell);
						}
						dummyColumnCount++;
					}
					
					if (this.hasHorizontalLines()) {
						add("\\\\ \\hline");
					} else {
						add("\\\\");
					}
				}			
			} else {
				throw new Java2TeXException("The text array is NULL. Did you load your data?");
			}
		}
		
		// This part is common; whether we added the content manually or not, 
		// we need to add the last horizontal line and close the environment
		addHorizontalLine();
		
		if ( nRows > getMaxRowsPerPage() ) {
			add("\\end{longtable}");
		} else {
			add("\\end{tabular}");
		}

		return latex.toString();
	}
	
	private String getColumnAlignment() {
		
		StringBuilder s = new StringBuilder("{");
		
		if ( columnMeta != null ) {
			
			for (int j=0; j< nCols; j++) {
				
				ColumnMeta colMeta = columnMeta.get(j);
				
				if (colMeta != null) {
					
					if (colMeta.hasLeftSeparator()) {
						s.append("|");
					}
			
					if (colMeta.getBackgroundColour() != null) {
						s.append(">{\\columncolor{"+colMeta.getBackgroundColour()+"}}");
					}
					s.append(colMeta.getAlignment());

					if (colMeta.getId() == nCols) {
						s.append("|");
					}
				
				} else {
					// If the column META information about the alignment was not set 
					// for this column then use the default
					s.append("|c|");
				}
			}
			
		} else {
			
			if ( cAlignment != null && cAlignment.length() > 0) {
				
				s.append(cAlignment.toString());
				
			} else {
				
				s.append("|");
				for (int j=0; j < nCols; j++) {
					s.append("l|");
				}
			}
		}
		
		s.append("}");
		
		return s.toString();
	}

	private void printHeaders() {
		int dummyColumnCount=0;

		if (headers != null) {

			for (String cell : headers) {
				if (dummyColumnCount > 0) {
					insert(" & \\bf{"+cell+"}");
				} else {
					insert("\\bf{"+cell+"}");
				}
				dummyColumnCount++;
			}
			
			add("\\\\ \\hline");
		}
	}

	public void addHorizontalLine() {
		add("\\hline");
	}
	
	/**
	 * This method allows us to draw a horizontal line that spans only
	 * a part of the table. In particular, it starts drawing a horizontal
	 * line from column <tt>start</tt> until the end of the table columns.
	 * 
	 * @param start
	 */
	public void addHorizontalLine(int start) {
		add("\\cline{"+start+"-"+nCols+"}");
	}
	
	/**
	 * This method allows us to draw a horizontal line that spans only
	 * a part of the table. In particular, it starts drawing a horizontal
	 * line from column <tt>start</tt> to column <tt>end</tt>.
	 *  
	 * @param start
	 * @param end
	 */
	public void addHorizontalLine(int start,int end) {
		add("\\cline{"+start+"-"+end+"}");
	}
	
	/**
	 * This method simply adds the LaTeX "end of row" mark, i.e. "\\"
	 */
	public void endRow() {
		add(" \\\\ ");
	}
	
	public void endColumn() {
		insert(" & ");
	}
	
	public void add(String[] rowCells) {
		int i=1;
		for (String txt : rowCells) {
			insert(txt);
			if (i < rowCells.length) {
				endColumn();
			}
			i++;
		}
	}
	
	public void add(String txt) {
		latex.append(txt).append("\n");
	}

	private void insert(String txt) {
		latex.append(txt);
	}

	public void setValues(String[][] values) {
		
		if (values.length > nRows) {
			log.error("You passed an array that exceeds the specified number of nodes in the constructor!");
			log.warn("The table will contain the first "+nRows+" number of rows, as specified in the constructor.");
		}

		int i=0;
		for (@SuppressWarnings("unused")
		String[] rows : values) {
			if ( i < nRows) {
				int j=0;
				tableArray[i] = new String[nCols];
				for (String cell : values[i]) {
					tableArray[i][j] = cell;
					j++;
				}
				i++;
			}
		}
	}
	
	/**
	 * This method allows us to merge the cells of outer columns that span several inner columns. 
	 * 
	 * It checks if the number of rows exceeds the total number of columns of this table.
	 *  
	 * @param columnSpan
	 * @param alignment
	 * @param columnName
	 * @throws Java2TeXException
	 */
	public void addMultiColumn(int columnSpan, Character alignment, String columnName) throws Java2TeXException {
	
		if (columnSpan > nCols) {
		
			throw new Java2TeXException("Invalid argument value for \"columnSpan\". " +
					"It cannot exceed "+nCols+" columns!");
		}
				
		insert("\\multicolumn{"+columnSpan+"}{|"+alignment+"|}{"+columnName+"}");
	}
	
	/**
	 * This method adds empty cells across many columns. It is useful for creating the
	 * empty space at the top left corner of a cross tabulation. 
	 * 
	 * It checks if the number of rows exceeds the total number of columns of this table
	 * 
	 * @param columnSpan
	 * @throws Java2TeXException
	 */
	public void addMultiColumn(int columnSpan) throws Java2TeXException {
		
		if (columnSpan > nCols) {
		
			throw new Java2TeXException("Invalid argument value for \"columnSpan\". It cannot exceed "+nCols+" columns!");
		}
		
		insert("\\multicolumn{"+columnSpan+"}{c}{}");
	}
	
	/**
	 * This method adds empty cells across many columns. It is useful for creating the
	 * empty space at the top left corner of a cross tabulation. 
	 * 
	 * It checks if the number of rows exceeds the total number of columns of this table
	 * 
	 * @param columnSpan
	 * @throws Java2TeXException
	 */
	public void addMultiColumn(int columnSpan, boolean addVerticalSeparator) throws Java2TeXException {
		
		if (columnSpan > nCols) {
		
			throw new Java2TeXException("Invalid argument value for \"columnSpan\". It cannot exceed "+nCols+" columns!");
		}
		
		insert("\\multicolumn{"+columnSpan+"}{c|}{}");
	}
	
	public void addMultiColumn(ColumnMeta c) throws Java2TeXException {
	
		StringBuilder txt = new StringBuilder("\\multicolumn{");
		if ( c.getColumnSpan() > nCols) {
			
			throw new Java2TeXException("Invalid argument value for \"columnSpan\". It cannot exceed "+nCols+" columns!");
			
		} else if ( c.getColumnSpan() < 2 ) {
			
			throw new Java2TeXException("Invalid argument value for \"columnSpan\". It must be greater than 1!");
		}
		
		txt.append(c.getColumnSpan());
		txt.append("}{");
		if ( c.getBackgroundColour() != null ) {
			txt.append(">\\columncolor{").append(c.getBackgroundColour()).append("}");
		} 
		
		txt.append(c.getAlignment()).append("}");
		
		txt.append("{");
		if ( c.getForegroundColor() != null ) {
			txt.append("\\color{").append(c.getForegroundColor()).append("}\textsf{");
			txt.append(c.getLabel());
			txt.append("}}");
		} else {
			txt.append(c.getLabel());
			txt.append("}");
		}
		
		insert(txt.toString());
	}
	
	/**
	 * This method is useful for cross tabulation.
	 * It merges the cells of multiple rows.
	 * 
	 * There is a heuristic involved here.
	 * 
	 * In order to get clean LaTeX implementations for the crosstabs, we resort to entering empty values
	 * for entries that correspond to multi-level information. This will work only for uniformly distributed
	 * nodes up to 2 levels.
	 * 
	 * TODO: Generalize it for arbitrary tree structure
	 * 
	 * @param rowSpan
	 * @param rowName
	 * @throws Java2TeXException
	 */
	public void addMultiRowCell(int[] rowSpan, String[][] cellContent) throws Java2TeXException {
		
		StringBuffer txt = new StringBuffer("\\multirow{");
		
		int depth = rowSpan.length;

		int numberOfLeafNodes=1;
		for (int i=0; i<depth; i++) {
			if ( rowSpan[i] <= 0) {
				throw new Java2TeXException("The cardinality for each level of the row span " +
						"should be greater than zero!\n Found: "+rowSpan[i]+" for i= "+i);
			}
			numberOfLeafNodes *=rowSpan[i];
		}
		
		if ( numberOfLeafNodes > nRows) {
			throw new Java2TeXException("Row span is greater than the table row size ("+nRows+")");
		} else {
			log.info("One row that spans "+numberOfLeafNodes+" rows");
		}
		
		int[] lidx = new int[numberOfLeafNodes];
		int[] dummy = new int[depth];
		for (int k=0; k<depth;k++) {
			dummy[k] = rowSpan[k]; 
		}

		//TODO: here, we could obviously use a recursive method
		//      For now, we limit it to 2 levels
		for (int i=0; i < numberOfLeafNodes; i++) {
			lidx[i] = getDepth(dummy,rowSpan,0);
		}

		txt.append(numberOfLeafNodes).append("}{*}{");
		txt.append(cellContent[0][0]).append("}");
		for (int j=1; j < nCols; j++) {
			txt.append(" & ").append(cellContent[0][j]);
		}
		
		txt.append("\\\\ \\cline{"+(depth+1)+"-"+nCols+"} \n");
		
		for (int i=1; i < numberOfLeafNodes; i++) {
			
			for (int j=1; j < nCols; j++) { //Even if the [i][0] element has a value, we must ignore it
				
				txt.append("  & ").append(cellContent[i][j]);
			}
			txt.append("\\\\ ");				
					
			if ( hasHorizontalLines()) {
				txt.append("\\cline{"+lidx[i]+"-"+nCols+"} \n");
			}
		}
		add(txt.toString());
	}
		
	private int getDepth(int[] values, int[] rowSpan, int cursor) {
		
		if ( (rowSpan.length -cursor) > 0) {
			values[cursor]--;
			if (values[cursor]>0) {
				return rowSpan.length + 1 -cursor;
			} else {
				values[cursor]=rowSpan[cursor];
				return getDepth(values,rowSpan,cursor+1);
			}
		} else {
			return rowSpan.length-1;
		}
	}
	
	public void addRow(int cursor, String[] row) throws Java2TeXException {
		
		if (cursor >= nRows) {
			throw new Java2TeXException("Row cursor is greater than the table row size ("+nRows+")");
		}
		
		if (row.length > nCols) {
			log.error("You passed an array whose size ("+row.length+")");
			log.error("exceeds the specified number of columns!");
			log.warn("The table will contain the first "+nCols+" columns.");
		}
		
		int j=0;
		for (String cell : row) {
			if (j<nCols) {
				tableArray[cursor][j] = cell;
				j++;
			}
		}
	}
	
	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the isLandscape
	 */
	public boolean isLandscape() {
		return isLandscape;
	}

	/**
	 * @param isLandscape the isLandscape to set
	 */
	public void setLandscape(boolean isLandscape) {
		this.isLandscape = isLandscape;
	}

	/**
	 * @return the hasHorizontalLines
	 */
	public boolean hasHorizontalLines() {
		return hasHorizontalLines;
	}

	/**
	 * @param hasHorizontalLines the hasHorizontalLines to set
	 */
	public void setHorizontalLines(boolean hasHorizontalLines) {
		this.hasHorizontalLines = hasHorizontalLines;
	}

	/**
	 * @return the headers
	 */
	public String[] getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

	/**
	 * @return the columnMeta
	 */
	public ArrayList<ColumnMeta> getColumnMeta() {
		return columnMeta;
	}

	/**
	 * @param columnMeta the columnMeta to set
	 */
	public void setColumnMeta(ArrayList<ColumnMeta> columnMeta) {
		this.columnMeta = columnMeta;
	}

	public void addColumn(ColumnMeta c) {
		columnMeta.add(c);
	}
	
	/**
	 * Convenience method for adding a range of columns with default values.
	 * It should be very useful for tables with many columns, only few of which need to be customized
	 * 
	 * @param begin
	 * @param end
	 */
	public void addColumn(int begin,int end) {
		for (int i=begin; i < end; i++) {
			addColumn(new ColumnMeta(i));
		}
	}
	
	/**
	 * Convenience method for adding a range of columns with given alignment.
	 * It should be very useful for tables with many columns, only few of which need to be customized
	 * 
	 * @param begin is the first index of the columns 
	 * @param end is the last index of the columns
	 * @param alignment is the alignment that all columns from 
	 * index <tt>begin</tt> to <tt>end</tt> should have
	 */
	public void addColumn(int begin,int end, char alignment) {
		for (int i=begin; i <= end; i++) {
			addColumn(new ColumnMeta(i,alignment));
		}
	}
	
	/**
	 * @return the nRows
	 */
	public int getNRows() {
		return nRows;
	}

	/**
	 * @return the nCols
	 */
	public int getNCols() {
		return nCols;
	}

	/**
	 * @return the cAlignment
	 */
	public StringBuilder getCAlignment() {
		return cAlignment;
	}

	/**
	 * @return the tableArray
	 */
	public String[][] getTableArray() {
		return tableArray;
	}

	/**
	 * @return the maxRowsPerPage
	 */
	public static int getMaxRowsPerPage() {
		return maxRowsPerPage;
	}

	/**
	 * @param maxRowsPerPage the maxRowsPerPage to set
	 */
	public static void setMaxRowsPerPage(int maxRowsPerPage) {
		LatexTable.maxRowsPerPage = maxRowsPerPage;
	}

}

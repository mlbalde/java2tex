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

	/** 
	 * This ID is set by the <CODE>LatexDocument</CODE>, it can be used for
	 * reference of the table from anywhere else inside the document.
	 */ 
	private String id=null;
	
	private int nRows;
	private int nCols;
	
	private String caption=null;
	
	private StringBuilder cAlignment=new StringBuilder();
	private String[] headers;
	
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
		tableArray = new String[rows][cols];
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

	public String getLatex() {
	
		//If there is anything in the buffer, erase it
		if (latex.length() > 0) {
			latex.delete(0, latex.length());
		}
		
		insert("\\begin{tabular}{");
		insert(cAlignment.toString());
		add("}");
		
		addHorizontalLine();
		
		printHeaders();
		
		if (this.hasHorizontalLines()) {
			add("\\hline");
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
		
		addHorizontalLine();
		
		add("\\end{tabular}");
		
		return latex.toString();
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
	
	private void add(String txt) {
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

}

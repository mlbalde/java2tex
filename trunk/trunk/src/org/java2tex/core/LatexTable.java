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
	
	private String caption=null;
	
	private StringBuilder cAlignment=new StringBuilder();
	
	private String[][] textArray;

	private StringBuilder latex = new StringBuilder(); 

	/**
	 * Elementary constructor. 
	 * @param caption
	 * @param rows
	 */
	public LatexTable(String caption, int rows) {
		this.caption = caption;
		this.nRows = rows;
		textArray = new String[rows][];
	}

	public void alignColumns(char cAlign) throws Java2TeXException {
		if (textArray != null) {
			int maxCols=-1;
			for (String[] rows : textArray) {
				if (rows.length > maxCols) {
					maxCols=rows.length;
				}
			}
	
			for (int j=0; j<maxCols; j++) {
				cAlignment.append("|").append(cAlign);
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
		
		/*
		 * TODO: Temporarily the location is [!htpb], however, this can be
		 * made configurable.  
		 */
		insert("\\begin{tabular}{");
		insert(cAlignment.toString());
		add("}");
		
		add("\\hline");
		int dummyColumnCount=0;
		for (String[] rows : textArray) {
			dummyColumnCount=0;
			for (String cell : rows) {
				if (dummyColumnCount > 0) {
					insert(" & "+cell);
				} else {
					insert(cell);
				}
				dummyColumnCount++;
			}
			add("\\\\ \\hline");
		}
		add("\\end{tabular}");
		return latex.toString();
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
		for (String[] rows : values) {
			if ( i < nRows) {
				int j=0;
				textArray[i] = new String[rows.length];
				for (String cell : values[i]) {
					textArray[i][j] = cell;
					j++;
				}
				i++;
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

}

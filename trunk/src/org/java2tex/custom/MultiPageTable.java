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
package org.java2tex.custom;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.java2tex.core.ColumnMeta;
import org.java2tex.core.Java2TeXException;
import org.java2tex.core.LatexTable;


/**
 * Instances of this class encapsulate a table inside a LaTeX document.
 * This is a bare bones implementation of a supertabular table. 
 * Our goal is to (eventually) expose all the versatility and power 
 * of LaTeX through our APIs. Hence, naturally, early adopters of 
 * Java2TeX should expect this class to change significantly.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * 
 * @since   <tt>0.3</tt> 
 * @version <tt>1.0</tt>
 */
public class MultiPageTable implements LatexTable {
	
	private static final Logger log = Logger.getLogger(MultiPageTable.class);

	/** 
	 * This ID is set by the <CODE>LatexDocument</CODE>, it can be used for
	 * reference of the table from anywhere else inside the document.
	 */ 
	private String id=null;
	
	// GENERAL
    private int nRows;
	private String caption=null;
	
	// COLUMNS
	private ArrayList<ColumnMeta> columnMeta;
	
	//ROWS
	private String shadeColor="lightgray";
	
	// BODY
	private ArrayList<String> tableRows; 
	private StringBuilder latex; 
	
	private boolean isLandscape = false;
	private boolean hasHorizontalLines=false;
	private boolean hasShading=false;
	
	/**
	 * Elementary constructor. 
	 * 
	 * @param caption
	 * @param rows
	 */
	public MultiPageTable(String caption) {
		
		this.caption = caption;
		columnMeta   = new ArrayList<ColumnMeta>();
		tableRows    = new ArrayList<String>();
		latex        = new StringBuilder();
	}

	/**
	 * This auxiliary method provides the common initialization steps 
	 * for the LaTeX source of the <code>supertabular</code> environment.
	 * 
	 * @throws Java2TeXException 
	 */
	public void initLatex() throws Java2TeXException {

		if ( isLandscape() ) {
			add("\\begin{landscape}");
		}
		
		printHeaders();

		printTails();
		
		add("\\bottomcaption{"+caption+"}\n");

		add("\\label{"+getId()+"}\n");
		
		if (hasShading()) {
			add("\\rowcolors{2}{"+getShadeColor()+"}{}");
		}

		add("\\begin{supertabular}"+getColumnAlignment());
	}

	/**
	 * It prints the headers of the table and adds a double horizontal line
	 */
	private void printHeaders() {
		
		add("\\tablehead {%");
		addHorizontalLine();
		addHorizontalLine();
		
		int count=0;
		for (ColumnMeta colMeta : getColumnMeta()) {
			if (count < getNCols()-1) {
				insert(colMeta.getHeader());
				insert(" & ");
			} else {
				insert(colMeta.getHeader());
				add("\\\\");
			}
			count++;
		}

		addHorizontalLine();
		addHorizontalLine();
		add("}");
	}

	/**
	 * It prints the tails of the table and adds some horizontal lines
	 * 
	 * @throws Java2TeXException
	 */
	private void printTails() throws Java2TeXException {
		add("\\tabletail{%");
		addHorizontalLine();
		addHorizontalLine();

		//TODO: Parameterize the alignment and the text
		addMultiColumn(getNCols(), "|c|", "Continued on next page $...$");
		add("\\\\");
		addHorizontalLine();
		add("}");

		add("\\tablelasttail{%");
		addHorizontalLine();
		addHorizontalLine();

		add("}");
	}

	/**
	 * This method contains a number of logical steps for building the LaTeX source:
	 * <UL>
	 *   <LI>It runs the initialization steps.</LI>
	 * 
	 *   <LI>It writes the table rows and adds horizontal lines, if <tt>horizontalLines</tt> is <tt>true</tt></LI>
	 *   
	 *   <LI>It adds the end statements, if <tt>isLandscape</tt> is <tt>true</tt></LI>
	 * </UL>
	 *    
	 * @return the LaTeX source representation of this table. 
	 * @throws Java2TeXException 
	 */
	public String getLatex() throws Java2TeXException {
	
		initLatex();
				
		addHorizontalLine();
	
		for (String row : tableRows) {
			
			insert(row); endRow();
			
			if ( hasHorizontalLines() ) {
				add("\\hline");
			}
		}
		
		//END statements
		add("\\end{supertabular}");
		
		if ( isLandscape() ) {
			add("\\end{landscape}");
		}

		return latex.toString();
	}

	/**
	 * The logic of this method is quite straightforward.
	 * The alignment for a column can either be defined explicitly by the caller 
	 * when the caller defines the <tt>ColumnMeta</tt> or it can be defined by
	 * setting the separators, the color, and the <tt>l,r,c</tt> values of the alignment.
	 * 
	 * Notice that if you want to use something like <tt>p{5cm}</tt> then you should explicitly
	 * declare the rest of the properties as well, since the length of the string for the 
	 * <tt>p{...}</tt> call has length greater than 1.
	 *  
	 * @return
	 * @throws Java2TeXException
	 */
	public String getColumnAlignment() throws Java2TeXException {
		
		StringBuilder s = new StringBuilder("{");
		
		if ( columnMeta != null ) {
			
			for (ColumnMeta colMeta : columnMeta) {
				
				if (colMeta.getAlignment().length() == 1) {
					if (colMeta.hasLeftSeparator()) {
						s.append("|");
					}
				
					if (colMeta.getBackgroundColour() != null) {
						s.append(">{\\columncolor{"+colMeta.getBackgroundColour()+"}}");
					}
					
					s.append(colMeta.getAlignment());
	
					if (colMeta.hasRightSeparator()) {
						s.append("|");
					}
				} else {
					
					s.append(colMeta.getAlignment());					
				}
			}
			
		} else {
			
			throw new Java2TeXException("Define the META information for the columns before using the table!");
		}
		
		s.append("}");
		
		return s.toString();
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
		add("\\cline{"+start+"-"+getNCols()+"}");
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
		add(" \\tabularnewline ");
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
	
	public void addRow(String row) {
		tableRows.add(row);
	}

	private void insert(String txt) {
		latex.append(txt);
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
	public void addMultiColumn(int columnSpan, String alignment, String columnName) throws Java2TeXException {
	
		if (columnSpan > getNCols()) {
		
			throw new Java2TeXException("Invalid argument value for \"columnSpan\". " +
					"It cannot exceed "+getNCols()+" columns!");
		}
				
		insert("\\multicolumn{"+columnSpan+"}{"+alignment+"}{"+columnName+"}");
	}
	
	/**
	 * This method adds empty cells across many columns. 
	 * 
	 * It checks if the number of rows exceeds the total number of columns of this table
	 * 
	 * @param columnSpan
	 * @throws Java2TeXException
	 */
	public void addMultiColumn(int columnSpan) throws Java2TeXException {
		
		if (columnSpan > getNCols()) {
		
			throw new Java2TeXException("Invalid argument value for \"columnSpan\". It cannot exceed "+getNCols()+" columns!");
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
		
		if (columnSpan > getNCols()) {
		
			throw new Java2TeXException("Invalid argument value for \"columnSpan\". It cannot exceed "+getNCols()+" columns!");
		}
		
		insert("\\multicolumn{"+columnSpan+"}{c|}{}");
	}
	
	public void addMultiColumn(ColumnMeta c) throws Java2TeXException {
	
		StringBuilder txt = new StringBuilder("\\multicolumn{");
		if ( c.getColumnSpan() > getNCols()) {
			
			throw new Java2TeXException("Invalid argument value for \"columnSpan\". It cannot exceed "+getNCols()+" columns!");
			
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
			txt.append(c.getHeader());
			txt.append("}}");
		} else {
			txt.append(c.getHeader());
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
		for (int j=1; j < getNCols(); j++) {
			txt.append(" & ").append(cellContent[0][j]);
		}
		
		txt.append("\\\\ \\cline{"+(depth+1)+"-"+getNCols()+"} \n");
		
		for (int i=1; i < numberOfLeafNodes; i++) {
			
			for (int j=1; j < getNCols(); j++) { //Even if the [i][0] element has a value, we must ignore it
				
				txt.append("  & ").append(cellContent[i][j]);
			}
			txt.append("\\\\ ");				
					
			if ( hasHorizontalLines()) {
				txt.append("\\cline{"+lidx[i]+"-"+getNCols()+"} \n");
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
	
	
	// =========================================
	//           GETTERS / SETTERS  
	// =========================================

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
	public void hasHorizontalLines(boolean hasHorizontalLines) {
		this.hasHorizontalLines = hasHorizontalLines;
	}

	/**
	 * @return the columnMeta
	 */
	public ArrayList<ColumnMeta> getColumnMeta() {
		return columnMeta;
	}

	public void addColumn(ColumnMeta c) {
		columnMeta.add(c);
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
		return columnMeta.size();
	}

	/**
	 * @return the tableRows
	 */
	public ArrayList<String> getTableRows() {
		return tableRows;
	}

	/**
	 * @param tableRows the tableRows to set
	 */
	public void setTableRows(ArrayList<String> tableRows) {
		this.tableRows = tableRows;
	}

	public StringBuilder getCAlignment() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the hasShading
	 */
	public boolean hasShading() {
		return hasShading;
	}

	/**
	 * @param hasShading the hasShading to set
	 */
	public void hasShading(boolean hasShading) {
		this.hasShading = hasShading;
	}

	/**
	 * @return the shadeColor
	 */
	public String getShadeColor() {
		return shadeColor;
	}

	/**
	 * @param shadeColor the shadeColor to set
	 */
	public void setShadeColor(String shadeColor) {
		this.shadeColor = shadeColor;
	}

}

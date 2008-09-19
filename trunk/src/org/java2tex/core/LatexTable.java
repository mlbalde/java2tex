package org.java2tex.core;

import java.util.ArrayList;

public interface LatexTable {

	/**
	 * You can create a LaTeX table manually or <i>in bulk</i> (by building the <tt>tableArray</tt>).
	 *  
	 * This auxiliary method provides the common initialization of the LaTeX source for the table.
	 * 
	 */
	public abstract void initLatex() throws Java2TeXException;

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
	public abstract String getLatex() throws Java2TeXException;

	public abstract void addHorizontalLine();

	/**
	 * This method allows us to draw a horizontal line that spans only
	 * a part of the table. In particular, it starts drawing a horizontal
	 * line from column <tt>start</tt> until the end of the table columns.
	 * 
	 * @param start
	 */
	public abstract void addHorizontalLine(int start);

	/**
	 * This method allows us to draw a horizontal line that spans only
	 * a part of the table. In particular, it starts drawing a horizontal
	 * line from column <tt>start</tt> to column <tt>end</tt>.
	 *  
	 * @param start
	 * @param end
	 */
	public abstract void addHorizontalLine(int start, int end);

	/**
	 * This method simply adds the LaTeX "end of row" mark, i.e. "\\"
	 */
	public abstract void endRow();

	/**
	 * @return the caption
	 */
	public abstract String getCaption();

	/**
	 * @return the id
	 */
	public abstract String getId();

	/**
	 * @return the isLandscape
	 */
	public abstract boolean isLandscape();

	/**
	 * @param isLandscape the isLandscape to set
	 */
	public abstract void setLandscape(boolean isLandscape);

	/**
	 * @return the hasHorizontalLines
	 */
	public abstract boolean hasHorizontalLines();

	/**
	 * @param hasHorizontalLines the hasHorizontalLines to set
	 */
	public abstract void setHorizontalLines(boolean hasHorizontalLines);

	/**
	 * @return the headers
	 */
	public abstract String[] getHeaders();

	/**
	 * @return the columnMeta
	 */
	public abstract ArrayList<ColumnMeta> getColumnMeta();

	public abstract void addColumn(ColumnMeta c);

	/**
	 * @return the nRows
	 */
	public abstract int getNRows();

	/**
	 * @return the nCols
	 */
	public abstract int getNCols();

	/**
	 * @return the cAlignment
	 */
	public abstract String getColumnAlignment() throws Java2TeXException;

}
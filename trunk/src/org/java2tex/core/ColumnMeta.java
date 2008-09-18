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

/**
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 *
 * @since <tt>0.3</tt>
 * @version <tt>0.3</tt>
 */
public class ColumnMeta {

	/**
	 * The ID of a column is the order in which it should appear in the table.
	 * Hence, each column will have a unique identifier. By setting the value
	 * equal to <tt>-1</tt>, we should get an exception if the ID has not been 
	 * set.
	 */
	private int id=-1;
	
	/**
	 * This is the alignment of the content inside the column.
	 * By default it is "l", which stands for left. Other possible values
	 * are "c" for "center" and "r" for "right". 
	 */
	private String alignment=LatexConstants.CENTER;
	
	/**
	 * This value will define whether a vertical line should be present on the left of that column
	 */
	private boolean hasLeftSeparator=true;
	
	/**
	 * If this variable is not <tt>NULL</tt>, it will define the color of the column. 
	 */
	private String backgroundColour=null;
	
	
	/**
	 * If we change the background color, we may want to change the foreground color as well 
	 */
	private String foregroundColor=null;
	
	/**
	 * The label of the column if any
	 */
	private String label=null;
	
	/**
	 * This is the maximum width of the column in centimeters.
	 * 
	 */
	private int maxWidth =-1;

	/**
	 * In order to span multiple columns, set this to a positive number
	 */
	private int columnSpan=-1;
	
	public ColumnMeta(int id) {
		this.id = id;
	}

	public ColumnMeta(int id, String alignment) {
		this.id = id;
		this.alignment = alignment;
	}

	public ColumnMeta(int id, boolean hasLeftSeparator) {
		this.id = id;
		this.hasLeftSeparator = hasLeftSeparator;
	}

	public ColumnMeta(int id, String alignment, boolean hasLeftSeparator) {
		this.id = id;
		this.alignment = alignment;
		this.hasLeftSeparator = hasLeftSeparator;
	}

	public ColumnMeta(int id, String alignment, boolean hasLeftSeparator, int maxCharWidth) {
		this.id = id;
		this.alignment = alignment;
		this.hasLeftSeparator = hasLeftSeparator;
		this.maxWidth = maxCharWidth;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the alignment
	 */
	public String getAlignment() {
		return alignment;
	}

	/**
	 * @param alignment the alignment to set
	 */
	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}

	/**
	 * @return the maxWidth in cm
	 */
	public int getMaxWidth() {
		return maxWidth;
	}

	/**
	 * @param maxWidth the maxWidth to set in cm
	 */
	public void setMaxWidth(int maxCharWidth) {
		this.maxWidth = maxCharWidth;
	}

	/**
	 * @return the hasLeftSeparator
	 */
	public boolean hasLeftSeparator() {
		return hasLeftSeparator;
	}

	/**
	 * @param hasLeftSeparator the hasLeftSeparator to set
	 */
	public void hasLeftSeparator(boolean hasLeftSeparator) {
		this.hasLeftSeparator = hasLeftSeparator;
	}

	/**
	 * @return the backgroundColour
	 */
	public String getBackgroundColour() {
		return backgroundColour;
	}

	/**
	 * @param backgroundColour the backgroundColour to set
	 */
	public void setBackgroundColour(String backgroundColour) {
		this.backgroundColour = backgroundColour;
	}

	/**
	 * @return the columnSpan
	 */
	public int getColumnSpan() {
		return columnSpan;
	}

	/**
	 * @param columnSpan the columnSpan to set
	 */
	public void setColumnSpan(int columnSpan) {
		this.columnSpan = columnSpan;
	}

	/**
	 * @return the foregroundColor
	 */
	public String getForegroundColor() {
		return foregroundColor;
	}

	/**
	 * @param foregroundColor the foregroundColor to set
	 */
	public void setForegroundColor(String foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
}

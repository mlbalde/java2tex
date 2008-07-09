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
 * Instances of this class encapsulate a graphics object in a LaTeX document.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * 
 * @since   <tt>0.1</tt> 
 * @version <tt>1.0</tt>
 */
public class LatexGraphics {

	/** 
	 * This ID is set by the <CODE>LatexDocument</CODE>, it can be used for
	 * reference of this figure from anywhere else inside the document.
	 */ 
	private String id=null;
	
	private StringBuilder latex;
	
	private String imgFile;
	private String caption;
	
	private String width;
	private String height;
	private String angle;
	private String scale;
	
	private boolean isLandscape=false;
	
	public LatexGraphics(String file) {
		
		this.latex = new StringBuilder(); 		
		this.imgFile = file;
	}

	public String getLatex() {
	
		insert("\\includegraphics");
		insert("[");
		if (width!=null) {
			insert("width="+width);
		}
		
		if (height!=null) {
			if (width!=null) {
				insert(",");				
			}
			insert("height="+height);
		}
		
		if (angle!=null) {
			if (width!=null || height!=null) {
				insert(",");				
			}
			insert("angle="+angle);				
		}
		
		if (scale!=null) {
			if (width!=null || height!=null || angle!=null) {
				insert(",");				
			}
			insert("scale="+scale);
		}
		
		add("]{"+imgFile+"}");

		return latex.toString();
	}
	
	/**
	 * @return the imgFile
	 */
	public String getImgFile() {
		return imgFile;
	}

	/**
	 * @param imgFile the imgFile to set
	 */
	public void setImgFile(String imgFile) {
		this.imgFile = imgFile;
	}

	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * @return the angle
	 */
	public String getAngle() {
		return angle;
	}

	/**
	 * @param angle the angle to set
	 */
	public void setAngle(String angle) {
		this.angle = angle;
	}

	/**
	 * @return the scale
	 */
	public String getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(String scale) {
		this.scale = scale;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
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
	public void setLandscape(boolean isLandascape) {
		this.isLandscape = isLandascape;
	}
	
	private void add(String txt) {
		latex.append(txt).append("\n");
	}

	private void insert(String txt) {
		latex.append(txt);
	}


}

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
	
	private String imgFile;
	private String caption;
	
	private String width;
	private String height;
	private String angle;
	private String scale;
	
	public LatexGraphics(String file) {
		this.imgFile = file;
	}

	public String getLatex() {
	
		StringBuilder latex = new StringBuilder(); 
		
		latex.append("\\includegraphics");
		latex.append("[");
		if (width!=null) {
			latex.append("width="+width);
		}
		
		if (height!=null) {
			if (width!=null) {
				latex.append(",");				
			}
			latex.append("height="+height);
		}
		
		if (angle!=null) {
			if (width!=null || height!=null) {
				latex.append(",");				
			}
			latex.append("angle="+angle);				
		}
		
		if (scale!=null) {
			if (width!=null || height!=null || angle!=null) {
				latex.append(",");				
			}
			latex.append("scale="+scale);
		}
		
		latex.append("]{"+imgFile+"}");

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
}

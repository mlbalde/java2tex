/*
 *                       Java2TeX 
 * Professional Document Preparation with Java and LaTeX/TeXLive
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
 * A Interface for processors. 
 * 
 * @author <a href="mailto:sanp.lokhande@gmail.com">Sanjay Lokhande</a>
 * 
 * This interface is used implementing TeX processing.
 * 
 * @since   <tt>1.0</tt> 
 * @version <tt>1.0</tt>
 *
 */
public interface TeXProcessor {
	
	/**
	 * @param doc
	 * @throws Java2TeXException
	 */
	public void process(LatexDocument doc) throws Java2TeXException;
	
	/**
	 * @param rootDir
	 * @throws Java2TeXException
	 */
	public void setupLatexRootDir(String rootDir) throws Java2TeXException;
	
	/**
	 * @param doc
	 * @throws Java2TeXException
	 */
	public void save(LatexDocument doc) throws Java2TeXException;
	
	/**
	 * @return
	 * @throws Java2TeXException
	 */
	public String getLatexRootDir() throws Java2TeXException;
	
	/**
	 * 
	 */
	public void terminate();

}

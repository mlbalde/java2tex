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

package org.java2tex.demo;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.java2tex.core.Java2TeXException;
import org.java2tex.core.LatexDocument;
import org.java2tex.core.LatexGraphics;
import org.java2tex.core.LatexProcessor;
import org.java2tex.custom.BaseDocument;
import org.java2tex.custom.SimpleTable;

/**
 * Read this class to see the current capabilities of Java2TeX.
 * Start from the <code>main</code> method, which you can invoke without any arguments,
 * and follow the execution steps. Everything has been kept simple, so that it is easy
 * to follow every step in the process of creating the document.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * 
 * @since <tt>0.1</tt>
 * @version <tt>0.1</tt>
 */
public class ArticleExample {

	private static final Logger log = Logger.getLogger(ArticleExample.class);

	private static LatexProcessor latexProc=null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();

		try {
			
			latexProc = new LatexProcessor();
			
		} catch(Java2TeXException j2tX) {
			log.error("FATAL ERROR: "+j2tX.getMessage());
			System.exit(1);
		}

		LatexDocument doc=null;
		try {
			doc = demo();
			
			try {
				latexProc.save(doc);
			} catch (Java2TeXException jX) {
				log.error(jX.getMessage());
			}
			
			try {
				latexProc.process(doc);
			} catch (Java2TeXException jX) {
				log.error(jX.getMessage());
			}			
		} catch(Java2TeXException jX) {
			log.error("Oops!");
			log.error(jX.getMessage());
		}
		
		log.info("The LaTeX file: "+doc.getFilename()+",\n");
		log.info("and its corresponding PDF file, have been created successfully!");
	}
	
	/**
	 * This is an auxiliary method that creates a LaTeX document for testing.
	 * 
	 * Read this code to understand how to create a new document.
	 * 
	 * @return a LaTeX document
	 * @throws Java2TeXException 
	 */
	public static LatexDocument demo() throws Java2TeXException {
		
		BaseDocument doc = new BaseDocument("MyDocument");
		
		doc.setDocumentStyle("article");
		
		doc.setStyleOptions("11pt,a4paper,twoside,fleqn");
		
		doc.setTitle("Creating PDF reports with Java code!");
		doc.setAuthor("Babis Marmanis");
		doc.setKeywords("java, latex");
		doc.setSubject("A transformation engine that takes Java objects and creates PDF documents based on \\LaTeX");
		doc.setNotes("This is where the notes of the user go.") ;
		
		doc.setLeftHeader("");
		doc.setRightHeader(doc.getTitle());
		doc.setCenterHeader("");
		doc.setCenterFooter("");
		
		//You could use an abstract, instead of notes.
		doc.add("\\vspace{1.5cm}");
		doc.addSectionNoLabel("Notes");
		doc.add("\\normalsize");
		doc.add(doc.getNotes());
		
		doc.newPage();
		
		doc.addSection("Introduction to LaTeX");
		
		doc.add("\\LaTeX{} is a document preparation system for the \\TeX{} typesetting program.");
		doc.add("It offers programmable desktop publishing features and extensive facilities ");
		doc.add("for automating most aspects of typesetting and desktop publishing, including ");
		doc.add("numbering and cross-referencing, tables and figures, page layout, bibliographies,"); 
		doc.add("and much more. \\LaTeX{} was originally written in 1984 by Leslie Lamport and ");
		doc.add("has become the dominant method for using \\TeX; few people write in plain \\TeX{} anymore. ");
		doc.add("The current version is \\LaTeXe.");
		
		doc.newLine();
		
		doc.addSection("Some science");
		
		doc.add("Hello Albert Einstein!");
		doc.addIndexEntry("Einstein");
		
		LatexGraphics aeJpeg = new LatexGraphics(getGraphicsFileName("Einstein.jpg"));
		aeJpeg.setHeight("4in");
		aeJpeg.setCaption("Albert Einstein (1879-1955)");
		
		/** 
		 * If your figure has large width then use the landscape mode.
		 * Notice that this is different from rotating the image.
		 * If you want to rotate just the image, use the <CODE>setAngle</CODE> method.
		 */
		// aeJpeg.setLandscape(true);
		
		doc.addFigure(aeJpeg);		
		
		doc.add("  % This is a comment, it is not shown in the final output.");
		doc.add("  % The following shows a little of the typesetting power of LaTeX");
		
		doc.add("  \\begin{eqnarray}");
		doc.add("    E &=& mc^2 \\"+"\\"); 
		doc.add("    m &=& \\frac{m_0}{\\sqrt{1-\\frac{v^2}{c^2}}}");
		doc.add("  \\end{eqnarray}");
		
		doc.addSection("US Politics");
		doc.add("The federal government of the United States is the centralized United States ");
		doc.add("governmental body established by the United States Constitution. The federal government ");
		doc.add("has three branches: the legislature, executive, and judiciary. Through a system of ");
		doc.add("separation of powers or \"checks and balances\", each of these branches has some ");
		doc.add("authority to act on its own, some authority to regulate the other two branches, ");
		doc.add("and has some of its own authority, in turn, regulated by the other branches. ");
		
		doc.newLine();
		
		doc.add("The policies of the federal government have a broad impact on both the domestic ");
		doc.add("and foreign affairs of the United States. In addition, the powers of the federal ");
		doc.add("government as a whole are limited by the Constitution, which leaves a great deal ");
		doc.add("of authority to the individual states.");

		doc.addSection("US Presidents");

		SimpleTable table = new SimpleTable("Former U.S. Presidents (1961-1981)",4,4);
		
		table.setHeaders(new String[] {"First Name","Last Name","From", "To"});
		table.alignColumns(new char[] {'l','l','r','r'});
		
		table.addRow(0,new String[]{"John", "Kennedy","1961","1963"});
		table.addRow(1,new String[]{"Lyndon", "Johnson","1963","1969"});
		table.addRow(2,new String[]{"Richard","Nixon","1969","1974"});
		table.addRow(3,new String[]{"Jimmy","Carter","1977","1981"});
		
		// -----------------------------------------------------------------------------------
		// The following sets the table contents at once, instead of using the addRow method
		//
		//		String[][] names = {{"John", "Kennedy","1961","1963"},
		//	            {"Lyndon", "Johnson","1963","1969"},
		//	            {"Richard","Nixon","1969","1974"},
		//	            {"Jimmy","Carter","1977","1981"}};
		//		table.setValues(names);
		// -----------------------------------------------------------------------------------

		// If your table is large then use the landscape mode
		// table.setLandscape(true);

		doc.addTable(table);

		doc.newPage();
		
		//
		// NOTICE that you must refer to a table AFTER you append it to the document.
		//
		doc.add("Table ~\\ref{"+table.getId()+"} lists the past US presidents between 1961 and 1981.");

		LatexGraphics jkJpeg = new LatexGraphics(getGraphicsFileName("jk35.jpg"));
		jkJpeg.setCaption("John Kennedy (1961-1963)");
		doc.addFigure(jkJpeg);
		
		LatexGraphics ljJpeg = new LatexGraphics(getGraphicsFileName("lj36.jpg"));
		ljJpeg.setCaption("Lyndon Johnson (1963-1969)");
		doc.addFigure(ljJpeg);
		
		LatexGraphics rnJpeg = new LatexGraphics(getGraphicsFileName("rn37.jpg"));
		rnJpeg.setCaption("Richard Nixon (1969-1974)");
		doc.addFigure(rnJpeg);

		LatexGraphics jcJpeg = new LatexGraphics(getGraphicsFileName("jc39.jpg"));
		jcJpeg.setCaption("Jimmy Carter (1977-1981)");
		doc.addFigure(jcJpeg);
		
		return doc;
	}
	
	private static String getGraphicsFileName(String graphicsFile) throws Java2TeXException {
		
		StringBuilder gFileName = new StringBuilder("c:/Java2TeX/docs/example/");
		gFileName.append(graphicsFile);
		
		return gFileName.toString();
	}
}

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

import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.java2tex.core.ColumnMeta;
import org.java2tex.core.Java2TeXException;
import org.java2tex.core.LatexConstants;
import org.java2tex.core.LatexDocument;
import org.java2tex.core.LatexProcessor;
import org.java2tex.custom.BaseDocument;
import org.java2tex.custom.SimpleTable;

/**
 * An example of a cross tabulation
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 *
 * @since <tt>0.1</tt>
 * @version <tt>0.1</tt>
 */
public class TableExample {

	private static final Logger log = Logger.getLogger(TableExample.class);

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

	private static LatexDocument demo() throws Java2TeXException {
		BaseDocument doc = new BaseDocument("MyTableExample");
		
		doc.setDocumentStyle("article");
		
		doc.setStyleOptions("11pt,a4paper,twoside,fleqn");
		
		doc.setTitle("Creating PDF documents with Java!");
		doc.setAuthor("Babis Marmanis");
		doc.setKeywords("java, latex");
		doc.setSubject("A transformation engine that takes Java objects and creates PDF documents based on \\LaTeX");

		doc.setLeftHeader("");
		doc.setRightHeader(doc.getTitle());
		doc.setCenterHeader("");
		doc.setCenterFooter("");
		
		doc.addChapterNoLabel("Creating cross-tables with LaTeX");
		
		SimpleTable table = new SimpleTable("Cross-tables with LaTeX",12,13);
		table.setId(doc.getNewTableId());
		
		// If your table is large then use the landscape mode. This method does not alter 
		// the LaTeX source that <tt>SimpleTable</tt> creates. Its value will be used later 
		// by the <tt>LatexDocument</tt> class, when it embeds the table in the document
		//
		table.setLandscape(true);

		table.setHorizontalLines(true);
		
		// Define the columns
		table.setColumnMeta(new ArrayList<ColumnMeta>());
		
		table.addColumn(new ColumnMeta(1)); // We do not want the first three to have separators
		table.addColumn(new ColumnMeta(2));
		table.addColumn(new ColumnMeta(3));
		table.addColumn(4,13,LatexConstants.RIGHT); // Build all the other columns as if they were holding numbers
				
		//Let's start building our cross tabulation
		table.initLatex();
		
		// First row
		table.addHorizontalLine(4); 
		table.addMultiColumn(3);
		table.endColumn();
		table.addMultiColumn(5, LatexConstants.CENTER, "North America");
		table.endColumn();
		table.addMultiColumn(5, LatexConstants.CENTER, "Europe");
		table.endRow();
		table.addHorizontalLine(4); // We want the horizontal line to start from the cells that have values 
		
		// Second row
		table.addMultiColumn(3);
		table.endColumn();
		table.addMultiColumn(2, LatexConstants.CENTER, "U.S.A.");
		table.endColumn();
		table.addMultiColumn(3, LatexConstants.CENTER, "Canada");
		table.endColumn();
		table.addMultiColumn(3, LatexConstants.CENTER, "U.K.");
		table.endColumn();
		table.addMultiColumn(2, LatexConstants.CENTER, "Germany");
		table.endRow();
		table.addHorizontalLine(4); 
		
		// Third row
		table.addMultiColumn(3,true);
		table.endColumn();
		table.add(new String[] {"East","West","Quebec","Toronto","Ottawa","England","Wales","Scotland","Berlin","Munich"});
		table.endRow();
		table.addHorizontalLine(); 
		
		// Fourth row
		String[][] multirowCells1 = new String[][] {
				{"2007","","January",  "100.00","110.00","120.00","130.00","140.00","150.00","160.00","170.00","180.00","190.00"},
				{"2007","Q1","February", "100.00","110.00","120.00","130.00","140.00","150.00","160.00","170.00","180.00","190.00"},
				{"2007","","March",    "100.00","110.00","120.00","130.00","140.00","150.00","160.00","170.00","180.00","190.00"},
				{"2007","","January",  "100.00","110.00","120.00","130.00","140.00","150.00","160.00","170.00","180.00","190.00"},
				{"2007","Q2","February", "100.00","110.00","120.00","130.00","140.00","150.00","160.00","170.00","180.00","190.00"},
				{"2007","","March",    "100.00","110.00","120.00","130.00","140.00","150.00","160.00","170.00","180.00","190.00"}
		};
		table.addMultiRowCell(new int[]{3,2}, multirowCells1);

		String[][] multirowCells2 = new String[][] {
				{"2008","","January",  "100.00","110.00","120.00","130.00","140.00","150.00","160.00","170.00","180.00","190.00"},
				{"2008","Q1","February", "100.00","110.00","120.00","130.00","140.00","150.00","160.00","170.00","180.00","190.00"},
				{"2008","","March",    "100.00","110.00","120.00","130.00","140.00","150.00","160.00","170.00","180.00","190.00"}
		};
		table.addMultiRowCell(new int[]{3,1}, multirowCells2);

		doc.addTable(table);

		//
		// NOTICE that you must refer to a table AFTER you append it to the document.
		//
		doc.add("Table ~\\ref{"+table.getId()+"} is an example of a cross tabulation");
		
		//DEBUG -- TODO: TEMPORARY CODE, REMOVE IT LATER
		boolean debug=false;
		if (debug) {
			throw new Java2TeXException("DEBUG");
		}
		return doc;
	}

}

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
import org.java2tex.core.ColumnMeta;
import org.java2tex.core.Java2TeXException;
import org.java2tex.core.LatexDocument;
import org.java2tex.core.LatexProcessor;
import org.java2tex.custom.BaseDocument;
import org.java2tex.custom.MultiPageTable;

/**
 * An example for the use of MultiPageTable
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 *
 * @since <tt>0.1</tt>
 * @version <tt>0.1</tt>
 */
public class TableCellWrapExample {

	private static final Logger log = Logger.getLogger(TableCellWrapExample.class);

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
		BaseDocument doc = new BaseDocument("MyTableCellWrapExample");
		
		doc.usePackage("{longtable}");
		doc.usePackage("{supertabular}");
		doc.usePackage("{array}");
		
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
		
		String caption = "Tables with columns whose text must wrap around inside the table cell "+
		"and have so many rows that the table spans multiple pages.";
		MultiPageTable bigTable = new MultiPageTable(caption);
		bigTable.setId(doc.getNewTableId());
		
		// If your table is large then use the landscape mode. This method does not alter 
		// the LaTeX source that <tt>SimpleTable</tt> creates. Its value will be used later 
		// by the <tt>LatexDocument</tt> class, when it embeds the table in the document
		//
		bigTable.setLandscape(true);

		// bigTable.hasHorizontalLines(true);
		bigTable.hasShading(true);
		
		ColumnMeta c1 = new ColumnMeta(1,"|p{4cm}",true);
		c1.setHeader("Vendor Name");
		bigTable.addColumn(c1);
		
		ColumnMeta c2 = new ColumnMeta(1,"|p{2.75cm}",true);
		c2.setHeader("Money (\\$)");
		bigTable.addColumn(c2);
		
		ColumnMeta c3 = new ColumnMeta(1,"|p{11cm}|",true);
		c3.setHeader("Description");
		bigTable.addColumn(c3);
		
		
		// Now the data...
		bigTable.addRow("International Business Machines Corp 01 & 5,134,131,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 02 & 6,234,242,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 03 & 7,334,353,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 04 & 8,434,464,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 05 & 9,534,575,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 06 & 1,634,686,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 07 & 2,734,797,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 08 & 3,839,808,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 09 & 4,938,769,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 10 & 5,217,660,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 11 & 6,226,560,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 12 & 7,235,469,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 13 & 8,244,368,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 14 & 1,253,267,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 15 & 2,262,366,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 16 & 3,271,465,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 17 & 4,282,564,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 18 & 5,293,663,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 19 & 6,294,762,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 20 & 7,285,861,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 21 & 8,276,962,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 22 & 9,267,063,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 22 & 1,258,064,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 23 & 1,249,065,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 24 & 1,234,066,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 25 & 1,224,067,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 26 & 1,214,068,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 27 & 1,234,069,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 28 & 1,234,060,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 29 & 1,234,169,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 30 & 1,234,268,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		bigTable.addRow("International Business Machines Corp 31 & 1,234,367,890 & International Business Machines Corporation (IBM) is an information technology (IT) company. The Company's major operations include a Global Technology Services (GTS) segment, a Global Business Services (GBS) segment, a Systems and Technology segment, a Software segment and a Global Financing segment. In February 2008, the Company acquired Arsenal Digital Solutions and Net Integration Technologies Inc.");
		
		doc.addTable(bigTable);

		//
		// NOTICE that you must refer to a table AFTER you append it to the document.
		//
		doc.add("Table ~\\ref{"+bigTable.getId()+"} some data!");
		
		//DEBUG -- TODO: TEMPORARY CODE, REMOVE IT LATER
		boolean debug=false;
		if (debug) {
			throw new Java2TeXException("DEBUG");
		}
		return doc;
	}

}

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
 * The basic representation of a LaTeX document. This is a bare bones
 * implementation. Our goal is to (eventually) expose all the versatility
 * and power of LaTeX through our APIs. Hence, naturally, early adopters
 * of Java2TeX should expect this class to change significantly.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * 
 * @since   <tt>0.1</tt> 
 * @version <tt>0.1</tt>
 */
public class LatexDocument {

/*
 * TODO: this is the bare bones implementation. There is a plethora of attributes that 
 * can be added in a LaTeX document, however, we want to do that by creating new classes 
 * that will cleanly encapsulate the various aspects of a LaTeX document rather than blot 
 * this class with all the possible embellishments.
 */    
	private static final Logger log = Logger.getLogger( LatexDocument.class );
	
	/** This is a counter for figures in the document */
	private int numberOfFigures=0;
	
	/** This is a counter for tables in the document */
	private int numberOfTables=0;
	
	/** 
	 * The default value for a new document is <CODE>article</CODE>.
	 * You can always reset it to whatever is required for your documents.
	 */
	private String documentClass = "article";
	
	private String author = "Nobody";
	
	private String title="No title";
	
	private String notes=null;
	
	private StringBuffer body;
	
	private String filename;
	
	public LatexDocument() {		
		log.debug("Creating a LaTeX document ...");
		this.body = new StringBuffer();		
	}
	
	public LatexDocument(String title) {
	
		log.debug("Creating a LaTeX document with title...");
		
		this.title = title;
		this.body = new StringBuffer();
	}
	
	/**
	 * Add custom LaTeX text in the body
	 * 
	 * @param latex the text that must be added in the body of the LaTeX document
	 */
	public void add(String latex) {
		body.append(latex).append("\n");
	}

	/**
	 * The only difference between this method and <CODE>add(String latex)</CODE>
	 * is the new line character. The <CODE>add(String latex)</CODE> method adds
	 * automatically a new line character to make the text human readable. This method
	 * inserts only the text that is passed as an argument, nothing more.
	 * 
	 * @param latex
	 */
	public void insert(String latex) {
		body.append(latex);		
	}
	
	/**
	 * The <CODE>pdflatex</CODE> compiler supports PNG, PDF, JPEG, and MPS image formats.
	 * PNG is good for screenshots and other images with few colors. 
	 * JPEG is great for photos because it is very space-efficient.
	 * 
	 * If you have encapsulated postcript files (EPS) you have two options:
	 * <UL>
	 *   <LI>You can transform your image files into one of the supported formats</LI>
	 *   <LI>You can use a different compiler and LaTeX preample (see the LaTeX documentation)</LI>
	 * </UL>
	 * The latter option is not recommended for LaTeX novices
	 *  
	 * @param graphics
	 */
	public void addFigure(LatexGraphics graphics) {
		add("\\begin{figure}[!htpb]");
		
		add(graphics.getLatex());
		
		add("\\caption{"+graphics.getCaption()+"}");
		
		String figureId = "FigureId-"+numberOfFigures;
		add("\\label{"+figureId+"}");
		graphics.setId(figureId);
		
		add("\\end{figure}");
		numberOfFigures++;
	}
	
	/**
	 * 
	 * @param table
	 */
	public void addTable(LatexTable table) {
		add("\\begin{table}[!htpb]");
		
		add(table.getLatex());
		
		add("\\caption{"+table.getCaption()+"}");
		
		String tableId = "TableId-"+numberOfTables;
		add("\\label{"+tableId+"}");
		table.setId(tableId);
		
		add("\\end{table}");
		
		numberOfTables++;
	}
	
	public void addSection(String sTitle) {
		add("\\section{"+sTitle+"}");
	}
	
	public void addSubsection(String sTitle) {
		add("\\subsection{"+sTitle+"}");
	}
	
	public void newPage() {
		add("\\newpage");
	}
	
	public void newLine() {
		add("\\newline");
	}
	
	/**
	 * Notice that we place the index and only the index -- no new line character.
	 * 
	 * @param idx
	 */
	public void addIndexEntry(String idx) {
		insert("\\index{"+idx+"}");
	}
	
	public String initLatex() {
		
		StringBuilder latex = new StringBuilder(); 
		
		latex.append("\\documentclass{"+getDocumentClass()+"}\n");
		latex.append("\\usepackage{thumbpdf}\n");
		latex.append("\\usepackage{makeidx}\n");
		latex.append("\\usepackage[pdftex,\n");
		latex.append("             colorlinks=true,\n");
		latex.append("             urlcolor=rltblue,       % \\href{...}{...} external (URL)\n");
		latex.append("             filecolor=rltgreen,     % \\href{...} local file\n");
		latex.append("             linkcolor=rltred,       % \\ref{...} and \\pageref{...}\n");
		latex.append("             pdftitle={"+title+"},\n");
		latex.append("             pdfauthor={"+author+"},\n");
		latex.append("             pdfsubject={Just a test},\n");
		latex.append("             pdfkeywords={test, testing, testable stuff},\n");
		latex.append("             pdfproducer={pdfLaTeX},\n");
		latex.append("             pagebackref,\n");
		latex.append("             pdfpagemode=None,\n");
		latex.append("             bookmarksopen=true]{hyperref}\n");
		latex.append("\\usepackage[pdftex]{color,graphicx}\n");
		latex.append("\\definecolor{rltred}{rgb}{0.75,0,0}\n");
		latex.append("\\definecolor{rltgreen}{rgb}{0,0.5,0}\n");
		latex.append("\\definecolor{rltblue}{rgb}{0,0,0.75}\n");
		latex.append("\\title{"+getTitle()+"}\n");
		latex.append("\\author{"+getAuthor()+"}\n");
		latex.append("\\date{\\today}\n");

		return latex.toString();
	}
	
	public String getLatex() {
		
		StringBuilder latex = new StringBuilder(); 
		
		latex.append(initLatex());

		latex.append("\\makeindex\n");

		latex.append("\\begin{document}\\label{start}\n");

		latex.append("\\maketitle\n");

		latex.append("\\tableofcontents\n");

		if (numberOfFigures > 0) {
			latex.append("\\listoffigures\n");			
		}

		if (numberOfTables > 0) {
			latex.append("\\listoftables\n");			
		}

		latex.append(getBody());

		latex.append("\\end{document}\n");

		return latex.toString();
	}

	//--------------------------------------------------------------------------
	// GETTERS + SETTERS
	//--------------------------------------------------------------------------
	/**
	 * @return the body of this LaTeX document
	 */
	public String getBody() {
		return body.toString();
	}

	/**
	 * @return the documentClass
	 */
	public String getDocumentClass() {
		return documentClass;
	}

	/**
	 * @param documentClass the documentClass to set
	 */
	public void setDocumentClass(String documentClass) {
		this.documentClass = documentClass;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
}

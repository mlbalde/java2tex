package org.java2tex.custom;

import org.apache.log4j.Logger;
import org.java2tex.core.Java2TeXException;
import org.java2tex.core.LatexTable;
import org.java2tex.core.LatexDocument;
import org.java2tex.core.LatexGraphics;

public class BaseDocument extends LatexDocument {
	
	private static final Logger log = Logger.getLogger( BaseDocument.class );

	private boolean hasCustomPdfPackage=true;
	
	private String centerHeader;
	private String rightHeader;
	private String leftHeader;
	private String centerFooter;
	private String rightFooter;
	private String leftFooter;
	
	public BaseDocument(String title) {
		
		super(title);
		
		packages.add("{fancyhdr}");
		packages.add("{thumbpdf}");
		packages.add("{makeidx}");
		packages.add("{lscape}");
		packages.add("{amsmath,amssymb,amsfonts}");
		packages.add("{multicol}");
		packages.add("{multirow}");
		packages.add("[pdftex]{color,graphicx}");
		packages.add("[table]{xcolor}");
	}

	@Override
	public void addFigure(LatexGraphics figure) {

		log.debug("Adding Figure: "+figure.getId());
		
		if (figure.isLandscape()) {
			add("\\begin{landscape}");
		}		
		add("\\begin{figure}[!htpb]");
		
		add(figure.getLatex());
		
		add("\\caption{"+figure.getCaption()+"}");
		
		int n = this.getNumberOfFigures();
		
		String figureId = "FigureId-"+n;
		add("\\label{"+figureId+"}");
		figure.setId(figureId);
		
		add("\\end{figure}");

		if (figure.isLandscape()) {
			add("\\end{landscape}");
		}
		
		this.setNumberOfFigures(n++);		
	}
	
	/**
	 * TODO: This method is essentially identical to the second addTable method.
	 * Define an interface and use that to simplify the code. For example, SimpleTable
	 * could be the interface and SimpleTable/BigTable the two implementations. 
	 * 
	 * @param table
	 */
	public void addTable(MultiPageTable table) {

		log.debug("Adding table: "+table.getId());

		try {
			
			add(table.getLatex());
			
		} catch (Java2TeXException j2tX) {
			log.error("FAILED TO ADD A TABLE!");
			log.error(j2tX.getMessage());
		}		
		
	}

	public synchronized String getNewTableId() {
		
		int n = getNumberOfTables();
		String tableId = "TableId-"+n;
		setNumberOfTables(n++);
		
		return tableId;
	}
	
	@Override
	public void addTable(LatexTable table) {
		
		log.debug("Adding table: "+table.getId());
		
		try {
			
			add(table.getLatex());
						
		} catch (Java2TeXException j2tX) {
			log.error("FAILED TO ADD A TABLE!");
			log.error(j2tX.getMessage());
		}		
	}

	@Override
	public String initLatex() {
		
		StringBuilder latex = new StringBuilder(); 
		
		latex.append("\\documentclass["+this.getStyleOptions());
		latex.append("]{"+getDocumentStyle()+"}\n");
		
		for (String latexPackage : packages) {
			latex.append("\\usepackage").append(latexPackage).append("\n");
		}
		//Add packages
		if (hasCustomPdfPackage()) {
			customPdfPackage();
		}
		latex.append("%\n");
		latex.append("% --- End of package imports ---\n");
		latex.append("%\n");
		latex.append("\\pagestyle{fancy}\n");

		latex.append("\\fancyhfoffset[LE,RO]{0pt}\n");
		
		latex.append("\\lhead{"+getLeftHeader()+"}\n");
		latex.append("\\chead{"+getCenterHeader()+"}\n");
		latex.append("\\rhead{\\bfseries "+getRightHeader()+"}\n");
		latex.append("\\lfoot{Created by: "+getAuthor()+"}\n");
		latex.append("\\cfoot{"+getCenterFooter()+"}\n");
		latex.append("\\rfoot{\\thepage}\n");		
		latex.append("\\renewcommand{\\headrulewidth}{0.4pt}\n");
		latex.append("\\renewcommand{\\footrulewidth}{0.4pt}\n");
		if ( getDefaultFontFamily() != null && getDefaultFontFamily().trim().length() > 0 ) {
			latex.append("\\renewcommand{\\rmdefault}{").append(getDefaultFontFamily()).append("}\\rmfamily\n");
		}
		latex.append("%\n");
		for ( String ttf : getDeclaredTrueTypeFonts() ) {
			String[] fontNames = ttf.split(":");
			latex.append("\\DeclareTruetypeFont{" + fontNames[0] + "}{" + fontNames[1] +  "}\n");
		}
		latex.append("% --- End of other definitions ---\n");
		latex.append("%\n");		
		latex.append("\\parindent 1cm \n");
		latex.append("\\parskip 0.2cm \n");
		latex.append("\\topmargin 0.2cm \n");
		latex.append("\\oddsidemargin 1cm \n");
		latex.append("\\evensidemargin 0.5cm \n");
		latex.append("\\textwidth 15cm \n");
		latex.append("\\textheight 21cm \n");
		latex.append("\\definecolor{rltred}{rgb}{0.75,0,0}\n");
		latex.append("\\definecolor{rltgreen}{rgb}{0,0.5,0}\n");
		latex.append("\\definecolor{rltblue}{rgb}{0,0,0.75}\n");
		latex.append("\\title{"+getTitle()+"}\n");
		latex.append("\\author{"+getAuthor()+"\\\\  Created on " + getLocaleDate()+ "}\n");

		return latex.toString();
	}

	@Override
	public String getLatex() {
		
		StringBuilder latex = new StringBuilder(); 
		
		latex.append(initLatex());

		latex.append("\\makeindex \n");
		
		latex.append("\\begin{document} \n");

		latex.append("\\thispagestyle{plain}");
		
//		latex.append("\\vspace{1cm}");
		
		latex.append("\\Large \n");
		latex.append("\\noindent \n");
		latex.append("\\begin{tabular*}{0.95\\textwidth}{@{\\extracolsep{\\fill}} ll} \n");
		latex.append("  \\hline \\\\ \n");
		latex.append("    \\bf{Title}      & " + getTitle() + " \\\\ \n");
		latex.append("    \\bf{Author}     & " + getAuthor() + " \\\\ \n");
		latex.append("	  Created on & " + getLocaleDate()+ " \\\\ \n");
		latex.append("  \\hline \n");
		latex.append("\\end{tabular*} \n");
		
		latex.append("\\normalsize \n");
				
		latex.append("\\tableofcontents \n");

		if (getNumberOfFigures() > 0) {
			latex.append("\\listoffigures \n");			
		}

		if (getNumberOfTables() > 0) {
			latex.append("\\listoftables \n");			
		}

		latex.append(getBody());

		latex.append("\\end{document} \n");

		return latex.toString();
	}
	
	

	private void customPdfPackage() {
		
		StringBuilder latex = new StringBuilder("\\usepackage[pdftex,\n");
		latex.append("             colorlinks=true,\n");
		latex.append("             urlcolor=rltblue,       % \\href{...}{...} external (URL)\n");
		latex.append("             filecolor=rltgreen,     % \\href{...} local file\n");
		latex.append("             linkcolor=rltred,       % \\ref{...} and \\pageref{...}\n");
		latex.append("             pdftitle={"+getTitle()+"},\n");
		latex.append("             pdfauthor={"+getAuthor()+"},\n");
		latex.append("             pdfsubject={"+getSubject()+"},\n");
		latex.append("             pdfkeywords={"+getKeywords()+"},\n");
		latex.append("             pdfproducer={pdfLaTeX},\n");
		latex.append("             pagebackref,\n");
		latex.append("             pdfpagemode=None,\n");
		latex.append("             bookmarksopen=true]{hyperref}\n");

		packages.add(latex.toString());
	}
	
	/**
	 * @return the hasCustomPdfPackage
	 */
	public boolean hasCustomPdfPackage() {
		return hasCustomPdfPackage;
	}

	/**
	 * @param hasCustomPdfPackage the hasCustomPdfPackage to set
	 */
	public void useCustomPdfPackage(boolean hasCustomPdfPackage) {
		this.hasCustomPdfPackage = hasCustomPdfPackage;
	}

	/**
	 * @return the rightHeader
	 */
	public String getRightHeader() {
		return rightHeader;
	}

	/**
	 * @param rightHeader the rightHeader to set
	 */
	public void setRightHeader(String rightHeader) {
		this.rightHeader = rightHeader;
	}

	/**
	 * @return the leftHeader
	 */
	public String getLeftHeader() {
		return leftHeader;
	}

	/**
	 * @param leftHeader the leftHeader to set
	 */
	public void setLeftHeader(String leftHeader) {
		this.leftHeader = leftHeader;
	}

	/**
	 * @return the centerHeader
	 */
	public String getCenterHeader() {
		return centerHeader;
	}

	/**
	 * @param centerHeader the centerHeader to set
	 */
	public void setCenterHeader(String centerHeader) {
		this.centerHeader = centerHeader;
	}

	/**
	 * @return the centerFooter
	 */
	public String getCenterFooter() {
		return centerFooter;
	}

	/**
	 * @param centerFooter the centerFooter to set
	 */
	public void setCenterFooter(String centerFooter) {
		this.centerFooter = centerFooter;
	}

	/**
	 * @return the rightFooter
	 */
	public String getRightFooter() {
		return rightFooter;
	}

	/**
	 * @param rightFooter the rightFooter to set
	 */
	public void setRightFooter(String rightFooter) {
		this.rightFooter = rightFooter;
	}

	/**
	 * @return the leftFooter
	 */
	public String getLeftFooter() {
		return leftFooter;
	}

	/**
	 * @param leftFooter the leftFooter to set
	 */
	public void setLeftFooter(String leftFooter) {
		this.leftFooter = leftFooter;
	}
}

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.log4j.Logger;

/**
 * This is the class that controls the execution of the LaTeX compiler
 * on the system of choice. The class assumes that <CODE>pdflatex</CODE>
 * is in the <CODE>PATH</CODE> environment variable and therefore can
 * be invoked from the command line and by the <CODE>ProcessBuilder</CODE>.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * 
 * @since   <tt>0.1</tt> 
 * @version <tt>1.0</tt>
 */
public class LatexProcessor {

	private static final Logger log = Logger.getLogger(LatexProcessor.class);
	
	private static final String JAVA2TEX = "Java2TeX";

	/**
	 * The root directory for storing the generated LaTeX files.
	 */
	private String latexRootDir;

	/**
	 * If the root directory is not supplied, 
	 * we check for the environment property <tt>java2tex.home</tt>.
	 * If that property is not defined,
	 * we use the user's root directory and build a 
	 * hierarchy of sub-directories whose root is a directory
	 * called <tt>java2tex</tt>
	 * @throws Java2TeXException 
	 */
	public LatexProcessor() throws Java2TeXException {
		
		this.setLatexRootDir(null);
		log.info("Created LatexProcessor instance ...");		
	}
	
	public LatexProcessor(String rootDir) {
		latexRootDir=rootDir;
		log.info("Created LatexProcessor instance ...");
	}
	
	/**
	 * The root directory for storing the generated LaTeX files.
	 * 
	 * @return <CODE>latexRootDir</CODE> the root directory for storing the
	 *         generated LaTeX files
	 * @throws Java2TeXException
	 */
	public String getLatexRootDir() throws Java2TeXException {

		if (latexRootDir == null) {
			throw new Java2TeXException(
					"The LaTeX root directory has not been set!");
		}
		return latexRootDir;
	}

	/**
	 * This method sets the <CODE>latexRootDir</CODE> value, which is root
	 * directory for storing the generated LaTeX files. If the
	 * <CODE>latexRootDir</CODE> value has not been set properly:
	 * <UL>
	 *   <LI><CODE>rootDir == null</CODE>, or </LI>
	 *   <LI><CODE>rootDir.length() < 2</CODE></LI>
	 * </UL> , this method will set it to a directory called <CODE>JaTeX</CODE>
	 * that is located inside the user's home directory (as determined by the
	 * system property <CODE>user.home</CODE>).
	 * 
	 * @param rootDir is the root directory for storing the generated LaTeX files
	 * @throws Java2TeXException 
	 */
	public void setLatexRootDir(String rootDir) throws Java2TeXException {

		if (rootDir == null || rootDir.length() < 2) {

			log.warn("NULL (or too short) file name for LaTeX root directory!");
			
			StringBuilder tmp;

			log.info("Checking environment variables ...");
			String home = System.getProperty("java2tex.home");
			
			if (home == null) {
			
				log.warn("Environment variable: java2tex.home doesn't exist!");
				log.info("Using the user's root directory ...");
				tmp = new StringBuilder(System.getProperty("user.home"));
				tmp.append(File.separator);
				tmp.append(JAVA2TEX);

			} else {
				
				log.info("Found envorinment variable: java2tex.home -> "+home);
				tmp = new StringBuilder(home);	
			}
			
			tmp.append(File.separator);
			
			Calendar calendar = Calendar.getInstance();
			tmp.append(calendar.get(Calendar.YEAR)).append("-");
			tmp.append(calendar.get(Calendar.MONTH)+1).append("-");
			tmp.append(calendar.get(Calendar.DAY_OF_MONTH));
			
			latexRootDir = tmp.toString();

			log.info("LaTeX root directory is now set to: " + latexRootDir);

		} else {
			
			latexRootDir = rootDir;
		}
		
		File f = new File(latexRootDir);

		if (f.exists()) {
			
			if (!f.isDirectory()) {
				
				log.warn("A file named \"" + latexRootDir + "\" already exists.");
				
				boolean t = mkdir(f); 
				if (t) {
					log.info("Created directory: "+latexRootDir);
				} else {
					throw new Java2TeXException("Could not create directory: "+latexRootDir);
				}
			} else {
				log.debug("OK! Found directory: "+latexRootDir);
			}

		} else {
			
			boolean t = mkdir(f); 
			if (t) {
				log.info("Created directory: "+latexRootDir);
			} else {
				throw new Java2TeXException("Could not create directory: "+latexRootDir);
			}
		}
	}

	public void process(LatexDocument doc) throws Java2TeXException {

		String os = System.getProperty("os.name");

		if (os.startsWith("Windows")) {

			log.warn("***  This will not work without installing MikTeX!  ***");
		
		} else {
		
			log.debug("Running on a Unix clone? \n You should have pdflatex in your path.");
			log.info("Type \n >> which pdflatex \n on a terminal to check if you have pdflatex on your PATH");
		}

		String[] args = { "pdflatex", doc.getFilename()};
		log.debug("Output: \n" + Arrays.toString(args));
		
		ProcessBuilder pb = new ProcessBuilder(args);
		pb.directory(new File(getLatexRootDir()));

		run(pb);
		
		//Run twice to get the references right
		run(pb);		
	}

	private static void run(ProcessBuilder pb) {
		Process process = null;

		try {
			process = pb.start();

			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;

			while ((line = br.readLine()) != null) {
				log.debug(line + "\n");
			}

		} catch (IOException ioX) {
			log.error(ioX.getMessage());
		}		
	}

	public void save(LatexDocument doc) throws Java2TeXException {
		// Create the file and store it on the disk
		StringBuilder filename = new StringBuilder(getLatexRootDir());
		filename.append(System.getProperty("file.separator"));
		
		String title = doc.getTitle().replace('\u0020','-');
		filename.append(title).append("-").append(System.currentTimeMillis()).append(".tex");

		//Save it for later
		doc.setFilename(filename.toString());
		
		File file = new File(doc.getFilename());

		try {
			FileWriter fw = new FileWriter(file);
			fw.write(doc.getLatex());
			fw.close();
		} catch (IOException ioX) {
			log.error(ioX.getMessage());
		}
	}

	private static boolean mkdir(File f) {
		
		boolean dirCreated=false;
		
		String dirPath=null;
		
		try {
			dirPath = f.getCanonicalPath();
		} catch (IOException ioX) {
			log.error(ioX.getMessage());
		}
		
		dirCreated = f.mkdirs();
		
		if (dirCreated) {
			log.info(dirPath + " directory created succesfully.");
		} else {
			log.error("Could not create directory: " + dirPath);
		}
		
		return dirCreated;
	}
}

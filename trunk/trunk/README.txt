-----------------------------------------------------------------
                    Welcome to Java2TeX
-----------------------------------------------------------------

Java2TeX is a software library that enables the use of LaTeX for 
Java applications. Java2TeX facilitates the creation of LaTeX 
documents through a friendly Java API. The goal of Java2TeX is 
the creation of professional grade documents within Java apps.
________________________________________________________________

[ We assume that you have downloaded Java2Tex on your system.  ]
[ If you did then keep reading. Otherwise, download the latest ]
[ version of Java2TeX.                                         ]
[
[ Every directory location mentioned below is with respect to  ]
[ the top level directory of Java2TeX, which is assumed to be  ]
[ located directly under 'c:\' on Windows systems; otherwise,  ]
[ please, make the necessary adjustments.                      ]


________________________________________________________________

PREREQUISITES
________________________________________________________________
 
In order to use Java2TeX effectively and create PDF documents,
you must be able to execute 'pdflatex'. 

----------------------
        LINUX
----------------------

The 'pdflatex' executable comes as default with many Linux distros. 
Typically, you can find it in the /usr/bin/ directory.

If you do not have it installed on your Linux distribution, you
can install it by typing in the command prompt:

   > yum install tetex-latex
   
YUM should resolve whatever dependencies exist automatically.

In order to test whether you have pdflatex available in your PATH,
type the following in the command prompt:

   > which pdflatex 


-----------------------
      WINDOWS
-----------------------

You can get the 'pdflatex' executable running on a Windows environment
by installing MikTeX (currently version 2.7). MiKTeX is an up-to-date 
TeX implementation for the Windows operating system.

You can obtain MikTeX from their website: 

   http://miktex.org/
   
Installation should be a breeze and there is plenty of documentation 
available.
 

________________________________________________________________

HOW-TO 
________________________________________________________________

Begin exploring Java2TeX:
   
	1. Go to the 'build' directory
	
	2. Type 'ant -projecthelp' and you should see the following:
	
	   ccompile  compile the source from scratch
	   clean     clean up
	   dist      generate the distribution
	   javadoc   Generates Javadoc for all source code.
	   run       Create a PDF file for demonstration purposes
	   tidy      clean up everything, even the Javadocs
	  
	3. Type 'ant run'. This will compile the code and create a
	   demo PDF document that contains figures and a table.

   	   
________________________________________________________________

QUESTIONS?
________________________________________________________________

If you have any questions, send an email to the project owner: 

	babis.marmanis@gmail.com
	

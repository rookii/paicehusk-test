/* 	author:   	Christopher O'Neill
	date:		Feb 2001
   	comments: 	Stemmer Eavaluation Program
   				also forms a suite of individual evaluation programs
   				which may be run sepratly from the Main() prog
   				as methods of the object*/



import java.io.*;
import java.lang.*;
import java.util.*;




/************************************************************
*Class:		Evaluation										*
*															*
*Purpose:	Stemming Evaluation								*
*			uses modified Hamming distace defined in fox 	*
*			and frakes strength and similarity of affix 	*
*			removal stemming algorthms includes option for	*
*			using modified algorithm adpted by Chris O'Neill*
*			2001 to support PrefixRemoval along side suffix *
*			removal. Also included a similarty metric using	*
*			the hamming distance measure designed by Chris	*
*			O'Neill which gives a percentage value for		*
*			similarity of output files.						*
*************************************************************/
public class Evaluation
{
/****************************************************************
*Method:		Clean											*
*Returns:		String											*
*Recievs:		String str										*
*Purpose:		remove all non letter or digit characters from 	*
*				word and toLowerCase then return				*
****************************************************************/
private String Clean( String word )
	{
	String clean = "";
	for ( int i=0; i < word.length(); i++ )
		{
	    if (Character.isLetterOrDigit(word.charAt(i)))
            {
			clean += word.charAt(i);
			}
     	}
	clean = clean.toLowerCase(); //change all letters in the input to lowercase
   	return clean;
  	} // end of clean method

/************************************************************
*Method:		MeanConflation								*
*Returns:		float average								*
*Receives:		String sourceFile, String stemmedFile		*
*Purpose:		mean no of words per conflation	class		*
************************************************************/
public float MeanConflation (String sourceFile, String stemmedFile)
	{
	float average=0;
	Vector stems = new Vector();
	boolean newstem;
	String text;
	String word;
	StringTokenizer line = new StringTokenizer("");
	try
		{
		FileReader fr = new FileReader(sourceFile);
		BufferedReader br = new BufferedReader(fr);
		while ((text=br.readLine())!= null)
			{
			line= new StringTokenizer(text);
			while (line.hasMoreTokens())
				{
				word = line.nextToken();
				average+=1;
				}
			}
		br.close();
		fr.close();
		}
	catch (Exception e)
		{
		System.err.println("Error Reading From Source File " + sourceFile + " error " + e);
		System.exit(0);
		}
	try
		{
		FileReader fr = new FileReader(stemmedFile);
		BufferedReader br = new BufferedReader(fr);
		while ((text=br.readLine())!= null)
			{
			line= new StringTokenizer(text);
			while (line.hasMoreTokens())
				{
				word = new String();
				word = line.nextToken();
				newstem=true;
				for (int i=0; i<stems.size(); i++)
					{
					if (word.equals((String) stems.elementAt(i)))
						{
						newstem=false;
						}
					}
				if (newstem)
					{
					stems.addElement(word);
					}
				}
			}
		br.close();
		fr.close();
		}
	catch (Exception e)
		{
		System.err.println("Error Reading From Stemmed File " + stemmedFile + " error " + e);
		System.exit(0);
		}
	try
		{
		average=average/stems.size();
		}
	catch (Exception e)
		{
		System.err.println("Error Reading From Stemmed File " + stemmedFile + " number of stems = 0 error " + e);
		System.exit(0);
		}
	return average;
	}
/********************************************************
*Method:		IndexCompression						*
*Returns:		float indexComp							*
*Recives:		String sourceFile, String stemmedFile 	*
*				Boolean deDupeWords						*
*Purpose:		index compression factor (n-s)/n		*
********************************************************/
public float IndexCopression (String sourceFile, String stemmedFile, boolean deDupeWords)
	{
	float indexComp=0;
	Vector stems = new Vector();
	Vector words = new Vector();
	boolean newstem;
	String text;
	String word;
	StringTokenizer line = new StringTokenizer("");
	try
		{
		FileReader fr = new FileReader(sourceFile);
		BufferedReader br = new BufferedReader(fr);
		while ((text=br.readLine())!= null)
			{
			line= new StringTokenizer(text);
			while (line.hasMoreTokens())
				{
				if (deDupeWords)
					{
					word = new String();
					word = Clean(line.nextToken());
					newstem=true;
					for (int i=0; i<words.size(); i++)
						{
						if (word.equals((String) words.elementAt(i)))
							{
							newstem=false;
							}
						}
					if (newstem)
						{
						words.addElement(word);
						}
					}
				else
					{
					word = line.nextToken();
					indexComp+=1.0;
					}
				}
			}
		if (deDupeWords)
			{
			indexComp=words.size();
			}
		br.close();
		fr.close();
		}
	catch (Exception e)
		{
		System.err.println("Error Reading From Source File " + sourceFile + " error " + e);
		System.exit(0);
		}
	try
		{
		FileReader fr = new FileReader(stemmedFile);
		BufferedReader br = new BufferedReader(fr);
		while ((text=br.readLine())!= null)
			{
			line= new StringTokenizer(text);
			while (line.hasMoreTokens())
				{
				word = new String();
				word = Clean(line.nextToken());
				newstem=true;
				for (int i=0; i<stems.size(); i++)
					{
					if (word.equals((String) stems.elementAt(i)))
						{
						newstem=false;
						}
					}
				if (newstem)
					{
					stems.addElement(word);
					}
				}
			}
		br.close();
		fr.close();
		}
	catch (Exception e)
		{
		System.err.println("Error Reading From Stemmed File " + stemmedFile + " error " + e);
		System.exit(0);
		}
	try
		{
		indexComp=(indexComp-stems.size())/indexComp;
		}
	catch (Exception e)
		{
		System.err.println("Error Reading From Source File " + sourceFile + " number of terms = 0 error " + e);
		System.exit(0);
		}
	return indexComp;
	}

/****************************************************************
*Method:		DifferenceCount									*
*Returns:		int diffCount									*
*Recievs:		String sourceFile, String stemmedFile			*
*Purpose:		counts the nuber of terms that are different 	*
*				in the stemmedFile to the sourceFile, to see	*
*				how many words have been altered durring 		*
*				stemming										*
****************************************************************/
public int DifferenceCount (String sourceFile, String stemmedFile)
	{
	int diffCount=0;
	String text1;
	String word1;
	StringTokenizer line1 = new StringTokenizer("");
	String text2;
	String word2;
	StringTokenizer line2 = new StringTokenizer("");
	try
		{
		FileReader fr1 = new FileReader(sourceFile);
		BufferedReader br1 = new BufferedReader(fr1);

		FileReader fr2 = new FileReader(stemmedFile);
		BufferedReader br2 = new BufferedReader(fr2);
		while (((text1=br1.readLine())!= null) & ((text2=br2.readLine())!= null))
			{
			line1= new StringTokenizer(text1);
			line2= new StringTokenizer(text2);
			while (line1.hasMoreTokens()&&line2.hasMoreTokens())
				{
				word1 = Clean(line1.nextToken());
				word2 = Clean(line2.nextToken());
				if (!(word1.equals(word2)))
					{
					diffCount++;
					}
				}
			while (line1.hasMoreTokens())
				{
				word1 = line1.nextToken();
				diffCount++;
				}
			while (line2.hasMoreTokens())
				{
				word2 = line2.nextToken();
				diffCount++;
				}
			}
		while ((text1=br1.readLine())!= null)
			{
			line1= new StringTokenizer(text1);
			while (line1.hasMoreTokens())
				{
				word1 = line1.nextToken();
				diffCount++;
				}
			}
		while ((text2=br2.readLine())!= null)
			{
			line2= new StringTokenizer(text2);
			while (line2.hasMoreTokens())
				{
				word2 = line2.nextToken();
				diffCount++;
				}
			}
		}
	catch (Exception e)
		{
		System.err.println("File Error Durring reading " + e);
		}
	return diffCount;
	}

/****************************************************************
*Method:		MeanCharRem										*
*Returns:		float meanCharRem								*
*Recievs:		String sourceFile, String stemmedFile			*
*Purpose:		average number of chars removed per term	 	*
****************************************************************/
public float MeanCharRem (String sourceFile, String stemmedFile)
	{
	float meanCharRem=0;
	int noWords=0;
	String text1;
	String word1;
	StringTokenizer line1 = new StringTokenizer("");
	String text2;
	String word2;
	StringTokenizer line2 = new StringTokenizer("");
	try
		{
		FileReader fr1 = new FileReader(sourceFile);
		BufferedReader br1 = new BufferedReader(fr1);

		FileReader fr2 = new FileReader(stemmedFile);
		BufferedReader br2 = new BufferedReader(fr2);
		while (((text1=br1.readLine())!= null)&((text2=br2.readLine())!= null))
			{
			line1= new StringTokenizer(text1);
			line2= new StringTokenizer(text2);
			while (line1.hasMoreTokens())
				{
				word1 = Clean(line1.nextToken());
				word2 = Clean(line2.nextToken());
				meanCharRem += CharRem(word1,word2);
				noWords++;
				}
			}
		}
	catch (Exception e)
		{
		System.err.println("File Error Durring reading " + e);
		}
	try
		{
		meanCharRem=meanCharRem/noWords;
		}
	catch (Exception e)
		{
		System.err.println("File Error Durring reading " + e);
		}
	return meanCharRem;
	}

/****************************************************************
*Method:		ModeCharRem										*
*Returns:		Vector modeCharRem								*
*Recievs:		String sourceFile, String stemmedFile			*
*Purpose:		mode number of chars removed per term	 		*
****************************************************************/
public Vector ModeCharRem (String sourceFile, String stemmedFile)
	{
	int charRem=0;
	Vector remCount = new Vector();
	String text1;
	String word1;
	StringTokenizer line1 = new StringTokenizer("");
	String text2;
	String word2;
	StringTokenizer line2 = new StringTokenizer("");
	try
		{
		FileReader fr1 = new FileReader(sourceFile);
		BufferedReader br1 = new BufferedReader(fr1);

		FileReader fr2 = new FileReader(stemmedFile);
		BufferedReader br2 = new BufferedReader(fr2);
		while (((text1=br1.readLine())!= null)&((text2=br2.readLine())!= null))
			{
			line1= new StringTokenizer(text1);
			line2= new StringTokenizer(text2);
			while (line1.hasMoreTokens())
				{
				word1 = Clean(line1.nextToken());
				word2 = Clean(line2.nextToken());
				charRem = CharRem(word1,word2);
				while (remCount.size() <= charRem)
					{
					Integer x = new Integer(0);
					remCount.addElement(x);
					}
				int x = ((Integer)remCount.elementAt(charRem)).intValue();
				x++;
				Integer y= new Integer(x);
				remCount.setElementAt(y,charRem);
				}
			}
		}
	catch (Exception e)
		{
		System.err.println("File Error Durring reading " + e);
		}

	int max=0;
	charRem=0;
	Vector modeCharRem =new Vector();
	for (int i=0; i<remCount.size(); i++)
		{
		if (((Integer) remCount.elementAt(i)).intValue() == max)
			{
			Integer x = new Integer(i);
			modeCharRem.addElement(x);
			}
		if (((Integer) remCount.elementAt(i)).intValue() > max)
			{
			max = ((Integer) remCount.elementAt(i)).intValue();
			Integer x = new Integer(i);
			modeCharRem.removeAllElements();
			modeCharRem.addElement(x);
			}
		}
	return modeCharRem;
	}

/****************************************************************
*Method:		medianCharRem									*
*Returns:		float medianCharRem								*
*Recievs:		String sourceFile, String stemmedFile			*
*Purpose:		median number of chars removed per term	 		*
****************************************************************/
public float MedianCharRem (String sourceFile, String stemmedFile)
	{
	int charRem=0;
	float noWords=0;
	Vector remCount = new Vector();
	String text1;
	String word1;
	StringTokenizer line1 = new StringTokenizer("");
	String text2;
	String word2;
	StringTokenizer line2 = new StringTokenizer("");
	try
		{
		FileReader fr1 = new FileReader(sourceFile);
		BufferedReader br1 = new BufferedReader(fr1);

		FileReader fr2 = new FileReader(stemmedFile);
		BufferedReader br2 = new BufferedReader(fr2);
		while (((text1=br1.readLine())!= null)&((text2=br2.readLine())!= null))
			{
			line1= new StringTokenizer(text1);
			line2= new StringTokenizer(text2);
			while (line1.hasMoreTokens())
				{
				word1 = Clean(line1.nextToken());
				word2 = Clean(line2.nextToken());
				charRem = CharRem(word1,word2);
				noWords+=1.0;
				while (remCount.size() <= charRem)
					{
					Integer x = new Integer(0);
					remCount.addElement(x);
					}
				Integer x = new Integer(((Integer) remCount.elementAt(charRem)).intValue() +1);
				remCount.setElementAt(x,charRem);
				}
			}
		}
	catch (Exception e)
		{
		System.err.println("File Error Durring reading " + e);
		}
	noWords=noWords/2;

	int max=0;
	charRem=0;
	float medianCharRem =0;
	for (int i=0; i<remCount.size(); i++)
		{
		if (noWords>0)
			{
			noWords = noWords - (((Integer) remCount.elementAt(i)).intValue());
			medianCharRem=i;
			if (noWords==0.5)
				{
				noWords=0;
				medianCharRem+=0.5;
				}
			}
		}
	return medianCharRem;
	}
/****************************************************************
*Method:		TableCharRem									*
*Returns:		Vector tableCharRem								*
*Recievs:		String sourceFile, String stemmedFile			*
*Purpose:		mode number of chars removed per term	 		*
****************************************************************/
public Vector TableCharRem (String sourceFile, String stemmedFile)
	{
	int charRem=0;
	Vector tableCharRem = new Vector();
	String text1;
	String word1;
	StringTokenizer line1 = new StringTokenizer("");
	String text2;
	String word2;
	StringTokenizer line2 = new StringTokenizer("");
	try
		{
		FileReader fr1 = new FileReader(sourceFile);
		BufferedReader br1 = new BufferedReader(fr1);

		FileReader fr2 = new FileReader(stemmedFile);
		BufferedReader br2 = new BufferedReader(fr2);
		while (((text1=br1.readLine())!= null)&((text2=br2.readLine())!= null))
			{
			line1= new StringTokenizer(text1);
			line2= new StringTokenizer(text2);
			while (line1.hasMoreTokens())
				{
				word1 = Clean(line1.nextToken());
				word2 = Clean(line2.nextToken());
				charRem = CharRem(word1,word2);
				while (tableCharRem.size() <= charRem)
					{
					Integer x = new Integer(0);
					tableCharRem.addElement(x);
					}
				int x = ((Integer)tableCharRem.elementAt(charRem)).intValue();
				x++;
				Integer y= new Integer(x);
				tableCharRem.setElementAt(y,charRem);
				}
			}
		}
	catch (Exception e)
		{
		System.err.println("File Error Durring reading " + e);
		}

	return tableCharRem;
	}

/****************************************************************
*Method:		CharRem											*
*Returns:		int charRem										*
*Recievs:		String sourceTerm, String stemmedTerm			*
*Purpose:		number of chars removed by stemming		 		*
****************************************************************/
public int CharRem (String sourceT, String stemmedT)
	{
	String sourceTerm =Clean(sourceT);
	String stemmedTerm =Clean(stemmedT);
	if (sourceTerm.length()<stemmedTerm.length())
		{
		String temp = sourceTerm;
		sourceTerm=stemmedTerm;
		stemmedTerm=temp;
		}
	int charRem = sourceTerm.length()-stemmedTerm.length();
	return charRem;
	}

/****************************************************************
*Method:		MeanHammingDist									*
*Returns:		float meanHammDist								*
*Recievs:		String sourceFile, String stemmedFile, 			*
*				boolean pre										*
*Purpose:		average modified Hamming distance per term use 	*
*				pre == true if prefix stripping has been used	*
****************************************************************/
public float MeanHammingDist (String sourceFile, String stemmedFile, boolean pre)
	{
	float meanHammDist=0;
	int noWords=0;
	String text1;
	String word1;
	StringTokenizer line1 = new StringTokenizer("");
	String text2;
	String word2;
	StringTokenizer line2 = new StringTokenizer("");
	try
		{
		FileReader fr1 = new FileReader(sourceFile);
		BufferedReader br1 = new BufferedReader(fr1);

		FileReader fr2 = new FileReader(stemmedFile);
		BufferedReader br2 = new BufferedReader(fr2);
		while (((text1=br1.readLine())!= null)&((text2=br2.readLine())!= null))
			{
			line1= new StringTokenizer(text1);
			line2= new StringTokenizer(text2);
			while (line1.hasMoreTokens())
				{
				word1 = Clean(line1.nextToken());
				word2 = Clean(line2.nextToken());
				if (pre)
					{
					meanHammDist += PreHammDist(word1,word2);
					}
				else
					{
					meanHammDist += HammDist(word1,word2);
					}
				noWords++;
				}
			}
		}
	catch (Exception e)
		{
		System.err.println("File Error Durring reading " + e);
		}
	try
		{
		meanHammDist=meanHammDist/noWords;
		}
	catch (Exception e)
		{
		System.err.println("File Error Durring reading " + e);
		}
	return meanHammDist;
	}

/****************************************************************
*Method:		ModeHammingDist									*
*Returns:		Vector modeHammDist								*
*Recievs:		String sourceFile, String stemmedFile, 			*
*				boolean pre										*
*Purpose:		mode modified Hamming distance per term	use pre	*
*				==true if prefix stripping is used				*
****************************************************************/
public Vector ModeHammingDist (String sourceFile, String stemmedFile, boolean pre)
	{
	int hammDist=0;
	Vector distCount = new Vector();
	String text1;
	String word1;
	StringTokenizer line1 = new StringTokenizer("");
	String text2;
	String word2;
	StringTokenizer line2 = new StringTokenizer("");
	try
		{
		FileReader fr1 = new FileReader(sourceFile);
		BufferedReader br1 = new BufferedReader(fr1);

		FileReader fr2 = new FileReader(stemmedFile);
		BufferedReader br2 = new BufferedReader(fr2);
		while (((text1=br1.readLine())!= null)&((text2=br2.readLine())!= null))
			{
			line1= new StringTokenizer(text1);
			line2= new StringTokenizer(text2);
			while (line1.hasMoreTokens())
				{
				word1 = Clean(line1.nextToken());
				word2 = Clean(line2.nextToken());
				if (pre)
					{
					hammDist = PreHammDist(word1,word2);
					}
				else
					{
					hammDist = HammDist(word1,word2);
					}
				while (distCount.size() <= hammDist)
					{
					Integer x =new Integer(0);
					distCount.addElement(x);
					}
				Integer x = new Integer(((Integer) distCount.elementAt(hammDist)).intValue() +1);
				distCount.setElementAt(x,hammDist);
				}
			}
		}
	catch (Exception e)
		{
		System.err.println("File Error Durring reading " + e);
		}

	int max=0;
	hammDist=0;
	Vector modeHammDist =new Vector();
	for (int i=0; i<distCount.size(); i++)
		{
		if (((Integer) distCount.elementAt(i)).intValue() == max)
			{
			Integer x = new Integer(i);
			modeHammDist.addElement(x);
			}
		if (((Integer) distCount.elementAt(i)).intValue() > max)
			{
			max = ((Integer) distCount.elementAt(i)).intValue();
			Integer x = new Integer(i);
			modeHammDist.removeAllElements();
			modeHammDist.addElement(x);
			}
		}
	return modeHammDist;
	}

/****************************************************************
*Method:		medianHammingDist								*
*Returns:		float medianHammDist							*
*Recievs:		String sourceFile, String stemmedFile, 			*
*				boolean pre										*
*Purpose:		median modified Hamming distance per term use 	*
*				pre==true if prefix stripping is used			*
****************************************************************/
public float MedianHammingDist (String sourceFile, String stemmedFile, boolean pre)
	{
	int hammDist=0;
	float noWords=0;
	Vector distCount = new Vector();
	String text1;
	String word1;
	StringTokenizer line1 = new StringTokenizer("");
	String text2;
	String word2;
	StringTokenizer line2 = new StringTokenizer("");
	try
		{
		FileReader fr1 = new FileReader(sourceFile);
		BufferedReader br1 = new BufferedReader(fr1);

		FileReader fr2 = new FileReader(stemmedFile);
		BufferedReader br2 = new BufferedReader(fr2);
		while (((text1=br1.readLine())!= null)&((text2=br2.readLine())!= null))
			{
			line1= new StringTokenizer(text1);
			line2= new StringTokenizer(text2);
			while (line1.hasMoreTokens())
				{
				word1 = Clean(line1.nextToken());
				word2 = Clean(line2.nextToken());
				if (pre)
					{
					hammDist = PreHammDist(word1,word2);
					}
				else
					{
					hammDist = HammDist(word1,word2);
					}
				noWords+=1.0;
				while (distCount.size() <= hammDist)
					{
					Integer x = new Integer(0);
					distCount.addElement(x);
					}
				Integer x = new Integer(((Integer) distCount.elementAt(hammDist)).intValue() +1);
				distCount.setElementAt(x,hammDist);
				}
			}
		}
	catch (Exception e)
		{
		System.err.println("File Error Durring reading " + e);
		}
	noWords=noWords/2;

	int max=0;
	hammDist=0;
	float medianHammDist =0;
	for (int i=0; i<distCount.size(); i++)
		{
		if (noWords>0)
			{
			noWords = noWords - ((Integer) distCount.elementAt(i)).intValue();
			medianHammDist=i;
			if (noWords==0.5)
				{
				noWords=0;
				medianHammDist+=0.5;
				}
			}
		}
	return medianHammDist;
	}

/****************************************************************
*Method:		TableHammingDist								*
*Returns:		Vector tableHammDist							*
*Recievs:		String sourceFile, String stemmedFile, 			*
*				boolean pre										*
*Purpose:		tale of modified Hamming distance per term use	*
*				pre==true if prefix stripping is used			*
****************************************************************/
public Vector TableHammingDist (String sourceFile, String stemmedFile, boolean pre)
	{
	int hammDist=0;
	Vector tableHammDist = new Vector();
	String text1;
	String word1;
	StringTokenizer line1 = new StringTokenizer("");
	String text2;
	String word2;
	StringTokenizer line2 = new StringTokenizer("");
	try
		{
		FileReader fr1 = new FileReader(sourceFile);
		BufferedReader br1 = new BufferedReader(fr1);

		FileReader fr2 = new FileReader(stemmedFile);
		BufferedReader br2 = new BufferedReader(fr2);
		while (((text1=br1.readLine())!= null)&((text2=br2.readLine())!= null))
			{
			line1= new StringTokenizer(text1);
			line2= new StringTokenizer(text2);
			while (line1.hasMoreTokens())
				{
				word1 = Clean(line1.nextToken());
				word2 = Clean(line2.nextToken());
				if (pre)
					{
					hammDist = PreHammDist(word1,word2);
					}
				else
					{
					hammDist = HammDist(word1,word2);
					}
				while (tableHammDist.size() <= hammDist)
					{
					Integer x =new Integer(0);
					tableHammDist.addElement(x);
					}
				Integer x = new Integer(((Integer) tableHammDist.elementAt(hammDist)).intValue() +1);
				tableHammDist.setElementAt(x,hammDist);
				}
			}
		}
	catch (Exception e)
		{
		System.err.println("File Error Durring reading " + e);
		}

	return tableHammDist;
	}
/****************************************************************
*Method:		PreHammDist										*
*Returns:		int hammDist									*
*Recievs:		String sourceT, String stemmedT					*
*Purpose:		modified Hamming distance per term		 		*
****************************************************************/
public int PreHammDist (String sourceT, String stemmedT)
	{
	String sourceTerm = Clean(sourceT);
	String stemmedTerm = Clean(stemmedT);
	String padding ="";
	int hammDist=sourceTerm.length();
	int temp;
	if (sourceTerm.length()<stemmedTerm.length())
		{
		String tem = sourceTerm;
		sourceTerm=stemmedTerm;
		stemmedTerm=tem;
		}

	for (int i=0; (i+stemmedTerm.length())<=sourceTerm.length();i++)
		{
		if ((temp=HammDist(sourceTerm,(padding+stemmedTerm)))<=hammDist)
			{
			hammDist=temp;
			}
		}
	return hammDist;
	}

/****************************************************************
*Method:		HammDist										*
*Returns:		int hammDist									*
*Recievs:		String sourceT, String stemmedT					*
*Purpose:		modified Hamming distance per term		 		*
****************************************************************/
public int HammDist (String sourceT, String stemmedT)
	{
	String sourceTerm = Clean(sourceT);
	String stemmedTerm = Clean(stemmedT);
	int hammDist=0;
	if (sourceTerm.length()<stemmedTerm.length())
		{
		String temp = sourceTerm;
		sourceTerm=stemmedTerm;
		stemmedTerm=temp;
		}

	for (int i=0; i<stemmedTerm.length();i++)
		{
		if (sourceTerm.charAt(i)!=stemmedTerm.charAt(i))
			{
			hammDist++;
			}
		}
	hammDist += sourceTerm.length()-stemmedTerm.length();
	return hammDist;
	}

/****************************************************************
*Method:		FoxFrakesSimilarityMetric						*
*Returns:		float simMet									*
*Recievs:		String sourceFile, String stemmedFile, 			*
*				boolean pre										*
*Purpose:		similarity metric per term use pre == true		*
*				if prefix stripping has been used				*
*				defined in fox and frakes strength and 			*
*				similarity of affix removal stemming algorthms	*
*				if identical file causes errors					*
****************************************************************/
public float FoxFrakesSimilarityMetric (String fileA, String fileB, boolean pre)
	{
	float ffSimMet=0;
	int noWords=0;
	String text1;
	String word1;
	StringTokenizer line1 = new StringTokenizer("");
	String text2;
	String word2;
	StringTokenizer line2 = new StringTokenizer("");
	try
		{
		FileReader fr1 = new FileReader(fileA);
		BufferedReader br1 = new BufferedReader(fr1);

		FileReader fr2 = new FileReader(fileB);
		BufferedReader br2 = new BufferedReader(fr2);
		while (((text1=br1.readLine())!= null)&((text2=br2.readLine())!= null))
			{
			line1= new StringTokenizer(text1);
			line2= new StringTokenizer(text2);
			while (line1.hasMoreTokens())
				{
				word1 = Clean(line1.nextToken());
				word2 = Clean(line2.nextToken());
				if (word1.length()<word2.length())
					{
					String temp = word1;
					word1=word2;
					word2=temp;
					}
				if (pre)
					{
					ffSimMet += PreHammDist(word1,word2);
					}
				else
					{
					ffSimMet += HammDist(word1,word2);
					}
				noWords++;
				}
			}
		}
	catch (Exception e)
		{
		System.err.println("File Error Durring reading " + e);
		}
	//if the content of the two files being compared is identical
	//the sum of hamming distance will = 0
	//the algorithm states divide by sum of hamming disance in the case of
	//the two files being identical this will result in a divide by 0
	//hence an imposible calculation therfore this catch has been inserted
	//so that an error is not thrown the method will return -1 in this case
	//to represent 100% idetical
	if(ffSimMet==0)
		{
		return -1;
		}
	try
		{
		ffSimMet=noWords/ffSimMet;
		}
	catch (Exception e)
		{
		System.err.println("File Error Durring reading " + e);
		}
	return ffSimMet;
	}

/****************************************************************
*Method:		SimilarityMetric								*
*Returns:		float simMet									*
*Recievs:		String sourceFile, String stemmedFile, 			*
*				boolean pre										*
*Purpose:		similarity metric per term use pre == true		*
*				if prefix stripping has been used				*
*				developed by Chris O'Neill 2001		 			*
****************************************************************/
public double SimilarityMetric (String fileA, String fileB, boolean pre)
	{
	double simMet=0;
	int noWords=0;
	String text1;
	String word1;
	StringTokenizer line1 = new StringTokenizer("");
	String text2;
	String word2;
	StringTokenizer line2 = new StringTokenizer("");
	try
		{
		FileReader fr1 = new FileReader(fileA);
		BufferedReader br1 = new BufferedReader(fr1);

		FileReader fr2 = new FileReader(fileB);
		BufferedReader br2 = new BufferedReader(fr2);
		while (((text1=br1.readLine())!= null)&((text2=br2.readLine())!= null))
			{
			line1= new StringTokenizer(text1);
			line2= new StringTokenizer(text2);
			while (line1.hasMoreTokens())
				{
				word1 = Clean(line1.nextToken());
				word2 = Clean(line2.nextToken());
				if (word1.length()<word2.length())
					{
					String temp = word1;
					word1=word2;
					word2=temp;
					}
				if (pre)
					{
					simMet +=((double)PreHammDist(word1,word2))/((double)word1.length());
					}
				else
					{
					simMet+= (( (double) HammDist(word1,word2))/((double) word1.length()));
					}
				noWords++;
				}
			}
		}
	catch (Exception e)
		{
		System.err.println("File Error Durring reading " + e);
		}
	simMet=100.0*(1.0-(simMet/noWords));
	return simMet;
	}

//***************MAIN METHOD***************
/********************************************************************
*Method:		main												*
*Returns:		void												*
*Recives:		String args[0] fileA, String args[1] fileB, 		*
*				String args[2] output, 								*
*				String args[3] prefix stipping						*
*				String args[4] deDupeWords for index compression	*
*Purpose:		creates instance of Evaluation						*
*				and calls Evaluation Methods on input files			*
*				where one is a source file fileA soule be source	*
*				fileB the stemmed file								*
********************************************************************/
public static void main(String args[] )
	{
	System.out.println("This Program Has a long run time Please Wait");
	Evaluation evaluation = new Evaluation();
	String fileA = args[0];
	String fileB = args[1];
	Vector out =new Vector();
	boolean pre;
	if (args[3].equals("/p"))
		{
		pre=true;
		}
	else
		{
		pre=false;
		}
	boolean deDupeWords;
	if (args[4].equals("/d"))
		{
		deDupeWords=true;
		}
	else
		{
		deDupeWords=false;
		}

	try
		{
		FileWriter fw = new FileWriter(args[2]);
		BufferedWriter bw = new BufferedWriter(fw);

		bw.write(	"********************");
		bw.newLine();
		bw.write(	"* Evaluation Data  *");
		bw.newLine();
		bw.write(	"********************");
		bw.newLine();
		bw.newLine();
		bw.write(	"FileA: "+ fileA);
		bw.newLine();
		bw.write(	"FileB: "+ fileB);
		bw.newLine();
		bw.newLine();


		bw.write(	"The mean number of words pre conflation class: " + evaluation.MeanConflation(fileA, fileB) );
		bw.newLine();
		bw.newLine();
		bw.write(	"The Index Compression Factor:                  " + evaluation.IndexCopression(fileA, fileB, deDupeWords) );
		bw.newLine();
		bw.newLine();
		bw.write(	"The number of words and stems that differ:     " + evaluation.DifferenceCount(fileA, fileB) );
		bw.newLine();
		bw.newLine();
		bw.newLine();
		bw.write(	"The mean number characters removed:            " + evaluation.MeanCharRem(fileA, fileB) );
		bw.newLine();
		bw.newLine();
		bw.write(	"The median number characters removed:          " + evaluation.MedianCharRem(fileA, fileB) );
		bw.newLine();
		bw.newLine();
		bw.write(	"The mode number characters removed:            ");
		out = evaluation.ModeCharRem(fileA, fileB);
		for (int i=0; i<out.size();i++)
			{
			bw.write(((Integer) out.elementAt(i)).intValue()+"" );
			bw.newLine();
			bw.write("                                              ");
			}
		bw.newLine();
		bw.newLine();
		bw.write(	"The characters removed table:                   ");
		out = evaluation.TableCharRem(fileA, fileB);
		for (int i=0; i<out.size();i++)
			{
			bw.write("Number of words with " + i + " Chars Removed " +((Integer) out.elementAt(i)).intValue());
			bw.newLine();
			bw.write("                                               ");
			}
		bw.newLine();
		bw.newLine();
		bw.write(	"The mean Hamming distance:                     " + evaluation.MeanHammingDist(fileA, fileB, pre) );
		bw.newLine();
		bw.newLine();
		bw.write(	"The median Hamming distance:                   " + evaluation.MedianHammingDist(fileA, fileB, pre) );
		bw.newLine();
		bw.newLine();
		bw.write(	"The mode Hamming distance:                     ");
		out = evaluation.ModeHammingDist(fileA, fileB, pre);
		for (int i=0; i<out.size();i++)
			{
			bw.write(((Integer) out.elementAt(i)).intValue()+"" );
			bw.newLine();
			bw.write("                                               ");
			}
		bw.newLine();
		bw.newLine();
		bw.write(	"The Hamming distance table:                    ");
		out = evaluation.TableHammingDist(fileA, fileB, pre);
		for (int i=0; i<out.size();i++)
			{
			bw.write("Number of words with " + i + " Hamming distance " +((Integer) out.elementAt(i)).intValue());
			bw.newLine();
			bw.write("                                               ");
			}
		bw.newLine();
		bw.newLine();
		bw.write(	"The Fox and Frakes Similarity Metric:          " + evaluation.FoxFrakesSimilarityMetric(fileA, fileB, pre)+ "");
		bw.newLine();
		bw.newLine();
		bw.write(	"The Chris O'Neill Similarity Metric:           " + evaluation.SimilarityMetric(fileA, fileB, pre) + "%");
		bw.newLine();
		bw.newLine();

		bw.close();
   		}
   	catch (Exception e)
   		{
		System.err.println("File Error Durring writing to file " + args[2] + " Error " +e);
		System.exit(0);
		}
	}//main

} //class

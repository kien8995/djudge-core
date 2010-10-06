/* $Id$ */

package djudge.judge.dcompiler;

import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.w3c.dom.*;

import utils.FileTools;

import djudge.common.JudgeDirs;

import javax.xml.parsers.*;

public class Compiler
{
	private static final Logger log = Logger.getLogger(Compiler.class);
	
	private static TreeMap<String, Language> languages = new TreeMap<String, Language>();
	
	private static boolean fSetUp = false; 
	
	static
	{
		setupCompiler();
	}
	
	public static void setupCompiler()
	{
		setupCompiler(JudgeDirs.getRootDir() + "languages.xml");
	}
	
	public static void setupCompiler(String filename)
	{
		if (fSetUp) return;
		fSetUp = true;
		try
		{
			log.info("Setting up compiler");
	        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
	        Document doc = docBuilder.parse(filename);
	        NodeList lang = doc.getElementsByTagName("language");
	        for (int i = 0; i < lang.getLength(); i++)
	        {
	        	Language l = new Language((Element) lang.item(i));
	        	languages.put(l.getID(), l);
	        	log.info("Language added: " + l.getID());
	        }
		}
		catch(Exception exc)
		{
			log.error("Error", exc);
		}
	}
	
	/**
	 * 
	 * @param filename - Path to source file
	 * @param languageID - Language id (or '%AUTO%' for auto)
	 * @return {@link CompilationInfo}
	 */
	public static CompilerResult compile(String filename, String languageID)
	{
		log.debug("Trying to compile " + filename + " as " + languageID);
		CompilerTask task = new CompilerTask();
		task.languageId = languageID;
		task.files = new DistributedFileset(filename);
		return compile(task);
	}
	
	public static CompilerResult compile(CompilerTask task)
	{
		String languageId = task.languageId;
		CompilerResult res = new CompilerResult();
		String file = task.files.map.keySet().toArray(new String[0])[0];
		log.info("Compiling file " + file + ", language " + task.languageId);
		
		// Empty ID
		if (languageId == null || languageId == "")
		{
			log.warn("Empty language ID");
			res.result = CompilationResult.UnknownCompiler;
		}
		// Automatic language
		else if (languageId.equalsIgnoreCase("%AUTO%"))
		{
			String native_output[] = new String[0];
			Object[] LangId = languages.keySet().toArray();
			String Extension = FileTools.getExtension(file);
			for (int i = 0; (i < LangId.length) && (res.result != CompilationResult.OK); i++)
			{
				Language lng = languages.get(LangId[i]);
				log.info("Trying to compile " + task.files.getFile() + " as " + lng.getID());
				res = lng.compile(task);
				if (Extension.equals(lng.getExtension()))
					native_output = res.getCompilerOutput();
			}
			if (res.result != CompilationResult.OK && native_output != null && native_output.length > 0)
				res.setCompilerOutput(native_output);
		}
		else
		{
			Language lang = languages.get(languageId);
			// No language
			if (lang == null)
			{
				res.result = CompilationResult.UnknownCompiler;
				log.warn("Unknown language ID: " + languageId);
			}
			// OK
			else
			{
				res = lang.compile(task);
			}
		}
		
		return res;
	}
}

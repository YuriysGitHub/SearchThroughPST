package pstSearchForPdf;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class SearchPdf {
	
	public static String directory;
	public static String lookUpWord;
	
	//Removes pdfbox warnings
	static {
	    System.setProperty("org.apache.commons.logging.Log",
	                 "org.apache.commons.logging.impl.NoOpLog");
	}
	
	public SearchPdf(String directory, String lookUpWord)  {
		this.directory = directory;
		this.lookUpWord = lookUpWord;
	}
    public static void main() throws IOException {
    	    	
        final File folder = new File(directory);
        List<String> result = new ArrayList<>();
        System.out.println("Searching...");
        System.out.println("\"" +lookUpWord + "\""+ "found in:");
        searchFile(folder, result);

        for (String s : result) {
            if(searchPdf(s,lookUpWord))System.out.println(s);                    
        }        
        System.out.println("Search Ended");
    }
    
    //goes through files in folder
    public static void searchFile( final File folder, List<String> result) {
        for (final File f : folder.listFiles()) {
             if (f.isFile()) {
               result.add(f.getAbsolutePath());            
            }
        }
    }
    //searches pdf for specific word
	public static boolean searchPdf(String filename, String lookUpWord)throws IOException {
    	
		File file = new File (filename);
        PDDocument document = PDDocument.load(file);
    	PDFTextStripper pdfStripper = new PDFTextStripper();
    	
    	String text = pdfStripper.getText(document);
        //System.out.println(text);
    	boolean found = text.toLowerCase().contains(lookUpWord.toLowerCase());
       
        document.close();
        return found;
	}
    
}
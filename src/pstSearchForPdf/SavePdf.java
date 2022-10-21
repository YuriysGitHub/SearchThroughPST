package pstSearchForPdf;



import com.pff.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;


public class SavePdf {
	//PST file Location 
    public static final String OST_FILENAME = "examplePstFile/outlookbackup.pst";
    //Directory path for files to save
    public static final String ATTACHMENTS_SAVE_PATH = "savePdfFolder/";
    //Choose type of file to save
    public static final String fileType = ".*\\.pdf";
    // Word to Search
    public static final String lookUpWord = "user";
    
    
    public static void main(String[] args) throws IOException {
        testOst(OST_FILENAME);
        SearchPdf example = new SearchPdf(ATTACHMENTS_SAVE_PATH, lookUpWord);
        example.main();
    }

    public static void testOst(String ostFilename) {
        try {
            PSTFile pstFile = new PSTFile(ostFilename);
           // System.out.println(pstFile.getMessageStore().getDisplayName());
            processFolder(pstFile.getRootFolder());
            System.out.println("Attachments saved in " + ATTACHMENTS_SAVE_PATH);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    static int depth = -1;
    public static void processFolder(PSTFolder folder)
            throws PSTException, java.io.IOException
    {
        depth++;
        // the root folder doesn't have a display name
        if (depth > 0) {
            //printDepth();
            //System.out.println(folder.getDisplayName());
        }

        // go through the folders...
        if (folder.hasSubfolders()) {
            Vector<PSTFolder> childFolders = folder.getSubFolders();
            for (PSTFolder childFolder : childFolders) {
                processFolder(childFolder);
            }
        }

        // and now the emails for this folder
        if (folder.getContentCount() > 0) {
            depth++;
            PSTMessage email = (PSTMessage)folder.getNextChild();
            while (email != null) {
                //printDepth();
               // System.out.println("Email: "+email.getSubject());
                
            	saveAttachments(email, ATTACHMENTS_SAVE_PATH,fileType);

                email = (PSTMessage)folder.getNextChild();
            }
            depth--;
        }
        depth--;
    }

    public static void printDepth() {
        for (int x = 0; x < depth-1; x++) {
            System.out.print(" | ");
        }
        System.out.print(" |- ");
    }

    public static void saveAttachments(PSTMessage email, String path, String fileType) throws PSTException, IOException {
        int numberOfAttachments = email.getNumberOfAttachments();
        for (int x = 0; x < numberOfAttachments; x++) {
            PSTAttachment attach = email.getAttachment(x);
            InputStream attachmentStream = attach.getFileInputStream();
            // both long and short filenames can be used for attachments
            String filename = attach.getLongFilename();
            //System.out.println("filename " + filename);
            if (filename.isEmpty()) {
                filename = attach.getFilename();            
            }
            //checks if type of file
            if (filename.matches(fileType)) {
            // System.out.println("Pdf match " + filename);
            FileOutputStream out = new FileOutputStream(ATTACHMENTS_SAVE_PATH + filename);
            // 8176 is the block size used internally and should give the best performance
            int bufferSize = 8176;
            byte[] buffer = new byte[bufferSize];
            int count = attachmentStream.read(buffer);
            while (count == bufferSize) {
                out.write(buffer);
                count = attachmentStream.read(buffer);
            }
            byte[] endBuffer = new byte[count];
            System.arraycopy(buffer, 0, endBuffer, 0, count);
            out.write(endBuffer);
            out.close();
            attachmentStream.close();
            }
        }
    }


}
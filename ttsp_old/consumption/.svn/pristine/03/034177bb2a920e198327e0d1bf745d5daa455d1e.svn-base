package trong.lixco.com.info;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
public class CompressZip {
    List<String> fileList;
    private static final String OUTPUT_ZIP_FILE = "E:\\TTSP2.zip";
    private static final String SOURCE_FOLDER = "E:\\TTSP2";
    public CompressZip() {
        fileList = new ArrayList<String>();
    }
    public static void main(String[] args) {
        CompressZip cz = new CompressZip();
        cz.generateFileList(new File(SOURCE_FOLDER));
        cz.zipIt(OUTPUT_ZIP_FILE);
    }
    /**
     * 
     * @param zipFile : output ZIP file location
     */
    public void zipIt(String zipFile) {
        byte[] buffer = new byte[1024];
        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            System.out.println("Output to Zip : " + zipFile);
            for (String file : this.fileList) {
                System.out.println("File Added : " + file);
                ZipEntry ze = new ZipEntry(file);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(SOURCE_FOLDER
                             + File.separator + file);
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
            }
            zos.closeEntry();
            zos.close();
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
     }

    /**
    * Traverse a dicrectory and get all files,
    * and add the file into fileList
    * @param node : file or directory
    */
    public void generateFileList(File node)
    {
        // add file only
        if (node.isFile()) {
        fileList.add(generateZipEntry(node.getAbsolutePath().toString()));
        }

        if (node.isDirectory()) {
            String[] subNode = node.list();
            for (String fileName : subNode) {
                generateFileList(new File(node,fileName));
            }
        }
    }
   /**
    * Format the file path for zip
    * @param file : file path
    * @return : Formatted file path
    */
    private String generateZipEntry(String file)
    {
        return file.substring(SOURCE_FOLDER.length()+1, file.length());
    }
}
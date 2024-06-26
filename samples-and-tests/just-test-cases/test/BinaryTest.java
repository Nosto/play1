import controllers.Binary;
import org.junit.Test;
import play.Play;
import play.exceptions.UnexpectedException;
import play.mvc.Http.Response;
import play.test.FunctionalTest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BinaryTest extends FunctionalTest {




    @Test
    public void testUploadBigFile() {

        Map<String,String> parameters= new HashMap<String,String>();

        Map<String, File> files= new HashMap<String, File>();
        File file = Play.getFile("test/winie.jpg");
        assertTrue(file.exists());
        files.put("file", file);
        Response uploadResponse = POST("/Binary/uploadFile", parameters, files);

        assertStatus(200, uploadResponse);

        String size = uploadResponse.getHeader("Content-Length");

        assertEquals("Size does not match", "1366949", size);
    }

    @Test
    public void testUploadBigFile2() {

        Map<String,String> parameters= new HashMap<String,String>();

        Map<String, File> files= new HashMap<String, File>();
        File file = Play.getFile("test/winie.jpg");
        assertTrue(file.exists());

        files.put("upload", file);
        Response uploadResponse = POST("/Binary/upload", parameters, files);

        assertStatus(200, uploadResponse);

        String size = uploadResponse.getHeader("Content-Length");

        assertEquals("Size does not match", "1366949", size);
    }

    @Test
    public void testUploadSmallFile() {

        Map<String,String> parameters= new HashMap<String,String>();

        Map<String, File> files= new HashMap<String, File>();
        File file = Play.getFile("test/angel.gif");
        assertTrue(file.exists());
        files.put("file", file);
        Response uploadResponse = POST("/Binary/uploadFile", parameters, files);

        assertStatus(200, uploadResponse);

        String size = uploadResponse.getHeader("Content-Length");

        assertEquals("Size does not match", "2440", size);
    }

    @Test
    public void testUploadSmallFile2() {

        Map<String,String> parameters= new HashMap<String,String>();

        Map<String, File> files= new HashMap<String, File>();
        File file = Play.getFile("test/angel.gif");
        assertTrue(file.exists());

        files.put("upload", file);
        Response uploadResponse = POST("/Binary/upload", parameters, files);

        assertStatus(200, uploadResponse);

        String size = uploadResponse.getHeader("Content-Length");

        assertEquals("Size does not match", "2440", size);
    }

    @Test
    public void testUploadZeroLenghtFile() {

        Map<String,String> parameters= new HashMap<String,String>();

        Map<String, File> files= new HashMap<String, File>();
        File file = Play.getFile("test/zeroLenghtFile.txt");
        assertTrue(file.exists());
        files.put("file", file);
        Response uploadResponse = POST("/Binary/uploadFile", parameters, files);

        assertStatus(200, uploadResponse);

        String size = uploadResponse.getHeader("Content-Length");

        assertEquals("Size does not match", "0", size);
    }

    @Test
    public void testUploadZeroLenghtFile2() {

        Map<String,String> parameters= new HashMap<String,String>();

        Map<String, File> files= new HashMap<String, File>();
        File file = Play.getFile("test/zeroLenghtFile.txt");
        assertTrue(file.exists());

        files.put("upload", file);
        Response uploadResponse = POST("/Binary/upload", parameters, files);

        assertStatus(200, uploadResponse);

        String size = uploadResponse.getHeader("Content-Length");

        assertEquals("Size does not match", "0", size);
    }
    
    @Test
    public void testUploadNoFile() {

        Map<String,String> parameters= new HashMap<String,String>();

        Map<String, File> files= new HashMap<String, File>();
        files.put("file", null);
        Response uploadResponse = POST("/Binary/uploadFile", parameters, files);

        assertStatus(200, uploadResponse);

        assertContentMatch("File is null" , uploadResponse);
    }

    @Test
    public void testUploadNoFile2() {

        Map<String,String> parameters= new HashMap<String,String>();

        Map<String, File> files= new HashMap<String, File>();
        files.put("upload", null);
        Response uploadResponse = POST("/Binary/upload", parameters, files);

        assertStatus(200, uploadResponse);

        assertContentMatch("Upload is null" , uploadResponse);
    }
    
//  TODO: Missing possibility to upload multiple files at once
//  See: http://play.lighthouseapp.com/projects/57987-play-framework/tickets/472-functionaltest-and-ws-client-library-dont-allow-upload-of-multiple-file#ticket-472-2 
//    @Test
//    public void testMultipleUpload() {
//
//        Map<String,String> parameters= new HashMap<String,String>();
//
//        Map<String, File[]> files= new HashMap<String, File[]>();
//        File file1 = Play.getFile("test/angel.gif");
//        assertTrue(file1.exists());
//
//        File file2 = Play.getFile("test/winie   .gif");
//        assertTrue(file1.exists());
//
//        files.put("upload", new File[] {file1, file2 });
//        Response uploadResponse = POST("/Binary/uploadMultiple", parameters, files);
//
//        assertStatus(200, uploadResponse);
//
//        String size = uploadResponse.getHeader("Content-Length");
//
//        assertEquals("Size does not match", "2440", size);
//    }

    @Test
    public void testGetBinaryWithCustomContentType() {
        Response response = GET("/binary/getBinaryWithCustomContentType");
        assertIsOk(response);
        assertContentType("custom/contentType", response);
    }

    // Tests to check whether input streams to renderBinary are closed.

    @Test
    public void testGetEmptyBinary() {
        Response response = GET("/binary/getEmptyBinary");
        assertIsOk(response);
        assertTrue(Binary.emptyInputStreamClosed);
    }

    @Test
    public void testGetErrorBinary() {
        try {
            GET("/binary/getErrorBinary");
            fail("expected wrapped IOException");
        }
        catch (RuntimeException expected) {
            assertTrue(expected.getCause() instanceof ExecutionException);
            assertTrue(expected.getCause().getCause() instanceof UnexpectedException);
            assertTrue(expected.getCause().getCause().getCause() instanceof IOException);
            assertEquals("io failed", expected.getCause().getCause().getCause().getMessage());
        }
        finally {
            assertTrue(Binary.errorInputStreamClosed);
        }
    }
}

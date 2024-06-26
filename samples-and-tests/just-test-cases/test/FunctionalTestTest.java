import models.User;
import org.junit.Test;
import play.mvc.Http.Cookie;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.results.NotFound;
import play.test.FunctionalTest;
import play.test.UnitTest;

import java.util.HashMap;

public class FunctionalTestTest extends FunctionalTest {


    @Test
    public void makeSureCookieSaved(){
        Request request = newRequest();
        request.contentType = "application/x-www-form-urlencoded";
        final Cookie cookie = new Cookie();
        cookie.name = "PLAY_TEST";
        cookie.value = "Is it keeping saved?";
        request.cookies = new HashMap<String, Cookie>(){{
            put("PLAY_TEST", cookie);
        }};

        Response response = POST(request, "/application/makeSureCookieSaved", "application/x-www-form-urlencoded", "body=dummy");
        assertIsOk(response);
        assertEquals("Is it keeping saved?", response.cookies.get("PLAY_TEST").value);
        response = PUT("/application/makeSureCookieSaved", "application/x-www-form-urlencoded", "body=dummy");
        assertIsOk(response);
        assertEquals("Is it keeping saved?", response.cookies.get("PLAY_TEST").value);
        response = DELETE("/application/makeSureCookieSaved");
        assertIsOk(response);
        assertEquals("Is it keeping saved?", response.cookies.get("PLAY_TEST").value);
    }
    
    public static class AnotherInnerTest extends UnitTest {
        
        @Test
        public void hello() {
            assertEquals(2, 1+1);
        }
        
    }

    @Test
    public void testGettingStaticFile() {
        Response response = GET("/public/session.test?req=1");
        assertIsStaticFile(response, "public/session.test");
    }
    
      /**
     * This is a regression test for [#2140], which is a bug in FunctionalTest that prevented it from
     * testing a controller action that uses {@link Response#writeChunk(Object)}.
     */
    @Test
    public void testWriteChunks() {
        Response response = GET("/application/writeChunks");
        assertTrue(response.chunked);
        assertIsOk(response);
        assertContentType("text/plain", response);
        assertContentEquals("abcæøåæøå", response);
    }
    

    /**
     * A simple call that should always work.
     */
    @Test
    public void testOk() {
        final Response response = GET("/status/ok/");
        assertStatus(200, response);
        assertContentEquals("Okay", response);
    }

    /**
     * When a route is called that is not even defined, an exception is expected.
     */
    @Test(expected = NotFound.class)
    public void testNoRoute() {
        GET("/status/route-not-defined/");
    }

    /**
     * When a defined route is called but the controller decides to render a 404,
     * the test code is expected to pass and we can assert on the status.
     */
    @Test
    public void testNotFound() {
      final Response response = GET("/status/not-found/");
      assertStatus(404, response);
    }

    /**
     * When a controller throws a normal exception, an exception is expected in
     * the test method as well.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testFailure() {
      GET("/status/failure/");
    }

    /**
     * When a controller renders a non-standard result code (which is, actually, implemented
     * through exception), the call is expected to pass and we can assert on the status.
     */
    @Test
    public void testUnauthorized() {
      final Response response = GET("/status/unauthorized/");
      assertStatus(401, response);
    }

    /**
     * Even when a controller makes use of continuations, e.g. by calling and waiting for a
     * job, it is expected that we can assert on the status code.
     */
    @Test
    public void testContinuationCustomStatus() {
      final Response response = POST("/status/job/");
      assertStatus(201, response);
    }

    /**
     * Even when a controller makes use of continuations, e.g. by calling and waiting for a
     * job, it is expected that we can assert on the content.
     */
    @Test
    public void testContinuationContent() {
      final Response response = POST("/status/job/");
      assertContentEquals("Job completed successfully", response);
    }

}

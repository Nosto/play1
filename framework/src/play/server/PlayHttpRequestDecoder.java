package play.server;

import org.jboss.netty.handler.codec.http.HttpRequestDecoder;

public class PlayHttpRequestDecoder extends HttpRequestDecoder {

    public PlayHttpRequestDecoder() {
        super(8192, 8192, 8192);
    }
}

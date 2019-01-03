package com.benjamin.http.Interceptor;


import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Locale;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static com.benjamin.http.Interceptor.JsonFormater.jsonFormat;
import static java.net.HttpURLConnection.HTTP_NOT_MODIFIED;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static okhttp3.internal.http.StatusLine.HTTP_CONTINUE;


/**
 * 全局拦截，打印http响应内容
 * @author user
 */
public class LogResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        long t1 = System.nanoTime();

        Request request = chain.request();
        Response response = chain.proceed(request);

        long t2 = System.nanoTime();

        ResponseBody responseBody = response.body();
        //long contentLength = responseBody.contentLength();
        //String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        //比如 <-- 200 OK http://121.40.227.8:8088/api (36ms)

        if (!hasBody(response)) {
            logComm(response, t2, t1);
        } else if (bodyEncoded(response.headers())) {
            logComm(response, t2, t1);
        } else {
            BufferedSource source = responseBody.source();
            // Buffer the entire body.
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            Charset UTF8 = Charset.defaultCharset();
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    UTF8 = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    logComm(response, t2, t1);
                    return response;
                }
            }

            if (!isPlaintext(buffer)) {
                logComm(response, t2, t1);
                return response;
            }

            if (buffer.size() > 0) {
                //获取Response的body的字符串 并打印
                String bodyStr = buffer.clone().readString(UTF8);
                logBody(response, t2, t1, buffer.size(), bodyStr);
            } else {
                logComm(response, t2, t1);
            }

        }
        return response;
    }

    private void logComm(Response response, long t2, long t1) {
        log(response, t2, t1,0,null);
    }

    private void logBody(Response response, long t2, long t1, long bodySize, String body) {
        log(response, t2, t1, bodySize, body);
    }

    private void log(Response response, long t2, long t1, long bodySize, String body) {
        String headerMsg = String.format(Locale.US, "Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers());
        String bodyMsg = body == null ? "" : "body size = " + bodySize / 1024 + "kB" + "\nresponse body = " + jsonFormat(body);
        Log.d("LogResponseInterceptor", headerMsg + bodyMsg);
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    static boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private static boolean hasBody(Response response) {
        // HEAD requests never yield a body regardless of the response headers.
        if (response.request().method().equals("HEAD")) {
            return false;
        }

        int responseCode = response.code();
        if ((responseCode < HTTP_CONTINUE || responseCode >= 200)
                && responseCode != HTTP_NO_CONTENT
                && responseCode != HTTP_NOT_MODIFIED) {
            return true;
        }

        // If the Content-Length or Transfer-Encoding headers disagree with the
        // response code, the response is malformed. For best compatibility, we
        // honor the headers.
        if (contentLength(response) != -1
                || "chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) {
            return true;
        }

        return false;
    }

    private static long contentLength(Response response) {
        String s = response.headers().get("Content-Length");
        if (s == null) return -1;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
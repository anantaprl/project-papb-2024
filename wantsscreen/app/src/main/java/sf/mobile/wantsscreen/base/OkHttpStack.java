package sf.mobile.wantsscreen.base;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;

public class OkHttpStack extends HurlStack {
    private final OkHttpClient client;

    public OkHttpStack() {
        this(new OkHttpClient());
    }

    public OkHttpStack(OkHttpClient client) {
        if (client == null) throw new NullPointerException("Client must not be null.");
        this.client = client;
    }

//    @Override
//    protected HttpURLConnection createConnection(URL url) throws IOException {
//        return new OkUrlFactory(client).open(url);
//    }
}
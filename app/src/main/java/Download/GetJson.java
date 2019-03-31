package Download;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class GetJson extends AsyncTask<String, Void, String>
{

    public interface ClientIF
    {
        void processFinish(String json);
    }

    private ClientIF delegate;
    private String json;
    //WeakReference for solving the "Leaking into Context" problem
    private WeakReference<ProgressBar> progressBar;


    public GetJson(ProgressBar progressBar, ClientIF delegate)
    {
        super();
        this.progressBar = new WeakReference<>(progressBar);
        this.delegate = delegate;
    }

    protected void onPreExecute()
    {
        progressBar.get().setVisibility(View.VISIBLE);

    }

    protected String doInBackground(String... params)
    {
        HttpsURLConnection conn = null;
        StringBuilder sb;
        try
        {
            String api_id = "8ad4db2c";
            String api_key = "898418bb965e8f51a332aec9e4f170f4";
            String urlString = "https://api.edamam.com/search?q=" + params[0] + "&app_id=" + api_id + "&app_key=" + api_key + "&from=" + params[1] + "&to=" + params[2];
            URL url = new URL(urlString);
            conn = (HttpsURLConnection) url.openConnection();
            conn.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1)
            {
                sb.append((char) cp);
            }
            return sb.toString();

        } catch (Exception e)
        {
            e.printStackTrace();
            return "";
        } finally {
            if (conn != null)
            {
                conn.disconnect();
            }
        }
    }

    protected void onPostExecute(String json)
    {
        delegate.processFinish(json);
    }
}


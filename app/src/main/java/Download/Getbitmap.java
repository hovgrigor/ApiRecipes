package Download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Getbitmap extends AsyncTask<Bitmap, Void, Bitmap>
{

    //Class to get Bitmap of an image

    public interface setimg
    {
        void processFinish(Bitmap bm);
    }

    private setimg delegate;
    private String url;

    protected void onPreExecute()
    {

    }

    public Getbitmap(String url, setimg delegate )
    {
        this.delegate = delegate;
        this.url = url;
    }

    protected Bitmap doInBackground(Bitmap... params)
    {
        Bitmap bm = null;
        try
        {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return bm;
    }

    protected void onPostExecute(Bitmap bm)
    {
        delegate.processFinish(bm);
    }
}


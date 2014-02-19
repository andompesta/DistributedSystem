package com.hadooptry;

import com.sun.jndi.toolkit.url.Uri;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSutil {

    private Configuration con = null;
    private FSDataInputStream in;
    private FSDataOutputStream out;

    public HDFSutil(Configuration con) {
        try {
            this.con = con;
            URI uri = new URI("s3n://masera-cavallari-melchiori/semaphore");
            FileSystem fileSystem = FileSystem.get(uri, con);
            setFinish(false);
        } catch (IOException ex) {
            Logger.getLogger(HDFSutil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(HDFSutil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setFinish(boolean bool) {
        FileSystem fileSystem = null;
        try {
            URI uri = new URI("s3n://masera-cavallari-melchiori/semaphore");
            fileSystem = FileSystem.get(uri, con);
            // Check if the file already exists
            Path path = new Path("s3n://masera-cavallari-melchiori/semaphore");
            if (fileSystem.exists(path)) {
                System.out.println("File " + path + " already exists");
            }
            out = fileSystem.create(path);

            out.writeBoolean(bool);

        } catch (IOException ex) {
            Logger.getLogger(HDFSutil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(HDFSutil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (fileSystem != null) {
                    fileSystem.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public boolean getFinish() {
        FileSystem fileSystem = null;
        try {
            fileSystem = FileSystem.get(con);

            Path path = new Path("semaphore");

            in = fileSystem.open(path);
            return in.readBoolean();

        } catch (IOException ex) {
            Logger.getLogger(HDFSutil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (fileSystem != null) {
                    fileSystem.close();
                }
            } catch (Exception e) {
            }
        }
        return true;
    }
}

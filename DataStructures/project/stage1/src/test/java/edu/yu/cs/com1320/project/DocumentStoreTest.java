package edu.yu.cs.com1320.project;

import edu.yu.cs.com1320.project.impl.DocumentStoreImpl;
import edu.yu.cs.com1320.project.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage1.DocumentStore;
import org.junit.Test;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class DocumentStoreTest {
    @Test

    public void putNewDocument(){
        DocumentStoreImpl store = new DocumentStoreImpl();
        URI myUri;
        try {
            myUri = new URI("hello");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(store.put("hi", myUri, DocumentStore.DocumentFormat.TXT);
    }
}

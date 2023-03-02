package edu.yu.cs.com1320.project;

import edu.yu.cs.com1320.project.impl.DocumentImpl;
import org.junit.Test;
import java.net.URI;
import java.net.URISyntaxException;

public class DocumentTest {

    @Test //testing if first constructor works
    // expected output: "LETS GO"
    public void constructorOneTest() {
        URI myUri;
        try {
            myUri = new URI("hello");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        DocumentImpl doc = new DocumentImpl(myUri, "LETS GO");
        System.out.println(doc.getDocumentTxt());
    }

    @Test // testing if second constructor works
    // expected output: [B@18ef96
    public void constructorTwoTest() {
        URI myUri;
        byte[] byteData = new byte[4];
        try {
            myUri = new URI("hello");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        DocumentImpl doc = new DocumentImpl(myUri, byteData);
        System.out.println(doc.getDocumentBinaryData());
    }

    @Test // expected output: IAE
    public void nullUriInConstructor(){
        URI myUri = null;
        DocumentImpl doc = new DocumentImpl(myUri, "hello");
    }

    @Test // expected output: IAE
    public void nullTextInConstructor(){
        String text = null;
        URI myUri;
        try {
            myUri = new URI("hello");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        DocumentImpl doc = new DocumentImpl(myUri, text);
    }

    @Test // expected output: IAE
    public void nullByteDataInConstructor(){
        byte[] nullByte = null;
        URI myUri;
        try {
            myUri = new URI("hello");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        DocumentImpl doc = new DocumentImpl(myUri, nullByte);
    }

    @Test // expected output: IAE
    public void emptyStringInConstructor(){
        String text = "";
        URI myUri;
        try {
            myUri = new URI("hello");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        DocumentImpl doc = new DocumentImpl(myUri, text);
    }

    @Test // expected output: IAE
    public void emptyByteDataInConstructor(){
        byte[] emptyByte = new byte[0];
        URI myUri;
        try {
            myUri = new URI("hello");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        DocumentImpl doc = new DocumentImpl(myUri, emptyByte);
    }

    @Test // expected output: IAE
    public void emptyUriInConstructor(){
        // find out how to create and check for an empty URI
    }

    @Test // expected output: "LETS GO"
    public void getDocumentTxtTest(){
        URI myUri;
        try {
            myUri = new URI("hello");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        DocumentImpl doc = new DocumentImpl(myUri, "LETS GO");
        System.out.println(doc.getDocumentTxt());
    }

    @Test // expected output: [B@18ef96
    public void getDocumentBinaryDataTest(){
        URI myUri;
        byte[] byteData = new byte[4];
        try {
            myUri = new URI("hello");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        DocumentImpl doc = new DocumentImpl(myUri, byteData);
        System.out.println(doc.getDocumentBinaryData());
    }

    @Test // expected output: "hello"
    public void getKeyTest(){
        URI myUri;
        byte[] byteData = new byte[4];
        try {
            myUri = new URI("hello");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        DocumentImpl doc = new DocumentImpl(myUri, byteData);
        System.out.println(doc.getKey());
    }

    @Test // expected output: true, false
    public void equalsTest(){
        URI myUri;
        try {
            myUri = new URI("hello");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        DocumentImpl doc = new DocumentImpl(myUri, "LETS GO");
        DocumentImpl test = new DocumentImpl(myUri, "LETS GO");
        System.out.println(doc.equals(test));
        DocumentImpl newDoc = new DocumentImpl(myUri, "LETS GO");
        DocumentImpl newTest = new DocumentImpl(myUri, "not gonna work");
        System.out.println(newDoc.equals(newTest));
    }
}

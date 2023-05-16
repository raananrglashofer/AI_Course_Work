package edu.yu.cs.com1320.project.stage5.impl;

import com.google.gson.*;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.file.FileSystem;

/**
 * created by the document store and given to the BTree via a call to BTree.setPersistenceManager
 */
public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {
    private File directory;

    public DocumentPersistenceManager(File baseDir){
        if(baseDir != null){
            this.directory = baseDir;
        } else{
            this.directory = new File(System.getProperty("user.dir"));
        }
    }

    @Override
    public void serialize(URI uri, Document val) throws IOException {
        if(val.getDocumentTxt() != null){
            Gson gson = new Gson();

        }
    }

    @Override
    public Document deserialize(URI uri) throws IOException {
        return null;
    }

    /**
     * delete the file stored on disk that corresponds to the given key
     * @param uri
     * @return true or false to indicate if deletion occured or not
     * @throws IOException
     */
    @Override
    public boolean delete(URI uri) throws IOException {
        return false;
    }

    private class docSerializer implements JsonSerializer<Document>{
        @Override
        public JsonElement serialize(Document document, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(" ", new JsonPrimitive(document.getDocumentTxt()));
            jsonObject.add(" ", document.getKey());
            jsonObject.add(" ", new JsonPrimitive(document.getWordMap()));
            //jsonGenerator.writeObject(document.getDocumentTxt() + " " + document.getKey() + " " + document.getWordMap());
            //return new JsonPrimitive(document.getDocumentTxt(), document.getKey(), document.getWordMap());
        }
    }

    private class docDeserializer implements JsonDeserializer<Document>{

        @Override
        public Document deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return null;
        }
    }
}
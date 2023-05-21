package edu.yu.cs.com1320.project.stage5.impl;

import com.google.gson.*;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import jakarta.xml.bind.DatatypeConverter;
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
        Gson gson = new GsonBuilder().registerTypeAdapter(DocumentImpl.class, new docSerializer()).setPrettyPrinting().create();
        String json = gson.toJson(val);
        String filePath = URItoFile(uri);
        File file = new File(this.directory, filePath);
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
        String filePath = URItoFile(uri);
        File deletedFile = new File(this.directory, filePath);
        return deletedFile.delete();
    }
    private String URItoFile(URI uri){
        String toFile = uri.toString().replace("http://", "");
        String path = toFile + ".json";
        return path;
    }

    private class docSerializer implements JsonSerializer<Document>{
        @Override
        public JsonElement serialize(Document document, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            if(document.getDocumentTxt() != null) {
                jsonObject.add("", new JsonPrimitive(document.getDocumentTxt()));
            } else{
                String base64Encoded = DatatypeConverter.printBase64Binary(document.getDocumentBinaryData());
                jsonObject.add("", new JsonPrimitive(base64Encoded));
            }
            //context.serialize
            jsonObject.add("WordMap", (JsonElement) document.getWordMap());
            jsonObject.add("URI",  document.getKey());
            return jsonObject;
        }
    }

    private class docDeserializer implements JsonDeserializer<Document>{

        @Override
        public Document deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = new JsonObject();

            return null;
        }
    }
}
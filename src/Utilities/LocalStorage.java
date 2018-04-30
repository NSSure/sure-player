package Utilities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Alert;
import org.hildan.fxgson.FxGson;
import java.io.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.util.ArrayList;

/**
 * Handles writing and reading from the local file system.
 *
 * @author Nick Gordon
 * @since 4/16/2018
 */
public class LocalStorage<TEntity>
{
    private String path;
    private Class<TEntity> genericType;

    public LocalStorage(String path, Class<TEntity> type)
    {
        this.path = path;
        this.genericType = type;
    }

    public String getPath()
    {
        return this.path;
    }

    /**
     * Checks if a file exists at the path.
     * @return A boolean representing the existence of the file.
     */
    public boolean doesFileExist()
    {
        File file = new File(this.getPath());
        return file.exists();
    }

    /**
     * Creates an empty file and the specified path for the file.
     */
    public void createEmpty()
    {
        try
        {
            File file = new File(this.getPath());
            file.createNewFile();
        }
        catch (Exception ex)
        {
            System.out.println("Failed to create default file state.");
        }
    }

    /**
     * Writes a json file to the local file system.
     * @param data The data to save to the json file.
     */
    public void write(TEntity data)
    {
        try
        {
            // Build the file writer for writing the data.
            Writer writer = new FileWriter(this.getPath());

            // Create the json object from the data and write to the local file system.
            Gson gson = FxGson.coreBuilder().create();
            gson.toJson(data, writer);

            writer.close();
        }
        catch (IOException ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Failed to Write to Local Storage");
            alert.setContentText("Whoops! Something went wrong attempting to write to local storage.  Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * Writes an array to the local file system
     * @param data The array of data to save as json.
     */
    public void writeMany(ArrayList<TEntity> data)
    {
        try
        {
            // Build the file writer for writing the data.
            Writer writer = new FileWriter(this.getPath());

            // Create the json object from the data and write to the local file system.
            Gson gson = FxGson.coreBuilder().create();
            gson.toJson(data, writer);

            writer.close();
        }
        catch (IOException ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Failed to Write to Local Storage");
            alert.setContentText("Whoops! Something went wrong attempting to write to local storage.  Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * Reads a json file containing a single object from the local file system.
     * @return The data contained in the local json file.
     */
    public TEntity read()
    {
        try
        {
            // Read the file from the local file system.
            BufferedReader br = new BufferedReader(new FileReader(this.getPath()));

            // Retrieve the data from buffered reader using gson.
            Gson gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            Type entityType = new TypeToken<TEntity>() {}.getType();
            return gson.fromJson(br, entityType);
        }
        catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Failed to Read to Local Storage");
            alert.setContentText("Whoops! Something went wrong attempting to read from local storage.  Please try again.");
            alert.showAndWait();
        }

        return null;
    }

    /**
     * Reads a json file containing a array of data from the local file system.
     * @return The array of data contained in the local json file.
     */
    public ArrayList<TEntity> readMany()
    {
        try
        {
            // Read the file from the local file system.
            BufferedReader reader = new BufferedReader(new FileReader(this.getPath()));

            Gson gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();

            // Retrieve the data from buffered reader using gson.  By using the class type of the array list.
            Type genericArrayListType = new GenericArrayListType(genericType);
            ArrayList<TEntity> data = gson.fromJson(reader, genericArrayListType);

            // If there is no data set to the default of an empty array list.
            if(data == null)
            {
                data = new ArrayList<TEntity>();
            }

            return data;
        }
        catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Failed to Read to Local Storage");
            alert.setContentText("Whoops! Something went wrong attempting to read from local storage.  Please try again.");
            alert.showAndWait();
        }

        return null;
    }

    /**
     * Used for getting the type of the entity contained in the array list.
     *
     * Source - StackOverflow (forgot to add the link here).
     *
     * @author Nick Gordon
     * @since 4/16/2018
     */
    private static class GenericArrayListType implements ParameterizedType
    {
        private Type type;

        public GenericArrayListType(Type type) {
            this.type = type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] {type};
        }

        @Override
        public Type getRawType() {
            return ArrayList.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}

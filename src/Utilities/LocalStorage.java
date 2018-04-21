package Utilities;

import Models.Playlist;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Alert;
import org.hildan.fxgson.FxGson;
import java.io.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 4/16/2018 by Nick Gordon
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

    public boolean doesFileExist()
    {
        File file = new File(this.getPath());
        return file.exists();
    }

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

    public void write(TEntity data)
    {
        try
        {
            Writer writer = new FileWriter(this.getPath());

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

    public void writeMany(ArrayList<TEntity> data)
    {
        try
        {
            Writer writer = new FileWriter(this.getPath());

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

    public TEntity read()
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(this.getPath()));

            Gson gson = new Gson();
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

    public ArrayList<TEntity> readMany()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(this.getPath()));

            Gson gson = new Gson();

            Type genericArrayListType = new GenericArrayListType(genericType);
            ArrayList<TEntity> data = gson.fromJson(reader, genericArrayListType);

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

package Utilities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Alert;
import org.hildan.fxgson.FxGson;
import java.io.*;

import java.lang.reflect.Type;

/**
 * Created on 4/16/2018 by Nick Gordon
 */
public class LocalStorage
{
    public boolean doesFileExist(String fileName)
    {
        File file = new File("Storage/" + fileName + ".json");
        return file.exists();
    }

    public <TEntity> void write(String fileName, TEntity data)
    {
        try
        {
            Writer writer = new FileWriter("Storage/" + fileName +  ".json");

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

    public <TEntity> TEntity read(String fileName)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("Storage/" + fileName +  ".json"));

            Gson gson = new Gson();

            Type entityType = new TypeToken<TEntity>() {}.getType();
            TEntity data = gson.fromJson(br, entityType);

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
}

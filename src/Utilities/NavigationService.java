package Utilities;

import Views.Layout.LayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.ArrayList;

public class NavigationService {
    private static ArrayList<String> fxmlPaths = new ArrayList<>();

    public static <TController> TController loadFxml(String fxmlPath) {
        try {
            // Get the fxml resource from the path and load it into the center node.
            FXMLLoader loader = new FXMLLoader(LayoutController.class.getResource(fxmlPath));
            Parent centerNode = loader.load();

            fxmlPaths.add(fxmlPath);

            AppGlobal.getLayoutController().setCenterPane(centerNode, fxmlPath);

            return loader.getController();
        }
        catch (IOException ex)
        {
            return null;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static void loadPreviousFxml() {
        if (fxmlPaths.size() < 2) {
            return;
        }

        String previousFxmlPath = fxmlPaths.get(fxmlPaths.size() - 2);
        fxmlPaths.remove(fxmlPaths.size() - 1);

        loadFxml(previousFxmlPath);
    }
}

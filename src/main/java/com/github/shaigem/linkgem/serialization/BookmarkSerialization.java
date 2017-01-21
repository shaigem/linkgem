package com.github.shaigem.linkgem.serialization;

import com.github.shaigem.linkgem.model.item.BookmarkItem;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.model.item.Item;
import com.github.shaigem.linkgem.ui.events.AddItemToFolderRequest;
import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Handles serializing and deserializing of bookmark items from a JSON file.
 *
 * @author Ronnie Tran
 */
public final class BookmarkSerialization {
    /**
     * The JSON file that will be used for serialization.
     */
    private final File itemsFile;

    private BookmarkSerialization() {
        this.itemsFile = new File("./data/items.json");
    }

    /**
     * Deserializes the JSON file and loads all of the stored bookmarks.
     *
     * @param masterFolder the master folder
     */
    public void deserialize(FolderItem masterFolder) {
        try (FileReader in = new FileReader(itemsFile)) {
            JsonParser parser = new JsonParser();
            JsonElement rootElement = parser.parse(in);
            if (!rootElement.isJsonNull()) {
                JsonArray array = rootElement.getAsJsonArray();
                JsonObject reader = (JsonObject) array.get(0);
                // read and set information about the master/root folder
                String name = reader.get("name").getAsString();
                String description = reader.get("description").getAsString();
                masterFolder.setName(name);
                masterFolder.setDescription(description);
                // now find any children that the master folder may have and add it to the master folder
                JsonArray childrenArray = reader.get("children").getAsJsonArray();
                childrenArray.forEach(element -> eventStudio().broadcast(new AddItemToFolderRequest(masterFolder, createItemFromJsonData(element.getAsJsonObject()))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a bookmark item from a given JsonObject.
     *
     * @param object the json object to create a bookmark item for
     * @return the newly created bookmark item
     */
    private Item createItemFromJsonData(JsonObject object) {
        final String name = object.get("name").getAsString();
        final String description = object.get("description").getAsString();
        final String location = object.get("location").getAsString();
        final boolean hasChildren = object.get("children") != null;
        Item itemToAdd;
        // if the bookmark has children or is a folder
        if (hasChildren) {
            final FolderItem newFolder = new FolderItem(name, description, false);
            final JsonArray childrenArray = object.get("children").getAsJsonArray();
            // find any children that the folder may have and set it
            childrenArray.forEach(element -> eventStudio().broadcast(new AddItemToFolderRequest(newFolder, createItemFromJsonData(element.getAsJsonObject()))));
            itemToAdd = newFolder;
        } else {
            itemToAdd = new BookmarkItem(name, description, location);
        }
        return itemToAdd;
    }

    /**
     * Saves the master folder into a JSON file for reuse.
     *
     * @param masterFolder the master folder that is being serialized
     */
    public boolean serialize(FolderItem masterFolder) {
        Gson builder = (new GsonBuilder()).setPrettyPrinting().create();
        JsonArray resultArray = new JsonArray();

        try (FileWriter writer = new FileWriter(itemsFile)) {
            JsonObject object = new JsonObject();

            // write information about the master folder
            object.addProperty("name", masterFolder.getName());
            object.addProperty("description", masterFolder.getDescription());

            // write any of the master folder's children
            JsonArray children = new JsonArray();
            masterFolder.getChildren().forEach(item -> children.add(createItemInformationForJson(item)));
            object.add("children", children);

            resultArray.add(object);
            writer.write(builder.toJson(resultArray));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Create the item information that will be written in the JSON file.
     *
     * @param item the item to create the JSON information for
     * @return the JsonObject
     */
    private JsonObject createItemInformationForJson(final Item item) {
        final JsonObject object = new JsonObject();
        object.addProperty("name", item.getName());
        object.addProperty("description", item.getDescription());
        object.addProperty("location", (item instanceof BookmarkItem) ? ((BookmarkItem) item).getLocation() : "");
        if (item instanceof FolderItem) {
            // if the item is a folder, we will create the item information for each of the children in the folder
            JsonArray children = new JsonArray();
            object.add("children", children);
            final FolderItem folderItem = (FolderItem) item;
            folderItem.getChildren().forEach(childItem -> children.add(createItemInformationForJson(childItem)));
        }
        return object;
    }

    private static class LazyHolder {
        static final BookmarkSerialization INSTANCE = new BookmarkSerialization();
    }

    public static BookmarkSerialization getInstance() {
        return BookmarkSerialization.LazyHolder.INSTANCE;
    }
}

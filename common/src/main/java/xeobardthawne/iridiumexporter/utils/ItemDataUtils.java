package xeobardthawne.iridiumexporter.utils;

import com.mojang.brigadier.context.CommandContext;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.Registrar;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static xeobardthawne.iridiumexporter.IridiumExporter.MANAGER;


/**
 * This class contains methods related to item data file building,
 * such as building a file for you, getting item names, and more.
 *
 * @version v1.0.0
 * @since v1.0.0
 */
public class ItemDataUtils {

    /**
     * Builds an item data file for you. Do not use File.createNewFile(), this will do that for you.
     *
     * @since v1.0.0
     * @param itemfile The file directory & name you want to write to
     * @param context Used for client debug messages, leave empty if you don't need them.
     * @param sortItems If the items in the file should be sorted alphabetically.
     */
    public static void buildItemDataFile(File itemfile, @Nullable CommandContext<ClientCommandRegistrationEvent.ClientCommandSourceStack> context, boolean sortItems) {
        try {
            if (itemfile.createNewFile()) {
                TextUtils.easyClientMessage("Creating Item Data File...", true);
                FileWriter itemdatawriter = new FileWriter(itemfile);
                itemdatawriter.write("// This file contains data on every single item in your game! Editing this file could cause problems with the program, so try to avoid doing so unless you know what you're doing.");
                itemdatawriter.write(TextUtils.buildLine(true, ",", "iridiumexporter"));
                itemdatawriter.write(TextUtils.buildLine(true, ",", Platform.getMinecraftVersion(), "1.0.0"));
                Registrar<Item> items = MANAGER.get().get(Registries.ITEM);
                List<String> itemlist = new ArrayList<>();
                for (Identifier id : items.getIds()) {
                    itemlist.add(TextUtils.buildLine(true, ",", ItemDataUtils.getModName(id), getModId(id), getItemName(id, items), getItemRegistryName(id)));
                }
                Collections.sort(itemlist);
                StringBuilder stringifiedlist = new StringBuilder();
                for (String item : itemlist) {
                    stringifiedlist.append(item);
                }
                itemdatawriter.write(stringifiedlist.toString());
                itemdatawriter.close();
                TextUtils.easyClientMessage("Finished Building Item Data File.", true);
            } else {
                TextUtils.easyClientMessage("Could/Did not create new Item File. Perhaps it already exists?", true);
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * @since v1.0.0
     * @param identifier The item identifier.
     * @return Item Registry Name (modid:itemid)
     */
    public static String getItemRegistryName(Identifier identifier) { return identifier.toString(); }

    /**
     * Gets a Mod ID from an item identifier.
     * @since v1.0.0
     * @param identifier The item identifier.
     * @return Mod ID
     */
    public static String getModId(Identifier identifier) { return identifier.getNamespace(); }

    /**
     * Gets an Item's name from an item identifier.
     * @since v1.0.0
     * @param identifier The item identifier.
     * @param items The Registrar<\Item> to look for the item in.
     * @return Item Name
     */
    public static String getItemName(Identifier identifier, Registrar<Item> items) {
        Item item = items.get(identifier);

        return item.getName().getString();
    }

    /**
     * Map of modid, modname for optimization purposes.
     * @since v1.0.0
     */
    private static Map<String, String> MOD_NAMES = new HashMap<String, String>();

    /**
     * Gets an item's mod's name from an item identifier.
     * @since v1.0.0
     * @param identifier The item identifier.
     * @return item's mod's name
     */
    public static String getModName(Identifier identifier) {
        if (MOD_NAMES.isEmpty()) {
            for (Mod mod : Platform.getMods()) {
                MOD_NAMES.put(mod.getModId(), mod.getName());
            }
        }

        String modid = getModId(identifier);
        String modname = MOD_NAMES.get(modid);

        if (modname == null) {
            if (modid.equalsIgnoreCase("minecraft")) {
                modname = "Minecraft";
            }
            else {
                modname = "Unknown";
            }
            MOD_NAMES.put(modid, modname);
        }

        return modname;
    }
}
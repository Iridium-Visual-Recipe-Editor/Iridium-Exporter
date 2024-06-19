package xeobardthawne.iridiumexporter.utils;

import com.mojang.brigadier.context.CommandContext;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import dev.architectury.registry.registries.Registrar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import xeobardthawne.iridiumexporter.screen.ItemScreen;

import java.io.File;

import static xeobardthawne.iridiumexporter.IridiumExporter.MANAGER;
import static xeobardthawne.iridiumexporter.IridiumExporter.openItemScreen;

/**
 * Contains methods for doing stuff such as generating images from items.
 * @since v1.0.0
 * @version v1.0.0
 */
public class ImageExportUtils {

    /**
     * Generates Images from the item registry and puts them in a directory.
     * @since v1.0.0
     * @param imageDirectory Where the images should be located.
     * @param context Used for client debug messages, leave empty if you don't need them.
     */
    public static void generateItemImages(File imageDirectory, @Nullable CommandContext<ClientCommandRegistrationEvent.ClientCommandSourceStack> context) {
        Registrar<Item> items = MANAGER.get().get(Registries.ITEM);
        TextUtils.easyClientMessage("Generating Image Files...", true);
        TextUtils.easyClientMessage("Press escape if you wish to stop generating images.", true);
        System.out.println(MinecraftClient.getInstance().currentScreen.getTitle());
        openItemScreen(items, imageDirectory);
//        MinecraftClient.getInstance().setScreen(null);
//        ItemScreen itemScreen = new ItemScreen(items, imageDirectory);
//        MinecraftClient.getInstance().setScreen(itemScreen);
    }
}

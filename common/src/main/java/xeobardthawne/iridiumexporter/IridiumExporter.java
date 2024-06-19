package xeobardthawne.iridiumexporter;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import xeobardthawne.iridiumexporter.screen.ItemScreen;
import xeobardthawne.iridiumexporter.utils.ImageExportUtils;
import xeobardthawne.iridiumexporter.utils.ItemDataUtils;
import xeobardthawne.iridiumexporter.utils.TextUtils;


import java.io.File;
import java.util.function.Supplier;

public final class IridiumExporter {
    public static final String MOD_ID = "iridium_exporter";

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    private static boolean openItemScreen = false;
    private static Registrar<Item> itemScreenRegistrar;
    private static File itemScreenDirectory;

    public static void openItemScreen(Registrar<Item> items, File directory) {
        openItemScreen = true;
        itemScreenRegistrar = items;
        itemScreenDirectory = directory;
    }

    public static void init() {
        // Write common init code here.
        ClientCommandRegistrationEvent.EVENT.register(((dispatcher, registerContext) -> dispatcher.register(ClientCommandRegistrationEvent.literal("iridiumexporter")
                .then(ClientCommandRegistrationEvent.literal("export")
                        .then(ClientCommandRegistrationEvent.literal("all")
                                .executes(context -> {
                                    final boolean sortitems = true;
                                    TextUtils.easyClientMessage("Starting to create files...", true);
                                    File itemdirectory = new File(MinecraftClient.getInstance().runDirectory + "\\exporteddata");
                                    TextUtils.easyClientMessage("Creating Directory...", true);
                                    if (itemdirectory.mkdirs()) {
                                        TextUtils.easyClientMessage("Directory Created.", true);
                                    } else {
                                        TextUtils.easyClientMessage("Directory Already Created.", true);
                                    }
                                    File itemfile = new File(MinecraftClient.getInstance().runDirectory + "\\exporteddata\\itemdata.txt");
                                    ItemDataUtils.buildItemDataFile(itemfile, context, sortitems);
                                    ImageExportUtils.generateItemImages(itemdirectory, context);
                                    return 1;
                                }))
                        .then(ClientCommandRegistrationEvent.literal("itemdata")
                                .executes(context -> {
                                    final boolean sortitems = true;
                                    TextUtils.easyClientMessage("Starting to create files...", true);
                                    File itemdirectory = new File(MinecraftClient.getInstance().runDirectory + "\\exporteddata");
                                    TextUtils.easyClientMessage("Creating Directory...", true);
                                    if (itemdirectory.mkdirs()) {
                                        TextUtils.easyClientMessage("Directory Created.", true);
                                    } else {
                                        TextUtils.easyClientMessage("Directory Already Created.", true);
                                    }
                                    File itemfile = new File(MinecraftClient.getInstance().runDirectory + "\\exporteddata\\itemdata.txt");
                                    ItemDataUtils.buildItemDataFile(itemfile, context, sortitems);
                                    return 1;
                                }))
                        .then(ClientCommandRegistrationEvent.literal("itemimages")
                                .executes(context -> {
                                    TextUtils.easyClientMessage("Starting to create files...", true);
                                    File itemdirectory = new File(MinecraftClient.getInstance().runDirectory + "\\exporteddata");
                                    TextUtils.easyClientMessage("Creating Directory...", true);
                                    if (itemdirectory.mkdirs()) {
                                        TextUtils.easyClientMessage("Directory Created.", true);
                                    } else {
                                        TextUtils.easyClientMessage("Directory Already Created.", true);
                                    }
                                    ImageExportUtils.generateItemImages(itemdirectory, context);
                                    return 1;
                                }))))));


        ClientTickEvent.CLIENT_POST.register((instance -> {
            if (openItemScreen) {
                openItemScreen = false;
                ItemScreen itemScreen = new ItemScreen(itemScreenRegistrar, itemScreenDirectory);
                MinecraftClient.getInstance().setScreen(itemScreen);
            }
        }));
    }
}

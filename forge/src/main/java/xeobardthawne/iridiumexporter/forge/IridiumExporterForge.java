package xeobardthawne.iridiumexporter.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import xeobardthawne.iridiumexporter.IridiumExporter;

@Mod(IridiumExporter.MOD_ID)
public final class IridiumExporterForge {
    public IridiumExporterForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(IridiumExporter.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        IridiumExporter.init();
    }
}

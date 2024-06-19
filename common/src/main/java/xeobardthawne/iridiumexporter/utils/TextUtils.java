package xeobardthawne.iridiumexporter.utils;

import com.mojang.brigadier.context.CommandContext;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

/**
 * Contains useful methods for Text related things such as creating client commands
 * or building strings out of other strings.
 *
 * @since v1.0.0
 * @version v1.0.0
 */
public class TextUtils {

    /**
     * Makes it easier to send client messages. Should only be run client-side.
     * Also has a boolean in cases where it would be easier to use rather than doing a bunch of if statements.
     * @since v1.0.0
     * @param message The message you want to send
     * @param shouldrun Should the message send
     */
    public static void easyClientMessage(String message, boolean shouldrun) {
        if (shouldrun) { MinecraftClient.getInstance().player.sendMessage(Text.of(message)); }
    }

    public static void overlayText(String message) {
        MinecraftClient.getInstance().player.sendMessage(Text.of(message), true);
    }

    /**
     * Builds a string for you with a starter, divider, and a list of Strings.
     * @since v1.0.0
     * @param startNewLine Do you want this string to start a new line?
     * @param divider The divider between each string.
     * @param items All the Strings you want to list in this string.
     * @return A string comprised of the strings provided in order, divided by the divider, and with a possible starter.
     */
    public static String buildLine(boolean startNewLine, String divider, String... items) {
        StringBuilder line = new StringBuilder();
        if (startNewLine) {
            line.append("\r\n");
        }
        var i = 0;
        for (String item : items) {
            if (i > 0) {
                line.append(divider);
            }
            line.append(item);
            i++;
        }
        return line.toString();
    }


}

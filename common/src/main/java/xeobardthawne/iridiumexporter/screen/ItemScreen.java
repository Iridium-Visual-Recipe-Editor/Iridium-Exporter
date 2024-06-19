package xeobardthawne.iridiumexporter.screen;

import dev.architectury.registry.registries.Registrar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import xeobardthawne.iridiumexporter.utils.ItemDataUtils;
import xeobardthawne.iridiumexporter.utils.TextUtils;

import java.awt.*;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * Basically just a screen that renders a bunch of items from a registrar and takes pictures of them
 * @version v1.0.0
 * @since v1.0.0
 */
public class ItemScreen extends Screen {
    private final Registrar<Item> itemRegistrar;
    private final List<Identifier> itemList;
    private File imageDirectory;
    private int ListIndex;

    /**
     * Creates an ItemScreen. Use MinecraftClient.getInstance().setScreen() to activate this screen's use.
     * @since v1.0.0
     * @param items The registrar the items are at
     * @param directory The directory the item images should be added to
     */
    public ItemScreen(Registrar<Item> items, File directory) {
        super(Text.of("ItemScreenTitle"));
        itemRegistrar = items;
        itemList = itemRegistrar.getIds().stream().toList();
        imageDirectory = directory;
        ListIndex = 0;
    }

    private static final int BACKGROUND_COLOR = Colors.WHITE;
    /**
     * The code that runs through and renders items and takes pictures of them
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        final int imagescale = 64;
        TextUtils.overlayText(String.valueOf((ListIndex + 1)) + "/" + itemList.size());
        Identifier identifier = itemList.get(ListIndex);
        ItemStack itemStack = new ItemStack(itemRegistrar.get(identifier));
        double guiscale = MinecraftClient.getInstance().getWindow().getScaleFactor();
        float scaleModified = (float) (imagescale / guiscale);
        int scaleModifiedRounded = (int) Math.ceil(scaleModified);
        context.fill(0, 0, scaleModifiedRounded, scaleModifiedRounded, BACKGROUND_COLOR);
        drawItem(context, itemStack, 0, 0, scaleModified);
        takeItemScreenshot(identifier, imagescale);
        ListIndex++;
        if (ListIndex + 1 > itemList.size()) {
            //ListIndex = 0;
            this.close();
            TextUtils.easyClientMessage("Finished Generating Images.", true);
        }
    }

    @Override
    public void removed() {
        System.out.println("Screen removed");
    }

    /**
     * Modified version of DrawContext.drawItem()
     */
    private void drawItem(DrawContext drawContext, ItemStack stack, int x, int y, float scaleModified) {
        if (!stack.isEmpty()) {
            BakedModel bakedModel = MinecraftClient.getInstance().getItemRenderer().getModel(stack, null, null, 1);
            drawContext.getMatrices().push();
            drawContext.getMatrices().scale(scaleModified / 16, scaleModified / 16, 1);
            drawContext.getMatrices().translate((float)(x + 8), (float)(y + 8), 100.0F);
            drawContext.getMatrices().multiplyPositionMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
            drawContext.getMatrices().scale(16.0F, 16.0F, 16.0F);
            boolean bl = !bakedModel.isSideLit();
            if (bl) {
                DiffuseLighting.disableGuiDepthLighting();
            }

            MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, false, drawContext.getMatrices(), drawContext.getVertexConsumers(), 15728880, OverlayTexture.DEFAULT_UV, bakedModel);
            drawContext.draw();
            if (bl) {
                DiffuseLighting.enableGuiDepthLighting();
            }

            drawContext.getMatrices().pop();
        }
    }

    private void takeItemScreenshot(Identifier identifier, int imageScale) {
        NativeImage imageFull = ScreenshotRecorder.takeScreenshot(MinecraftClient.getInstance().getFramebuffer());
        NativeImage image = getSubImage(imageFull, imageScale, imageScale);
        imageFull.close();

        for (int ix = 0; ix < image.getWidth(); ix++) {
            for (int iy = 0; iy < image.getHeight(); iy++) {
                int color = image.getColor(ix, iy);

                if (color == BACKGROUND_COLOR) {
                    image.setColor(ix, iy, 0);
                }
            }
        }
        String convertedName = ItemDataUtils.getItemRegistryName(identifier).replace(":", "__");


        try {
            File imageFile = new File(imageDirectory, convertedName + ".png").getCanonicalFile();
            image.writeTo(imageFile);
        } catch (IOException e) {
            System.out.println("IOException Caught. Possible error while writing the PNG image for name " + convertedName);
        }
        finally {
            image.close();
        }
    }


    /**
     * Resizes an image from the top left.
     * @param imageFull Original image to resize
     * @param width Width to resize to
     * @param height Height to resize to
     * @return Resized Image
     */
    public static NativeImage getSubImage(NativeImage imageFull, int width, int height) {
        NativeImage imageNew = new NativeImage(width, height, false);

        for (int ix = 0; ix < width; ix++) {
            for (int iy = 0; iy < height; iy++) {
                int color = imageFull.getColor(ix, iy);

                imageNew.setColor(ix, iy, color);
            }
        }
        //imageFull.resizeSubRectTo(0, 0, width, height, imageNew);
        return imageNew;
    }
}


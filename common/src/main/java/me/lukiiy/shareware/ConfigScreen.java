package me.lukiiy.shareware;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ConfigScreen extends Screen {
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 33, 33);
    private ConfigList list;
    private final Screen before;

    public ConfigScreen(Screen before) {
        super(Component.literal(Common.config.modName));

        this.before = before;
    }

    @Override
    protected void init() {
        layout.addToHeader(LinearLayout.vertical().spacing(8)).addChild(new StringWidget(this.getTitle(), font), LayoutSettings::alignHorizontallyCenter);
        list = new ConfigList();

        layout.addToContents(list);
        layout.addToFooter(LinearLayout.horizontal().spacing(8)).addChild(Button.builder(CommonComponents.GUI_DONE, _ -> onClose()).width(100).build());
        layout.visitWidgets(this::addRenderableWidget);

        repositionElements();
    }

    @Override
    protected void repositionElements() {
        layout.arrangeElements();

        if (list != null) list.updateSize(width, layout);
    }

    @Override
    public void onClose() {
        if (before != null) minecraft.setScreenAndShow(before);
    }

    private class ConfigList extends ContainerObjectSelectionList<ConfigList.Entry> {
        abstract static class Entry extends ContainerObjectSelectionList.Entry<Entry> {}

        public ConfigList() {
            super(ConfigScreen.this.minecraft, ConfigScreen.this.width, layout.getContentHeight(), layout.getHeaderHeight(), 24);
        }

        public void addBooleanOption(Config.Key<Boolean> key) {
            this.addEntry(new BooleanEntry(key));
        }

        public void addFloatOption(Config.Key<Float> key, float min, float max) {
            this.addEntry(new FloatEntry(key, min, max));
        }

        @Override
        public int getRowWidth() {
            return 310;
        }

        class BooleanEntry extends Entry {
            private final Checkbox box;
            private final Component label;

            public BooleanEntry(Config.Key<Boolean> key) {
                this.label = Component.literal(key.key);
                this.box = Checkbox.builder(Component.empty(), font).selected(key.get()).onValueChange((_, v) -> key.set(v)).build();
            }

            @Override
            public List<? extends GuiEventListener> children() {
                return List.of(box);
            }

            @Override
            public List<? extends NarratableEntry> narratables() {
                return List.of(box);
            }

            @Override
            public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
                graphics.text(font, label, getContentX(), getContentY() + 6, -1);

                box.setX(getContentRight() - box.getWidth());
                box.setY(getContentY() + (24 - box.getHeight()) / 2);
                box.extractRenderState(graphics, mouseX, mouseY, a);
            }
        }

        class FloatEntry extends Entry {
            private final EditBox box;
            private final Component label;

            public FloatEntry(Config.Key<Float> key, float min, float max) {
                this.label = Component.literal(key.key);
                this.box = new EditBox(font, 0, 0, 80, 20, Component.literal(key.key));

                box.setValue(String.valueOf(key.get()));
                box.setMaxLength(32);
                box.setResponder(it -> {
                    try {
                        if (!it.isEmpty() && !it.equals(".")) key.set(Math.clamp(Float.parseFloat(it), min, max));
                    } catch (NumberFormatException _) {}
                });
            }

            @Override
            public List<? extends GuiEventListener> children() {
                return List.of(box);
            }

            @Override
            public List<? extends NarratableEntry> narratables() {
                return List.of(box);
            }

            @Override
            public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
                graphics.text(font, label, getContentX(), getContentY() + 6, -1);

                box.setX(getContentRight() - box.getWidth());
                box.setY(getContentY());
                box.extractRenderState(graphics, mouseX, mouseY, a);
            }
        }
    }
}

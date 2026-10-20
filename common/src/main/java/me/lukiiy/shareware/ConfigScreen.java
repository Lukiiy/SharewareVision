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
    public boolean isPauseScreen() {
        return before != null && before.isPauseScreen();
    }

    @Override
    public void onClose() {
        if (before != null) minecraft.setScreenAndShow(before);
    }

    private class ConfigList extends ContainerObjectSelectionList<ConfigList.Entry> {
        public final Font font;

        public ConfigList() {
            super(ConfigScreen.this.minecraft, ConfigScreen.this.width, layout.getContentHeight(), layout.getHeaderHeight(), 24);

            font = ConfigScreen.this.font;

            // entries and whatnot
        }

        @Override
        public int getRowWidth() {
            return 310;
        }

        private <T> EditBox createEditBox(Config.Key<T> key) {
            EditBox box = new EditBox(font, 0, 0, 80, 20, Component.literal(key.key));
            T value = key.get();

            box.setMaxLength(512);
            box.setValue(value == null ? "" : value.toString());

            return box;
        }

        private record StaticNarration(Component text) implements NarratableEntry {
            @Override
            public NarrationPriority narrationPriority() {
                return NarrationPriority.HOVERED;
            }

            @Override
            public void updateNarration(NarrationElementOutput output) {
                output.add(NarratedElementType.TITLE, text);
            }
        }

        abstract class Entry extends ContainerObjectSelectionList.Entry<Entry> {
            protected final Config.Key<?> key;
            protected final List<AbstractWidget> children = Lists.newArrayList();
            protected Component label;
            protected final StaticNarration labelNarration;
            protected AbstractWidget widget;

            protected Entry(Config.Key<?> key, AbstractWidget widget) {
                this.key = key;
                this.label = key == null ? null : Component.translatable("lysten.setting." + key.key);
                this.labelNarration = label == null ? null : new StaticNarration(label);

                setWidget(widget);
            }

            protected void setWidget(AbstractWidget widget) {
                this.widget = widget;

                if (widget != null) {
                    if (key != null && !key.reloadable) widget.setTooltip(Tooltip.create(Component.translatable("lysten.config.nonReloadable")));
                    if (!(widget instanceof CycleButton<?>)) widget.setMessage(label);

                    children.add(widget);
                }
            }

            @Override
            public List<? extends GuiEventListener> children() {
                return children;
            }

            @Override
            public List<? extends NarratableEntry> narratables() {
                return widget != null ? children : (labelNarration != null ? List.of(labelNarration) : List.of());
            }

            @Override
            public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
                if (label != null) graphics.text(ConfigList.this.font, label, getContentX(), getContentY() + 6, -1);

                if (widget != null) {
                    widget.setX(getContentRight() - widget.getWidth());
                    widget.setY(getContentY());
                    widget.extractRenderState(graphics, mouseX, mouseY, a);
                }
            }
        }

        class BooleanEntry extends Entry {
            public BooleanEntry(Config.Key<Boolean> key) {
                super(key, Checkbox.builder(Component.empty(), font).selected(key.get()).onValueChange((b, v) -> key.set(v)).build());
            }
        }

        class FloatEntry extends Entry {
            public FloatEntry(Config.Key<Float> key, float min, float max) {
                super(key, createEditBox(key));

                EditBox box = (EditBox) widget;

                box.setValue(String.valueOf(key.get()));
                box.setResponder(text -> {
                    if (text.isEmpty() || text.equals(".")) return;

                    try {
                        float parsed = Float.parseFloat(text);
                        float clamped = Math.clamp(parsed, min, max);

                        key.set(clamped);
                    } catch (NumberFormatException _) {}
                });
            }
        }
    }
}

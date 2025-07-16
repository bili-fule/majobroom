package net.fabricmc.majobroom.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class ModMenuEntryPoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return new ConfigScreenFactory<Screen>() {
            @Override
            public Screen create(Screen parent) {
                MajoBroomConfig config = MajoBroomConfig.getInstance();
                ConfigBuilder configBuilder = ConfigBuilder.create();
                configBuilder.setSavingRunnable(new Runnable() {
                    @Override
                    public void run() {
                        MajoBroomConfig.getInstance().saveConfig();
                    }
                });
                configBuilder.setParentScreen(MinecraftClient.getInstance().currentScreen);
                configBuilder.setTitle(Text.literal("Majo's Broom Config"));
                ConfigCategory category = configBuilder.getOrCreateCategory(Text.literal("Basic"));
                category.addEntry(ConfigEntryBuilder.create().startBooleanToggle(Text.literal("Switch to Third-person View"),config.autoThirdPersonView).setSaveConsumer(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        MajoBroomConfig.getInstance().autoThirdPersonView = aBoolean;
                    }
                }).build());
                category.addEntry(ConfigEntryBuilder.create().startBooleanToggle(Text.literal("Require EXP level"),config.requireXpLevel).setSaveConsumer(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        MajoBroomConfig.getInstance().requireXpLevel = aBoolean;
                    }
                }).build());
                category.addEntry(ConfigEntryBuilder.create().startDoubleField(Text.literal("Max broom speed"),config.maxMoveSpeed).setMax(30).setMin(10).setSaveConsumer(new Consumer<Double>() {
                    @Override
                    public void accept(Double d) {
                        MajoBroomConfig.getInstance().maxMoveSpeed = d;
                    }
                }).build());
                category.addEntry(ConfigEntryBuilder.create().startBooleanToggle(Text.literal("Legacy movement control"),config.classicalMovement).setSaveConsumer(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        MajoBroomConfig.getInstance().classicalMovement = aBoolean;
                    }
                }).build());
                return configBuilder.build();
            }
        };
    }
}
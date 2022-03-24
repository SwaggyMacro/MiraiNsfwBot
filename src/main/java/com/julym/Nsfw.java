package com.julym;

import com.julym.bot.BotRunner;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;

public final class Nsfw extends JavaPlugin {
    public static final Nsfw INSTANCE = new Nsfw();
    private static BotRunner bot;

    public static Nsfw getInstance() {
        return INSTANCE;
    }

    private Nsfw() {
        super(new JvmPluginDescriptionBuilder("com.julym.nsfw", "1.0")
                .name("nsfw")
                .info("Swaggy Macro")
                .author("Swaggy Macro")
                .build());
    }



    @Override
    public void onEnable() {
        getLogger().info("Plugin loaded!");
        getLogger().info("Initializing CNN Model...");
        getLogger().info("Version: 1.0.0, Created By: Swaggy Macro. Home: julym.com");
        bot = new BotRunner();
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,
                event -> bot.receiveMessage(event)
        );
    }
}
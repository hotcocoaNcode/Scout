package com.hcnc;

import com.hcnc.data.log.GlobalUserIndex;
import com.hcnc.discord.MessageReceiveListener;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Arrays;
import java.util.EnumSet;

public class Main {
    public static void main(String[] args) {
        GlobalUserIndex.init();
        JDABuilder.createLight(args[Arrays.asList(args).indexOf("--token")+1], EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
                .addEventListeners(new MessageReceiveListener())
                .build();
    }
}
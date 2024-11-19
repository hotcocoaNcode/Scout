package com.hcnc;

import com.hcnc.data.UserAnalyzer;
import com.hcnc.data.log.GlobalUserIndex;
import com.hcnc.discord.MessageReceiveListener;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Arrays;
import java.util.EnumSet;

public class Main {
    public static void main(String[] args) {
        GlobalUserIndex.init();
        if (args.length == 2 && args[0].equals("--analyze")) {
            UserAnalyzer.analyzeUser(args[1]);
            return;
        } else if (args.length == 0) {
            System.out.println("Please specify a command. Either --analyze username to analyze saved data or --token bot_token to run the bot.");
        }
        JDABuilder.createLight(args[Arrays.asList(args).indexOf("--token")+1], EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
                .addEventListeners(new MessageReceiveListener())
                .build();
    }
}
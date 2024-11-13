package com.hcnc.discord;

import com.hcnc.data.log.GlobalUserIndex;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceiveListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        GlobalUserIndex.messageEvent(event.getAuthor().getAsTag(), event.getMessage().getContentDisplay());
        System.out.printf("[%s] %s: %s\n",
                event.getChannel().getName(),
                event.getAuthor().getAsTag(),
                event.getMessage().getContentDisplay());
    }
}
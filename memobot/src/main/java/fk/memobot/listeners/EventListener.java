package fk.memobot.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String msg = event.getMessage().getContentRaw();
        if (msg.equalsIgnoreCase("hi")) {
        	if (!event.getAuthor().isBot()) {
                event.getChannel().sendMessage("メッセージを受け取りました！").queue();
            }
        }
	}
}

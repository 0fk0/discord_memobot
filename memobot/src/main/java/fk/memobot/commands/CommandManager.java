package fk.memobot.commands;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CommandManager extends ListenerAdapter {

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		String command = event.getName();
		if (command.equals("write")) {
			User user = event.getUser();
			String note = event.getOption("note").getAsString();
			event.reply("Expired!").setEphemeral(true).queue();
			user.openPrivateChannel()
				.flatMap(channel -> channel.sendMessage(note))
				.queue();
		} else if (command.equals("delete")) {
			User user = event.getUser();
			PrivateChannel pc = user.openPrivateChannel().complete();
			List<Message> getMsg = pc.getHistory().retrievePast(100).complete();
			for (Message msg : getMsg) {
				if (msg.getAuthor().isBot()) {
					msg.delete().queue();
				}
			}
			event.reply(getMsg.size() + "message is deleted!").setEphemeral(true).queue();
		} else if (command.equals("show")) {
			int num = event.getOption("num").getAsInt();
			User user = event.getUser();
			TextChannel tc = event.getChannel().asTextChannel();
			PrivateChannel pc = user.openPrivateChannel().complete();
			if (num <= 100) {
				List<Message> getMsg = pc.getHistory().retrievePast(num).complete();
				for (Message msg : getMsg) {
					tc.sendMessage(msg.getContentDisplay()).queue();
				}
				event.reply("Expired!").queue();;
			} else {
				event.reply("Number must be under the 100!").queue();;
			}
		}
	}

	@Override
	public void onGuildReady(GuildReadyEvent event) {
		List<CommandData> commandData = new ArrayList<>();
		OptionData note = new OptionData(OptionType.STRING, "note", "Texts you want to remain as notes", true);
		commandData.add(Commands.slash("write", "Write texts you want to remain as notes.").addOptions(note));
		
		commandData.add(Commands.slash("delete", "Delete the all message in Private Channel."));
		
		OptionData num = new OptionData(OptionType.INTEGER, "num", "Number of memo you want to show in TextChannel ( <= 100 )", true);
		commandData.add(Commands.slash("show", "Show the memos to your members.").addOptions(num));
		
		event.getGuild().updateCommands().addCommands(commandData).queue();
	}
	
}

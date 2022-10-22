package fk.memobot;

import javax.security.auth.login.LoginException;

import fk.memobot.commands.CommandManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

/**
 * Hello world!
 *
 */
public class App extends ListenerAdapter
{
	
	private static Dotenv config = null;
	private ShardManager shardManager = null;
	
	public App() throws LoginException {
		config = Dotenv.configure().load();
    	String token = config.get("TOKEN");
    	
    	DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
    	builder.setStatus(OnlineStatus.ONLINE);
    	builder.setActivity(Activity.playing("メモ機能"));
    	builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT);
    	shardManager = builder.build();
    	shardManager.addEventListener(new CommandManager());
	}
	
	public ShardManager getShardManager() {
		return shardManager;
	}

    public static void main(String[] args) {
        try {
           App bot = new App();
        } catch (LoginException e) {
            System.out.println("ERROR");
        }
    }
}
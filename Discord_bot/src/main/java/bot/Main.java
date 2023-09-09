package bot;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;


public class Main {
    public static JDA jda;
    public static String prefix="-";

    public static void main(String[] args) throws LoginException {
        JDA jda = JDABuilder.createDefault("insert Discord API token here").build();
        jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        jda.getPresence().setActivity(Activity.playing("Algorithms"));
        jda.addEventListener(new bot.Commands());
        Database.createConnection();
    }
}

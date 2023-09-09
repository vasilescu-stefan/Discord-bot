package bot;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


public class Commands extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event){
        event.getChannel().sendTyping().queue();
        String[] args=event.getMessage().getContentRaw().split("\\s+");
        System.out.println(args);
        Template t=new Template();
        Search s= new Search();
        try {
            if(args[0].equalsIgnoreCase(bot.Main.prefix+ "info")){
            Template t1=Database.searchTitles(args[1]);
            if(t1!=null)
            {
            String message="**"+t1.getTitle()+"**\n"+t1.getContent();
            event.getChannel().sendMessage(message.substring(0,1900)+"\n See more here:https://en.wikipedia.org/?curid="+t1.getPageId()).queue();

            }
            else
            {
                t=s.searchPages(args[1]);
                String message="**"+t.getTitle()+"**\n"+t.getContent();
                if(message.length()>1900)
                {
                    event.getChannel().sendMessage(message.substring(0,1900)+"\n See more here:https://en.wikipedia.org/?curid="+t.getPageId()).queue();
                }
                else
                {
                    event.getChannel().sendMessage(message+"\n See more here:https://en.wikipedia.org/?curid="+t.getPageId()).queue();
                }
                Database.insertArticle(t);

            }
            
            
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    public static String buildLink(HashMap<String, String> values) {
        StringBuilder out = new StringBuilder();

        for (Map.Entry<String, String> pair : values.entrySet()) {
            out.append(pair.getKey());
            out.append("=");
            out.append(pair.getValue());
            out.append("&");
        }

        return out.substring(0, out.length() - 1);
    }
}

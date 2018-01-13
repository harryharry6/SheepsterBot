package co.neweden.speedy;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static co.neweden.speedy.Connection.db;

public class Coms extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContent();
        String rank = event.getMember().getRoles().toString();
        String author = event.getAuthor().getName();



        if (message.startsWith(">b ")) {
            if (rank.contains("Senior")) {
                //What was that command function
                if (message.startsWith(">b wwt")) {
                    try {
                        PreparedStatement allst = db.prepareStatement("SELECT * FROM factoids WHERE id=?");
                        allst.setInt(1, mcmessages.previousId.str);
                        ResultSet rs = allst.executeQuery();
                        while (rs.next()) {
                            String Ffactoid = rs.getString("factoid");
                            Integer Fid = rs.getInt("id");
                            String Fresponse = rs.getString("response");
                            String Fauthor = rs.getString("author");
                            String factoids = "```md\n" + "[ID] " + Fid + " | [Factoid] " + Ffactoid + "  | [Response] " + Fresponse + "  | [Author] " + Fauthor + "\n```";
                            event.getTextChannel().sendMessage(factoids).queue();
                        }
                    }
                    catch (SQLException e) {
                        System.out.println("There was an error with MySQL - >b wwt");
                        System.out.println(e);
                    }
                }
                //Next command
                if (message.startsWith(">b add ")) {
                    //Division of the message
                    message = message.substring(7, message.length());
                    String[] parts = message.split(" <> ", 2);
                    String AddFactoid = parts[0];
                    String AddResponse = parts[1];


                    //Adding to the database
                    try {
                        PreparedStatement st = db.prepareStatement("INSERT INTO `factoids` (`factoid`, `response`, `author`) VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
                        st.setString(1, AddFactoid);
                        st.setString(2, AddResponse);
                        st.setString(3, author);
                        st.executeUpdate();
                        System.out.println("the following string were added to the database: " + AddFactoid + " | " + AddResponse + " | " +  author);
                        ResultSet genkeys = st.getGeneratedKeys();
                        genkeys.next();
                        Integer AddId = genkeys.getInt(1);
                        event.getTextChannel().sendMessage("```Factoid has been successfully added with the ID: " + AddId + "```").queue();
                    }
                    catch (SQLException e) {
                        System.out.println("There was an error with MySQL - >b add");
                        System.out.println(e);
                        event.getTextChannel().sendMessage("``` Factoid has not been added : error: " + e + "```").queue();
                    }
                }
                if(message.startsWith(">b remove ")){
                    message = message.substring(10, message.length());
                    try {
                        PreparedStatement st = db.prepareStatement("DELETE FROM `factoids` WHERE id=?");
                        st.setString(1, message);
                        st.executeUpdate();
                        event.getTextChannel().sendMessage("```The factoid with the ID: " + message + " Has been successfully removed ```").queue();
                    }
                    catch (SQLException e){
                        System.out.println("There was an error with MYSQL - >b remove");
                        System.out.println(e);
                        event.getTextChannel().sendMessage("``` Factoid has not been removed : error: " + e + "```");
                    }

                }
            }
            else {
                event.getTextChannel().sendMessage("```diff\n- Error : You must be senior or higher to execute this command\n```").queue();
            }
        }
    }
}






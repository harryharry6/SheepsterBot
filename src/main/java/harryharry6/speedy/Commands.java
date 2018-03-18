package harryharry6.speedy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
    public Commands() {
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContent();
        String rank = event.getMember().getRoles().toString();
        String author = event.getAuthor().getName();
        if (message.startsWith(">b ")) {
            if (!rank.contains("Senior")) {
                event.getTextChannel().sendMessage("```diff\n- Error : You must be senior or higher to execute this command\n```").queue();
            } else if (message.startsWith(">b wwt")) {
                this.wwtCommand(event);
            } else if (message.startsWith(">b add ")) {
                this.addCommand(event, message, author);
            } else if (message.startsWith(">b remove c ")) {
                this.CremoveCommand(event, message);
            } else if (message.startsWith(">b remove f ")) {
                this.FremoveCommand(event, message);
            } else if (message.startsWith(">b count ")) {
                this.countCommand(event, message, author);
            } else if (message.startsWith(">b hmt ")) {
                this.hmtCommand(event, message);
            }
        }
    }

    private void wwtCommand(MessageReceivedEvent event) {
        Factoid f = mcmessages.getLastFactoid();
        String info = "```md\n[ID] " + f.getID() + " | [Factoid] " + f.getFactoid() + " | [Type] " + f.getMessageType() + " | [Response] " + f.getResponse() + " | [Author] " + f.getAuthor() + "\n```";
        event.getTextChannel().sendMessage(info).queue();
    }

    private void addCommand(MessageReceivedEvent event, String message, String author) {
        message = message.substring(7, message.length());
        String AddFactoid = "";
        String AddResponse = "";
        String Addtype = "";
        if (!message.contains("<reply>") && !message.contains("<action>")) {
            event.getTextChannel().sendMessage("```Factoid failed to be added. Error: Invalid Arguments```").queue();
        } else {
            String[] parts;
            if (message.contains("<reply>")) {
                parts = message.split(" <reply> ", 2);
                AddFactoid = parts[0];
                AddResponse = parts[1];
                Addtype = "reply";
            }

            if (message.contains("<action>")) {
                parts = message.split(" <action> ", 2);
                AddFactoid = parts[0];
                AddResponse = parts[1];
                Addtype = "action";
            }

            try {
                PreparedStatement st = Connection.db.prepareStatement("INSERT INTO `factoids` (`factoid`,`type`, `response`, `author`) VALUES (?, ?, ?, ?);", 1);
                st.setString(1, AddFactoid);
                st.setString(2, Addtype);
                st.setString(3, AddResponse);
                st.setString(4, author);
                st.executeUpdate();
                System.out.println("the following string were added to the database: " + AddFactoid + " | " + AddResponse + " | " + author);
                ResultSet genkeys = st.getGeneratedKeys();
                genkeys.next();
                Integer AddId = genkeys.getInt(1);
                event.getTextChannel().sendMessage("```Factoid has been successfully added with the ID: " + AddId + "```").queue();
            } catch (SQLException var10) {
                System.out.println("There was an error with MySQL - >b add");
                System.out.println(var10);
                event.getTextChannel().sendMessage("``` Factoid has not been added : error: " + var10 + "```").queue();
            }

        }
    }

    private void FremoveCommand(MessageReceivedEvent event, String message) {
        message = message.substring(12, message.length());

        try {
            PreparedStatement st = Connection.db.prepareStatement("DELETE FROM `factoids` WHERE id=?");
            st.setString(1, message);
            st.executeUpdate();
            event.getTextChannel().sendMessage("```The factoid with the ID: " + message + " Has been successfully removed ```").queue();
        } catch (SQLException var4) {
            System.out.println("There was an error with MYSQL - >b remove");
            System.out.println(var4);
            event.getTextChannel().sendMessage("``` Factoid has not been removed : error: " + var4 + "```");
        }

    }

    private void countCommand(MessageReceivedEvent event, String message, String author) {
        message = message.substring(9, message.length());
        String[] parts = message.split(" <> ");
        String AddWord = parts[0].toLowerCase();
        String AddPlayer = parts[1];
        String AddAuthor = author;
        System.out.println(AddWord);
        System.out.println(AddPlayer);
        System.out.println(author);
        if (message.contains(" <> ")) {
            try {
                PreparedStatement st = Connection.db.prepareStatement("INSERT INTO `word_log` (`word`, `player`, `author`, `amount`) VALUES (?, ?, ?, ?);", 1);
                st.setString(1, AddWord);
                st.setString(2, AddPlayer);
                st.setString(3, AddAuthor);
                st.setString(4, "0");
                st.executeUpdate();
                ResultSet genkeys = st.getGeneratedKeys();
                genkeys.next();
                Integer AddId = genkeys.getInt(1);
                event.getTextChannel().sendMessage("```Count for the word: " + AddWord + " has been successfully added with the ID: " + AddId + "```").queue();
            } catch (SQLException var11) {
                System.out.println("There was an error with MySQL - >b count");
                System.out.println(var11);
                event.getTextChannel().sendMessage("``` Count has not been added : error: " + var11 + "```").queue();
            }
        }

    }

    private void hmtCommand(MessageReceivedEvent event, String message) {
        message = message.substring(7, message.length());
        String[] parts = message.split(" <> ");
        String Word = parts[0];
        String Player = parts[1];
        if (Player.length() >= 1) {
            try {
                PreparedStatement st = Connection.db.prepareStatement("SELECT * FROM `word_log` WHERE word=? AND player=?;");
                st.setString(1, Word);
                st.setString(2, Player);
                ResultSet rs = st.executeQuery();

                while(rs.next()) {
                    Integer amount = rs.getInt("amount");
                    String Cauthor = rs.getString("author");
                    Integer id = rs.getInt("id");
                    String Final = "The player: `" + Player + "` has said `" + Word + "` `" + amount + "` times! Author of the count is: `" + Cauthor + "` ID is: `" + id + "`";
                    event.getTextChannel().sendMessage(Final).queue();
                    System.out.println(Final);
                }
            } catch (SQLException var12) {
                System.out.println("There was an error with MySQL with hmt");
            }
        }

    }

    private void CremoveCommand(MessageReceivedEvent event, String message) {
        message = message.substring(12, message.length());

        try {
            PreparedStatement st = Connection.db.prepareStatement("DELETE FROM `word_log` WHERE id=?");
            st.setString(1, message);
            st.executeUpdate();
            event.getTextChannel().sendMessage("```The count with the ID: " + message + " Has been successfully removed ```").queue();
        } catch (SQLException var4) {
            System.out.println("There was an error with MYSQL - count");
            System.out.println(var4);
            event.getTextChannel().sendMessage("``` Factoid has not been removed : error: " + var4 + "```");
        }

    }
}

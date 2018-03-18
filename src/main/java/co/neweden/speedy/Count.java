package co.neweden.speedy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Count extends ListenerAdapter {
    public Count() {
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContent().toLowerCase();
        String nickname = event.getMember().getEffectiveName();
        String author = event.getAuthor().getName();
        if (!event.getJDA().getSelfUser().equals(event.getAuthor())) {
            if (nickname.startsWith("[MC] ") && nickname.length() >= 5) {
                nickname = nickname.substring(5, nickname.length());
            }

            System.out.println(author);
            String[] parts = message.split(" ");
            HashSet words = new HashSet();

            try {
                PreparedStatement st = Connection.db.prepareStatement("SELECT word FROM word_log WHERE player=?");
                st.setString(1, author);
                ResultSet rs = st.executeQuery();

                while(rs.next()) {
                    words.add(rs.getString("word"));
                }
            } catch (SQLException var13) {
                System.out.println("There was an error with MySQL with author update");
            }

            String[] var14 = parts;
            int var15 = parts.length;

            for(int var9 = 0; var9 < var15; ++var9) {
                String part = var14[var9];
                if (words.contains(part)) {
                    System.out.println(part);

                    try {
                        PreparedStatement st = Connection.db.prepareStatement("UPDATE word_log SET amount = amount + 1  WHERE word=? AND player=?");
                        st.setString(1, part);
                        st.setString(2, author);
                        st.executeUpdate();
                    } catch (SQLException var12) {
                        System.out.println("There was an error with MySQL with amount update");
                    }
                }
            }

        }
    }
}
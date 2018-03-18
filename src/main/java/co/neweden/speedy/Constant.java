package co.neweden.speedy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Constant {
    public static final String discordToken = token();

    public Constant() {
    }

    public static String token() {
        try {
            PreparedStatement st = Connection.db.prepareStatement("SELECT `key` FROM `keys` WHERE id=?");
            st.setInt(1, 1);
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getString("key") : null;
        } catch (SQLException var2) {
            System.out.println("There was an error with MySQL with Token Verification");
            return null;
        }
    }
}
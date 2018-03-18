package harryharry6.speedy;

import java.sql.DriverManager;
import java.sql.SQLException;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class Connection {
    public static java.sql.Connection db;

    public Connection() {
    }

    public static boolean loadDBConnection() {
        String host = "localhost";
        String port = "3306";
        String database = "database";
        if (host != null && port != null && database != null) {
            String url = String.format("jdbc:mysql://%s:%s/%s?autoReconnect=true", host, port, database);

            try {
                db = DriverManager.getConnection(url, "harryharry6", "harry");
            } catch (SQLException var5) {
                var5.printStackTrace();
                return false;
            }

            System.out.println("Connected to MySQL Database");
            return true;
        } else {
            System.out.println("No database found");
            return false;
        }
    }

    public static boolean setupDB() {
        try {
            db.createStatement().execute("CREATE TABLE IF NOT EXISTS `factoids` (\n            `id` INT NOT NULL AUTO_INCREMENT,\n  `factoid` VARCHAR(140) NOT NULL,\n  `type` VARCHAR(140) NOT NULL,\n  `response` VARCHAR(140) NOT NULL,\n  `author` VARCHAR(70) NOT NULL,\n    PRIMARY KEY (`id`));");
            System.out.println("MySQL Table Created/Found");
        } catch (SQLException var3) {
            System.out.println("Failed to create table");
            var3.printStackTrace();
            return false;
        }

        try {
            db.createStatement().execute("CREATE TABLE IF NOT EXISTS `word_log` (\n            `id` INT NOT NULL AUTO_INCREMENT,\n  `word` VARCHAR(140) NOT NULL,\n  `player` VARCHAR(70) NOT NULL,\n  `author` VARCHAR(70) NOT NULL,\n  `amount` INT NOT NULL,\n    PRIMARY KEY (`id`));");
            System.out.println("MySQL Table Created/Found");
        } catch (SQLException var2) {
            System.out.println("Failed to create table");
            var2.printStackTrace();
            return false;
        }

        try {
            db.createStatement().execute("CREATE TABLE IF NOT EXISTS `keys` (\n            `id` INT NOT NULL AUTO_INCREMENT,\n  `key` VARCHAR(140) NOT NULL,\n     PRIMARY KEY (`id`));");
            System.out.println("MySQL Table Created/Found");
            return true;
        } catch (SQLException var1) {
            System.out.println("Failed to create table");
            var1.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        loadDBConnection();
        setupDB();
        JDA discord = null;

        try {
            discord = (new JDABuilder(AccountType.BOT)).setToken(Constant.discordToken).buildBlocking();
        } catch (LoginException var3) {
            var3.printStackTrace();
        } catch (InterruptedException var4) {
            var4.printStackTrace();
        } catch (RateLimitedException var5) {
            var5.printStackTrace();
        }

        discord.addEventListener(new Object[]{new mcmessages()});
        discord.addEventListener(new Object[]{new Commands()});
        discord.addEventListener(new Object[]{new Count()});
    }
}

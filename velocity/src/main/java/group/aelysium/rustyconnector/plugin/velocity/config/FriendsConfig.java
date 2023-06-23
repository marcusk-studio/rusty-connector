package group.aelysium.rustyconnector.plugin.velocity.config;

import group.aelysium.rustyconnector.core.lib.exception.NoOutputException;
import group.aelysium.rustyconnector.core.lib.lang_messaging.Lang;
import group.aelysium.rustyconnector.plugin.velocity.PluginLogger;
import group.aelysium.rustyconnector.plugin.velocity.VelocityRustyConnector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FriendsConfig extends YAML {
    private static FriendsConfig config;
    private String rootFamily_name = "lobby";
    private Boolean rootFamily_catchDisconnectingPlayers = false;
    private List<String> scalar = new ArrayList<>();
    private List<String> staticF = new ArrayList<>();

    private String mysql_host = "";
    private int mysql_port = 3306;
    private String mysql_user = "root";
    private String mysql_password = "password";
    private String mysql_database = "RustyConnector";

    private FriendsConfig(File configPointer, String template) {
        super(configPointer, template);
    }

    /**
     * Get the current config.
     * @return The config.
     */
    public static FriendsConfig getConfig() {
        return config;
    }

    /**
     * Create a new config for the proxy, this will delete the old config.
     * @return The newly created config.
     */
    public static FriendsConfig newConfig(File configPointer, String template) {
        config = new FriendsConfig(configPointer, template);
        return FriendsConfig.getConfig();
    }

    /**
     * Delete all configs associated with this class.
     */
    public static void empty() {
        config = null;
    }

    public String getRootFamilyName() {
        return this.rootFamily_name;
    }
    public Boolean shouldRootFamilyCatchDisconnectingPlayers() {
        return this.rootFamily_catchDisconnectingPlayers;
    }

    public List<String> getScalarFamilies() {
        return this.scalar;
    }
    public List<String> getStaticFamilies() {
        return this.staticF;
    }

    public String getMysql_host() {
        return this.mysql_host;
    }
    public int getMysql_port() {
        return this.mysql_port;
    }
    public String getMysql_password() {
        return this.mysql_password;
    }
    public String getMysql_user() {
        return this.mysql_user;
    }
    public String getMysql_database() {
        return this.mysql_database;
    }

    @SuppressWarnings("unchecked")
    public void register() throws IllegalStateException, NoOutputException {
        PluginLogger logger = VelocityRustyConnector.getAPI().getLogger();

        // Families
        this.rootFamily_name = this.getNode(this.data,"root-family.name",String.class);
        this.rootFamily_catchDisconnectingPlayers = this.getNode(this.data,"root-family.catch-disconnecting-players",Boolean.class);
        try {
            this.scalar = (List<String>) (this.getNode(this.data,"scalar",List.class));
        } catch (Exception e) {
            throw new IllegalStateException("The node [scalar] in "+this.getName()+" is invalid! Make sure you are using the correct type of data!");
        }
        try {
            this.staticF = (List<String>) (this.getNode(this.data,"static",List.class));
        } catch (Exception e) {
            throw new IllegalStateException("The node [scalar] in "+this.getName()+" is invalid! Make sure you are using the correct type of data!");
        }

        this.scalar.forEach(familyName -> {
            if(familyName.length() > 32)
                throw new IllegalStateException("All family names must be under 32 characters long! `" + familyName + "` was " + familyName.length());
        });

        this.staticF.forEach(familyName -> {
            if(familyName.length() > 32)
                throw new IllegalStateException("All family names must be under 32 characters long! `" + familyName + "` was " + familyName.length());
        });

        if(this.checkForAll())
            throw new IllegalStateException("You can't name a family: `all`");

        if(this.doDuplicatesExist())
            throw new IllegalStateException("You can't have two families with the same name! This rule is regardless of what type the family is!");

        if(this.isRootFamilyDuplicated()) {
            Lang.BOXED_MESSAGE_COLORED.send(logger, Component.text(this.rootFamily_name + " was found duplicated in your family nodes. This is no longer supported. Instead, ONLY place the name of your root family in [root-family.name]. Ignoring..."), NamedTextColor.YELLOW);
            this.scalar.remove(this.rootFamily_name);
            this.staticF.remove(this.rootFamily_name);
        }


        if(this.staticF.isEmpty()) return;
        // MySQL

        this.mysql_host = this.getNode(this.data, "mysql.host", String.class);
        if (this.mysql_host.equals("")) throw new IllegalStateException("Please configure your MySQL settings.");

        this.mysql_port = this.getNode(this.data, "mysql.port", Integer.class);
        this.mysql_user = this.getNode(this.data, "mysql.user", String.class);
        this.mysql_password = this.getNode(this.data, "mysql.password", String.class);

        if (this.mysql_password.length() != 0 && this.mysql_password.length() < 16)
            throw new IllegalStateException("Your MySQL password is to short! For security purposes, please use a longer password! " + this.mysql_password.length() + " < 16");

        this.mysql_database = this.getNode(this.data, "mysql.database", String.class);
        if (this.mysql_database.equals(""))
            throw new IllegalStateException("You must pass a proper name for the database to use with MySQL!");
    }

    private boolean doDuplicatesExist() {
        return this.scalar.stream().filter(this.staticF::contains).toList().size() > 0;
    }

    private boolean isRootFamilyDuplicated() {
        return this.scalar.contains(this.rootFamily_name) ||
               this.staticF.contains(this.rootFamily_name);
    }

    private boolean checkForAll() {
        return this.rootFamily_name.equalsIgnoreCase("all") ||
               this.scalar.contains("all") ||
               this.staticF.contains("all");
    }
}

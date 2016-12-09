package com.manulaiko.weabot.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

/**
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Settings
{
    /**
     * Moderators list.
     */
    public static final HashSet<User> moderators = new HashSet<>();

    /**
     * Administrators list.
     */
    public static final HashSet<User> administrators = new HashSet<>();

    /**
     * Channels to save images list.
     */
    public static final HashMap<String, ArrayList<File>> saveImagesChannels = new HashMap<>();

    /**
     * Checks whether an user is administrator or moderator.
     *
     * @param user User to check.
     *
     * @return `true` if `user` is administrator or moderator, `false` if not.
     */
    public static boolean isAdminOrMod(User user)
    {
        for(User u : Settings.administrators) {
            if(u.getId().equalsIgnoreCase(user.getId())) {
                return true;
            }
        }

        for(User u : Settings.moderators) {
            if(u.getId().equalsIgnoreCase(user.getId())) {
                return true;
            }
        }

        return false;
    }
}

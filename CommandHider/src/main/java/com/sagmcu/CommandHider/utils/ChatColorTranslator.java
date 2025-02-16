package com.sagmcu.CommandHider.utils;

import java.util.regex.Matcher;

import net.md_5.bungee.api.ChatColor;
import java.util.regex.Pattern;

public class ChatColorTranslator {

    public static String translate(String input) {
        input = translateHexColors(input);
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    private static String translateHexColors(String input) {
        String regex = "(#(?:[0-9a-fA-F]{6}))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // Replace hex color codes with ChatColor format
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(result, ChatColor.of(hex).toString());
        }
        matcher.appendTail(result);

        return result.toString();
    }
}

package Harsho.Hype.Bot;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    public static final Map<Long, String> PREFIXES = new HashMap<>();
    public static final Map<Long, Long> logChannel = new HashMap<>();
    public static final Map<Long, Boolean> memberJoin = new HashMap<>();
    public static final Map<Long, Boolean> memberLeave = new HashMap<>();

    public static void print() {
        System.out.println(PREFIXES);
        System.out.println(logChannel);
        System.out.println(memberJoin);
        System.out.println(memberLeave);
    }
}

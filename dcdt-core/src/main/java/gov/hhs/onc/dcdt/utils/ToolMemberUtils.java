package gov.hhs.onc.dcdt.utils;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public abstract class ToolMemberUtils {
    public static List<Member> filter(int mods, Member ... members) {
        return filter(mods, ToolArrayUtils.asList(members));
    }
    
    public static List<Member> filter(int mods, Iterable<Member> members) {
        List<Member> membersFiltered = new ArrayList<>();

        for (Member member : members) {
            if (hasModifiers(member, mods)) {
                membersFiltered.add(member);
            }
        }

        return membersFiltered;
    }

    public static boolean hasPrivateAccess(Member member) {
        return hasModifiers(member, Modifier.PRIVATE);
    }
    
    public static boolean hasProtectedAccess(Member member) {
        return hasModifiers(member, Modifier.PROTECTED);
    }
    
    public static boolean hasPackageAccess(Member member) {
        return hasModifiers(member, Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE);
    }
    
    public static boolean hasPublicAccess(Member member) {
        return hasModifiers(member, Modifier.PUBLIC);
    }
    
    public static boolean hasModifiers(Member member, int mods) {
        return ToolReflectionUtils.hasModifiers(mods, member.getModifiers());
    }
}

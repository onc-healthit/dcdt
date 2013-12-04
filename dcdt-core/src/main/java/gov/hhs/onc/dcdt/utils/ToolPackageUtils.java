package gov.hhs.onc.dcdt.utils;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

public abstract class ToolPackageUtils {
    public final static Package TOOL_PKG = getParent(ToolPackageUtils.class.getPackage());

    public static Package getParent(Package pkg) {
        return fromSubPackageNames(ToolListUtils.removeLast(getSubPackageNames(pkg)));
    }

    public static boolean isInToolPackage(Object obj) {
        return isInToolPackage(obj.getClass());
    }

    public static boolean isInToolPackage(Class<?> clazz) {
        return isInPackage(TOOL_PKG, clazz);
    }

    public static boolean isInPackage(Package pkg, Object obj) {
        return isInPackage(pkg, obj.getClass());
    }

    public static boolean isInPackage(Package pkg, Class<?> clazz) {
        return isSubPackage(pkg, clazz.getPackage());
    }

    public static boolean isSubPackage(Package pkg1, Package pkg2) {
        return ToolListUtils.startsWith(getSubPackages(pkg1), getSubPackages(pkg2));
    }

    public static Package fromSubPackages(Package[] subPkgs) {
        StrBuilder pkgNameBuilder = new StrBuilder();

        for (Package subPkg : subPkgs) {
            pkgNameBuilder.appendSeparator(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            pkgNameBuilder.append(getName(subPkg));
        }

        return Package.getPackage(pkgNameBuilder.toString());
    }

    public static Package fromSubPackages(Iterable<Package> subPkgs) {
        StrBuilder pkgNameBuilder = new StrBuilder();
        Iterator<Package> subPkgsIter = subPkgs.iterator();
        Package subPkg;

        while(subPkgsIter.hasNext() && ((subPkg = subPkgsIter.next()) != null)) {
            pkgNameBuilder.appendSeparator(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            pkgNameBuilder.append(getName(subPkg));
        }

        return Package.getPackage(pkgNameBuilder.toString());
    }

    public static Package fromSubPackageNames(String[] subPkgNames) {
        return Package.getPackage(ToolStringUtils.joinDelimit(subPkgNames, ClassUtils.PACKAGE_SEPARATOR));
    }

    public static Package fromSubPackageNames(Iterable<String> subPkgNames) {
        return Package.getPackage(ToolStringUtils.joinDelimit(subPkgNames, ClassUtils.PACKAGE_SEPARATOR));
    }

    public static List<Package> getSubPackages(Object obj) {
        return getSubPackages(obj.getClass());
    }

    public static List<Package> getSubPackages(Class<?> clazz) {
        return getSubPackages(clazz.getPackage());
    }

    public static List<Package> getSubPackages(Package pkg) {
        List<String> subPkgNames = getSubPackageNames(pkg);
        List<Package> subPkgs = new ArrayList<>(subPkgNames.size());

        for (String subPkgName : subPkgNames) {
            subPkgs.add(Package.getPackage(subPkgName));
        }

        return subPkgs;
    }

    public static List<String> getSubPackageNames(Package pkg) {
        return new ArrayList<>(ToolArrayUtils.asList(StringUtils.split(getName(pkg), ClassUtils.PACKAGE_SEPARATOR_CHAR)));
    }

    public static String getName(Object obj) {
        return getName(obj, null);
    }

    public static String getName(Object obj, String defaultIfNull) {
        return ClassUtils.getPackageName(obj, defaultIfNull);
    }

    public static String getName(Class<?> clazz) {
        return getName(clazz, null);
    }

    public static String getName(Class<?> clazz, String defaultIfNull) {
        return (clazz != null) ? getName(clazz.getPackage(), defaultIfNull) : defaultIfNull;
    }

    public static String getName(Package pkg) {
        return getName(pkg, null);
    }

    public static String getName(Package pkg, String defaultIfNull) {
        return (pkg != null) ? pkg.getName() : defaultIfNull;
    }
}

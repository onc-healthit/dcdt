package gov.hhs.onc.dcdt.io.utils;

import gov.hhs.onc.dcdt.io.FileTypeId;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

public abstract class ToolFileUtils {
    public final static LinkOption[] LINK_OPTS_FOLLOW = new LinkOption[0];
    public final static LinkOption[] LINK_OPTS_NO_FOLLOW = ArrayUtils.toArray(LinkOption.NOFOLLOW_LINKS);

    public static boolean isType(FileTypeId fileType, @Nullable Path ... paths) {
        return isType(true, fileType, paths);
    }

    public static boolean isType(boolean followLinks, FileTypeId fileType, @Nullable Path ... paths) {
        return isType(followLinks, fileType, ToolArrayUtils.asList(paths));
    }

    public static boolean isType(FileTypeId fileType, @Nullable Iterable<Path> paths) {
        return isType(true, fileType, paths);
    }

    public static boolean isType(boolean followLinks, FileTypeId fileType, @Nullable Iterable<Path> paths) {
        switch (fileType) {
            case FILE:
                return isFile(followLinks, paths);

            case DIRECTORY:
                return isDirectory(followLinks, paths);

            case SYM_LINK:
                return isLink(paths);

            case OTHER:
                return isOther(followLinks, paths);
        }

        return false;
    }

    public static boolean isOther(@Nullable Path ... paths) {
        return isOther(true, paths);
    }

    public static boolean isOther(boolean followLinks, @Nullable Path ... paths) {
        return isOther(followLinks, ToolArrayUtils.asList(paths));
    }

    public static boolean isOther(@Nullable Iterable<Path> paths) {
        return isOther(true, paths);
    }

    public static boolean isOther(boolean followLinks, @Nullable Iterable<Path> paths) {
        return !isFile(followLinks, paths) && !isDirectory(followLinks, paths) && !isLink(paths);
    }

    public static boolean isLink(@Nullable Path ... paths) {
        return isLink(ToolArrayUtils.asList(paths));
    }

    public static boolean isLink(@Nullable Iterable<Path> paths) {
        if (paths == null) {
            return false;
        }

        for (Path path : paths) {
            if (!Files.isSymbolicLink(path)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isDirectory(@Nullable Path ... paths) {
        return isDirectory(true, paths);
    }

    public static boolean isDirectory(boolean followLinks, @Nullable Path ... paths) {
        return isDirectory(followLinks, ToolArrayUtils.asList(paths));
    }

    public static boolean isDirectory(@Nullable Iterable<Path> paths) {
        return isDirectory(true, paths);
    }

    public static boolean isDirectory(boolean followLinks, @Nullable Iterable<Path> paths) {
        if (paths == null) {
            return false;
        }

        for (Path path : paths) {
            if (!Files.isDirectory(path, getLinkOptions(followLinks))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isFile(@Nullable Path ... paths) {
        return isFile(true, paths);
    }

    public static boolean isFile(boolean followLinks, @Nullable Path ... paths) {
        return isFile(followLinks, ToolArrayUtils.asList(paths));
    }

    public static boolean isFile(@Nullable Iterable<Path> paths) {
        return isFile(true, paths);
    }

    public static boolean isFile(boolean followLinks, @Nullable Iterable<Path> paths) {
        if (paths == null) {
            return false;
        }

        for (Path path : paths) {
            if (!Files.isRegularFile(path, getLinkOptions(followLinks))) {
                return false;
            }
        }

        return true;
    }

    public static boolean exists(@Nullable Path ... paths) {
        return exists(true, paths);
    }

    public static boolean exists(boolean followLinks, @Nullable Path ... paths) {
        return exists(followLinks, ToolArrayUtils.asList(paths));
    }

    public static boolean exists(@Nullable Iterable<Path> paths) {
        return exists(true, paths);
    }

    public static boolean exists(boolean followLinks, @Nullable Iterable<Path> paths) {
        if (paths == null) {
            return false;
        }

        for (Path path : paths) {
            if (!Files.exists(path, getLinkOptions(followLinks))) {
                return false;
            }
        }

        return true;
    }

    public static List<Path> toPaths(@Nullable Iterable<?> objs) {
        List<Path> paths = new ArrayList<>();

        if (objs != null) {
            for (Object obj : objs) {
                paths.add(toPath(obj));
            }
        }

        return paths;
    }

    @Nullable
    public static Path toPath(@Nullable Object obj) {
        if (obj == null) {
            return null;
        } else {
            Class<?> objClass = ToolClassUtils.getClass(obj);

            if (ToolClassUtils.isAssignable(Path.class, objClass)) {
                return (Path) obj;
            } else if (ToolClassUtils.isAssignable(File.class, objClass)) {
                return ((File) obj).toPath();
            } else {
                return Paths.get(ObjectUtils.toString(obj, null));
            }
        }
    }

    public static LinkOption[] getLinkOptions(boolean followLinks) {
        return followLinks ? LINK_OPTS_FOLLOW : LINK_OPTS_NO_FOLLOW;
    }
}

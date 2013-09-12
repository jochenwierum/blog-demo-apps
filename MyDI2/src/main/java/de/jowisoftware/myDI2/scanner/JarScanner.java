package de.jowisoftware.myDI2.scanner;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.jowisoftware.myDI2.annotations.Resource;

public class JarScanner {
    private final File rootFile;
    private final String[] packages;

    public JarScanner(final File rootFile, final String[] packages) {
        this.rootFile = rootFile;

        final List<String> zipPathes = new LinkedList<>();
        for (final String packageName : packages) {
            zipPathes.add(packageName.replaceAll("\\.", "/") + "/");
        }
        this.packages = zipPathes.toArray(new String[zipPathes.size()]);
    }

    public void scan(final List<Class<?>> classes) {
        try (ZipFile jar = new ZipFile(rootFile)) {

        final Enumeration<? extends ZipEntry> zipEntries = jar.entries();

        for (ZipEntry entry = zipEntries.nextElement(); zipEntries
                .hasMoreElements(); entry = zipEntries.nextElement()) {

                for (final String prefix : packages) {
                    final String className = entry.getName();
                    if (className.startsWith(prefix)
                            && className.endsWith(".class")) {
                        addFile(className, classes);
                        break;
                    }
                }
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addFile(final String name, final List<Class<?>> classes) {
        final String className = name.substring(0, name.length() - 6)
                .replaceAll("/", ".");
        final Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (clazz.getAnnotation(Resource.class) != null) {
            classes.add(clazz);
        }
    }
}

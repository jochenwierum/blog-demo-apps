package de.jowisoftware.myDI2.scanner;

import java.io.File;
import java.util.List;

import de.jowisoftware.myDI2.annotations.Resource;

public class FileScanner {
    private final File rootFile;
    private final String[] packages;

    public FileScanner(final File rootFile, final String[] packages) {
        this.rootFile = rootFile;
        this.packages = packages;
    }

    public void scan(final List<Class<?>> classes) {
        for (final String packageName : packages) {
            final File packageDir = new File(rootFile, packageName.replace('.',
                    File.separatorChar));

            if (packageDir.exists()) {
                recursiveScanDir(packageDir, packageName, classes);
            }
        }
    }

    private void recursiveScanDir(final File packageDir,
            final String packageName, final List<Class<?>> classes) {
        for (final File file : packageDir.listFiles()) {
            final String filename = file.getName();

            if (file.isDirectory()) {
                recursiveScanDir(file, packageName + "." + filename, classes);
            } else {
                if (file.isFile() && filename.endsWith(".class")) {
                    final String fqcn = packageName + "."
                            + filename.substring(0, filename.length() - 6);
                    analyzeClass(classes, fqcn);
                }
            }
        }
    }

    private void analyzeClass(final List<Class<?>> classes, final String fqcn) {
        try {
            final Class<?> clazz = Class.forName(fqcn);

            if (clazz.getAnnotation(Resource.class) != null) {
                classes.add(clazz);
            }
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}

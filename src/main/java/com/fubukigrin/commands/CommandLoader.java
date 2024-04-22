package com.fubukigrin.commands;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

// import com.fubukigrin.commands.generic.InitGeneric;
// import com.fubukigrin.commands.osu.InitOsu;

public class CommandLoader {

    // this gonna use in config file but for now...
    public final static String[] AllowedCommands = {
            "generic",
            "osu"
    };

    private static String toTitleCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private static boolean dynamicLoad(URLClassLoader cl, String command) {
        try {
            @SuppressWarnings("rawtypes")
            Class c = cl.loadClass(
                    "com.fubukigrin.commands." + command + ".Init" + toTitleCase(command));

            @SuppressWarnings("unchecked")
            Method m = c.getMethod("load", new Class[] {});
            m.invoke(null);

            return true;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            try {
                cl.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static void main() {
        URLClassLoader cl;
        File file = new File("commands\\");
        try {
            URL url = file.toURI().toURL();
            URL[] urls = new URL[] { url };
            cl = new URLClassLoader(urls);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        for (String command : AllowedCommands) {
            boolean success = dynamicLoad(cl, command);

            if (!success) {
                System.out.println("!!! Command " + command + " failed to load !!!");
            }
        }

        System.out.println("!!! All commands loaded !!!");
    }
}

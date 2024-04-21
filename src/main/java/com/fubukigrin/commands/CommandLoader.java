package com.fubukigrin.commands;

import com.fubukigrin.commands.generic.InitGeneric;
import com.fubukigrin.commands.osu.InitOsu;

public class CommandLoader {

    public static void main() {
        InitGeneric.load();
        InitOsu.load();
        System.out.println("!!! All commands loaded !!!");
    }
}

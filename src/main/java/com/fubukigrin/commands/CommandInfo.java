package com.fubukigrin.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {
    String NameId();

    String CommandClass();

    String Description();

    String[] AltNames();

    Class<?>[] Args() default OptionData.class;
}

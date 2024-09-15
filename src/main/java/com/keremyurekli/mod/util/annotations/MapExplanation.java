package com.keremyurekli.mod.util.annotations;

public @interface MapExplanation {

    String key() default "key";

    String value() default "value";

    String extra() default "extra";
}


package cz.habarta.typescript.generator;

import cz.habarta.typescript.generator.emitter.*;
import cz.habarta.typescript.generator.parser.*;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;


public class TypeScriptGenerator {

    public static JavaToTypescriptTypeConverter generateTypeScript(List<? extends Class<?>> classes, Settings settings, File file) {
        try {
            return generateTypeScript(classes, settings, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static JavaToTypescriptTypeConverter generateTypeScript(List<? extends Class<?>> classes, Settings settings, OutputStream output) {
        final Logger logger = Logger.getGlobal();
        final ModelCompiler compiler = new ModelCompiler(logger, settings);

        final ModelParser modelParser;
        if (settings.jsonLibrary == JsonLibrary.jackson2) {
            modelParser = new Jackson2Parser(logger, settings, compiler);
        } else {
            modelParser = new Jackson1Parser(logger, settings, compiler);
        }
        final Model model = modelParser.parseModel(classes);

        final TsModel tsModel = compiler.javaToTypescript(model);

        Emitter.emit(logger, settings, output, tsModel);
        return compiler.getJavaToTypescriptTypeParser();
    }

}
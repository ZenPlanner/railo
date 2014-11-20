package com.zenplanner;

import railo.runtime.Mapping;
import railo.runtime.SourceFile;
import railo.transformer.bytecode.Page;
import railo.transformer.cfml.tag.CFMLTransformer;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class App {

    private static Map<String,Set<String>> map = new TreeMap<String, Set<String>>();

    public static void main(String[] args) throws Exception {
        String rootPath = args[0];

        File root = new File(rootPath);

        MockConfig config = new MockConfig();
        config.setMappings(new Mapping[]{
                new MockMapping(root)
        });

        CFMLTransformer cfmlTransformer = new CFMLTransformer();
        File file = new File(root, "zenplanner\\qq\\pricing\\schedules\\index.cfm");
        MockResource physicalFile = new MockResource(file);
        SourceFile source = new MockSourceFile(root, physicalFile);

        Page page = cfmlTransformer.transform(config, source, config.getTLDs(), config.getFLDs());
        Set<String> refs = new HashSet<String>();
        Map<String,Set<String>> map = new TreeMap<String, Set<String>>();
        map.put(file.getName(), refs);
        new StatementScanner(refs).process(page);
    }

}

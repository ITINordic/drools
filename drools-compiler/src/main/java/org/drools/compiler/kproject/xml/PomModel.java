package org.drools.compiler.kproject.xml;

import org.kie.api.builder.ReleaseId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PomModel {

    private static final String NATIVE_MAVEN_PARSER_CLASS = "org.kie.scanner.MavenPomModelGenerator";

    private static final Logger log = LoggerFactory.getLogger(PomModel.class);

    private ReleaseId releaseId;
    private ReleaseId parentReleaseId;
    private Set<ReleaseId> dependencies = new HashSet<ReleaseId>();


    public ReleaseId getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(ReleaseId releaseId) {
        this.releaseId = releaseId;
    }

    public ReleaseId getParentReleaseId() {
        return parentReleaseId;
    }

    public void setParentReleaseId(ReleaseId parentReleaseId) {
        this.parentReleaseId = parentReleaseId;
    }

    public Collection<ReleaseId> getDependencies() {
        return dependencies;
    }

    public void addDependency(ReleaseId dependency) {
        this.dependencies.add(dependency);
    }

    public static class Parser {

        private static class PomModelGeneratorHolder {
            private static PomModelGenerator pomModelGenerator;

            static {
                try {
                    pomModelGenerator = (PomModelGenerator) Class.forName(NATIVE_MAVEN_PARSER_CLASS).newInstance();
                } catch (Exception e) {
                    pomModelGenerator = new DefaultPomModelGenerator();
                }
            }
        }

        public static PomModel parse(String path, InputStream is) {
            try {
                return PomModelGeneratorHolder.pomModelGenerator.parse(path, is);
            } catch (Exception e) {
                if (PomModelGeneratorHolder.pomModelGenerator.getClass().getName().equals(NATIVE_MAVEN_PARSER_CLASS) && isOpen(is)) {
                    log.warn("Error generated by the maven pom parser, falling back to the internal one", e);
                    return MinimalPomParser.parse(path, is);
                }
                if (e instanceof RuntimeException) {
                    throw (RuntimeException)e;
                } else {
                    throw new RuntimeException(e);
                }
            }
        }

        private static boolean isOpen(InputStream is) {
            try {
                return is.available() > 0;
            } catch (IOException ioe) {
                return false;
            }
        }
    }

    private static class DefaultPomModelGenerator implements PomModelGenerator {
        @Override
        public PomModel parse(String path, InputStream is) {
            return MinimalPomParser.parse(path, is);
        }
    }
}

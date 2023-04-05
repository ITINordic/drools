/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.reliability;

import org.drools.core.common.ReteEvaluator;
import org.infinispan.Cache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.marshall.JavaSerializationMarshaller;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.globalstate.ConfigurationStorage;
import org.infinispan.manager.DefaultCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static org.drools.reliability.CacheManager.createCacheId;
import static org.drools.reliability.CacheManagerFactory.*;

class EmbeddedCacheManager implements CacheManager {

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedCacheManager.class);

    private static final String[] ALLOWED_PACKAGES;

    static {
        List<String> allowList = new ArrayList<>();
        allowList.add("org.kie.*");
        allowList.add("org.drools.*");
        allowList.add("java.*");
        String additionalPkgs = System.getProperty(RELIABILITY_CACHE_ALLOWED_PACKAGES);
        if (additionalPkgs != null) {
            Arrays.stream(additionalPkgs.split(",")).forEach(p -> allowList.add(p + ".*"));
        }
        ALLOWED_PACKAGES = allowList.toArray(new String[allowList.size()]);
    }

    static final EmbeddedCacheManager INSTANCE = new EmbeddedCacheManager();

    private DefaultCacheManager embeddedCacheManager;
    private Configuration cacheConfiguration;

    public static final String GLOBAL_STATE_DIR = "global/state";
    public static final String CACHE_DIR = "cache";

    private EmbeddedCacheManager() {}

    @Override
    public void initCacheManager() {
        LOG.info("Using Embedded Cache Manager");

        // Set up a clustered Cache Manager.
        GlobalConfigurationBuilder global = new GlobalConfigurationBuilder();
        global.serialization()
              .marshaller(new JavaSerializationMarshaller())
              .allowList()
              .addRegexps(ALLOWED_PACKAGES);
        global.globalState()
              .enable()
              .persistentLocation(GLOBAL_STATE_DIR)
              .configurationStorage(ConfigurationStorage.OVERLAY);

        // Initialize the default Cache Manager.
        embeddedCacheManager = new DefaultCacheManager(global.build());

        // Create a distributed cache with synchronous replication.
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.persistence().passivation(false)
               .addSoftIndexFileStore()
               .shared(false)
               .dataLocation(CACHE_DIR + "/data")
               .indexLocation(CACHE_DIR + "/index");
        builder.clustering()
               .cacheMode(CacheMode.LOCAL);
        cacheConfiguration = builder.build();
    }

    @Override
    public <k, V> Cache<k, V> getOrCreateCacheForSession(ReteEvaluator reteEvaluator, String cacheName) {
        return embeddedCacheManager.administration().getOrCreateCache(createCacheId(reteEvaluator, cacheName), cacheConfiguration);
    }

    @Override
    public void close() {
        embeddedCacheManager.stop();
    }

    @Override
    public void removeCache(String cacheName) {
        if (embeddedCacheManager.cacheExists(cacheName)) {
            embeddedCacheManager.removeCache(cacheName);
        }
    }

    @Override
    public void removeCachesBySessionId(String sessionId) {
        embeddedCacheManager.getCacheNames()
                            .stream()
                            .filter(cacheName -> cacheName.startsWith(SESSION_CACHE_PREFIX + sessionId + DELIMITER))
                            .forEach(this::removeCache);
    }

    @Override
    public void removeAllSessionCaches() {
        embeddedCacheManager.getCacheNames()
                            .stream()
                            .filter(cacheName -> cacheName.startsWith(SESSION_CACHE_PREFIX))
                            .forEach(this::removeCache);
    }

    @Override
    public Set<String> getCacheNames() {
        return embeddedCacheManager.getCacheNames();
    }

    @Override
    public void setRemoteCacheManager(RemoteCacheManager remoteCacheManager) {
        throw new UnsupportedOperationException("setRemoteCacheManager is not supported in " + this.getClass());
    }

    //--- test purpose

    @Override
    public void restart() {
        // JVM crashed
        embeddedCacheManager.stop();
        embeddedCacheManager = null;
        cacheConfiguration = null;

        // Reboot
        initCacheManager();
    }

    @Override
    public void restartWithCleanUp() {
        // JVM down
        embeddedCacheManager.stop();
        embeddedCacheManager = null;
        cacheConfiguration = null;

        // Remove GlobalState and FileStore
        cleanUpGlobalStateAndFileStore();

        // Reboot
        initCacheManager();
    }

    @Override
    public void setEmbeddedCacheManager(DefaultCacheManager embeddedCacheManager) {
        if (this.embeddedCacheManager != null) {
            this.embeddedCacheManager.stop();
        }
        this.embeddedCacheManager = embeddedCacheManager;
    }

    // test purpose to remove GlobalState and FileStore
    static void cleanUpGlobalStateAndFileStore() {
        try {
            Path path = Paths.get(GLOBAL_STATE_DIR);
            if (Files.exists(path)) {
                try (Stream<Path> walk = Files.walk(path)) {
                    walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public org.infinispan.client.hotrod.configuration.ConfigurationBuilder provideAdditionalRemoteConfigurationBuilder() {
        throw new UnsupportedOperationException("provideRemoteConfigurationBuilder is not supported in " + this.getClass());
    }

    @Override
    public boolean isRemote() {
        return false;
    }
}

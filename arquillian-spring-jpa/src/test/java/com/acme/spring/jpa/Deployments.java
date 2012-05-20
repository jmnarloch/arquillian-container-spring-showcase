/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.spring.jpa;

import com.acme.spring.jpa.domain.Stock;
import com.acme.spring.jpa.repository.StockRepository;
import com.acme.spring.jpa.repository.impl.JpaStockRepository;
import com.acme.spring.jpa.service.StockService;
import com.acme.spring.jpa.service.impl.DefaultStockService;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>A helper class for creating the tests deployments.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public final class Deployments {

    /**
     * <p>Creates new instance of {@link Deployments} class.</p>
     *
     * <p>Private constructor prevents from instantiation outside of this class.</p>
     */
    private Deployments() {
        // empty constructor
    }

    /**
     * <p>Creates new tests deployment.</p>
     *
     * @return the create archive
     */
    public static WebArchive createDeployment() {

        return ShrinkWrap.create(WebArchive.class, "spring-test.war")
                .addClasses(Stock.class, StockRepository.class, StockService.class,
                        JpaStockRepository.class, DefaultStockService.class, JpaTestHelper.class)
                .addAsWebInfResource("jbossas-ds.xml")
                .addAsWebInfResource("web.xml")
                .addAsResource("applicationContext-repository.xml")
                .addAsResource("applicationContext-service.xml")
                .addAsResource("applicationContext.xml")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("create.sql")
                .addAsResource("delete.sql")
                .addAsResource("insert.sql")
                .addAsLibraries(springDependencies());
    }

    /**
     * <p>Retrieves the dependencies.</p>
     *
     * @return the array of the dependencies
     */
    public static File[] springDependencies() {

        ArrayList<File> files = new ArrayList<File>();

        files.addAll(resolveDependencies("org.springframework:spring-context:3.1.1.RELEASE"));
        files.addAll(resolveDependencies("org.springframework:spring-orm:3.1.1.RELEASE"));
        files.addAll(resolveDependencies("org.springframework:spring-tx:3.1.1.RELEASE"));
        files.addAll(resolveDependencies("org.hibernate:hibernate-core:3.6.0.Final"));
        files.addAll(resolveDependencies("org.hibernate:hibernate-annotations:3.4.0.GA"));
        files.addAll(resolveDependencies("javassist:javassist:3.6.0.GA"));
        files.addAll(resolveDependencies("com.h2database:h2:1.3.166"));

        return files.toArray(new File[files.size()]);
    }

    /**
     * <p>Resolves the given artifact by it's name with help of maven build system.</p>
     *
     * @param artifactName the fully qualified artifact name
     *
     * @return the resolved files
     */
    public static List<File> resolveDependencies(String artifactName) {
        MavenDependencyResolver mvnResolver = DependencyResolvers.use(MavenDependencyResolver.class);

        mvnResolver.loadMetadataFromPom("pom.xml");

        return Arrays.asList(mvnResolver.artifacts(artifactName).resolveAsFiles());
    }
}

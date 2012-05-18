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
package com.acme.spring.inject;

import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * <p>A helper class for executing scripts in database.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public final class JDBCTestHelper {

    /**
     * <p>Creates new instance of {@link JDBCTestHelper} class.</p>
     *
     * <p>Private constructor prevents from instantiation outside of this class.</p>
     */
    private JDBCTestHelper() {
        // empty constructor
    }

    /**
     * <p>Executes the give script.</p>
     *
     * @param jdbcTemplate the jdbc template
     * @param fileName     the file name
     *
     * @throws IOException if any error occurs
     */
    public static void runScript(JdbcTemplate jdbcTemplate, String fileName) throws IOException {

        // retrieves the resource from class path
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));

        // loads the entire file
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = inputReader.readLine()) != null) {

            if (!line.startsWith("--")) {
                stringBuilder.append(line);
            }
        }

        // splits the commands by semicolon
        String[] commands = stringBuilder.toString().split(";");

        for (String command : commands) {

            jdbcTemplate.execute(command);
        }
    }
}

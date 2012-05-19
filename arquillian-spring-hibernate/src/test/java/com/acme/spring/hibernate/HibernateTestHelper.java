package com.acme.spring.hibernate;

import com.acme.spring.hibernate.domain.Stock;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>A helper class for performing operations on hibernate session.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public final class HibernateTestHelper {

    /**
     * <p>Creates new instance of {@link HibernateTestHelper} class.</p>
     *
     * <p>Private constructor prevents from instantiation outside of this class.</p>
     */
    private HibernateTestHelper() {
        // empty constructor
    }

    /**
     * <p>Executes a sql script.</p>
     *
     * @param session the hibernate session
     * @param fileName     the file name
     *
     * @throws java.io.IOException if any error occurs
     */
    public static void runScript(Session session, String fileName) throws IOException {

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

        for (final String command : commands) {

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {

                    connection.prepareStatement(command).execute();
                }
            });
        }
    }

    /**
     * <p>Retrieves all Stocks stored in database.</p>
     *
     * @param session the hibernate session to use
     *
     * @return list of stocks retrieved from database
     */
    public static List<Stock> retrieveAllStocks(Session session) {

        return session.createQuery("from Stock").list();
    }
}

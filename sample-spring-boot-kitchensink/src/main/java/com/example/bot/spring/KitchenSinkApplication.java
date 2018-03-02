/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class KitchenSinkApplication {
    static Path downloadedContentDir;
    
    //@Autowired
    //private static DataSource dataSource;

    public static void main(String[] args) throws IOException {
    	try {
    		downloadedContentDir = Files.createDirectory(FileSystems.getDefault().getPath("line-bot"));
    	} catch (FileAlreadyExistsException ignore) {}
        SpringApplication.run(KitchenSinkApplication.class, args);
    }

    public static Connection getConnection() throws URISyntaxException, SQLException {
    	//return dataSource.getConnection();
URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    	/*String dbUrl = System.getenv("JDBC_DATABASE_URL");
    	Connection connection = DriverManager.getConnection(dbUrl);
    	try (Statement stmt = connection.createStatement()) {
    		stmt.execute("CREATE TABLE IF NOT EXISTS 'birthdays' (name text, date text, lastWished numeric, PRIMARY KEY (name))");
    	}
    	return connection; */
    }
}

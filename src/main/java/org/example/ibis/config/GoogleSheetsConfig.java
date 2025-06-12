package org.example.ibis.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleSheetsConfig {

    @Bean
    public Sheets sheetsService() throws GeneralSecurityException, IOException {
        // Cargar el archivo JSON desde /resources
        InputStream credentialsStream = new ClassPathResource("src/main/resources/credentials.json").getInputStream();

    GoogleCredentials credentials = GoogleCredentials
        .fromStream(
            new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("credentials.json")
            )
        )
    .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));
        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("Sistema de Pagos IBIS")
                .build();
    }
}

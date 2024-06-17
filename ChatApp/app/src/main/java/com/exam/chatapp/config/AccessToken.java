package com.exam.chatapp.config;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AccessToken {
    private static final String firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging";

    public String getAccessToken() {
        try {
            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"chat-app-for-final-ae33d\",\n" +
                    "  \"private_key_id\": \"581a4b6a4d2f36284b059d12159759813d2ec532\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDCUCCxm/qMVncZ\\nCleMkrbgIPkPaBmsuSi9ZnDsIjNlQDqjzfaYm0pD1dx6tqHk+GNoBd145DoJFaaX\\n3jT+unCF3emqwtvv9vCDK8XRtwW3wY8gQ9Qt3OL1Gy8aBzsEF2LxXoDAyBfC+vx9\\nWZ9oyHyqKLOdLtixUn9r5yXE1Codj2W5KUFM24bgZTGFrRSLCIatdVmfmQfbcQSc\\n+y6cBCIj87v252XTNCIdO/QlTs1Kr81CzV8KRYT30V8uwi8Q8nNDpb84V7urTCYM\\nMGFbbW5mArQI2hQsIqVV05IxiyVb5naGW+af5dWEUDfpQU4jkux05sENPlxwHRXh\\n/gLcW0OrAgMBAAECggEAEwgsG5lOcf5qDd7QcmlQViWEn3ivwxYjomFtQAOdixY+\\naBrdQ1c6b+h7ft1yf+3ANskURyn7cTZYqdlO0YBLUyFIKLyLNDa4mfBooMDPY48m\\nyJ88If3XkPBHrcH1itILk96Qt9EhWqygnFpx3Z59b2ErLoMPIFcXOeda8KtjMJ/W\\nO4sqtSnpiT8rRhdoRVcsjM/+GyONnjMCn2fZXMPfjZKtHZyQ5gbEFOoMqoKtkF74\\n138KlASaVThN48SnEJAJJafIMIx0FT8y+wycpcPS4PptQixsfKaA6P8+ivcua8Tg\\n8XGUSc2bb/nR+pwxmtVmmjNIgFK+Jeux4nYGcg5bmQKBgQDnUuKvmEieovrpe39w\\n6xNUuYprYjuUMnY1xLHN+t+SA7B1YW/HOCRXWwsv4oFhzazKkTCnJnFuZn3Pq4dJ\\nEg/UIwoaDNf+FPBaSHS3Da266Yy02oWMg7ruk2//y3V/WBTiWJG6I9iKEnuRgHgA\\nWoF6G20v896wSaOzSbbRso39DQKBgQDXCog0ShrqWOfEnpcmw/42dcAkExqo6XF2\\nfZO53Ch/oxrtjoAsexcL0XW9GY0Wf6mWZYXSxI3XuKoFADy9WQN+4B9M8En+r6Hb\\n3elhtEQAqjuZxytOTh0StNKzSu+qcZnLws5zqHG/MpupOUfMujtifhc7DOGMTF4x\\nkvo5amDFlwKBgFt9LKrGT/JuA07UyVKfpsWbnGNcNrQmQRqC8M/GqSXWz7OdFdh1\\nG+pw17vYmdNUxUJYqiVxzusbHidEb/02Z4w/T39+FO8o2ObcQU0k29FRDZV519f7\\nQbOaHHHOxmnXLGtZHEnCY6I4LSce6Fc7dHoW3thYW3rdpSEDxoqpU/8BAoGARj6K\\nf4o1gJfKHdAwT9l2ERQlz6Lt8n9PMQWUXAIuuILvKOqDtAQBYzro2vydudu56gb/\\nz/mk2/LWlwucy71oBunvT5KumSOGG+xBcEkSbaUuEu2cl9esZ4/pw+eWona5ZnYc\\nOmurvGkrr/umGJo9C89kh0WN5+aKXmdElbzKH8ECgYAdsp5SZrL8vlAd0HWV7lZD\\nMBFnccLeqeGOAGMHgY9ZWosumMBYPiHV0ldHigD73pW9+QoAK7u1JDJk29w6o9Hs\\naIM5z8A8gTVjIpPaOX9b4IO/jbrhjlh302Xev6QO5kvCpb9xiar52GFa7DV9OZPN\\n37DYVVDneC4csme4CS4zkw==\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-mjjlo@chat-app-for-final-ae33d.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"101112446286745742368\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-mjjlo%40chat-app-for-final-ae33d.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n";
            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream).
                    createScoped(Lists.newArrayList(firebaseMessagingScope));
            googleCredentials.refresh();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            Log.d("getAccessTokennnnn", "getAccessToken: " + e.getMessage());
            return null;
        }
    }
}

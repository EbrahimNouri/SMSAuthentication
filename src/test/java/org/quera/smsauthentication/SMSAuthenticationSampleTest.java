package org.quera.smsauthentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import org.junit.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class SMSAuthenticationSampleTest {

    @Test
    public void testGenerateCode_Length() {
        assertEquals(5, SMSAuthentication.generateCode().length());
        assertTrue(Integer.parseInt(SMSAuthentication.generateCode()) < 100000);
    }

    @Test
    public void testGenerateCode_Random() {
        String code1 = SMSAuthentication.generateCode();
        String code2 = SMSAuthentication.generateCode();
        String code3 = SMSAuthentication.generateCode();
        assertNotEquals(code1, code2);
        assertNotEquals(code2, code3);
        assertNotEquals(code1, code3);
    }
}

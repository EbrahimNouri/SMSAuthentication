package org.quera.smsauthentication;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class SMSAuthentication {

    private static final String DATE_TIME_FORMAT = "yyyy/MM/dd-HH:mm:ss";

    public static String generateCode() {
        int random = (int) (Math.random() * (99999 - 10000)) + 10000;
        return String.valueOf(random);
    }


    public static String sendAuthenticationSMS(String phone, String path) {

        String generatedCode = generateCode();
        String expDate = String.valueOf(LocalDateTime.now().plusSeconds(10));
        String format = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT,
                Locale.GERMANY).format(LocalDateTime.now().plusSeconds(10));

        String json = "{\n" +
                "\t\"phone\" : \"" + phone + "\",\n" +
                "\t\"code\" : \"" + generatedCode + "\",\n" +
                "\t\"exp\" : " + "\"" + format + "\"\n" +
                "}";

        File file = new File(path);
        try {
            CSVWriter writer;
            boolean b = !file.exists();
            System.out.println(b);
            if (b) {
                FileWriter outputfile = new FileWriter(file);

                writer = new CSVWriter(outputfile);
                String[] header = {"Phone", "Code", "Expiration"};
                writer.writeNext(header);
            } else {
                FileWriter outputfile = new FileWriter(file, true);

                writer = new CSVWriter(outputfile);
            }

            String[] data = {phone, generatedCode, format};
            writer.writeNext(data);

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public static List<String[]> loadData(String path) throws FileNotFoundException {

        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            String[] tempArr;
            List<String[]> result = new ArrayList<String[]>();
            while ((line = br.readLine()) != null) {
                tempArr = line.split(",");
                String[] tempArrArr = new String[3];
                for (int i = 0; i < Arrays.stream(tempArr).count(); i++) {
                    if (i != 2)
                        tempArrArr[i] = tempArr[i].replaceAll("\"", "") + " ";
                    else
                        tempArrArr[i] = tempArr[i].replaceAll("\"", "");

                }
                result.add(tempArrArr);
            }
            br.close();
            return result;

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return null;
    }


    public static boolean verifyCode(String code, String phone, String path) throws FileNotFoundException {
       return Objects.requireNonNull(loadData(path)).stream().anyMatch(s -> s[1].contains(code) && LocalDateTime.now()
               .isBefore(LocalDateTime.parse(s[2], DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))));

    }

    public static void main(String[] args) throws FileNotFoundException {
//        System.out.println(generateCode());
        System.out.println(sendAuthenticationSMS("09381358888", "test.csv"));
//        System.out.println(loadData("test.csv"));
        Scanner sc = new Scanner(System.in);
        String code = sc.next();
        System.out.println(verifyCode(code, "09381358888", "test.csv"));

    }
}


package beans;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cliente.Cliente;

public class Utils {
	
	//Genera un hash (SHA256) data una string -- Funziona
	public static String generateHash(String input) 
	{
	    try 
	    {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
	        StringBuilder hexString = new StringBuilder();

	        for (byte b : hash) 
	        {
	            String hex = Integer.toHexString(0xff & b);
	            if (hex.length() == 1) 
	            {
	                hexString.append('0');
	            }
	            hexString.append(hex);
	        }

	        return hexString.toString();
	    } 
	    catch (NoSuchAlgorithmException e) 
	    {
	        throw new RuntimeException(e);
	    }
	}
		
	//Dato un json Cliente ottiene la struttura dati CLIENTE - Funziona
	public static Cliente readClienteFromJson(String json)
    {
    	Gson gson = new Gson();
    	Cliente c = null;
		
    	c = gson.fromJson(json.toString(), Cliente.class);
    	return c;
    }
	
	//Gestisce i json come risposta al server
	public static String returnJsonResponse(boolean error, boolean info ,int codiceErrore, String contenuto)
	{
		
		String response = null;
		response = "{\"errore\": "+ error +", \"info\": "+ info +", \"codiceErrore\": "+ codiceErrore +", \"contenuto\": \""+ contenuto +"\"}";
		return response;
	}
	
	public static String returnJsonResponseJ(boolean error, boolean info ,int codiceErrore, String contenuto)
	{
		
		String response = null;
		response = "{\"errore\": "+ error +", \"info\": "+ info +", \"codiceErrore\": "+ codiceErrore +", \"contenuto\": "+ contenuto + "}";
		return response;
	}
	
	// Funzione AUSILIARIA DI generateUniqueToken() 
	// Permette di convertire gli array di byte in una stringa esadecimale
	private static String bytesToHex(byte[] bytes) 
	{
        StringBuilder stringBuilder = new StringBuilder();

        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(hex);
        }

        return stringBuilder.toString();
    }
	
	// Genera una stringa Univoca per Autenticazione
	public static String generateUniqueToken() {
	    
		int LENGTH = 50;
		String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		
		String result = "";
        boolean isUnique = false;
        MessageDigest messageDigest;

        while (!isUnique) 
        {
            StringBuilder stringBuilder = new StringBuilder();
            Random random = new Random();

            for (int i = 0; i < LENGTH; i++) 
            {
                int randomIndex = random.nextInt(CHARACTERS.length());
                stringBuilder.append(CHARACTERS.charAt(randomIndex));
            }

            String randomString = stringBuilder.toString();

            try 
            {
                messageDigest = MessageDigest.getInstance("SHA-256");
                byte[] hashedBytes = messageDigest.digest(randomString.getBytes());
                String hashedString = bytesToHex(hashedBytes);
                result = hashedString.substring(0, LENGTH);
                isUnique = true;
            } 
            catch (NoSuchAlgorithmException e) 
            {
                e.printStackTrace();
            }
        }

        return result;
    }
	
	//Mi filtra il json per offuscare delle proprieta che il Cliente non puo vedere
	public static String filterJson(String json) {
	    Gson gson = new Gson();

	    JsonArray array = gson.fromJson(json, JsonArray.class);
	    JsonArray filteredArray = new JsonArray();

	    for (JsonElement element : array) {
	        JsonObject farmacia = element.getAsJsonObject();
	        JsonObject filteredFarmacia = new JsonObject();

	        filteredFarmacia.addProperty("idFarmacia", farmacia.get("idFarmacia").getAsString());
	        filteredFarmacia.addProperty("nomeFarmacia", farmacia.get("nomeFarmacia").getAsString());
	        filteredFarmacia.addProperty("indirizzo", farmacia.get("indirizzo").getAsString());
	        filteredFarmacia.addProperty("CAP", farmacia.get("CAP").getAsInt());
	        filteredFarmacia.add("orarioApertura", farmacia.get("orarioApertura"));
	        filteredFarmacia.add("c", farmacia.get("c"));

	        filteredArray.add(filteredFarmacia);
	    }

	    return gson.toJson(filteredArray);
	}
	
	public static String filterJson2(String json) {
	    Gson gson = new Gson();

	    JsonArray array = gson.fromJson(json, JsonArray.class);
	    JsonArray filteredArray = new JsonArray();

	    for (JsonElement element : array) {
	        JsonObject farmacia = element.getAsJsonObject();
	        JsonObject filteredClinica = new JsonObject();

	        filteredClinica.addProperty("idClinica", farmacia.get("idClinica").getAsString());
	        filteredClinica.addProperty("nomeClinica", farmacia.get("nomeClinica").getAsString());
	        filteredClinica.addProperty("indirizzo", farmacia.get("indirizzo").getAsString());
	        filteredClinica.addProperty("CAP", farmacia.get("CAP").getAsInt());
	        filteredClinica.add("orarioApertura", farmacia.get("orarioApertura"));
	        filteredClinica.add("c", farmacia.get("c"));

	        filteredArray.add(filteredClinica);
	    }

	    return gson.toJson(filteredArray);
	}
	
	//Ritorna il timestamp dell'orario corrente
	public static long getCurrentTimestamp() 
	{
	    return System.currentTimeMillis();
	}

	
	public static void main(String[] args) 
	{		
		//System.out.println(generateUniqueToken());
		
	}
}

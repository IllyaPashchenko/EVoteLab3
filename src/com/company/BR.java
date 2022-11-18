package com.company;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.UUID;

public class BR {

    private static ArrayList<UUID> registrationNumbers = new ArrayList<>();
    private static ArrayList<UUID> idOfVotersWithNumber = new ArrayList<>();


    public static byte[] registerForVoting(Citizen citizen){
        if (!idOfVotersWithNumber.contains(citizen.getId())){
            UUID newRegistered = UUID.randomUUID();
            registrationNumbers.add(newRegistered);
            idOfVotersWithNumber.add(citizen.getId());
            return encryptRegistrationNumber(newRegistered, citizen.publicKey);
        } else {
            System.out.println("This id was already used to get registered");
        }
        return null;
    }

    private static byte[] encryptRegistrationNumber(UUID rn, PublicKey pb){
        byte[] encryptedNumber = {};
        try {
            Cipher encryptionCipher = Cipher.getInstance("RSA");
            encryptionCipher.init(Cipher.ENCRYPT_MODE, pb);
            byte[] number = rn.toString().getBytes();
            encryptedNumber = encryptionCipher.doFinal(number);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            System.out.println(e.getMessage());
        }
        return encryptedNumber;
    }

    public static void sendRegistrationListToVK(VK vk){
        vk.setRegisteredNumbers(registrationNumbers);
    }
}

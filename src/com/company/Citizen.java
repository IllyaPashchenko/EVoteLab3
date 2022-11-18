package com.company;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.ArrayList;
import java.util.UUID;

public class Citizen {
    public UUID id;
    public String firsName;
    public String surname;
    boolean voteAllowed;
    boolean voted;
    private UUID registrationNumber;

    private PrivateKey privateKey;
    public PublicKey publicKey;
    public VK vk;
    public CitizenType citizenType;

    public Citizen(String firsName, String surname, CitizenType citizenType, boolean voteAllowed) {
        this.id = UUID.randomUUID();
        this.firsName = firsName;
        this.surname = surname;
        this.citizenType = citizenType;
        this.voteAllowed = voteAllowed;
        voted = false;


        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(512, new SecureRandom());
            KeyPair keyPair = keyGen.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Message " + ex.getMessage());
        }

        byte[] encryptedRegistrationCode = BR.registerForVoting(this);
        this.registrationNumber = decryptRegistrationNumber(encryptedRegistrationCode);
    }

    private UUID decryptRegistrationNumber(byte[] encryptedNumber){
        try {
            Cipher decryptionCipher = Cipher.getInstance("RSA");
            decryptionCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedMessage = decryptionCipher.doFinal(encryptedNumber);
            String decryption = new String(decryptedMessage);
            return UUID.fromString(decryption);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e){
            System.out.println("hello" + e.getMessage());
        }
        return null;
    }

    public void vote(UUID candidatesId){
        if (voteAllowed) {
            try {
                Vote vote = new Vote();
                vote.setVoterID(id);
                vote.setVotePublicKey(publicKey);
                vote.setRegistrationNumber(registrationNumber);
                String messageString = "I vote " + candidatesId;

                //Encrypt message with CVK key
                Cipher encryptionCipher = Cipher.getInstance("RSA");
                encryptionCipher.init(Cipher.ENCRYPT_MODE, vk.publicKey);
                byte[] encryptedMessage = encryptionCipher.doFinal(messageString.getBytes());

                vote.setEncryptedMessage(encryptedMessage);

                // sign the vote
                Signature signature = Signature.getInstance("SHA1withRSA");
                signature.initSign(privateKey, new SecureRandom());

                signature.update(vote.getEncryptedMessage());
                byte[] sigBytes = signature.sign();

                vote.setSigBytes(sigBytes);


                vk.checkVote(vote);
            } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | SignatureException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public UUID getId() {
        return id;
    }

    public String getFirsName() {
        return firsName;
    }

    public String getSurname() {
        return surname;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public void setVk(VK vk) {
        this.vk = vk;
    }
}

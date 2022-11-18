package com.company;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class VK {
    private PrivateKey privateKey;
    public PublicKey publicKey;
    ArrayList<UUID> registeredNumbers;
    HashMap<UUID, Integer> candidates;

    public VK() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(512, new SecureRandom());
            KeyPair keyPair = keyGen.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();

            candidates = new HashMap<>();
            registeredNumbers = new ArrayList<>();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
    }

    public void checkVote(Vote vote){
        try {
            // Verify
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(vote.getVotePublicKey());
            signature.update(vote.getEncryptedMessage());
            boolean result = signature.verify(vote.getSigBytes());

            if (result) {
                // Decrypt
                Cipher decryptionCipher = Cipher.getInstance("RSA");
                decryptionCipher.init(Cipher.DECRYPT_MODE, privateKey);
                byte[] decryptedMessage = decryptionCipher.doFinal(vote.getEncryptedMessage());
                String decryption = new String(decryptedMessage);
//                System.out.println("decrypted message = " + decryption);

                if (!registeredNumbers.contains(vote.getRegistrationNumber())){
                    System.out.println("This registration number is incorrect or was already used to vote");
                } else {
                    // Count in
                    UUID chosenId = UUID.fromString(decryption.split(" ")[2]);
                    if (candidates.containsKey(chosenId)){
                        Integer currentVotes = candidates.get(chosenId);
                        candidates.put(chosenId, currentVotes + 1);
                        registeredNumbers.remove(vote.getRegistrationNumber());
                        System.out.println("Voter with id: " + vote.getVoterID() + " successfully voted");
                    } else {
                        System.out.println("The candidate given is wrong");
                    }
                }
            } else {
                System.out.println("Signature verification failed");
            }

        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | SignatureException e){
            System.out.println(e.getMessage());
        }
    }

    public void selectAndAddCandidates(ArrayList<Citizen> people) {
        for (Citizen someone : people) {
            if (someone.citizenType == CitizenType.CANDIDATE){
                candidates.put(someone.getId(), 0);
            }
        }
    }

    public void finalStandings(){
        for (UUID id : candidates.keySet()) {
            System.out.println(candidates.get(id));
        }
    }

    public ArrayList<UUID> getRegisteredNumbers() {
        return registeredNumbers;
    }

    public void setRegisteredNumbers(ArrayList<UUID> registeredNumbers) {
        this.registeredNumbers = registeredNumbers;
    }

    public HashMap<UUID, Integer> getCandidates() {
        return candidates;
    }

    public void setCandidates(HashMap<UUID, Integer> candidates) {
        this.candidates = candidates;
    }
}

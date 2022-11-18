package com.company;

import java.security.PublicKey;
import java.util.UUID;

public class Vote {
    UUID voterID;
    UUID registrationNumber;
    PublicKey votePublicKey;
    byte[] encryptedMessage;
    byte[] sigBytes;

    public UUID getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(UUID registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public UUID getVoterID() {
        return voterID;
    }

    public void setVoterID(UUID voterID) {
        this.voterID = voterID;
    }

    public byte[] getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setEncryptedMessage(byte[] encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    public byte[] getSigBytes() {
        return sigBytes;
    }

    public void setSigBytes(byte[] sigBytes) {
        this.sigBytes = sigBytes;
    }

    public PublicKey getVotePublicKey() {
        return votePublicKey;
    }

    public void setVotePublicKey(PublicKey votePublicKey) {
        this.votePublicKey = votePublicKey;
    }
}

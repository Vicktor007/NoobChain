package src.main.java;

import src.main.java.utility.StringUtil;

import java.security.*;
import java.util.ArrayList;

public class Transaction {

    public String transactionID;
    public PublicKey senderAddress;
    public PublicKey recipientAddress;
    public float value;
    public byte[] signature; // to prevent anyone else from spending our wallet funds

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0; // a rough count of how many transactions have been generated

    public Transaction(PublicKey senderAddress, PublicKey recipientAddress, float value, ArrayList<TransactionInput> inputs){
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.value = value;
        this.inputs = inputs;
    }

    // this calculates the transaction hash (which will be used as its id)
    private String getTransactionID(){
        sequence++; // increase the sequence to avoid 2 identical transactions having the same hash
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(senderAddress) +
                        StringUtil.getStringFromKey(recipientAddress) +
                        Float.toString(value) + sequence
        );
    }

    // signs all the data we dont wish to be tampered with
    public  void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(senderAddress) + StringUtil.getStringFromKey(recipientAddress) + Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    // verifies the data we signed hasnt been tampered with
    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(senderAddress) + StringUtil.getStringFromKey(recipientAddress) + Float.toString(value);
        return StringUtil.verifyECDSASig(senderAddress, data, signature);
    }
}

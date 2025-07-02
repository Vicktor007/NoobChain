package src.main.java;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

    public Wallet(){
        generateKeyPair();
    }
    //  this method  uses Java.security.KeyPairGenerator to generate an Elliptic Curve KeyPair. This methods makes and sets our Public and Private keys.
    public void generateKeyPair(){
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("prime192v1");
//          Initialize the key generator and generate a keyPair
            keyPairGenerator.initialize(ecGenParameterSpec, random); // 256 bytes provides an acceptable security level
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
//            // set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch(Exception e){
            throw  new RuntimeException(e);
        }
    }
}

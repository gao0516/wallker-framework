package com.wallker.framework.core.encrypt;

import java.security.*;

public class RSAKeyPairGenerator {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(1024, new SecureRandom());
		KeyPair pair = generator.generateKeyPair();
		PublicKey pubKey = pair.getPublic();
		PrivateKey privKey = pair.getPrivate();
		byte[] pk = pubKey.getEncoded();
		byte[] privk = privKey.getEncoded();
		String strpk = new String(Base64.encode(pk));
		String strprivk = new String(Base64.encode(privk));
		System.out.println("strpk:" + strpk);
		System.out.println("strprivk:" + strprivk);
	}

}

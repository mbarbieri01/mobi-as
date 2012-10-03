package com.cesaco.mobias.security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAPrivateKeySpec;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;

import com.cesaco.mobias.util.Base64;

@Stateful
@ApplicationScoped
public class SecurityManager {

	// RSA
	private static final String storedPrivateKeyExp = "IFthH32k0pux2Mkq3JDHHp17lHUy5zBegWGeRrA4HO5IU/HvTjiXKqOHo5+599GM11mtjAgMXJfaF09UDtS2Qy1XrY8+Foi8Q6i9KOeV0blRm74d9Q+Kq1aFmrH6Xe3c7Eh/fpvVwrYfp4lT4muRXSxNwfTjU11A/LBZ3PopP7l1VtHdua3iAJCwf/GjreipOAVvqR5KFfqC0oQqaT6ZLVCIlVGAO0RxBxOeQS4FuIdLDWQ6j/aGXZcgqgCokBEIr4ob5JU44mzfluVaGi5P6keQd7461Z0kjaNnxGBKROssgD8rvot09X7ACaZPkz1l14rBen/ec6J/EKoqqgQDgQ==";
	private static final String storedPrivateKeyMod = "14F+022WsVcdQTkfXmCrES+4CZggX9MxKguleK0iDybZWIOfboOtqhzH/bGuMWsZn5jlF3TFbZYHDftM9dJYCUBi7/ImcjuZFwZD5KZ4ipea+v9relC/WLHEgad34WkuDXYbqHa/cR2ds6RNuuL10iaUj0MnwmiYMZHcjGJmPa8iMv6Hj3zMr+CCnRbWT67MW7DmBWGQiXIsQk0G/0X9QfMRCj805LvWTTG+c4f44COWsNcClXgnJXLJXdCrt5jlQLIZ9B86CioqUe25YVpU1qh73zeczJmP/Bk63tdXtCF9QaSqH1IlB8u2ncNnvNLnnNNo97hS8PrVAe/KYNQp8w==";
	private PrivateKey pk;
	private Cipher RSAcipher;

	// AES
	private Cipher AEScipher;
	// private static final String saltString = "2degckF";
	private static final String initialVector = "vss0r671r77u9kss";
	// private final int nIter = 2;
	private final int AESkeySize = 128;
	private AlgorithmParameterSpec paramSpec;
	private final String defaultKey = "5djtfn4rjmxf5p17";

	@PostConstruct
	public void initialize() {
		try {
			RSAcipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BigInteger mod = new BigInteger(
				deShiftBigIntegerByteArray(Base64.decode(storedPrivateKeyMod)));
		BigInteger exp = new BigInteger(
				deShiftBigIntegerByteArray(Base64.decode(storedPrivateKeyExp)));
		pk = generateRsaPrivateKey(mod, exp);

		// AES
		byte[] iv;
		try {
			iv = initialVector.getBytes("UTF-8");
			AEScipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			paramSpec = new IvParameterSpec(iv);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public byte[] RSAdecrypt(byte[] eKey) {
 		byte[] tempKey = null;
		tempKey = decryptMessageRSA(eKey);
		return tempKey;
	}

	public String getPlainData(int clientID, long transID, byte[] eData,
			byte[] pKey) {
		String tempStringData = "";
		byte[] tempPlainData = null;
		tempPlainData = decryptAES(eData, pKey);
		if (!tempPlainData.equals(null)) {
			try {
				tempStringData = new String(tempPlainData, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tempStringData;
	}

	private byte[] decryptMessageRSA(byte[] eData) {
		byte[] pData = null;

		try {
			RSAcipher.init(Cipher.DECRYPT_MODE, pk);
			pData = RSAcipher.doFinal(eData);
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pData;
	}

	private byte[] decryptAES(byte[] eData, byte[] password) {
		byte[] plainText = null;
		try {

			SecretKey secret = new SecretKeySpec(password, "AES");

			AEScipher.init(Cipher.DECRYPT_MODE, secret, paramSpec);

			plainText = AEScipher.doFinal(eData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plainText;
	}

	public byte[] encryptAESdefault(byte[] pData) {
		byte[] eText = null;
		try {

			SecretKey secret = new SecretKeySpec(defaultKey.getBytes("UTF-8"),
					"AES");

			AEScipher.init(Cipher.ENCRYPT_MODE, secret, paramSpec);

			eText = AEScipher.doFinal(pData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eText;
	}

	private byte[] shiftBigIntegerByteArray(byte[] array) {
		if (array[0] == 0) {
			byte[] tmp = new byte[array.length - 1];
			System.arraycopy(array, 1, tmp, 0, tmp.length);
			array = tmp;
		}
		return array;
	}

	private byte[] deShiftBigIntegerByteArray(byte[] array) {
		if (!(array[0] == 0)) {
			byte[] tmp = new byte[array.length + 1];
			System.arraycopy(array, 0, tmp, 1, array.length);
			tmp[0] = 0;
			array = tmp;
		}
		return array;
	}

	public static PrivateKey generateRsaPrivateKey(BigInteger modulus,
			BigInteger publicExponent) {
		try {
			return KeyFactory.getInstance("RSA").generatePrivate(
					new RSAPrivateKeySpec(modulus, publicExponent));
		} catch (Exception e) {
			// Logger.e(e.toString());
		}
		return null;
	}

}

package com.cesaco.mobias.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.marshalling.reflect.PublicReflectiveCreator;

import sun.security.x509.X509CertImpl;


import com.cesaco.mobias.util.Base64;
import com.cesaco.mobias.util.TransStatus;
import com.cesaco.mobias.util.jms.AsynchReceiver;
import com.cesaco.mobias.util.jms.AsynchSender;

@Stateful
@ApplicationScoped
@Named("JMScontroller")
public class JMScontroller {

	@Inject
	private AsynchSender jmsConsumer;

	@Inject
	private AsynchReceiver jmsReceiver;

	@Inject
	private TransStatus transStatus;

	private int i;
	private PrintWriter out;
	private FileWriter outFile;

	public JMScontroller() {
		// TODO Auto-generated constructor stub
		i = 0;
		
		try {
			outFile = new FileWriter("C:\\Temp\\stressTestS.txt");
		
			out = new PrintWriter(outFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		generateRSAkeyPair();
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("sono JMScontroller, i = "+i+" -mi sto creando.."+this.hashCode());
	}

	public void testState() {
		System.out.println("testState: " + i + "-" + this.hashCode());
		i++;
	}

	public String addRequest(int client_id, String mtext) {
		i++;
		// System.out.println("sono nel bean. Hash: "+this.hashCode());
		// qui si dovrebbe implementare la logica applicativa che comunica con
		// il gestore di coda JMS
		// hornetq di jBoss

		// String mdx = getMDXfromFeatures(mtext);
		String mdx = mtext;
		// codice JMS
		long transID = transStatus.addTrans();
		String filename = getNewFilename(client_id, transID);
		if (jmsConsumer.sendMessage(mdx, filename, client_id, transID)) {
			//startListner(client_id, transID);
		} else {
			transID = -1;
		}

		return Long.toString(transID);
		// return listenResponse(client_id);
		// return "sei il numero: "+Integer.toString(i);

	}

	private String getNewFilename(int client_id, long transID) {
		return "file-".concat(Integer.toString(client_id)).concat("-")
				.concat(Long.toString(transID));
	}

	private String getMDXfromFeatures(String mtext) {
		return "SELECT ([Measures].[Margine Budget Assoluto]) ON COLUMNS, filter([Voci].[Voce].Members, NOT ISEMPTY ([Measures].[Margine Budget Assoluto])) ON ROWS FROM [COGESBudget]";
		// TODO Auto-generated method stub

	}

	public void updateTransStatus(int client_id, long transID, String status) {
		transStatus.update(client_id, transID, status);
	}

	private void startListner(int client_id, long transID) {
		System.out.println("Start listener...");
		try {
			// Step 1. Lookup the initial context
			InitialContext ic = new InitialContext();

			// JMS operations

			// Step 2. Look up the XA Connection Factory
			ConnectionFactory cf = (ConnectionFactory) ic
					.lookup("java:/ConnectionFactory");

			// Step 3. Look up the Queue
			Queue queue = (Queue) ic.lookup("java:/infoQueue");

			// Step 4. Create a connection, a session and a message producer for
			// the queue
			Connection jmsConnection = cf.createConnection();
			Session session = jmsConnection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(queue);
			TextMessage message = session.createTextMessage();
			message.setIntProperty("client_id", client_id);
			message.setLongProperty("transID", transID);
			// Step 6. Send The Text Message
			messageProducer.send(message);
		} catch (NamingException ne) {
			System.out.println(ne.toString());
		} catch (JMSException je) {
			System.out.println(je.toString());
		}
	}

	private Boolean listenResponse(int client_id, long transID) {
		/*
		 * in teoria qui risponde il servizio JMS quando la richiesta è
		 * effettuata, fornendo il nome del file dove è stato salvato il
		 * documento XML prodotto.
		 */
		System.out.println("JMSController listenResponse");
		String message = jmsReceiver.getMessage(client_id, transID, this);

		return message.equalsIgnoreCase("ok");
	}

	public String getStatusAndFilename(String transID) {
		return transStatus.getStatus(transID);
	}

	public String getPublicRSAexp(int clientID) {

		return publicKeyExp;
	}

	private PrivateKey privateKey;
	private PublicKey publicKey;
	private String publicKeyExp;
	private String publicKeyMod;
	private BigInteger publicKeyExpBI;
	private BigInteger publicKeyModBI;
	private static final String storedPrivateKeyExp = "IFthH32k0pux2Mkq3JDHHp17lHUy5zBegWGeRrA4HO5IU/HvTjiXKqOHo5+599GM11mtjAgMXJfaF09UDtS2Qy1XrY8+Foi8Q6i9KOeV0blRm74d9Q+Kq1aFmrH6Xe3c7Eh/fpvVwrYfp4lT4muRXSxNwfTjU11A/LBZ3PopP7l1VtHdua3iAJCwf/GjreipOAVvqR5KFfqC0oQqaT6ZLVCIlVGAO0RxBxOeQS4FuIdLDWQ6j/aGXZcgqgCokBEIr4ob5JU44mzfluVaGi5P6keQd7461Z0kjaNnxGBKROssgD8rvot09X7ACaZPkz1l14rBen/ec6J/EKoqqgQDgQ==";
	private static final String storedPrivateKeyMod = "14F+022WsVcdQTkfXmCrES+4CZggX9MxKguleK0iDybZWIOfboOtqhzH/bGuMWsZn5jlF3TFbZYHDftM9dJYCUBi7/ImcjuZFwZD5KZ4ipea+v9relC/WLHEgad34WkuDXYbqHa/cR2ds6RNuuL10iaUj0MnwmiYMZHcjGJmPa8iMv6Hj3zMr+CCnRbWT67MW7DmBWGQiXIsQk0G/0X9QfMRCj805LvWTTG+c4f44COWsNcClXgnJXLJXdCrt5jlQLIZ9B86CioqUe25YVpU1qh73zeczJmP/Bk63tdXtCF9QaSqH1IlB8u2ncNnvNLnnNNo97hS8PrVAe/KYNQp8w==";
	private byte[] AESpassword;
	private String sAESpassword;
	
	public byte[] decryptMessage(byte[] eData) {
		Cipher cipher;
		byte[] pData = null;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			BigInteger mod = new BigInteger(deShiftBigIntegerByteArray(Base64.decode(storedPrivateKeyMod)));
			BigInteger exp = new BigInteger(deShiftBigIntegerByteArray(Base64.decode(storedPrivateKeyExp)));
			PrivateKey pk = generateRsaPrivateKey(mod, exp);
			cipher.init(Cipher.DECRYPT_MODE, pk);
			pData = cipher.doFinal(eData);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AESpassword = pData;
		//System.out.println("AES password: "+toHexString(AESpassword));
		sAESpassword = Base64.encode(AESpassword);
		return pData;
	}
	
	public byte[] encryptMessage(byte[] pData) {
		Cipher cipher;
		byte[] eData = null;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			BigInteger mod = new BigInteger(Base64.decode(publicKeyMod));
			BigInteger exp = new BigInteger(Base64.decode(publicKeyExp));
			PublicKey pk = generateRsaPublicKey(mod, exp);
			cipher.init(Cipher.ENCRYPT_MODE, pk);
			eData = cipher.doFinal(pData);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return eData;
	}
	
	public static PublicKey generateRsaPublicKey(BigInteger modulus, BigInteger publicExponent)
	  {
	    try
	    {
	      return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
	    }
	    catch(Exception e)
	    {
	    //  Logger.e(e.toString());
	    }
	    return null;
	  }
	
	public static PrivateKey generateRsaPrivateKey(BigInteger modulus, BigInteger publicExponent)
	  {
	    try
	    {
	      return KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateKeySpec(modulus, publicExponent));
	    }
	    catch(Exception e)
	    {
	    //  Logger.e(e.toString());
	    }
	    return null;
	  }
	
	private void generateRSAkeyPair() {
		KeyPairGenerator keyGen;

		try {
			keyGen = KeyPairGenerator.getInstance("RSA");

			keyGen.initialize(2048, new SecureRandom());
			
			KeyPair keypair = keyGen.genKeyPair();
			privateKey = keypair.getPrivate();
			publicKey = keypair.getPublic();
			
			RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;

			publicKeyExpBI = rsaPublicKey.getPublicExponent();
			//publicKeyExp = new String(publicKeyExpBI.toByteArray());
			
			//System.out.println("public key: ");
			publicKeyExp = Base64.encode(shiftBigIntegerByteArray(publicKeyExpBI.toByteArray()));
			//System.out.println("exp BASE64: ");
			//System.out.println(publicKeyExp);
			
			publicKeyModBI = rsaPublicKey.getModulus();
			//publicKeyMod = new String(publicKeyModBI.toByteArray());
			publicKeyMod = Base64.encode(shiftBigIntegerByteArray(publicKeyModBI.toByteArray()));
			//System.out.println("mod BASE64: ");
			//System.out.println(publicKeyMod);
			
			//System.out.println("private key: ");
			BigInteger privateKeyExpBI = rsaPrivateKey.getPrivateExponent();
			//publicKeyExp = new String(publicKeyExpBI.toByteArray());
			
			String privateKeyExp = Base64.encode(shiftBigIntegerByteArray(privateKeyExpBI.toByteArray()));
			//System.out.println("exp BASE64: ");
			//System.out.println(privateKeyExp);
			
			BigInteger privateKeyModBI = rsaPrivateKey.getModulus();
			//publicKeyMod = new String(publicKeyModBI.toByteArray());
			String privateKeyMod = Base64.encode(shiftBigIntegerByteArray(privateKeyModBI.toByteArray()));
			//System.out.println("mod BASE64: ");
			//System.out.println(privateKeyMod);
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		}
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

	public String getPublicRSAmod(int clientID) {
		//System.out.println("exp bi: "+publicKeyExpBI);
		//System.out.println("mod bi: "+publicKeyModBI);
		return publicKeyMod;
	}

	public String getModulus() {
		return publicKeyMod;
	}

	public String testEnc(String pText) {
		byte[] pData = pText.getBytes();
		byte[] eData = encryptMessage(pData);
		byte[] pByte = decryptMessage(eData);
		return new String(pByte);
	}
	
	private static String toHexString(byte[] bytes) {
	    char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j*2] = hexArray[v/16];
	        hexChars[j*2 + 1] = hexArray[v%16];
	    }
	    return new String(hexChars);
	}
	
	//private static final String saltString = "2degckF";
	private static final String initialVector = "vss0r671r77u9kss";
	//private final int nIter = 2;
	private final int keySize = 256;
	
	public byte[] decryptAES(byte[] eData, byte[] password) {
		byte[] plainText = null;
		try {
		//SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		//byte[] salt = saltString.getBytes("US-ASCII");
		byte[] iv = initialVector.getBytes("UTF-8");
		//String _password = new String(password, "US-ASCII");
		//char[] cPassword = _password.toCharArray();
		//System.out.println("password length: "+password.length);
		//KeySpec spec = new PBEKeySpec(cPassword, salt, nIter, keySize);
		//SecretKey tmp = factory.generateSecret(spec);
		/// new SecretKey(passwordInBYTE_256bit, "AES");
		//System.out.println("password length: "+password.length);
		//System.out.println("iv length: "+iv.length);
		SecretKey secret = new SecretKeySpec(password, "AES");

		//System.out.println("secret getEncoder"+toHexString(secret.getEncoded()));
		/* Encrypt the message. */
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, secret, paramSpec);
		
		plainText = cipher.doFinal(eData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plainText;
	}
	
	private final String defaultKey = "5djtfn4rjmxf5p17";
	public byte[] encryptAES(byte[] pData) {
		byte[] eText = null;
		try {
		//SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		//byte[] salt = saltString.getBytes("US-ASCII");
		byte[] iv = initialVector.getBytes("UTF-8");
		//String _password = new String(password, "US-ASCII");
		//char[] cPassword = _password.toCharArray();
		//System.out.println("password length: "+password.length);
		//KeySpec spec = new PBEKeySpec(cPassword, salt, nIter, keySize);
		//SecretKey tmp = factory.generateSecret(spec);
		/// new SecretKey(passwordInBYTE_256bit, "AES");
		//System.out.println("password length: "+password.length);
		//System.out.println("iv length: "+iv.length);
		SecretKey secret = new SecretKeySpec(defaultKey.getBytes("UTF-8"), "AES");

		//System.out.println("secret getEncoder"+toHexString(secret.getEncoded()));
		/* Encrypt the message. */
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, secret, paramSpec);
		
		eText = cipher.doFinal(pData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eText;
	}
	
	public byte[] getAESpassword() {
		return AESpassword;
	}
	
	private final String characterEncoding = "UTF-8";
	private final String cipherTransformation = "AES/CBC/PKCS5Padding";
	private final String aesEncryptionAlgorithm = "AES";

	public  byte[] decrypt(byte[] cipherText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		Cipher cipher = Cipher.getInstance(cipherTransformation);
		SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
		cipherText = cipher.doFinal(cipherText);
		return cipherText;
	}

	public byte[] encrypt(byte[] plainText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		Cipher cipher = Cipher.getInstance(cipherTransformation);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		plainText = cipher.doFinal(plainText);
		return plainText;
	}

	private byte[] getKeyBytes(String key) throws UnsupportedEncodingException{
		//System.out.println("------key: "+key);
		byte[] keyBytes= new byte[32];
		byte[] parameterKeyBytes= key.getBytes(characterEncoding);
		System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
		return keyBytes;
	}


	

	/// <summary>
	/// Decrypts a base64 encoded string using the given key (AES 128bit key and a Chain Block Cipher)
	/// </summary>
	/// <param name="encryptedText">Base64 Encoded String</param>
	/// <param name="key">Secret Key</param>
	/// <returns>Decrypted String</returns>
	public String decrypt(String encryptedText) throws KeyException, GeneralSecurityException, GeneralSecurityException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException{
		String key = sAESpassword;
		byte[] cipheredBytes = Base64.decode(encryptedText);
		byte[] keyBytes = getKeyBytes(key);
		return new String(decrypt(cipheredBytes, keyBytes, keyBytes), characterEncoding);
	}

	private static String getHexString(byte[] b) throws Exception {
		  String result = "";
		  for (int i=0; i < b.length; i++) {
		    result +=
		          Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		    if (i < b.length-1) {
		    	result += "-";   
		    }
		  }
		  return result;
		}
	
	private MessageDigest digest;
	private byte[] md5sum;
	
	public void saveMD5(byte[] pText) {
		md5sum = digest.digest(pText);
		try {
			String hexMD5 = getHexString(md5sum);
			System.out.println(hexMD5);
			//out.println(hexMD5);
			out.flush();
			outFile.write(hexMD5+"\n");
			outFile.flush();
		} catch (Exception e) {
			out.println("error");
			out.flush();
			try {
				outFile.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	
	}

	public byte[] encryptStandard(String string) {
		// TODO Auto-generated method stub
		return null;
	}
		
	


}

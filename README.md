# Message Encryption Example

This example shows how to encrypt and decrypt the payload of the message. 
The example could be used as a base for encrypting payloads for API's or certain field of API's.
The cryptografic algorithm used in this example is AES-256 since this was an explicit request from security.
The key used in the example was obtained from a keystore. 

For extra security purposes AES encryption can be extended by using a so called Initialization Vector, which is similar as a NONCE a random number used per request.
In this example a random 16 bit byte[] is used.

For more information about AES encryption: https://en.wikipedia.org/wiki/Advanced_Encryption_Standard

For more information about Initialization Vectors: https://en.wikipedia.org/wiki/Initialization_vector

## Building

The example can be built with

    mvn clean install


## Running the example locally

This example is not meant to run locally!

## Implementation details

This section contains some additional explanation of the implementation and use. 

### Generating the key

The key in this example was generated with the keytool utility

    keytool -genseckey -keystore aes-keystore.jck -storetype jceks -storepass mystorepass -keyalg AES -keysize 256 -alias jceksaes -keypass mykeypass 
    
### Helper methods
 
For convenience a helper class containing static methods was implemented. The helper class is named EncryptionUtils.
 
One of the helper methods retrieves the Key from the keystore:

```java
public static Key getKeyFromKeystore(KeyConfig keyConfig) {

    String keystorePass = keyConfig.getKeystorePass();
    String alias = keyConfig.getAlias();
    String keyPass = keyConfig.getKeyPass();
    String keystoreLocation = keyConfig.getKeystoreLocation();
    String keystoreType = keyConfig.getKeystoreType();

    InputStream keystoreStream = null;
    KeyStore keystore = null;
    Key key = null ;

    try {
      keystoreStream = new FileInputStream(keystoreLocation);
      keystore = KeyStore.getInstance(keystoreType);

      keystore.load(keystoreStream, keystorePass.toCharArray());
      if (!keystore.containsAlias(alias)) {
        throw new RuntimeException("Alias for key not found");
      }

      key = keystore.getKey(alias, keyPass.toCharArray());

    } catch (Exception e) {
      e.printStackTrace();
    }

    return key;
  }
```

The Initialization Vector is also generated using a helper method:

```java
  public static byte[] generateIV() {
    byte[] iv = new byte[16];
    new Random().nextBytes(iv);

    return iv;
  }
```

### Encrypting and Decrypting in a Camel route

To encrypt and decrypt messages Camel has made an abstraction independend of the algorithm used for encryption and decryption.
This abstraction is called the CryptoDataFormat and can be used as any other data format. 
The CryptoDataFormat is documented here: http://camel.apache.org/crypto.html 

In order to initialize the CryptoDataFormat in the CamelContext the example uses a PostConstruct:

```java
  @PostConstruct
  public void setupEncryption() {
    cryptoFormat = new CryptoDataFormat(keyConfig.getCryptoAlgorithm(), EncryptionUtils.getKeyFromKeystore(keyConfig), keyConfig.getCryptoProvider());
  }
```

To leverage the Initialization Vector and perform the encryption in the Camel route the following statements have to be executed in the Camel Route:

```java
.setHeader("iv", method("nl.rubix.eos.poc.util.EncryptionUtils", "generateIV"))
.bean(cryptoFormat, "setInitializationVector(${header.iv})")
.marshal(cryptoFormat)
```

Since the example uses the same instance of the CryptoDataFormat decryption is as simple as:

```java
.unmarshal(cryptoFormat)
```

In practice it won't often be feasible to use the same intance of the CryptoDataFormat for both encryption and decrypion.
When decrypting the exact same key and Initialization Vector must be used to initialize the CryptoDataFormat instance used for decryption as was used for encryption.
Since this is symmetric encryption. The key used for encrypion must be available to the party which performs the decryption. This opposed to assymmetric public/private key signage.
This is inherently part of the AES encryption protocol.

## java.security.InvalidKeyException: Illegal key size

For reasons unknown to mankind Oracle decided not to include key sizes of 256 in the standard JRE despite the fact we are living in the 21st century.
This results in a **Caused by: java.security.InvalidKeyException: Illegal key size** exception.
To resolve this the so called **"Unlimited Strength Juristriction Policy"** files must be downloaded and extracted to the JRE.

Since Oracle has a tendency to change download links it won't be posted here. But searching the internet will provide plenty of examples on how to download and install. 

package nl.rubix.eos.poc.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.Random;

public class EncryptionUtils {

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

  public static byte[] generateIV() {
    byte[] iv = new byte[16];
    new Random().nextBytes(iv);

    return iv;
  }
}

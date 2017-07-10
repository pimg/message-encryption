package nl.rubix.eos.poc.util;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import javax.inject.Inject;
import javax.inject.Named;

@Named("keyConfig")
public class KeyConfig {

  @Inject
  @ConfigProperty(name = "message-encryption.keystore.password")
  private String keystorePass;

  @Inject
  @ConfigProperty(name = "message-encryption.keystore.type")
  private String keystoreType;

  @Inject
  @ConfigProperty(name = "message-encryption.keystore.location")
  private String keystoreLocation;

  @Inject
  @ConfigProperty(name = "message-encryption.alias")
  private String alias;

  @Inject
  @ConfigProperty(name = "message-encrypton.key.password")
  private String keyPass;

  @Inject
  @ConfigProperty(name = "message-encryption.crypto.algorithm")
  private String cryptoAlgorithm;

  @Inject
  @ConfigProperty(name ="message-encryption.crypto.provider")
  private String cryptoProvider;

  public String getKeystorePass() {
    return keystorePass;
  }

  public String getKeystoreType() {
    return keystoreType;
  }

  public String getKeystoreLocation() {
    return keystoreLocation;
  }

  public String getAlias() {
    return alias;
  }

  public String getKeyPass() {
    return keyPass;
  }

  public String getCryptoAlgorithm() {
    return cryptoAlgorithm;
  }

  public String getCryptoProvider() {
    return cryptoProvider;
  }
}

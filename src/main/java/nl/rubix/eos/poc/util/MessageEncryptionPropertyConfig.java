package nl.rubix.eos.poc.util;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;

import javax.inject.Named;

@Named
public class MessageEncryptionPropertyConfig implements PropertyFileConfig {

  @Override public String getPropertyFileName() {
    return "message-encryption.properties";
  }

  @Override public boolean isOptional() {
    return true;
  }
}
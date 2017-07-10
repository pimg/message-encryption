package nl.rubix.eos.poc;

import nl.rubix.eos.poc.util.EncryptionUtils;
import nl.rubix.eos.poc.util.KeyConfig;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.converter.crypto.CryptoDataFormat;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@ContextName("messageEncryption")
public class MessageEncryptionRoute extends RouteBuilder {

  private CryptoDataFormat cryptoFormat;

  @Inject
  private KeyConfig keyConfig;

  @PostConstruct
  public void setupEncryption() {
    cryptoFormat = new CryptoDataFormat(keyConfig.getCryptoAlgorithm(), EncryptionUtils.getKeyFromKeystore(keyConfig), keyConfig.getCryptoProvider());
  }


  @Override
    public void configure() throws Exception {

        from("direct:start")
          .setHeader("iv", method("nl.rubix.eos.poc.util.EncryptionUtils", "generateIV"))
          .bean(cryptoFormat, "setInitializationVector(${header.iv})")
          .marshal(cryptoFormat)
          .log("The message is encrypted: ${body}")
          .inOnly("seda:decrypt");

        from("seda:decrypt")
          .log("received encrypted message: ${body} going to decrypt...")
          .unmarshal(cryptoFormat)
          .log("message decrypted: ${body}")
          .to("mock:decrypted");
    }

}

package gov.hhs.onc.dcdt.crypto.keys.impl;

import gov.hhs.onc.dcdt.crypto.CryptographyConfig.GenerateConstraintGroup;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyGenerator;
import gov.hhs.onc.dcdt.crypto.keys.KeyException;
import gov.hhs.onc.dcdt.crypto.keys.KeyGenerator;
import gov.hhs.onc.dcdt.crypto.keys.KeyConfig;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.crypto.utils.KeyUtils;
import gov.hhs.onc.dcdt.crypto.utils.SecureRandomUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.utils.ToolValidationUtils;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component("keyGenImpl")
@Scope("singleton")
public class KeyGeneratorImpl extends AbstractCryptographyGenerator<KeyConfig, KeyInfo> implements KeyGenerator {
    private final static int KEYS_GEN_RAND_SEED_SIZE_DEFAULT = 32;

    @Override
    public KeyInfo generateKeys(KeyConfig keyConfig) throws CryptographyException {
        return generateKeys(keyConfig, SecureRandomUtils.getRandom(KEYS_GEN_RAND_SEED_SIZE_DEFAULT));
    }

    @Override
    public KeyInfo generateKeys(KeyConfig keyConfig, SecureRandom secureRandom) throws CryptographyException {
        BindingResult keyConfigBindingResult = this.validateConfig(keyConfig, GenerateConstraintGroup.class);

        if (keyConfigBindingResult.hasErrors()) {
            throw new KeyException(String.format("Invalid key configuration (class=%s): %s", ToolClassUtils.getName(keyConfig),
                ToolStringUtils.joinDelimit(ToolValidationUtils.mapErrorMessages(this.msgSourceValidation, keyConfigBindingResult).entrySet(), ", ")));
        }

        KeyPairGenerator keyPairGen = KeyUtils.getKeyPairGenerator(keyConfig.getKeyAlgorithm());
        keyPairGen.initialize(keyConfig.getKeySize(), secureRandom);

        return new KeyInfoImpl(keyPairGen.generateKeyPair());
    }
}

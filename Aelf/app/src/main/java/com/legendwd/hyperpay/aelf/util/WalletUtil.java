package com.legendwd.hyperpay.aelf.util;

import java.security.SecureRandom;

import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;

/**
 * @author lovelyzxing
 * @date 2019/6/12
 * @Description
 */
public class WalletUtil {

    public static String generateMnemonic() {
        StringBuilder sb = new StringBuilder();
        byte[] entropy = new byte[Words.TWELVE.byteLength()];
        new SecureRandom().nextBytes(entropy);
        new MnemonicGenerator(English.INSTANCE).createMnemonic(entropy, sb::append);
        return sb.toString();
    }

}

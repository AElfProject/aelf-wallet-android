package com.legendwd.hyperpay.aelf.business.discover.cyano;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;

public final class CastleProvider {

    public static Provider getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final Provider INSTANCE;

        static {
            Provider p = Security.getProvider("SC");

            INSTANCE = (p != null) ? p : new BouncyCastleProvider();

            INSTANCE.put("MessageDigest.TRON-KECCAK-256", "org.tron.common.crypto" +
                    ".cryptohash.Keccak256");


            INSTANCE.put("MessageDigest.TRON-KECCAK-512", "org.tron.common.crypto" +
                    ".cryptohash.Keccak512");
        }
    }
}

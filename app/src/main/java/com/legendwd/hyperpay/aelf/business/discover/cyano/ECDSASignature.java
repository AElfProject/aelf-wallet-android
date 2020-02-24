package com.legendwd.hyperpay.aelf.business.discover.cyano;

import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.DLSequence;
import org.spongycastle.util.encoders.Hex;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;

import static com.legendwd.hyperpay.aelf.business.discover.dapp.GameWebActivity.CURVE;
import static com.legendwd.hyperpay.aelf.business.discover.dapp.GameWebActivity.HALF_CURVE_ORDER;
import static com.legendwd.hyperpay.aelf.business.discover.dapp.GameWebActivity.SECP256K1N;
import static org.bitcoinj.core.Utils.bigIntegerToBytes;

public class ECDSASignature {

    /**
     * The two components of the signature.
     */
    public final BigInteger r, s;
    public byte v;

    /**
     * Constructs a signature with the given components. Does NOT automatically canonicalise the
     * signature.
     *
     * @param r -
     * @param s -
     */
    public ECDSASignature(BigInteger r, BigInteger s) {
        this.r = r;
        this.s = s;
    }

    /**
     * t
     *
     * @return -
     */
    private static ECDSASignature fromComponents(byte[] r, byte[] s) {
        return new ECDSASignature(new BigInteger(1, r), new BigInteger(1,
                s));
    }

    /**
     * @param r -
     * @param s -
     * @param v -
     * @return -
     */
    public static ECDSASignature fromComponents(byte[] r, byte[] s, byte
            v) {
        ECDSASignature signature = fromComponents(r, s);
        signature.v = v;
        return signature;
    }

    public static boolean validateComponents(BigInteger r, BigInteger s,
                                             byte v) {

        if (v != 27 && v != 28) {
            return false;
        }

        if (isLessThan(r, BigInteger.ONE)) {
            return false;
        }
        if (isLessThan(s, BigInteger.ONE)) {
            return false;
        }

        if (!isLessThan(r, SECP256K1N)) {
            return false;
        }
        return isLessThan(s, SECP256K1N);
    }

    public static ECDSASignature decodeFromDER(byte[] bytes) {
        ASN1InputStream decoder = null;
        try {
            decoder = new ASN1InputStream(bytes);
            DLSequence seq = (DLSequence) decoder.readObject();
            if (seq == null) {
                throw new RuntimeException("Reached past end of ASN.1 " +
                        "stream.");
            }
            ASN1Integer r, s;
            try {
                r = (ASN1Integer) seq.getObjectAt(0);
                s = (ASN1Integer) seq.getObjectAt(1);
            } catch (ClassCastException e) {
                throw new IllegalArgumentException(e);
            }
            // OpenSSL deviates from the DER spec by interpreting these
            // values as unsigned, though they should not be
            // Thus, we always use the positive versions. See:
            // http://r6.ca/blog/20111119T211504Z.html
            return new ECDSASignature(r.getPositiveValue(), s
                    .getPositiveValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (decoder != null) {
                try {
                    decoder.close();
                } catch (IOException x) {
                }
            }
        }
    }

    public boolean validateComponents() {
        return validateComponents(r, s, v);
    }

    public ECDSASignature toCanonicalised() {
        if (s.compareTo(HALF_CURVE_ORDER) > 0) {
            return new ECDSASignature(r, CURVE.getN().subtract(s));
        } else {
            return this;
        }
    }

    /**
     * @return -
     */
    public String toBase64() {
        byte[] sigData = new byte[65];  // 1 header + 32 bytes for R + 32
        // bytes for S
        sigData[0] = v;
        System.arraycopy(bigIntegerToBytes(this.r, 32), 0, sigData, 1, 32);
        System.arraycopy(bigIntegerToBytes(this.s, 32), 0, sigData, 33, 32);
        return new String(org.spongycastle.util.encoders.Base64.encode(sigData), Charset.forName("UTF-8"));
    }

    public byte[] toByteArray() {
        final byte fixedV = this.v >= 27
                ? (byte) (this.v - 27)
                : this.v;

        return ByteUtil.merge(
                ByteUtil.bigIntegerToBytes(this.r, 32),
                ByteUtil.bigIntegerToBytes(this.s, 32),
                new byte[]{fixedV});
    }

    public String toHex() {
        return Hex.toHexString(toByteArray());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ECDSASignature signature = (ECDSASignature) o;

        if (!r.equals(signature.r)) {
            return false;
        }
        return s.equals(signature.s);
    }

    @Override
    public int hashCode() {
        int result = r.hashCode();
        result = 31 * result + s.hashCode();
        return result;
    }

    public static boolean isLessThan(BigInteger valueA, BigInteger valueB) {
        return valueA.compareTo(valueB) < 0;
    }
}

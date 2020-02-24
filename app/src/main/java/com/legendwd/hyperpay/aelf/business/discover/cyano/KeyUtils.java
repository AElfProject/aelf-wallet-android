package com.legendwd.hyperpay.aelf.business.discover.cyano;

import com.legendwd.hyperpay.aelf.business.discover.dapp.GameWebActivity;

import org.spongycastle.asn1.x9.X9IntegerConverter;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.math.ec.ECAlgorithms;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class KeyUtils {

    public static byte[] recoverPubBytesFromSignature(ECDomainParameters CURVE,int recId,
                                                      ECDSASignature sig,
                                                      byte[] messageHash) {
        // 1.0 For j from 0 to h   (h == recId here and the loop is outside
        // this function)
        //   1.1 Let x = r + jn
        BigInteger n = CURVE.getN();  // Curve order.
        BigInteger i = BigInteger.valueOf((long) recId / 2);
        BigInteger x = sig.r.add(i.multiply(n));
        //   1.2. Convert the integer x to an octet string X of length mlen
        // using the conversion routine
        //        specified in Section 2.3.7, where mlen = ⌈(log2 p)/8⌉ or
        // mlen = ⌈m/8⌉.
        //   1.3. Convert the octet string (16 set binary digits)||X to an
        // elliptic curve point R using the
        //        conversion routine specified in Section 2.3.4. If this
        // conversion routine outputs “invalid”, then
        //        do another iteration of Step 1.
        //
        // More concisely, what these points mean is to use X as a compressed
        // public key.
        ECCurve.Fp curve = (ECCurve.Fp) CURVE.getCurve();
        BigInteger prime = curve.getQ();  // Bouncy Castle is not consistent
        // about the letter it uses for the prime.
        if (x.compareTo(prime) >= 0) {
            // Cannot have point co-ordinates larger than this as everything
            // takes place modulo Q.
            return null;
        }
        // Compressed allKeys require you to know an extra bit of data about the
        // y-coord as there are two possibilities.
        // So it's encoded in the recId.
        ECPoint R = decompressKey(CURVE,x, (recId & 1) == 1);
        //   1.4. If nR != point at infinity, then do another iteration of
        // Step 1 (callers responsibility).
        if (!R.multiply(n).isInfinity()) {
            return null;
        }
        //   1.5. Compute e from M using Steps 2 and 3 of ECDSA signature
        // verification.
        BigInteger e = new BigInteger(1, messageHash);
        //   1.6. For k from 1 to 2 do the following.   (loop is outside this
        // function via iterating recId)
        //   1.6.1. Compute a candidate public key as:
        //               Q = mi(r) * (sR - eG)
        //
        // Where mi(x) is the modular multiplicative inverse. We transform
        // this into the following:
        //               Q = (mi(r) * s ** R) + (mi(r) * -e ** G)
        // Where -e is the modular additive inverse of e, that is z such that
        // z + e = 0 (mod n). In the above equation
        // ** is point multiplication and + is point addition (the EC group
        // operator).
        //
        // We can find the additive inverse by subtracting e from zero then
        // taking the mod. For example the additive
        // inverse of 3 modulo 11 is 8 because 3 + 8 mod 11 = 0, and -3 mod
        // 11 = 8.
        BigInteger eInv = BigInteger.ZERO.subtract(e).mod(n);
        BigInteger rInv = sig.r.modInverse(n);
        BigInteger srInv = rInv.multiply(sig.s).mod(n);
        BigInteger eInvrInv = rInv.multiply(eInv).mod(n);
        ECPoint.Fp q = (ECPoint.Fp) ECAlgorithms.sumOfTwoMultiplies(CURVE
                .getG(), eInvrInv, R, srInv);
        return q.getEncoded(/* compressed */ false);
    }

    /** Decompress a compressed public key (x co-ord and low-bit of y-coord). */
    private static ECPoint decompressKey(ECDomainParameters CURVE,BigInteger xBN, boolean yBit) {
        X9IntegerConverter x9 = new X9IntegerConverter();
        byte[] compEnc = x9.integerToBytes(xBN, 1 + x9.getByteLength(CURVE.getCurve()));
        compEnc[0] = (byte)(yBit ? 0x03 : 0x02);
        return CURVE.getCurve().decodePoint(compEnc);
    }
}

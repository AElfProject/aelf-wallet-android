package com.legendwd.hyperpay.aelf.business.discover.cyano;

import android.text.TextUtils;
import android.util.Log;

import com.google.protobuf.ByteString;
import com.legendwd.hyperpay.aelf.business.discover.dapp.GameWebActivity;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.LazyECPoint;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.crypto.signers.ECDSASigner;
import org.spongycastle.crypto.signers.HMacDSAKCalculator;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;

import static com.legendwd.hyperpay.aelf.business.discover.dapp.GameWebActivity.CURVE;

public class DappUtils {
    private static String publicKey;


    private static ECPrivateKeyParameters privParams;
    private static LazyECPoint pub;

    public static void setPub(LazyECPoint pub) {
        DappUtils.pub = pub;
    }

    public static void setPrivParams(ECPrivateKeyParameters privParams) {
        DappUtils.privParams = privParams;
    }

    public static void setPublicKey(String publicKey) {
        DappUtils.publicKey = publicKey;
    }

    public static boolean checkTimestamp(String timestamp){
        long checkTime = Long.parseLong(timestamp);
        long now = new Date().getTime() / 1000;
        long diff = now - checkTime;

        return diff >= -1 && diff <= 240;
    }


    public static void VerifySignParam(String raw, String signature, GameWebActivity.Callback callback){
        VerifySignParam(raw.getBytes(),signature,callback);
    }

    public static void VerifySignParam(byte[] raw,String signature,GameWebActivity.Callback callback){

        new Thread(() -> {
            if(!TextUtils.isEmpty(publicKey)){
                byte[] keyBytes = ByteUtil.toBytes(publicKey);
                ECDSASigner signer = new ECDSASigner();
                ECPublicKeyParameters params_ = new ECPublicKeyParameters(CURVE
                        .getCurve().decodePoint(keyBytes), CURVE);
                signer.init(false, params_);
                ByteString sign = ByteString.copyFrom(ByteUtil.toBytes(signature));

                byte[] r = sign.substring(0, 32).toByteArray();
                byte[] s = sign.substring(32, 64).toByteArray();
                byte v = sign.byteAt(64);
                if (v < 27) {
                    v += 27; //revId -> v
                }
                ECDSASignature ecdsaSignature = ECDSASignature.fromComponents(r, s, v);
                callback.callback(signer.verifySignature(raw,ecdsaSignature.r,ecdsaSignature.s));
            }else{
                callback.callback(false);
            }
        }).start();

    }




    public static void doSign(String hex, GameWebActivity.Callback callback){
        new Thread(() -> {
            byte[] messageHash = ByteUtil.toBytes(hex);
            if (privParams == null) {
                throw new ECKey.MissingPrivateKeyException();
            }
            ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new
                    SHA256Digest()));
            ECPrivateKeyParameters privKeyParams = new ECPrivateKeyParameters
                    (privParams.getD(), CURVE);
            signer.init(true, privKeyParams);
            BigInteger[] components = signer.generateSignature(messageHash);
            ECDSASignature ecdsaSignature = new ECDSASignature(components[0], components[1]);
            int recId = -1;
            byte[] thisKey = pub.getEncoded();
            for (int i = 0; i < 4; i++) {
                byte[] k = KeyUtils.recoverPubBytesFromSignature(CURVE,i, ecdsaSignature, messageHash);
                if (k != null && Arrays.equals(k, thisKey)) {
                    recId = i;
                    break;
                }
            }
            if (recId == -1) {
                recId = 0;
            }
            ecdsaSignature.v = (byte) (recId + 27);
            callback.callback(ecdsaSignature.toHex());
        }).start();
    }

    public static void doSign(byte[] messageHash, GameWebActivity.Callback callback){
        new Thread(() -> {
            if (privParams == null) {
                throw new ECKey.MissingPrivateKeyException();
            }
            ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new
                    SHA256Digest()));
            ECPrivateKeyParameters privKeyParams = new ECPrivateKeyParameters
                    (privParams.getD(), CURVE);
            signer.init(true, privKeyParams);
            BigInteger[] components = signer.generateSignature(messageHash);
            ECDSASignature ecdsaSignature = new ECDSASignature(components[0], components[1]);
            int recId = -1;
            byte[] thisKey = pub.getEncoded();
            for (int i = 0; i < 4; i++) {
                byte[] k = KeyUtils.recoverPubBytesFromSignature(CURVE,i, ecdsaSignature, messageHash);
                if (k != null && Arrays.equals(k, thisKey)) {
                    recId = i;
                    break;
                }
            }
            if (recId == -1) {
                recId = 0;
            }
            ecdsaSignature.v = (byte) (recId + 27);
            callback.callback(ecdsaSignature.toHex());
        }).start();
    }
}

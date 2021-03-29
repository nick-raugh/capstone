  

package com.coinbase;

import android.util.Base64;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class CallbackVerifierImpl implements com.coinbase.CallbackVerifier {
    private static PublicKey publicKey = null;

    private static synchronized PublicKey getPublicKey() {
        if (publicKey == null) {
            try {
                InputStream keyStream = CallbackVerifierImpl.class.getResourceAsStream("/com/coinbase/api/coinbase-callback.pub.der");
                byte[] keyBytes = IOUtils.toByteArray(keyStream);
                X509EncodedKeySpec keySpec =
                        new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                publicKey = keyFactory.generatePublic(keySpec);
            } catch (NoSuchAlgorithmException ex) {
                // Should never happen, java implementations must support RSA
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                // Should never happen
                throw new RuntimeException(ex);
            } catch (InvalidKeySpecException ex) {
                // Should never happen
                throw new RuntimeException(ex);
            }
        }

        return publicKey;
    }

    @Override
    public boolean verifyCallback(String body, String signature) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(getPublicKey());
            sig.update(body.getBytes());
            return sig.verify(Base64.decode(signature, Base64.DEFAULT));
        } catch (NoSuchAlgorithmException ex) {
            // Should never happen
            throw new RuntimeException(ex);
        } catch (InvalidKeyException ex) {
            // Should never happen
            throw new RuntimeException(ex);
        } catch (SignatureException e) {
            return false;
        }
    }
}

package com.wythe.mall.util;

import android.util.Log;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignUtils {

	private static final String ALGORITHM = "RSA";

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

	private static final String DEFAULT_CHARSET = "UTF-8";

	private static String getAlgorithms(boolean rsa2) {
		return rsa2 ? SIGN_SHA256RSA_ALGORITHMS : SIGN_ALGORITHMS;
	}

	private static final String TAG = "SignUtils";
	
	public static String sign(String content, String privateKey, boolean rsa2) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyFactory.generatePrivate(priPKCS8);
			Log.e(TAG, "sign: ---- " + priKey.toString());
			java.security.Signature signature = java.security.Signature
					.getInstance(getAlgorithms(rsa2));

			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));

			byte[] signed = signature.sign();
			Log.e(TAG, "sign: -- " + signed.length);

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}

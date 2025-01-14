/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jdk.test.lib;

import java.security.*;
import java.security.spec.*;
import java.util.*;

/*
 * Utility class used by various Signature related regression tests for
 * common functions such as generating the list of to-be-tested algorithms
 * based on key size, etc. Currently, this is mostly needed by RSA
 * signatures.
 */
@Bean
public class SigTestUtil {

    public enum SignatureType {
        RSA("RSA"),
        RSASSA_PSS("RSASSA-PSS")
        ;

        private String keyAlg;

        SignatureType(String keyAlg) {
            this.keyAlg = keyAlg;
        }
        @Override
        public String toString() {
            return keyAlg;
        }
    }

    // collection of all supported digest algorithms
    // note that the entries are ordered by required key sizes
    private static final String[] DIGEST_ALGS = {
        "SHA-512",
        "SHA-384",
        "SHA-256",
        "SHA-512/256",
        "SHA-224",
        "SHA-512/224",
        "SHA-1",
        "MD2", "MD5" // these aren't supported by RSA PSS
    };

    // indice for message digest algorithms lookup
    // may need to be adjusted if new algorithms are added
    private static final int PKCS1_5_INDEX_768 = 0;
    private static final int PKCS1_5_INDEX_512 = 2;
    private static final int PKCS1_5_INDEX_END = DIGEST_ALGS.length;
    private static final int PSS_INDEX_2048 = 0;
    private static final int PSS_INDEX_1024 = 1;
    private static final int PSS_INDEX_768 = 2;
    private static final int PSS_INDEX_512 = 4;
    private static final int PSS_INDEX_END = 7;

    public static Iterable<String> getDigestAlgorithms(SignatureType type,
            int keysize) throws RuntimeException {

        // initialize to all, then trim based on key size
        List<String> result = new ArrayList<>(Arrays.asList(DIGEST_ALGS));
        int index = 0;
        switch (type) {
        case RSA:
            if (keysize >= 768) {
                index = PKCS1_5_INDEX_768;
            } else if (keysize >= 512) {
                index = PKCS1_5_INDEX_512;
            } else {
                throw new RuntimeException("Keysize too small: " + keysize);
            }
            result = result.subList(index, PKCS1_5_INDEX_END);
            break;
        case RSASSA_PSS:
            if (keysize >= 2048) {
                index = PSS_INDEX_2048;
            } else if (keysize >= 1024) {
                index = PSS_INDEX_1024;
            } else if (keysize >= 768) {
                index = PSS_INDEX_768;
            } else if (keysize >= 512) {
                index = PSS_INDEX_512;
            } else {
                throw new RuntimeException("Keysize too small: " + keysize);
            }
            result = result.subList(index, PSS_INDEX_END);
            break;
        default:
            // XXX maybe just return result instead of error out?
            throw new RuntimeException("Unsupported signature type: " + type);
        }
        return result;
    }

    public static AlgorithmParameterSpec generateDefaultParameter(
            SignatureType type, String mdAlg) throws RuntimeException {
        // only RSASSA-PSS signature uses parameters
        switch (type) {
        case RSASSA_PSS:
            try {
                MessageDigest md = MessageDigest.getInstance(mdAlg);
                return new PSSParameterSpec(mdAlg, "MGF1",
                    new MGF1ParameterSpec(mdAlg), md.getDigestLength(),
                    PSSParameterSpec.TRAILER_FIELD_BC);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        default:
            return null;
        }
    }

    public static String generateSigAlg(SignatureType type,
            String mdAlg) throws RuntimeException {
        switch (type) {
        case RSA:
            int idx = mdAlg.indexOf("-");
            if (idx != -1) {
                mdAlg = mdAlg.substring(0, idx) + mdAlg.substring(idx+1);
            }
            return mdAlg + "with" + type.toString();
        case RSASSA_PSS:
            return type.toString();
        default:
            throw new RuntimeException("Unsupported signature type " + type );
        }
    }

}

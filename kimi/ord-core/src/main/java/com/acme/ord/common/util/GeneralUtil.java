package com.acme.ord.common.util;

import com.acme.ord.common.constants.DefConstants;
import org.springframework.stereotype.Component;

/**
 * pck_general fonksiyonlarinin stub karsiligi.
 */
@Component
public class GeneralUtil {

    private static int stanCounter = 0;

    public static void setOutVariables(StringBuilder cevapKodu, String rc,
                                       StringBuilder errorType, String errorTypeVal,
                                       StringBuilder errorNum, int errorNumVal,
                                       StringBuilder errorStr, String errorStrVal) {
        cevapKodu.setLength(0);
        cevapKodu.append(rc);
        errorType.setLength(0);
        errorType.append(errorTypeVal);
        errorNum.setLength(0);
        errorNum.append(String.valueOf(errorNumVal));
        errorStr.setLength(0);
        errorStr.append(errorStrVal);
    }

    public static synchronized String getStan() {
        stanCounter++;
        if (stanCounter > 99999999) {
            stanCounter = 1;
        }
        return String.valueOf(stanCounter);
    }

    public static String getIslemCevapAck(String rc) {
        // Diger package'ler migrate edildikce zenginlestirilecek
        return rc;
    }

}

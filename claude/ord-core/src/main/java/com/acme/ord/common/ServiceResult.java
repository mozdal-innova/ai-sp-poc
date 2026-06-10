package com.acme.ord.common;

import lombok.Getter;
import lombok.Setter;

/**
 * PL/SQL fonksiyonlarinin Return Number + OUT parametrelerinin Java karsiligi.
 * Her service metodu bu nesneyi dondurur.
 */
@Getter
@Setter
public class ServiceResult<T> {

    private int returnCode;
    private String cevapKodu;
    private String errorType;
    private Integer errorNum;
    private String errorStr;
    private T data;

    private ServiceResult() {
    }

    public static <T> ServiceResult<T> success() {
        ServiceResult<T> r = new ServiceResult<>();
        r.returnCode = Constants.SUCCESS;
        r.cevapKodu = Constants.RC_SUCCESS;
        return r;
    }

    public static <T> ServiceResult<T> success(T data) {
        ServiceResult<T> r = success();
        r.data = data;
        return r;
    }

    public static <T> ServiceResult<T> fail(String cevapKodu, String errorType, Integer errorNum, String errorStr) {
        ServiceResult<T> r = new ServiceResult<>();
        r.returnCode = Constants.FAIL;
        r.cevapKodu = cevapKodu;
        r.errorType = errorType;
        r.errorNum = errorNum;
        r.errorStr = errorStr;
        return r;
    }

    public boolean isSuccess() {
        return returnCode == Constants.SUCCESS;
    }
}

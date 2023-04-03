package com.winxo.portailwinxo.Model;

public class Parameters {
    private float PRICE_MIN_SSP;
    private float PRICE_MAX_SSP;
    private float PRICE_MIN_GASOIL;
    private float PRICE_MAX_GASOIL;
    private float PRICE_MIN_2T;
    private float PRICE_MAX_2T;
    private float PRICE_STEP;
    private int INPUT_INTO_DAY;
    private int DAY_LIMIT_FOR_LISTING;
    private int INTERVAL_BY_SECS;
    private int ADD_COUNT_PER_DAY;
    private int ADD_INTERVAL_PER_DAY;
    private int LOGIN_ATTEMPT;
    private boolean SEND_MAIL;
    private  boolean SEND_SMS;
    private String PW_CHECKSUM;
    private boolean AVIS_MAINTENANCE;
    private boolean MAINTENANCE;
    private boolean ENABLE_APAG;
    private boolean ENABLE_ORDERS;
    private boolean ENABLE_CLAIM;
    private int ORDER_ADD_COUNT_PER_DAY;
    private String ORDER_SUSPEND_DAY_FROM;
    private String ORDER_SUSPEND_DAY_TO;
    private String ORDER_ADD_TIME_FROM;
    private String ORDER_ADD_TIME_TO;
    private String ORDER_CANCEL_DELAY;
    private int ORDER_DATE_SHIP_DAY_UPON;
    private int ORDER_CAR_MIN;
    private int ORDER_CAR_MAX;
    private String ORDER_CAR_UNIT;

    public Parameters(float PRICE_MIN_SSP, float PRICE_MAX_SSP, float PRICE_MIN_GASOIL, float PRICE_MAX_GASOIL, float PRICE_MIN_2T, float PRICE_MAX_2T, float PRICE_STEP, int INPUT_INTO_DAY, int DAY_LIMIT_FOR_LISTING, int INTERVAL_BY_SECS, int ADD_COUNT_PER_DAY, int ADD_INTERVAL_PER_DAY, int LOGIN_ATTEMPT, boolean SEND_MAIL, boolean SEND_SMS, String PW_CHECKSUM, boolean AVIS_MAINTENANCE, boolean MAINTENANCE, boolean ENABLE_APAG, boolean ENABLE_ORDERS, boolean ENABLE_CLAIM, int ORDER_ADD_COUNT_PER_DAY, String ORDER_SUSPEND_DAY_FROM, String ORDER_SUSPEND_DAY_TO, String ORDER_ADD_TIME_FROM, String ORDER_ADD_TIME_TO, String ORDER_CANCEL_DELAY, int ORDER_DATE_SHIP_DAY_UPON, int ORDER_CAR_MIN, int ORDER_CAR_MAX, String ORDER_CAR_UNIT) {
        this.PRICE_MIN_SSP = PRICE_MIN_SSP;
        this.PRICE_MAX_SSP = PRICE_MAX_SSP;
        this.PRICE_MIN_GASOIL = PRICE_MIN_GASOIL;
        this.PRICE_MAX_GASOIL = PRICE_MAX_GASOIL;
        this.PRICE_MIN_2T = PRICE_MIN_2T;
        this.PRICE_MAX_2T = PRICE_MAX_2T;
        this.PRICE_STEP = PRICE_STEP;
        this.INPUT_INTO_DAY = INPUT_INTO_DAY;
        this.DAY_LIMIT_FOR_LISTING = DAY_LIMIT_FOR_LISTING;
        this.INTERVAL_BY_SECS = INTERVAL_BY_SECS;
        this.ADD_COUNT_PER_DAY = ADD_COUNT_PER_DAY;
        this.ADD_INTERVAL_PER_DAY = ADD_INTERVAL_PER_DAY;
        this.LOGIN_ATTEMPT = LOGIN_ATTEMPT;
        this.SEND_MAIL = SEND_MAIL;
        this.SEND_SMS = SEND_SMS;
        this.PW_CHECKSUM = PW_CHECKSUM;
        this.AVIS_MAINTENANCE = AVIS_MAINTENANCE;
        this.MAINTENANCE = MAINTENANCE;
        this.ENABLE_APAG = ENABLE_APAG;
        this.ENABLE_ORDERS = ENABLE_ORDERS;
        this.ENABLE_CLAIM = ENABLE_CLAIM;
        this.ORDER_ADD_COUNT_PER_DAY = ORDER_ADD_COUNT_PER_DAY;
        this.ORDER_SUSPEND_DAY_FROM = ORDER_SUSPEND_DAY_FROM;
        this.ORDER_SUSPEND_DAY_TO = ORDER_SUSPEND_DAY_TO;
        this.ORDER_ADD_TIME_FROM = ORDER_ADD_TIME_FROM;
        this.ORDER_ADD_TIME_TO = ORDER_ADD_TIME_TO;
        this.ORDER_CANCEL_DELAY = ORDER_CANCEL_DELAY;
        this.ORDER_DATE_SHIP_DAY_UPON = ORDER_DATE_SHIP_DAY_UPON;
        this.ORDER_CAR_MIN = ORDER_CAR_MIN;
        this.ORDER_CAR_MAX = ORDER_CAR_MAX;
        this.ORDER_CAR_UNIT = ORDER_CAR_UNIT;
    }

    public float getPRICE_MIN_SSP() {
        return PRICE_MIN_SSP;
    }

    public void setPRICE_MIN_SSP(float PRICE_MIN_SSP) {
        this.PRICE_MIN_SSP = PRICE_MIN_SSP;
    }

    public float getPRICE_MAX_SSP() {
        return PRICE_MAX_SSP;
    }

    public void setPRICE_MAX_SSP(float PRICE_MAX_SSP) {
        this.PRICE_MAX_SSP = PRICE_MAX_SSP;
    }

    public float getPRICE_MIN_GASOIL() {
        return PRICE_MIN_GASOIL;
    }

    public void setPRICE_MIN_GASOIL(float PRICE_MIN_GASOIL) {
        this.PRICE_MIN_GASOIL = PRICE_MIN_GASOIL;
    }

    public float getPRICE_MAX_GASOIL() {
        return PRICE_MAX_GASOIL;
    }

    public void setPRICE_MAX_GASOIL(float PRICE_MAX_GASOIL) {
        this.PRICE_MAX_GASOIL = PRICE_MAX_GASOIL;
    }

    public float getPRICE_MIN_2T() {
        return PRICE_MIN_2T;
    }

    public void setPRICE_MIN_2T(float PRICE_MIN_2T) {
        this.PRICE_MIN_2T = PRICE_MIN_2T;
    }

    public float getPRICE_MAX_2T() {
        return PRICE_MAX_2T;
    }

    public void setPRICE_MAX_2T(float PRICE_MAX_2T) {
        this.PRICE_MAX_2T = PRICE_MAX_2T;
    }

    public float getPRICE_STEP() {
        return PRICE_STEP;
    }

    public void setPRICE_STEP(float PRICE_STEP) {
        this.PRICE_STEP = PRICE_STEP;
    }

    public int getINPUT_INTO_DAY() {
        return INPUT_INTO_DAY;
    }

    public void setINPUT_INTO_DAY(int INPUT_INTO_DAY) {
        this.INPUT_INTO_DAY = INPUT_INTO_DAY;
    }

    public int getDAY_LIMIT_FOR_LISTING() {
        return DAY_LIMIT_FOR_LISTING;
    }

    public void setDAY_LIMIT_FOR_LISTING(int DAY_LIMIT_FOR_LISTING) {
        this.DAY_LIMIT_FOR_LISTING = DAY_LIMIT_FOR_LISTING;
    }

    public int getINTERVAL_BY_SECS() {
        return INTERVAL_BY_SECS;
    }

    public void setINTERVAL_BY_SECS(int INTERVAL_BY_SECS) {
        this.INTERVAL_BY_SECS = INTERVAL_BY_SECS;
    }

    public int getADD_COUNT_PER_DAY() {
        return ADD_COUNT_PER_DAY;
    }

    public void setADD_COUNT_PER_DAY(int ADD_COUNT_PER_DAY) {
        this.ADD_COUNT_PER_DAY = ADD_COUNT_PER_DAY;
    }

    public int getADD_INTERVAL_PER_DAY() {
        return ADD_INTERVAL_PER_DAY;
    }

    public void setADD_INTERVAL_PER_DAY(int ADD_INTERVAL_PER_DAY) {
        this.ADD_INTERVAL_PER_DAY = ADD_INTERVAL_PER_DAY;
    }

    public int getLOGIN_ATTEMPT() {
        return LOGIN_ATTEMPT;
    }

    public void setLOGIN_ATTEMPT(int LOGIN_ATTEMPT) {
        this.LOGIN_ATTEMPT = LOGIN_ATTEMPT;
    }

    public boolean isSEND_MAIL() {
        return SEND_MAIL;
    }

    public void setSEND_MAIL(boolean SEND_MAIL) {
        this.SEND_MAIL = SEND_MAIL;
    }

    public boolean isSEND_SMS() {
        return SEND_SMS;
    }

    public void setSEND_SMS(boolean SEND_SMS) {
        this.SEND_SMS = SEND_SMS;
    }

    public String getPW_CHECKSUM() {
        return PW_CHECKSUM;
    }

    public void setPW_CHECKSUM(String PW_CHECKSUM) {
        this.PW_CHECKSUM = PW_CHECKSUM;
    }

    public boolean isAVIS_MAINTENANCE() {
        return AVIS_MAINTENANCE;
    }

    public void setAVIS_MAINTENANCE(boolean AVIS_MAINTENANCE) {
        this.AVIS_MAINTENANCE = AVIS_MAINTENANCE;
    }

    public boolean isMAINTENANCE() {
        return MAINTENANCE;
    }

    public void setMAINTENANCE(boolean MAINTENANCE) {
        this.MAINTENANCE = MAINTENANCE;
    }

    public boolean isENABLE_APAG() {
        return ENABLE_APAG;
    }

    public void setENABLE_APAG(boolean ENABLE_APAG) {
        this.ENABLE_APAG = ENABLE_APAG;
    }

    public boolean isENABLE_ORDERS() {
        return ENABLE_ORDERS;
    }

    public void setENABLE_ORDERS(boolean ENABLE_ORDERS) {
        this.ENABLE_ORDERS = ENABLE_ORDERS;
    }

    public boolean isENABLE_CLAIM() {
        return ENABLE_CLAIM;
    }

    public void setENABLE_CLAIM(boolean ENABLE_CLAIM) {
        this.ENABLE_CLAIM = ENABLE_CLAIM;
    }

    public int getORDER_ADD_COUNT_PER_DAY() {
        return ORDER_ADD_COUNT_PER_DAY;
    }

    public void setORDER_ADD_COUNT_PER_DAY(int ORDER_ADD_COUNT_PER_DAY) {
        this.ORDER_ADD_COUNT_PER_DAY = ORDER_ADD_COUNT_PER_DAY;
    }

    public String getORDER_SUSPEND_DAY_FROM() {
        return ORDER_SUSPEND_DAY_FROM;
    }

    public void setORDER_SUSPEND_DAY_FROM(String ORDER_SUSPEND_DAY_FROM) {
        this.ORDER_SUSPEND_DAY_FROM = ORDER_SUSPEND_DAY_FROM;
    }

    public String getORDER_SUSPEND_DAY_TO() {
        return ORDER_SUSPEND_DAY_TO;
    }

    public void setORDER_SUSPEND_DAY_TO(String ORDER_SUSPEND_DAY_TO) {
        this.ORDER_SUSPEND_DAY_TO = ORDER_SUSPEND_DAY_TO;
    }

    public String getORDER_ADD_TIME_FROM() {
        return ORDER_ADD_TIME_FROM;
    }

    public void setORDER_ADD_TIME_FROM(String ORDER_ADD_TIME_FROM) {
        this.ORDER_ADD_TIME_FROM = ORDER_ADD_TIME_FROM;
    }

    public String getORDER_ADD_TIME_TO() {
        return ORDER_ADD_TIME_TO;
    }

    public void setORDER_ADD_TIME_TO(String ORDER_ADD_TIME_TO) {
        this.ORDER_ADD_TIME_TO = ORDER_ADD_TIME_TO;
    }

    public String getORDER_CANCEL_DELAY() {
        return ORDER_CANCEL_DELAY;
    }

    public void setORDER_CANCEL_DELAY(String ORDER_CANCEL_DELAY) {
        this.ORDER_CANCEL_DELAY = ORDER_CANCEL_DELAY;
    }

    public int getORDER_DATE_SHIP_DAY_UPON() {
        return ORDER_DATE_SHIP_DAY_UPON;
    }

    public void setORDER_DATE_SHIP_DAY_UPON(int ORDER_DATE_SHIP_DAY_UPON) {
        this.ORDER_DATE_SHIP_DAY_UPON = ORDER_DATE_SHIP_DAY_UPON;
    }

    public int getORDER_CAR_MIN() {
        return ORDER_CAR_MIN;
    }

    public void setORDER_CAR_MIN(int ORDER_CAR_MIN) {
        this.ORDER_CAR_MIN = ORDER_CAR_MIN;
    }

    public int getORDER_CAR_MAX() {
        return ORDER_CAR_MAX;
    }

    public void setORDER_CAR_MAX(int ORDER_CAR_MAX) {
        this.ORDER_CAR_MAX = ORDER_CAR_MAX;
    }

    public String getORDER_CAR_UNIT() {
        return ORDER_CAR_UNIT;
    }

    public void setORDER_CAR_UNIT(String ORDER_CAR_UNIT) {
        this.ORDER_CAR_UNIT = ORDER_CAR_UNIT;
    }
}

package com.colvir.bootcamp.homework16.consumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExchangeRateDto {
    @JsonProperty("USD_in")
    private String usdIn;
    @JsonProperty("USD_out")
    private String usdOut;
    @JsonProperty("EUR_in")
    private String eurIn;
    @JsonProperty("EUR_out")
    private String eurOut;
    @JsonProperty("RUB_in")
    private String rubIn;
    @JsonProperty("RUB_out")
    private String rubOut;
    @JsonProperty("GBP_in")
    private String gbpIn;
    @JsonProperty("GBP_out")
    private String gbpOut;
    @JsonProperty("CAD_in")
    private String cadIn;
    @JsonProperty("CAD_out")
    private String cadOut;
    @JsonProperty("PLN_in")
    private String plnIn;
    @JsonProperty("PLN_out")
    private String plnOut;
    @JsonProperty("UAH_in")
    private String uahIn;
    @JsonProperty("UAH_out")
    private String uahOut;
    @JsonProperty("SEK_in")
    private String sekIn;
    @JsonProperty("SEK_out")
    private String sekOut;
    @JsonProperty("CHF_in")
    private String chfIn;
    @JsonProperty("CHF_out")
    private String chfOut;
    @JsonProperty("JPY_in")
    private String jpyIn;
    @JsonProperty("JPY_out")
    private String jpyOut;
    @JsonProperty("CNY_in")
    private String cnyIn;
    @JsonProperty("CNY_out")
    private String cnyOut;
    @JsonProperty("CZK_in")
    private String czkIn;
    @JsonProperty("CZK_out")
    private String czkOut;
    @JsonProperty("NOK_in")
    private String nokIn;
    @JsonProperty("NOK_out")
    private String nokOut;
    @JsonProperty("USD_EUR_in")
    private String usdEurIn;
    @JsonProperty("USD_EUR_out")
    private String usdEurOut;
    @JsonProperty("USD_RUB_in")
    private String usdRubIn;
    @JsonProperty("USD_RUB_out")
    private String usdRubOut;
    @JsonProperty("RUB_EUR_in")
    private String rubEurIn;
    @JsonProperty("RUB_EUR_out")
    private String rubEurOut;
    @JsonProperty("filial_id")
    private String filialId;
    @JsonProperty("info_worktime")
    private String infoWorkTime;
    @JsonProperty("filials_text")
    private String filialText;
    @JsonProperty("name_type")
    private String nameType;
    private String name;
    @JsonProperty("street_type")
    private String streetType;
    private String street;
    @JsonProperty("home_number")
    private String homeNumber;
}

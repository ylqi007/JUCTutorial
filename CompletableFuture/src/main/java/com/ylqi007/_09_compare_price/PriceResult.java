package com.ylqi007._09_compare_price;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PriceResult {
    private int price;
    private int discount;
    private int realPrice;
    private String platform;

    public PriceResult(String platform) {
        this.platform = platform;
    }
}

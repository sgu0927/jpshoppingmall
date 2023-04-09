package com.jpshoppingmall.common;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@Getter
public class Address implements Serializable {
    private String postCode;
    private String roadNameAddress;
    private String detailAddress;
}

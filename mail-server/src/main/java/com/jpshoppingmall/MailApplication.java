package com.jpshoppingmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * keytool -genkey -alias bns-ssl -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
 * 키 비밀번호 shoppingmall
 * 이름과 성 abc
 * 조직 단위/ 이름 / 구 군 시 / 시 도 / mylazysong
 * 국가 코드 82
 */
@SpringBootApplication
public class MailApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailApplication.class, args);
	}

}

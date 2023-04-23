package com.jpshoppingmall.domain.member.entity;

import com.jpshoppingmall.common.Address;
import com.jpshoppingmall.common.BaseTimeEntity;
import com.jpshoppingmall.domain.cart.entity.Cart;
import com.jpshoppingmall.domain.member.dto.MemberDto;
import com.jpshoppingmall.domain.notification.entity.Notification;
import com.jpshoppingmall.type.EnumMaster.GenderType;
import com.jpshoppingmall.type.EnumMaster.Role;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseTimeEntity implements Serializable {

    @Id     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String phone;
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    private GenderType genderType;
    @Embedded
    private Address address;
    private String company;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ProfileImage profileImage;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    public void addCart(Cart cart) {
        carts.add(cart);
        cart.setMember(this);
    }

    public void changeProfileImage(ProfileImage to) {
        profileImage = to;
    }

    public void changeNickname(String to) {
        validateNickname(to);
        nickname = to;
    }

    public void changeEmail(String to) {
        validateEmail(to);
        email = to;
    }

    public void changePassword(String to) {
        password = to;
    }

    public MemberDto toDto() {
        return new MemberDto(name, email, nickname, address, phone);
    }

    private void validateNickname(String nickname) {
        if (this.company == null) {
            return;
        }

        // Assert.isTrue(nickname.length() <= NAME_MAX_LENGTH, "최대 길이를 초과했습니다.");

        final String pattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";
        if (!Pattern.matches(pattern, nickname)) {
            throw new IllegalArgumentException("공백 혹은 특수문자가 입력되었습니다.");
        }
    }

    private void validateEmail(String email) {
        final String pattern =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (!Pattern.matches(pattern, email)) {
            throw new IllegalArgumentException("이메일 형식에 맞지 않습니다.");
        }
    }
}

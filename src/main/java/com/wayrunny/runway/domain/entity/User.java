
package com.wayrunny.runway.domain.entity;

import com.sun.istack.NotNull;
import com.wayrunny.runway.domain.dto.UserDto;
import com.wayrunny.runway.util.RandomGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends Timestamped implements UserDetails {

    @Id
    @GeneratedValue(generator = RandomGenerator.generatorName)
    @GenericGenerator(name = RandomGenerator.generatorName, strategy = "com.wayrunny.runway.util.RandomGenerator")
    @Column
    private String id;

    @NotNull
    @Column
    private String nickname;

    @Column
    private String imageUrl;

    @NotNull
    @Column(name="social_type")
    private String socialType;

    @NotNull
    @Column(name="social_id")
    private String socialId;

    @Column
    private Integer level;

    @Column
    private Integer exp;


    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public User(UserDto requestDto){
        this.socialType = requestDto.getSocialType();
        this.socialId = requestDto.getSocialId();
        this.nickname = requestDto.getNickname();
        this.imageUrl = getImageUrl();
        this.level = 1;
        this.exp = 0;
    }

    public void updateProfile(String nickname, String imageUrl){
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    public void setLevelAndExp(Integer exp, Integer level){
        this.exp = exp;
        this.level = level;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

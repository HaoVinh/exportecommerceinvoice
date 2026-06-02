package lixco.com.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shopee_token")
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class ShopeeToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "expire_time")
    private Long expireTime; // Unix timestamp

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private boolean usedAuthCode;

    // Constructor bổ sung cho shop_id
    public ShopeeToken( String accessToken, String refreshToken, Long expireTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expireTime = expireTime;
        this.usedAuthCode = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}
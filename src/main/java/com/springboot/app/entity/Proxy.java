package com.springboot.app.entity;

import com.springboot.app.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "proxies")
public class Proxy {
    @Id
    private String id;
    @Column(name = "http_local")
    private String httpLocal;
    @Column(name = "sock_local")
    private String sockLocal;
    @Column(name = "http_remote")
    private String httpRemote;
    @Column(name = "sock_remote")
    private String sockRemote;
    private String auth;
    @Column(name = "ip_public")
    private String ipPublic;
    private String status;
    @Column(name = "eth_port")
    private String ethPort;
    private String mtu;
    @Column(name = "last_link_up_time")
    private String lastLinkUpTime;
    @Column(name = "total_rx")
    private String totalRx;
    @Column(name = "total_tx")
    private String totalTx;
}

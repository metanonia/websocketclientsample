package com.metanonia.wsclient.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Entity
@Table(name="depth")
public class Depth {
    @EmbeddedId
    private DepthId depthId;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "amt")
    private BigDecimal amt;
    @Column(name = "ts")
    private Timestamp ts;
}

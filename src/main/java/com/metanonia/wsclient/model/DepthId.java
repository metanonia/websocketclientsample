package com.metanonia.wsclient.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DepthId implements Serializable {
    @Column(name="symbol")
    private String symbol;
    @Column(name = "level")
    private Integer level;
    @Column(name="ordertype")
    private String orderType;
}

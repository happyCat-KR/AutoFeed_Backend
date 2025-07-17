package kr.soft.autofeed.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "action_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActionType {

    @Id
    @Column(name = "type_code", length = 30)
    private String typeCode;

    @Column(name = "score_value", nullable = false)
    private Double soreValue;
}

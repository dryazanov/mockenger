package org.mockenger.data.model.persistent.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author Dmitry Ryazanov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractPersistentEntity<T extends Serializable> {

    @Id
    private T id;
}

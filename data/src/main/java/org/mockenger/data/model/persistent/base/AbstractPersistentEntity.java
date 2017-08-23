package org.mockenger.data.model.persistent.base;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author Dmitry Ryazanov
 */
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractPersistentEntity<T extends Serializable> {

    @Id
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}

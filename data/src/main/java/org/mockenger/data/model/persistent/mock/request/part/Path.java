package org.mockenger.data.model.persistent.mock.request.part;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.mockenger.data.model.persistent.transformer.Transformer;

import java.util.List;

/**
 * @author Dmitry Ryazanov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Path extends AbstractPart<Transformer> {

    private String value;


    public Path(final List<Transformer> transformers, final String value) {
        this.transformers = transformers;
        this.value = value;
    }
}

package org.mockenger.data.model.persistent.mock.request.part;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.mockenger.data.model.persistent.transformer.Transformer;

import java.util.List;
import java.util.Set;

/**
 * @author Dmitry Ryazanov
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Headers extends AbstractPart<Transformer> {

    private Set<Pair> values;


    public Headers(final List<Transformer> transformers, final Set<Pair> values) {
        this.transformers = transformers;
        this.values = values;
    }
}

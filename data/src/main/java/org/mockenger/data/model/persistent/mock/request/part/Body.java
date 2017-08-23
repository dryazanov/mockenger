package org.mockenger.data.model.persistent.mock.request.part;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.mockenger.data.model.persistent.transformer.Transformer;
import lombok.ToString;

import java.util.List;

import static java.util.Optional.ofNullable;

/**
 * @author Dmitry Ryazanov
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Body extends AbstractPart<Transformer> {

    private String value;


    public Body(final List<Transformer> transformers, final String value) {
        this.transformers = transformers;
        setValue(value);
    }

    public String getValue() {
        return ofNullable(value).orElse("");
    }

    public void setValue(final String value) {
        this.value = value;
    }
}

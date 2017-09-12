package org.mockenger.data.model.persistent.mock.request.part;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.mockenger.data.model.persistent.transformer.Transformer;

import java.util.List;

/**
 * @author Dmitry Ryazanov
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Body extends AbstractPart<Transformer> {

    private String value;


    public Body(final List<Transformer> transformers, final String value) {
        this.transformers = transformers;
		this.value = value;
    }
}

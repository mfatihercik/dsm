package com.github.mfatihercik.dsb.json;

import com.github.mfatihercik.dsb.AbstractPathGenerator;
import com.github.mfatihercik.dsb.model.ParsingElement;

public class AbsoluteJsonPathGenerator extends AbstractPathGenerator {
    @Override
    public String getTagParentPath(ParsingElement parsingElement) {
        return parsingElement.getTagParentPath();
    }

    @Override
    public String getTagPath(ParsingElement parsingElement) {
        return parsingElement.getTagPath();

    }

}

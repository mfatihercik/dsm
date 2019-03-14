package com.github.mfatihercik.dsb.xml;

import com.github.mfatihercik.dsb.AbstractPathGenerator;
import com.github.mfatihercik.dsb.model.ParsingElement;

public class AbsoluteXmlPathGenerator extends AbstractPathGenerator {

    @Override
    public String getTagParentPath(ParsingElement parsingElement) {
        return parsingElement.getTagXmlParentPath();
    }

    @Override
    public String getTagPath(ParsingElement parsingElement) {
        return parsingElement.getTagXmlPath();
    }

}

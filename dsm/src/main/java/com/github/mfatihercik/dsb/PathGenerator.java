package com.github.mfatihercik.dsb;

import com.github.mfatihercik.dsb.model.ParsingElement;

public interface PathGenerator {


    String generatePath(ParsingElement parsingElement);

    String getTagParentPath(ParsingElement parsingElement);

    String getTagPath(ParsingElement parsingElement);

    String concatPaths(String parentPath, String child);

}

.. _`Transformation Element`:

_`Transformation Element Object`
===================================

------------------------------------------

Transformation is a very powerful feature that used to map value of a tag from the `source document`_ to destination document.
Transformation Element holds the mapping and how the mapping will be used with `Parsing Element`_s.
We can consider, transformation as switch-case in programing language. 
Every record in the mapping table is a case and DEFAULT record is a default case fo switch-case statement. 
   
Fields:
    .. csv-table::
      :header: Field Name, Type, Description
      :stub-columns: 1
      :delim: |
      
      map | Map<String,Object> |  **REQUIRED** mapping table from source to destination
      onlyIfExist  | boolean | transform source value only if exist in mapping table. if not exist use as is.
      `$ref`_ | string | ref field is used to extends_ current Transformation Element to another Transformation Element. it is an expression.
      
      
..  content-tabs::

      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            transformations:
                COUNTRY_CODE_TO_NAME:
                    map:
                      DEFAULT: Other
                      TR: Turkey
                      US: United States
                COUNTRY_CODe_TO_NAME_IF_EXIST:
                     $ref: $transformations.COUNTRY_CODE_TO_NAME
                     onlyIfExist: true

      .. tab-container:: json
         :title: JSON

         .. code-block:: json

           {
         	    "version": 1,
         	    "transformations": {
         	    	"COUNTRY_CODE_TO_NAME": {
         	    		"map": {
         	    			"DEFAULT": "Other",
         	    			"TR": "Turkey",
         	    			"US": "United States"
         	    		}
         	    	},
         	    	"COUNTRY_CODe_TO_NAME_IF_EXIST": {
         	    		"$ref": "$transformations.COUNTRY_CODE_TO_NAME",
         	    		"onlyIfExist": true
         	    	}
         	    }
             }


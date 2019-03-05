
.. _fields:

fields
-----------

------------


The fields field is used to define the properties of complex objects. Only `Parsing Element`_ that has complex tagType_  can have the "fields" field.

The fields field is  a map that keys are fieldName of  `Parsing Element`_ , values are string, `Parsing Element`_ or list of `Parsing Element`_ 




Type of Value Definition
^^^^^^^^^^^^^^^^^^^^^^^^
The value of the map can be empty, string, `Parsing Element`_ or array of `Parsing Element`_. 

Different value definitions create `Parsing Element`_ with some default values.

Below explain type of value definition and the default values of the `Parsing Element`_ that it creates.

empty:
      :fieldName_: key of the map.
      :type_: string
      :tagPath_: key of the map (fieldName)
      :tagParentPath_: null

string:
      :fieldName_: key of the map.
      :type_: value of the map
      :tagPath_: key of the map (fieldName)
      :tagParentPath_: null
      
`Parsing Element`_:
      :fieldName_: key of the map.      
      other fields are can be defined or initialized with default values.
      
Array of `Parsing Element`_:
   some fields of objects can be read from the different tag in the source document.  By making multiple definitions for one field, the value of different tags can be read.
   
Example of different type of value definition:
   
..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result: 
              tagType: object
              tagPath: /
              fields:
                 name:     # fieldName is "name" and type is  string and the tagPath is "/name"
                 category: 
                 price: long      # fieldName is "price" and type is  "long"  and the tagPath is "/price"
                 categoryType:   # fieldName is "categoryType" and it is  "string" value and the tagPath is "/categoryType" it has extra definition (default)        
                        default: "foo"   # default value a is a string. 
                 productUnit:  # this field contains two definition. one  of that  will win depending on the structure of source document.
                       - tagPath: unit/unit_name      # fieldName is "productUnit" and type is  "long"  and the tagPath is "/unit/unit_name"
                         default: $ self.data.categoryType=='foo'? 'LT': 'KG'  
                         
                       - tagPath: mainUnit/unit_name  # fieldName is "productUnit" and type is  "long"  and the tagPath is "/mainUnit/unit_name"

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "tagType":"object",
                  "tagPath":"/"  ,       
                  "fields":{
                     "name":"",
                     "category":"", 
                     "price":"long",
                     "categoryType":{
                        "default": "foo"
                     },
                     "productUnit":[
                     {  "tagPath": "unit/unit_name",
                        "default": " $self.data.categoryType=='foo'? 'LT': 'KG'"
                     },
                     {
                        "tagPath": "mainUnit/unit_name",
                     },
                     ],
                   }
               }
             }
  
      

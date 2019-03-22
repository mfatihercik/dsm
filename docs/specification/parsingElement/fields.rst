
.. _fields:

fields
-----------

------------


The fields field is used to define the properties of complex objects. Only `Parsing Element`_ that has complex type_  can have the "fields" field.

The fields field is  a map that keys are fieldName of  `Parsing Element`_ , values are string, `Parsing Element`_ or list of `Parsing Element`_ 




Type of Value Definition
^^^^^^^^^^^^^^^^^^^^^^^^
The value of the map can be empty, string, `Parsing Element`_ or array of `Parsing Element`_. 

Different value definitions create `Parsing Element`_ with some default values.

Below explain type of value definition and the default values of the `Parsing Element`_ that it creates.

empty:
      :fieldName_: key of the map.
      :dataType_: string
      :path_: key of the map (fieldName)
      :parentPath_: null

string:
      :fieldName_: key of the map.
      :dataType_: value of the map
      :path_: key of the map (fieldName)
      :parentPath_: null
      
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
              type: object
              path: /
              fields:
                 name:     # fieldName is "name" and dataType is  string and the path is "/name"
                 category: 
                 price: long      # fieldName is "price" and dataType is  "long"  and the path is "/price"
                 categoryType:   # fieldName is "categoryType" and it is  "string" value and the path is "/categoryType" it has extra definition (default)        
                        default: "foo"   # default value a is a string. 
                 productUnit:  # this field contains two definition. one  of that  will win depending on the structure of source document.
                       - path: unit/unit_name      # fieldName is "productUnit" and dataType is  "long"  and the path is "/unit/unit_name"
                         default: $ self.data.categoryType=='foo'? 'LT': 'KG'  
                         
                       - path: mainUnit/unit_name  # fieldName is "productUnit" and dataType is  "long"  and the path is "/mainUnit/unit_name"

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "type"object",
                  "path":"/"  ,       
                  "fields":{
                     "name":"",
                     "category":"", 
                     "price":"long",
                     "categoryType":{
                        "default": "foo"
                     },
                     "productUnit":[
                     {  "path": "unit/unit_name",
                        "default": " $self.data.categoryType=='foo'? 'LT': 'KG'"
                     },
                     {
                        "path": "mainUnit/unit_name",
                     },
                     ],
                   }
               }
             }
  
      
